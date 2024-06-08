package models;


public record Word(int id, String text) {
    @Override
    public final String toString() {
        return String.format("{id=%d, text='%s'}", id, text);
    }
}
