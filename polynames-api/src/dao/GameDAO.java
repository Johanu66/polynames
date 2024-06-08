package dao;

import java.sql.*;
import java.util.*;
import database.PolyBayDatabase;
import models.Game;

public class GameDAO {
    private PolyBayDatabase database;

    public GameDAO() {
        try {
            this.database = new PolyBayDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Game> findAll() {
        try {
            PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT * FROM game;");
            ResultSet results = myPreparedStatement.executeQuery();
            ArrayList<Game> games = new ArrayList<>();

            while (results.next()) {
                final int id = results.getInt("id");
                final int score = results.getInt("score");
                final String code = results.getString("code");
                final String status = results.getString("status");
                final String currentPlayer = results.getString("current_player");

                Game game = new Game(id, score, code, status, currentPlayer);
                games.add(game);
            }
            return games;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Game findById(int id) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT * FROM game WHERE id = ?;");
        myPreparedStatement.setInt(1, id);
        ResultSet results = myPreparedStatement.executeQuery();

        if (results.next()) {
            final int gameId = results.getInt("id");
            final int score = results.getInt("score");
            final String code = results.getString("code");
            final String status = results.getString("status");
            final String currentPlayer = results.getString("current_player");

            return new Game(gameId, score, code, status, currentPlayer);
        }
        return null;
    }

    public Game findByCode(String code) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT * FROM game WHERE code = ?;");
        myPreparedStatement.setString(1, code);
        ResultSet results = myPreparedStatement.executeQuery();

        if (results.next()) {
            final int id = results.getInt("id");
            final int score = results.getInt("score");
            final String gameCode = results.getString("code");
            final String status = results.getString("status");
            final String currentPlayer = results.getString("current_player");

            return new Game(id, score, gameCode, status, currentPlayer);
        }
        return null;
    }

    public Game insert(Game game) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("INSERT INTO game (score, code, status, current_player) VALUES (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
        myPreparedStatement.setInt(1, game.score());
        myPreparedStatement.setString(2, game.code());
        myPreparedStatement.setString(3, game.status());
        myPreparedStatement.setString(4, game.current_player());
        
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

    public void update(Game game) throws SQLException {
        PreparedStatement myPreparedStatement = this.database
                .prepareStatement("UPDATE game SET score = ?, code = ?, status = ?, current_player = ? WHERE id = ?;");
        myPreparedStatement.setInt(1, game.score());
        myPreparedStatement.setString(2, game.code());
        myPreparedStatement.setString(3, game.status());
        myPreparedStatement.setString(4, game.current_player());
        myPreparedStatement.setInt(5, game.id());

        myPreparedStatement.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("DELETE FROM game WHERE id = ?;");
        myPreparedStatement.setInt(1, id);

        myPreparedStatement.executeUpdate();
    }

    public String getCode(int id) throws SQLException {
        PreparedStatement myPreparedStatement = this.database.prepareStatement("SELECT code FROM game WHERE id = ?;");
        myPreparedStatement.setInt(1, id);
        ResultSet results = myPreparedStatement.executeQuery();

        if (results.next()) {
            return results.getString("code");
        }
        return null;
    }

    public Game newGame() throws SQLException {

        Connection connection = this.database.getConnection();

        // Generate a unique game code
        String uniqueCode = generateUniqueCode(connection);

        Game game = new Game(0, 0, uniqueCode, "waiting", "spymaster");

        // Insert the game into the database
        game = this.insert(game);

        // Fetch 25 random unique words
        List<Integer> wordIds = getRandomIds(connection, "word", 25);

        // Fetch the color IDs
        Map<String, Integer> colorIdMap = getColorIdMap(connection);

        // Prepare the color distribution: 8 blue, 15 gray, 2 black
        List<Integer> colorIds = new ArrayList<>();
        colorIds.addAll(Collections.nCopies(8, colorIdMap.get("blue")));
        colorIds.addAll(Collections.nCopies(15, colorIdMap.get("gray")));
        colorIds.addAll(Collections.nCopies(2, colorIdMap.get("black")));

        // Shuffle the colors to randomize their positions
        Collections.shuffle(colorIds);

        // Insert cards into the database
        String insertCardSQL = "INSERT INTO card (position, status, id_game, id_color, id_word) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertCardSQL)) {
            for (int i = 0; i < 25; i++) {
                stmt.setInt(1, i+1);
                stmt.setString(2, "hidden");
                stmt.setInt(3, game.id());
                stmt.setInt(4, colorIds.get(i));
                stmt.setInt(5, wordIds.get(i));
                stmt.addBatch();
            }
            stmt.executeBatch();
        }

        return game;
    }

    private static String generateUniqueCode(Connection connection) throws SQLException {
        String code;
        boolean isUnique;
        do {
            code = UUID.randomUUID().toString().substring(0, 8); // Generate a random 8-character code
            String checkCodeSQL = "SELECT COUNT(*) FROM game WHERE code = ?";
            try (PreparedStatement stmt = connection.prepareStatement(checkCodeSQL)) {
                stmt.setString(1, code);
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    isUnique = rs.getInt(1) == 0;
                }
            }
        } while (!isUnique);
        return code;
    }

    private static List<Integer> getRandomIds(Connection connection, String tableName, int limit) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String getIdsSQL = "SELECT id FROM " + tableName + " ORDER BY RAND() LIMIT " + limit;
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(getIdsSQL)) {
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
        }
        return ids;
    }

    private static Map<String, Integer> getColorIdMap(Connection connection) throws SQLException {
        Map<String, Integer> colorIdMap = new HashMap<>();
        String getColorIdsSQL = "SELECT id, name FROM color";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(getColorIdsSQL)) {
            while (rs.next()) {
                colorIdMap.put(rs.getString("name"), rs.getInt("id"));
            }
        }
        return colorIdMap;
    }
}
