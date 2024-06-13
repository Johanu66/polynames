package models;


public record HieraCard(int id, int position, String status, String color, String word) {
    @Override
    public final String toString() {
        return String.format("{id=%d, position=%d, status='%s', color='%s', word='%s'}", id, position, status, color, word);
    }
}
