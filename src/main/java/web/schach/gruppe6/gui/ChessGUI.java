package web.schach.gruppe6.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChessGUI extends Application {
	
	public static final double SCALE_FACTOR = 1.5;
	public static boolean SWAP_START_TO_END_NOT_NORMAL = false;
	
	public Controller controller;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChessGUI.fxml"));
		Parent root = loader.load();
		root.setId("pane");
		Scene scene = new Scene(root, SCALE_FACTOR * 800, SCALE_FACTOR * 600);
		scene.getStylesheets().addAll(this.getClass().getResource("styles/BackgroundStyle.css").toExternalForm());
		primaryStage.setResizable(false);
		primaryStage.setTitle("WEB-Schach");
		primaryStage.setScene(scene);
		primaryStage.show();
		controller = loader.getController();

//		//networking
//		Thread networkThread = new Thread(() -> {
//			Game game = new Game(1);
//
//			if (!SWAP_START_TO_END_NOT_NORMAL) {
//				int moveId = 0;
//				//noinspection InfiniteLoopStatement
//				while (true) {
//					List<Layout> layouts = game.getList();
//					for (; moveId < layouts.size(); moveId++)
//						controller.addLayout(layouts.get(moveId));
//
////					try {
////						game.update();
////						Thread.sleep(1000);
////					} catch (InterruptedException | IOException ignore) {
////
////					}
//				}
//			} else {
////				try {
////					game.update();
////				} catch (IOException e) {
////					e.printStackTrace();
////				}
//
//				List<Layout> layouts = game.getList();
//				int count = layouts.size() - 1;
//				System.out.println(count);
//				//noinspection InfiniteLoopStatement
//				while (true) {
//					controller.addLayout(layouts.get(count));
//					controller.addLayout(layouts.get(0));
//
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException ignore) {
//
//					}
//				}
//			}
//		}, "NetworkThread");
//		networkThread.setDaemon(true);
//		networkThread.start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
