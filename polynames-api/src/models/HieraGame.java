package models;

import java.util.ArrayList;

public record HieraGame(int id, int score, String code, String status, String current_player, ArrayList<HieraPlayer> players, HieraTurn turn, ArrayList<HieraCard> cards) {
    @Override
    public final String toString() {
        return String.format("{id=%d, score=%d, code='%s', status='%s', current_player='%s', players='%s', lastTurn=%s, cards='%s'}", id, score, code, status, current_player, players, turn, cards);
    }
}
