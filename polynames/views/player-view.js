import { PlayerService } from "../services/player-service.js";
import { GameService } from "../services/game-service.js";

export class PlayerView {
    game_exist = false;

    constructor() {    }
    
    static attachCreatePlayerEvents() {
        document.getElementById("create-player-form").addEventListener("submit", async (event)=>{
            event.preventDefault();
            await PlayerView.createPlayer();
        });
    }
    
    static async createPlayer() {
        const pseudo = document.getElementById("pseudo").value;
        const player = await PlayerService.create({pseudo: pseudo});
        if (player) {
            // Save the player in the local storage
            localStorage.setItem("player", JSON.stringify(player));
            // Redirect to the game view
            window.location.href = "../game.html?code="+player.game_code;
        }
    }

    static async attachJoinGameEvents() {
        const params = new URLSearchParams(window.location.search);
        const code = params.get('code');
        var game = null;
        if (code){
            const game = await GameService.findByCode(code);
            if(game){
                this.game_exist = true;
                document.getElementById("code").value = code;
                document.getElementById("code_container").classList.toggle("hidden");
                document.getElementById("pseudo_container").classList.toggle("hidden");
            }else{
                alert("Game not found");
            }
        }
        document.getElementById("join-game-form").addEventListener("submit", async (event)=>{
            event.preventDefault();
            if(this.game_exist){
                await PlayerView.joinGame();
            }else{
                await PlayerView.checkGameExist();
            }
        });
    }
    
    static async joinGame() {
        const code = document.getElementById("code").value;
        const pseudo = document.getElementById("pseudo").value;
        const game = await GameService.findByCode(code);
        if(game){
            const player = await PlayerService.create({pseudo: pseudo, game_code: code});
            if (player) {
                // Save the player in the local storage
                localStorage.setItem("player", JSON.stringify(player));
                // Redirect to the game view
                window.location.href = "../game.html?code="+player.game_code;
            }
        }else{
            alert("Game not found");
        }
    }

    static async checkGameExist(){
        const code = document.getElementById("code").value;
        const game = await GameService.findByCode(code);
        if(game){
            this.game_exist = true;
            document.getElementById("code_container").classList.toggle("hidden");
            document.getElementById("pseudo_container").classList.toggle("hidden");
        }else{
            alert("Game not found");
        }
    }
}