package web.schach.gruppe6.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import web.schach.gruppe6.obj.Game;

import java.io.IOException;

public class ChessGUI extends Application {
	
	public Controller controller;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChessGUI.fxml"));
		Parent root = loader.load();
		root.setId("pane");
		Scene scene = new Scene(root, 800, 600);
		scene.getStylesheets().addAll(this.getClass().getResource("styles/BackgroundStyle.css").toExternalForm());
		primaryStage.setResizable(false);
		primaryStage.setTitle("WEB-Chess");
		primaryStage.setScene(scene);
		primaryStage.show();
		controller = loader.getController();
		
		//networking
		Thread networkThread = new Thread(() -> {
			Game game = new Game(1);
			
			int moveId = 0;
			//noinspection InfiniteLoopStatement
			while (true) {
				for (; moveId < game.layouts.size(); moveId++)
					controller.addLayout(game.layouts.get(moveId));
				
				try {
					game.update();
					Thread.sleep(1000);
				} catch (InterruptedException | IOException ignore) {
				
				}
			}
		}, "NetworkThread");
		networkThread.setDaemon(true);
		networkThread.start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
