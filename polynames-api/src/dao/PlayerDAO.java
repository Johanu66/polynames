package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import database.PolyBayDatabase;
import models.HieraPlayer;
import models.Player;

public class PlayerDAO {
    private PolyBayDatabase database;

    public PlayerDAO() {
        try {
            this.database = new PolyBayDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Player> findAll() {
        try {
            PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT * FROM player;");
            ResultSet results = myPreparedStatement.executeQuery();
            ArrayList<Player> players = new ArrayList<>();

            while (results.next()) {
                final int id = results.getInt("id");
                final String role = results.getString("role");
                final String pseudo = results.getString("pseudo");
                final int idGame = results.getInt("id_game");

                GameDAO gameDAO = new GameDAO();

                Player player = new Player(id, role, pseudo, idGame, gameDAO.getCode(idGame));
                players.add(player);
            }
            return players;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<HieraPlayer> findHieraPlayersByGameId(int gameId) {
        try {
            PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT * FROM player WHERE id_game = ?;");
            myPreparedStatement.setInt(1, gameId);
            ResultSet results = myPreparedStatement.executeQuery();
            ArrayList<HieraPlayer> players = new ArrayList<>();

            while (results.next()) {
                final int id = results.getInt("id");
                final String role = results.getString("role");
                final String pseudo = results.getString("pseudo");

                HieraPlayer player = new HieraPlayer(id, role, pseudo);
                players.add(player);
            }
            return players;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Player findById(int id) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT * FROM player WHERE id = ?;");
        myPreparedStatement.setInt(1, id);
        ResultSet results = myPreparedStatement.executeQuery();

        if (results.next()) {
            final int playerId = results.getInt("id");
            final String role = results.getString("role");
            final String pseudo = results.getString("pseudo");
            final int idGame = results.getInt("id_game");

            GameDAO gameDAO = new GameDAO();

            return new Player(playerId, role, pseudo, idGame, gameDAO.getCode(idGame));
        }
        return null;
    }

    public Player insert(Player player) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("INSERT INTO player (role, pseudo, id_game) VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
        myPreparedStatement.setString(1, player.role());
        myPreparedStatement.setString(2, player.pseudo());
        myPreparedStatement.setInt(3, player.id_game());

        // Execute the update and retrieve the generated keys
        int affectedRows = myPreparedStatement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Inserting player failed, no rows affected.");
        }

        try (ResultSet generatedKeys = myPreparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                return findById(newId);
            } else {
                throw new SQLException("Inserting player failed, no ID obtained.");
            }
        }
    }

    public void update(Player player) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("UPDATE player SET role = ?, pseudo = ?, id_game = ? WHERE id = ?;");
        myPreparedStatement.setString(1, player.role());
        myPreparedStatement.setString(2, player.pseudo());
        myPreparedStatement.setInt(3, player.id_game());
        myPreparedStatement.setInt(4, player.id());

        myPreparedStatement.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("DELETE FROM player WHERE id = ?;");
        myPreparedStatement.setInt(1, id);

        myPreparedStatement.executeUpdate();
    }
}
