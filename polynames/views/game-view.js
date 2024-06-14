import { SSEClient } from "../lib//sse-client.js";
import { GameService } from "../services/game-service.js";
import { PlayerService } from "../services/player-service.js";

window.addEventListener("load", async () => {
    const game = await getGame();
    if (game) {
        reflesh(game);
    } else {
        alert('Game not found');
    }
});

async function getGame() {
    const params = new URLSearchParams(window.location.search);
    const code = params.get('code');
    var game = null;
    if (code){
        game = await GameService.getGame(code);
    }
    return game;
}

// Fonction pour mettre à jour la page avec les données du jeu
function reflesh(game) {
    var oldPlayer = JSON.parse(localStorage.getItem('player'));
    var player = game.players.find(p => p.id === oldPlayer?.id);
    if(player){
        player.game_code = game.code;
        player.id_game = game.id;
        localStorage.setItem('player', JSON.stringify(player));

        // Mise à jour des informations du Spymaster
        const spymasterPseudo = document.getElementById('spymaster_pseudo');
        const spymaster = game.players.find(player => player.role?.toLowerCase().includes('spymaster'));
        if (spymaster) {
            spymasterPseudo.textContent = spymaster.pseudo;
            document.getElementById("join_as_spymaster_button").disabled = true;
            document.getElementById("join_as_spymaster_button").classList.add('disabled:opacity-50');
        } else {
            spymasterPseudo.textContent = '----';
            document.getElementById("join_as_spymaster_button").disabled = false;
        }

        // Mise à jour des informations de l'Operative
        const operativePseudo = document.getElementById('operative_pseudo');
        const operative = game.players.find(player => player.role?.toLowerCase().includes('operative'));
        if (operative) {
            operativePseudo.textContent = operative.pseudo;
            document.getElementById("join_as_operative_button").disabled = true;
            document.getElementById("join_as_operative_button").classList.add('disabled:opacity-50');
        } else {
            operativePseudo.textContent = '----';
            document.getElementById("join_as_operative_button").disabled = false;
        }

        // Mise à jour des cartes sur le plateau
        const cardsContainer = document.getElementById('cards');
        cardsContainer.innerHTML = ''; // Clear existing cards
        game.cards.forEach(card => {
            const cardElement = document.createElement('div');
            cardElement.className = 'bg-gray-200 rounded-lg p-4 text-center';
            cardElement.textContent = card.word.toUpperCase();

            // Add card color based on its status and color
            if (card.status === 'revealed' || (card.status === 'hidden' && player.role === 'spymaster')) {
                if (card.color === 'blue') {
                    cardElement.classList.add('bg-blue-500');
                } else if (card.color === 'red') {
                    cardElement.classList.add('bg-red-500');
                } else if (card.color === 'gray') {
                    cardElement.classList.add('bg-gray-500');
                } else if (card.color === 'black') {
                    cardElement.classList.add('bg-black');
                    cardElement.classList.add('text-white');
                }
            }

            cardsContainer.appendChild(cardElement);
        });

        document.getElementById("join_as_spymaster_button").addEventListener("click", async (event)=>{
            event.preventDefault();
            player.role = 'spymaster';
            await PlayerService.updatePlayer(player);
        });

        document.getElementById("join_as_operative_button").addEventListener("click", async (event)=>{
            event.preventDefault();
            player.role = 'operative';
            await PlayerService.updatePlayer(player);
        });

        const mySSEClient = new SSEClient("localhost:8080");
        mySSEClient.connect();
        mySSEClient.subscribe(game.code, reflesh);

        document.getElementById('game_container').classList.remove('hidden');
        document.getElementById('loading').classList.add('hidden');
    }else{
        alert("Veullez créer d'abord votre pseudo dans ce jeu, avant de continuer.");
        window.location.href = "/join.html?code=" + game.code;
    }
    
}


