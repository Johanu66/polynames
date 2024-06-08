import { PlayerView } from "./views/player-view.js";

window.addEventListener("load", () => {
    run();
});

function run() {
    PlayerView.attachEvents();
}