package controllers;

import dao.GameDAO;
import dao.PlayerDAO;
import models.Game;
import models.Player;
import webserver.WebServerContext;

public class PlayerController {
    static public PlayerDAO playerDAO = new PlayerDAO();

    static public void findAll(WebServerContext context) {
        context.getResponse().json(playerDAO.findAll());
    }

    static public void findById(WebServerContext context) {
        int playerId = Integer.parseInt(context.getRequest().getParam("playerId"));
        try {
            Player player = playerDAO.findById(playerId);
            if (player != null) {
                context.getResponse().json(player);
            } else {
                context.getResponse().notFound("Player not found");
            }
        } catch (Exception e) {
            context.getResponse().serverError("Error finding player");
        }
    }

    static public void create(WebServerContext context) {
        try {
            Player player = context.getRequest().extractBody(Player.class);

            if(player.pseudo() == null) {
                context.getResponse().badRequest("Missing parameters : pseudo");
                return;
            }

            Game game = null;

            if(player.game_code() ==  null){
                GameDAO gameDAO = new GameDAO();
                game = gameDAO.newGame();
                player = player.withIdGame(game.id());
            }else{
                GameDAO gameDAO = new GameDAO();
                game = gameDAO.findByCode(player.game_code());
                if(game == null){
                    context.getResponse().badRequest("Game not found");
                    return;
                }
                player = player.withIdGame(game.id());
            }

            player = playerDAO.insert(player);
            context.getResponse().json(player);

            ApplicationController.diffuseGame(game.code(), context);
        } catch (Exception e) {
            System.out.println(e);
            context.getResponse().serverError("Error creating player");
        }
    }

    static public void update(WebServerContext context) {
        int playerId = Integer.parseInt(context.getRequest().getParam("playerId"));
        try {
            Player player = context.getRequest().extractBody(Player.class);
            playerDAO.update(new Player(playerId, player.role(), player.pseudo(), player.id_game(), ""));
            context.getResponse().ok("Player updated successfully");

            ApplicationController.diffuseGame(player.game_code(), context);
        } catch (Exception e) {
            e.printStackTrace();
            context.getResponse().serverError("Error updating player");
        }
    }

    static public void delete(WebServerContext context) {
        int playerId = Integer.parseInt(context.getRequest().getParam("playerId"));
        try {
            playerDAO.delete(playerId);
            context.getResponse().ok("Player deleted successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error deleting player");
        }
    }
}
