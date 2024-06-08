package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import database.PolyBayDatabase;
import models.Turn;

public class TurnDAO {
    private PolyBayDatabase database;

    public TurnDAO() {
        try {
            this.database = new PolyBayDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Turn> findAll() {
        try {
            PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT * FROM turn;");
            ResultSet results = myPreparedStatement.executeQuery();
            ArrayList<Turn> turns = new ArrayList<>();

            while (results.next()) {
                final int id = results.getInt("id");
                final String hint = results.getString("hint");
                final int score = results.getInt("score");
                final String status = results.getString("status");
                final int hintCount = results.getInt("hint_count");
                final int discoveredCards = results.getInt("discovered_cards");
                final int idGame = results.getInt("id_game");

                Turn turn = new Turn(id, hint, score, status, hintCount, discoveredCards, idGame);
                turns.add(turn);
            }
            return turns;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Turn findById(int id) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT * FROM turn WHERE id = ?;");
        myPreparedStatement.setInt(1, id);
        ResultSet results = myPreparedStatement.executeQuery();

        if (results.next()) {
            final int turnId = results.getInt("id");
            final String hint = results.getString("hint");
            final int score = results.getInt("score");
            final String status = results.getString("status");
            final int hintCount = results.getInt("hint_count");
            final int discoveredCards = results.getInt("discovered_cards");
            final int idGame = results.getInt("id_game");

            return new Turn(turnId, hint, score, status, hintCount, discoveredCards, idGame);
        }
        return null;
    }

    public void insert(Turn turn) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("INSERT INTO turn (hint, score, status, hint_count, discovered_cards, id_game) VALUES (?, ?, ?, ?, ?, ?);");
        myPreparedStatement.setString(1, turn.hint());
        myPreparedStatement.setInt(2, turn.score());
        myPreparedStatement.setString(3, turn.status());
        myPreparedStatement.setInt(4, turn.hint_count());
        myPreparedStatement.setInt(5, turn.discovered_cards());
        myPreparedStatement.setInt(6, turn.id_game());

        myPreparedStatement.executeUpdate();
    }

    public void update(Turn turn) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("UPDATE turn SET hint = ?, score = ?, status = ?, hint_count = ?, discovered_cards = ?, id_game = ? WHERE id = ?;");
        myPreparedStatement.setString(1, turn.hint());
        myPreparedStatement.setInt(2, turn.score());
        myPreparedStatement.setString(3, turn.status());
        myPreparedStatement.setInt(4, turn.hint_count());
        myPreparedStatement.setInt(5, turn.discovered_cards());
        myPreparedStatement.setInt(6, turn.id_game());
        myPreparedStatement.setInt(7, turn.id());

        myPreparedStatement.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("DELETE FROM turn WHERE id = ?;");
        myPreparedStatement.setInt(1, id);

        myPreparedStatement.executeUpdate();
    }
}
