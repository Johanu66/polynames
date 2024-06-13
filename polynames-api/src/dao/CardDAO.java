package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import database.PolyBayDatabase;
import models.Card;
import models.HieraCard;

public class CardDAO {
    private PolyBayDatabase database;

    public CardDAO() {
        try {
            this.database = new PolyBayDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Card> findAll() {
        try {
            PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT * FROM card;");
            ResultSet results = myPreparedStatement.executeQuery();
            ArrayList<Card> cards = new ArrayList<>();

            while (results.next()) {
                final int id = results.getInt("id");
                final int position = results.getInt("position");
                final String status = results.getString("status");
                final int idGame = results.getInt("id_game");
                final int idColor = results.getInt("id_color");
                final int idWord = results.getInt("id_word");

                Card card = new Card(id, position, status, idGame, idColor, idWord);
                cards.add(card);
            }
            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Card findById(int id) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT * FROM card WHERE id = ?;");
        myPreparedStatement.setInt(1, id);
        ResultSet results = myPreparedStatement.executeQuery();

        if (results.next()) {
            final int cardId = results.getInt("id");
            final int position = results.getInt("position");
            final String status = results.getString("status");
            final int idGame = results.getInt("id_game");
            final int idColor = results.getInt("id_color");
            final int idWord = results.getInt("id_word");

            return new Card(cardId, position, status, idGame, idColor, idWord);
        }
        return null;
    }

    public void insert(Card card) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("INSERT INTO card (position, status, id_game, id_color, id_word) VALUES (?, ?, ?, ?, ?);");
        myPreparedStatement.setInt(1, card.position());
        myPreparedStatement.setString(2, card.status());
        myPreparedStatement.setInt(3, card.id_game());
        myPreparedStatement.setInt(4, card.id_color());
        myPreparedStatement.setInt(5, card.id_word());

        myPreparedStatement.executeUpdate();
    }

    public void update(Card card) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("UPDATE card SET position = ?, status = ?, id_game = ?, id_color = ?, id_word = ? WHERE id = ?;");
        myPreparedStatement.setInt(1, card.position());
        myPreparedStatement.setString(2, card.status());
        myPreparedStatement.setInt(3, card.id_game());
        myPreparedStatement.setInt(4, card.id_color());
        myPreparedStatement.setInt(5, card.id_word());
        myPreparedStatement.setInt(6, card.id());

        myPreparedStatement.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("DELETE FROM card WHERE id = ?;");
        myPreparedStatement.setInt(1, id);

        myPreparedStatement.executeUpdate();
    }

    public HieraCard findHieraCardById(int cardId) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT card.id, card.position, card.status, color.name, word.name FROM card INNER JOIN color ON card.id_color = color.id INNER JOIN word ON card.id_word = word.id WHERE card.id = ?;");
        myPreparedStatement.setInt(1, cardId);
        ResultSet results = myPreparedStatement.executeQuery();

        if (results.next()) {
            final int id = results.getInt("id");
            final int position = results.getInt("position");
            final String status = results.getString("status");
            final String color = results.getString("color.name");
            final String word = results.getString("word.name");

            return new HieraCard(id, position, status, color, word);
        }
        return null;
    }


    public ArrayList<HieraCard> findHieraCardsByGameId(int gameId) throws SQLException {
        try {
            PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT card.id, card.position, card.status, color.name, word.text FROM card INNER JOIN color ON card.id_color = color.id INNER JOIN word ON card.id_word = word.id WHERE card.id_game = ?;");
            myPreparedStatement.setInt(1, gameId);
            ResultSet results = myPreparedStatement.executeQuery();
            ArrayList<HieraCard> cards = new ArrayList<>();

            while (results.next()) {
                final int id = results.getInt("id");
                final int position = results.getInt("position");
                final String status = results.getString("status");
                final String color = results.getString("color.name");
                final String word = results.getString("word.text");
    
                HieraCard card = new HieraCard(id, position, status, color, word);
                cards.add(card);
            }
            return cards;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<HieraCard> findHieraCardsByTurnId(int turnId) throws SQLException {
        try {
            PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT card.id, card.position, card.status, color.name, word.text FROM card INNER JOIN color ON card.id_color = color.id INNER JOIN word ON card.id_word = word.id INNER JOIN turnCard ON card.id = turnCard.id_card WHERE turnCard.id_turn = ?;");
            myPreparedStatement.setInt(1, turnId);
            ResultSet results = myPreparedStatement.executeQuery();
            ArrayList<HieraCard> cards = new ArrayList<>();

            while (results.next()) {
                final int id = results.getInt("id");
                final int position = results.getInt("position");
                final String status = results.getString("status");
                final String color = results.getString("color.name");
                final String word = results.getString("word.text");

                HieraCard card = new HieraCard(id, position, status, color, word);
                cards.add(card);
            }
            return cards;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
