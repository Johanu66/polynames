package controllers;

import dao.CardDAO;
import models.Card;
import webserver.WebServerContext;

public class CardController {
    static public CardDAO cardDAO = new CardDAO();

    static public void findAll(WebServerContext context) {
        context.getResponse().json(cardDAO.findAll());
    }

    static public void findById(WebServerContext context) {
        int cardId = Integer.parseInt(context.getRequest().getParam("cardId"));
        try {
            Card card = cardDAO.findById(cardId);
            if (card != null) {
                context.getResponse().json(card);
            } else {
                context.getResponse().notFound("Card not found");
            }
        } catch (Exception e) {
            context.getResponse().serverError("Error finding card");
        }
    }

    static public void create(WebServerContext context) {
        try {
            Card card = context.getRequest().extractBody(Card.class);
            cardDAO.insert(card);
            context.getResponse().ok("Card created successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error creating card");
        }
    }

    static public void update(WebServerContext context) {
        int cardId = Integer.parseInt(context.getRequest().getParam("cardId"));
        try {
            Card card = context.getRequest().extractBody(Card.class);
            cardDAO.update(new Card(cardId, card.position(), card.status(), card.id_game(), card.id_color(), card.id_word()));
            context.getResponse().ok("Card updated successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error updating card");
        }
    }

    static public void delete(WebServerContext context) {
        int cardId = Integer.parseInt(context.getRequest().getParam("cardId"));
        try {
            cardDAO.delete(cardId);
            context.getResponse().ok("Card deleted successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error deleting card");
        }
    }
}
