package models;

public record Color(int id, String name) {
    @Override
    public final String toString() {
        return String.format("{id=%d, name='%s'}", id, name);
    }
}
