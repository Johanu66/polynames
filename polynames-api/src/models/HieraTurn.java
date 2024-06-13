package models;

import java.util.ArrayList;

public record HieraTurn(int id, String hint, int score, String status, int hint_count, int discovered_cards, ArrayList<HieraCard> cards) {
    @Override
    public final String toString() {
        return String.format("{id=%d, hint='%s', score=%d, status='%s', hint_count=%d, discovered_cards=%d, cards=%s}", id, hint, score, status, hint_count, discovered_cards, cards);
    }
}
