package models;

public record Game(int id, int score, String code, String status, String current_player) {
    @Override
    public final String toString() {
        return String.format("{id=%d, score=%d, code='%s', status='%s', current_player='%s'}", id, score, code, status, current_player);
    }
}
