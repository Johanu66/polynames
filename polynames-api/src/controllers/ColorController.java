package controllers;

import dao.ColorDAO;
import models.Color;
import webserver.WebServerContext;

public class ColorController {
    static public ColorDAO colorDAO = new ColorDAO();

    static public void findAll(WebServerContext context) {
        context.getResponse().json(colorDAO.findAll());
    }

    static public void findById(WebServerContext context) {
        int colorId = Integer.parseInt(context.getRequest().getParam("colorId"));
        try {
            Color color = colorDAO.findById(colorId);
            if (color != null) {
                context.getResponse().json(color);
            } else {
                context.getResponse().notFound("Color not found");
            }
        } catch (Exception e) {
            context.getResponse().serverError("Error finding color");
        }
    }

    static public void create(WebServerContext context) {
        try {
            Color color = context.getRequest().extractBody(Color.class);
            colorDAO.insert(color);
            context.getResponse().ok("Color created successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error creating color");
        }
    }

    static public void update(WebServerContext context) {
        int colorId = Integer.parseInt(context.getRequest().getParam("colorId"));
        try {
            Color color = context.getRequest().extractBody(Color.class);
            colorDAO.update(new Color(colorId, color.name()));
            context.getResponse().ok("Color updated successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error updating color");
        }
    }

    static public void delete(WebServerContext context) {
        int colorId = Integer.parseInt(context.getRequest().getParam("colorId"));
        try {
            colorDAO.delete(colorId);
            context.getResponse().ok("Color deleted successfully");
        } catch (Exception e) {
            context.getResponse().serverError("Error deleting color");
        }
    }
}
