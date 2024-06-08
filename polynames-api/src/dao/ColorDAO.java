package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import database.PolyBayDatabase;
import models.Color;

public class ColorDAO {
    private PolyBayDatabase database;

    public ColorDAO() {
        try {
            this.database = new PolyBayDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Color> findAll() {
        try {
            PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT * FROM color;");
            ResultSet results = myPreparedStatement.executeQuery();
            ArrayList<Color> colors = new ArrayList<>();

            while (results.next()) {
                final int id = results.getInt("id");
                final String name = results.getString("name");

                Color color = new Color(id, name);
                colors.add(color);
            }
            return colors;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Color findById(int id) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT * FROM color WHERE id = ?;");
        myPreparedStatement.setInt(1, id);
        ResultSet results = myPreparedStatement.executeQuery();

        if (results.next()) {
            final int colorId = results.getInt("id");
            final String name = results.getString("name");

            return new Color(colorId, name);
        }
        return null;
    }

    public void insert(Color color) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("INSERT INTO color (name) VALUES (?);");
        myPreparedStatement.setString(1, color.name());

        myPreparedStatement.executeUpdate();
    }

    public void update(Color color) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("UPDATE color SET name = ? WHERE id = ?;");
        myPreparedStatement.setString(1, color.name());
        myPreparedStatement.setInt(2, color.id());

        myPreparedStatement.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("DELETE FROM color WHERE id = ?;");
        myPreparedStatement.setInt(1, id);

        myPreparedStatement.executeUpdate();
    }
}
