import controllers.CardController;
import controllers.ColorController;
import controllers.GameController;
import controllers.PlayerController;
import controllers.TurnController;
import controllers.WordController;
import webserver.WebServer;
import webserver.WebServerContext;

public class App {
    public static void main(String[] args) throws Exception {
        WebServer webServer = new WebServer();
        webServer.listen(8080);

        // Routes for Players
        webServer.getRouter().get(
            "/players",
            (WebServerContext context) -> { PlayerController.findAll(context); }
        );

        webServer.getRouter().get(
            "/players/:playerId",
            (WebServerContext context) -> { PlayerController.findById(context); }
        );

        webServer.getRouter().post(
            "/players",
            (WebServerContext context) -> { PlayerController.create(context); }
        );

        webServer.getRouter().put(
            "/players/:playerId",
            (WebServerContext context) -> { PlayerController.update(context); }
        );

        webServer.getRouter().delete(
            "/players/:playerId",
            (WebServerContext context) -> { PlayerController.delete(context); }
        );

        // Routes for Games
        webServer.getRouter().get(
            "/games",
            (WebServerContext context) -> { GameController.findAll(context); }
        );

        webServer.getRouter().get(
            "/games/:gameCode",
            (WebServerContext context) -> { GameController.findHierarchicalGameByCode(context); }
        );

        webServer.getRouter().post(
            "/games",
            (WebServerContext context) -> { GameController.create(context); }
        );

        webServer.getRouter().post(
            "/games/:gameCode/check-card",
            (WebServerContext context) -> { GameController.checkCard(context); }
        );

        webServer.getRouter().put(
            "/games/:gameId",
            (WebServerContext context) -> { GameController.update(context); }
        );

        webServer.getRouter().delete(
            "/games/:gameId",
            (WebServerContext context) -> { GameController.delete(context); }
        );

        // Routes for Turns
        webServer.getRouter().get(
            "/turns",
            (WebServerContext context) -> { TurnController.findAll(context); }
        );

        webServer.getRouter().get(
            "/turns/:turnId",
            (WebServerContext context) -> { TurnController.findById(context); }
        );

        webServer.getRouter().post(
            "/turns",
            (WebServerContext context) -> { TurnController.create(context); }
        );

        webServer.getRouter().put(
            "/turns/:turnId",
            (WebServerContext context) -> { TurnController.update(context); }
        );

        webServer.getRouter().delete(
            "/turns/:turnId",
            (WebServerContext context) -> { TurnController.delete(context); }
        );

        // Routes for Cards
        webServer.getRouter().get(
            "/cards",
            (WebServerContext context) -> { CardController.findAll(context); }
        );

        webServer.getRouter().get(
            "/cards/:cardId",
            (WebServerContext context) -> { CardController.findById(context); }
        );

        webServer.getRouter().post(
            "/cards",
            (WebServerContext context) -> { CardController.create(context); }
        );

        webServer.getRouter().put(
            "/cards/:cardId",
            (WebServerContext context) -> { CardController.update(context); }
        );

        webServer.getRouter().delete(
            "/cards/:cardId",
            (WebServerContext context) -> { CardController.delete(context); }
        );

        // Routes for Words
        webServer.getRouter().get(
            "/words",
            (WebServerContext context) -> { WordController.findAll(context); }
        );

        webServer.getRouter().get(
            "/words/:wordId",
            (WebServerContext context) -> { WordController.findById(context); }
        );

        webServer.getRouter().post(
            "/words",
            (WebServerContext context) -> { WordController.create(context); }
        );

        webServer.getRouter().put(
            "/words/:wordId",
            (WebServerContext context) -> { WordController.update(context); }
        );

        webServer.getRouter().delete(
            "/words/:wordId",
            (WebServerContext context) -> { WordController.delete(context); }
        );

        // Routes for Colors
        webServer.getRouter().get(
            "/colors",
            (WebServerContext context) -> { ColorController.findAll(context); }
        );

        webServer.getRouter().get(
            "/colors/:colorId",
            (WebServerContext context) -> { ColorController.findById(context); }
        );

        webServer.getRouter().post(
            "/colors",
            (WebServerContext context) -> { ColorController.create(context); }
        );

        webServer.getRouter().put(
            "/colors/:colorId",
            (WebServerContext context) -> { ColorController.update(context); }
        );

        webServer.getRouter().delete(
            "/colors/:colorId",
            (WebServerContext context) -> { ColorController.delete(context); }
        );
    }
}
