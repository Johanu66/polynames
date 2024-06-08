package controllers;

import dao.GameDAO;
import models.Game;
import webserver.WebServerContext;

public class GameController {
    static public GameDAO gameDAO = new GameDAO();

    static public void findAll(WebServerContext context) {
        context.getResponse().json(gameDAO.findAll());
    }

    static public void findById(WebServerContext context) {
        int gameId = Integer.parseInt(context.getRequest().getParam("gameId"));
        try {
            Game game = gameDAO.findById(gameId);
            if (game != null) {
                context.getResponse().json(game);
            } else {
                context.getResponse().notFound("Game not found");
            }
        } catch (Exception e) {
            context.getResponse().serverError("Error finding game");
        }
    }

    static public void findByCode(WebServerContext context) {
        String gameCode = context.getRequest().getParam("gameCode");
        try {
            Game game = gameDAO.findByCode(gameCode);
            if (game != null) {
                context.getResponse().json(game);
            } else {
                context.getResponse().notFound("Game not found");
            }
        } catch (Exception e) {
            context.getResponse().serverError("Error finding game");
        }
    }

    static public void create(WebServerContext context) {
        try {
            Game game = context.getRequest().extractBody(Game.class);
            gameDAO.insert(game);
            context.getResponse().ok("Game created successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error creating game");
        }
    }

    static public void update(WebServerContext context) {
        int gameId = Integer.parseInt(context.getRequest().getParam("gameId"));
        try {
            Game game = context.getRequest().extractBody(Game.class);
            gameDAO.update(new Game(gameId, game.score(), game.code(), game.status(), game.current_player()));
            context.getResponse().ok("Game updated successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error updating game");
        }
    }

    static public void delete(WebServerContext context) {
        int gameId = Integer.parseInt(context.getRequest().getParam("gameId"));
        try {
            gameDAO.delete(gameId);
            context.getResponse().ok("Game deleted successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error deleting game");
        }
    }
}
