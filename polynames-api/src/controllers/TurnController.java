package controllers;

import dao.TurnDAO;
import models.Turn;
import webserver.WebServerContext;

public class TurnController {
    static public TurnDAO turnDAO = new TurnDAO();

    static public void findAll(WebServerContext context) {
        context.getResponse().json(turnDAO.findAll());
    }

    static public void findById(WebServerContext context) {
        int turnId = Integer.parseInt(context.getRequest().getParam("turnId"));
        try {
            Turn turn = turnDAO.findById(turnId);
            if (turn != null) {
                context.getResponse().json(turn);
            } else {
                context.getResponse().notFound("Turn not found");
            }
        } catch (Exception e) {
            context.getResponse().serverError("Error finding turn");
        }
    }

    static public void create(WebServerContext context) {
        try {
            Turn turn = context.getRequest().extractBody(Turn.class);
            turnDAO.insert(turn);
            context.getResponse().ok("Turn created successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error creating turn");
        }
    }

    static public void update(WebServerContext context) {
        int turnId = Integer.parseInt(context.getRequest().getParam("turnId"));
        try {
            Turn turn = context.getRequest().extractBody(Turn.class);
            turnDAO.update(new Turn(turnId, turn.hint(), turn.score(), turn.status(), turn.hint_count(), turn.discovered_cards(), turn.id_game()));
            context.getResponse().ok("Turn updated successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error updating turn");
        }
    }

    static public void delete(WebServerContext context) {
        int turnId = Integer.parseInt(context.getRequest().getParam("turnId"));
        try {
            turnDAO.delete(turnId);
            context.getResponse().ok("Turn deleted successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error deleting turn");
        }
    }
}
