package models;

public record HieraPlayer(int id, String role, String pseudo) {
    @Override
    public final String toString() {
        return String.format("{id=%d, role='%s', pseudo='%s'}", id, role, pseudo);
    }
}
