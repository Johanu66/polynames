package controllers;

import dao.WordDAO;
import models.Word;
import webserver.WebServerContext;

public class WordController {
    static public WordDAO wordDAO = new WordDAO();

    static public void findAll(WebServerContext context) {
        context.getResponse().json(wordDAO.findAll());
    }

    static public void findById(WebServerContext context) {
        int wordId = Integer.parseInt(context.getRequest().getParam("wordId"));
        try {
            Word word = wordDAO.findById(wordId);
            if (word != null) {
                context.getResponse().json(word);
            } else {
                context.getResponse().notFound("Word not found");
            }
        } catch (Exception e) {
            context.getResponse().serverError("Error finding word");
        }
    }

    static public void create(WebServerContext context) {
        try {
            Word word = context.getRequest().extractBody(Word.class);
            wordDAO.insert(word);
            context.getResponse().ok("Word created successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error creating word");
        }
    }

    static public void update(WebServerContext context) {
        int wordId = Integer.parseInt(context.getRequest().getParam("wordId"));
        try {
            Word word = context.getRequest().extractBody(Word.class);
            wordDAO.update(new Word(wordId, word.text()));
            context.getResponse().ok("Word updated successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error updating word");
        }
    }

    static public void delete(WebServerContext context) {
        int wordId = Integer.parseInt(context.getRequest().getParam("wordId"));
        try {
            wordDAO.delete(wordId);
            context.getResponse().ok("Word deleted successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error deleting word");
        }
    }
}
