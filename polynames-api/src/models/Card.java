package models;


public record Card(int id, int position, String status, int id_game, int id_color, int id_word) {
    @Override
    public final String toString() {
        return String.format("{id=%d, position=%d, status='%s', id_game=%d, id_color=%d, id_word=%d}", id, position, status, id_game, id_color, id_word);
    }
}
