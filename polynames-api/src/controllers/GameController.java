package controllers;

import dao.CardDAO;
import dao.GameDAO;
import dao.TurnDAO;
import models.Card;
import models.Game;
import models.HieraCard;
import models.HieraGame;
import models.Turn;
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

    static public void findHierarchicalGameByCode(WebServerContext context) {
        String gameCode = context.getRequest().getParam("gameCode");
        try {
            HieraGame game = gameDAO.findHierarchicalGameByCode(gameCode);
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

            ApplicationController.diffuseGame(game.code(), context);
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

    static public void checkCard(WebServerContext context) {
        String gameCode = context.getRequest().getParam("gameCode");
        try {
            Card card = context.getRequest().extractBody(Card.class);

            CardDAO cardDAO = new CardDAO();
            TurnDAO turnDAO = new TurnDAO();

            HieraCard hieraCard = cardDAO.findHieraCardById(card.id());
            HieraGame hieraGame = gameDAO.findHierarchicalGameByCode(gameCode);

            cardDAO.update(new Card(card.id(), card.position(), "visible", card.id_game(), card.id_color(), card.id_word()));

            if (hieraGame != null) {
                //A chaque tour, on comptabilise les points obtenus pour chaque carte bleue découverte. La première carte vaut 1 point, la seconde 2 points, la troisième 3 points, ... Si le Maître des intuitions découvre une carte bleue en N+1, cette carte vaut N² points
                int score = 0;
                if (hieraCard.color().equals("black")) {
                    gameDAO.update(new Game(hieraGame.id(), hieraGame.score(), hieraGame.code(), "done", hieraGame.current_player()));
                } else if (hieraCard.color().equals("gray")) {
                    turnDAO.update(new Turn(hieraGame.turn().id(), hieraGame.turn().hint(), hieraGame.turn().score(), "done", hieraGame.turn().hint_count(), hieraGame.turn().discovered_cards(), hieraGame.id()));
                    turnDAO.insert(new Turn(0, "", 0, "pending", 0, 0, hieraGame.id()));
                    gameDAO.update(new Game(hieraGame.id(), hieraGame.score(), hieraGame.code(), hieraGame.status(), "spymaster"));
                }else if (hieraCard.color().equals("blue")) {
                    if((hieraGame.turn().discovered_cards()+1) <= hieraGame.turn().hint_count()){
                        score = hieraGame.turn().discovered_cards() + 1;
                        turnDAO.update(new Turn(hieraGame.turn().id(), hieraGame.turn().hint(), hieraGame.turn().score() + score, hieraGame.turn().status(), hieraGame.turn().hint_count(), hieraGame.turn().discovered_cards() + 1, hieraGame.id()));
                        gameDAO.update(new Game(hieraGame.id(), hieraGame.score() + score, hieraGame.code(), hieraGame.status(), "operative"));
                    }else{
                        score = (int) Math.pow(hieraGame.turn().hint_count(), 2);
                        turnDAO.update(new Turn(hieraGame.turn().id(), hieraGame.turn().hint(), hieraGame.turn().score() + score, hieraGame.turn().status(), hieraGame.turn().hint_count(), hieraGame.turn().discovered_cards() + 1, hieraGame.id()));
                        gameDAO.update(new Game(hieraGame.id(), hieraGame.score() + score, hieraGame.code(), hieraGame.status(), "spymaster"));
                        turnDAO.insert(new Turn(0, "", 0, "pending", 0, 0, hieraGame.id()));
                    }
                }

                ApplicationController.diffuseGame(hieraGame.code(), context);
            } else {
                context.getResponse().notFound("Game not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.getResponse().serverError("Error checking card");
        }
    }
}
