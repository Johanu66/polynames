package models;

public record Player(int id, String role, String pseudo, int id_game, String game_code) {
    @Override
    public final String toString() {
        return String.format("{id=%d, role='%s', pseudo='%s', id_game=%d, id_game=%s}", id, role, pseudo, id_game, game_code);
    }

    public Player withIdGame(int newIdGame) {
        return new Player(this.id, this.role, this.pseudo, newIdGame, this.game_code);
    }

    public Player withGameCode(String newGameCode) {
        return new Player(this.id, this.role, this.pseudo, this.id_game, newGameCode);
    }
}
