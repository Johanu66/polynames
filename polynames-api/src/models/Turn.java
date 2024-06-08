package models;


public record Turn(int id, String hint, int score, String status, int hint_count, int discovered_cards, int id_game) {
    @Override
    public final String toString() {
        return String.format("{id=%d, hint='%s', score=%d, status='%s', hint_count=%d, discovered_cards=%d, id_game=%d}", id, hint, score, status, hint_count, discovered_cards, id_game);
    }
}
