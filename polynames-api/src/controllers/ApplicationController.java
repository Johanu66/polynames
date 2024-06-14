package controllers;

import java.sql.SQLException;

import dao.GameDAO;
import models.HieraGame;
import webserver.WebServerContext;

public class ApplicationController {
    static GameDAO gameDAO = new GameDAO();

    static public void diffuseGame(String code, WebServerContext context){
        HieraGame game;
        try {
            game = ApplicationController.gameDAO.findHierarchicalGameByCode(code);
            context.getSSE().emit(game.code(), game);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
