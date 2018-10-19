package web.schach.gruppe6.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import web.schach.gruppe6.obj.FigureType;
import web.schach.gruppe6.obj.PlayerColor;
import web.schach.gruppe6.obj.Position;

public class ChessGUI extends Application {
    public static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChessGUI.fxml"));
        Parent root = loader.load();
        root.setId("pane");
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().addAll(this.getClass().getResource("styles/backgroundStyle.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setTitle("WEB-Chess");
        primaryStage.setScene(scene);
        primaryStage.show();
        controller = loader.getController();

        //testing
        controller.placeIcon(controller.getBeatenFiguresBot(), new Position(1, 1), FigureType.PAWN, PlayerColor.BLACK);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
