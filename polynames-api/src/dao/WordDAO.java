package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import database.PolyBayDatabase;
import models.Word;

public class WordDAO {
    private PolyBayDatabase database;

    public WordDAO() {
        try {
            this.database = new PolyBayDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Word> findAll() {
        try {
            PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT * FROM word;");
            ResultSet results = myPreparedStatement.executeQuery();
            ArrayList<Word> words = new ArrayList<>();

            while (results.next()) {
                final int id = results.getInt("id");
                final String text = results.getString("text");

                Word word = new Word(id, text);
                words.add(word);
            }
            return words;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Word findById(int id) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT * FROM word WHERE id = ?;");
        myPreparedStatement.setInt(1, id);
        ResultSet results = myPreparedStatement.executeQuery();

        if (results.next()) {
            final int wordId = results.getInt("id");
            final String text = results.getString("text");

            return new Word(wordId, text);
        }
        return null;
    }

    public void insert(Word word) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("INSERT INTO word (text) VALUES (?);");
        myPreparedStatement.setString(1, word.text());

        myPreparedStatement.executeUpdate();
    }

    public void update(Word word) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("UPDATE word SET text = ? WHERE id = ?;");
        myPreparedStatement.setString(1, word.text());
        myPreparedStatement.setInt(2, word.id());

        myPreparedStatement.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("DELETE FROM word WHERE id = ?;");
        myPreparedStatement.setInt(1, id);

        myPreparedStatement.executeUpdate();
    }
}
