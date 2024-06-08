import { PlayerService } from "../services/player-service.js";

export class PlayerView {
    constructor() {    }
    
    static attachEvents() {
        document.getElementById("create-player-form").addEventListener("submit", async (event)=>{
            event.preventDefault();
            await PlayerView.createPlayer();
        });
    }
    
    static async createPlayer() {
        const pseudo = document.getElementById("pseudo").value;
        const player = await PlayerService.create(pseudo);
        if (player) {
            // Save the player in the local storage
            localStorage.setItem("player", JSON.stringify(player));
            // Redirect to the game view
            window.location.href = "game.html#"+player.game_code;
        }
    }
}