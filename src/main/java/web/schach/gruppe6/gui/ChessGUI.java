package web.schach.gruppe6.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ChessGUI extends Application {
	
	private static int MIN_WIDTH = 800;
	private static int MIN_HEIGHT = 600;
	
	public static final double SCALE_FACTOR = 1;
	public static boolean SWAP_START_TO_END_NOT_NORMAL = false;
	public Controller controller;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChessGUI.fxml"));
		Parent root = loader.load();
		root.setId("pane");
		Scene scene = new Scene(root, SCALE_FACTOR * MIN_WIDTH, SCALE_FACTOR * MIN_HEIGHT);
		scene.getStylesheets().addAll(this.getClass().getResource("styles/BackgroundStyle.css").toExternalForm());
		primaryStage.getIcons().add(new Image("/web/schach/gruppe6/gui/iconsAndImages/iconset1/queen_white.png"));
		primaryStage.setMinWidth(MIN_WIDTH);
		primaryStage.setMinHeight(MIN_HEIGHT);
		primaryStage.setTitle("WEB-Schach");
		primaryStage.setScene(scene);
		primaryStage.show();
		controller = loader.getController();
		
		primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal.doubleValue() < primaryStage.getHeight()) {
				controller.getGlobalCenterFlowPlane().setScaleX(newVal.doubleValue() / MIN_WIDTH);
				controller.getChessFieldPane().setScaleY(newVal.doubleValue() / MIN_WIDTH);
				controller.getOccupancyListView().setScaleY(newVal.doubleValue() / MIN_WIDTH);
				controller.getShuffleControlPane().setScaleY(newVal.doubleValue() / MIN_WIDTH);
			} else {
				controller.getGlobalCenterFlowPlane().setScaleX(primaryStage.getHeight() / MIN_HEIGHT);
				controller.getChessFieldPane().setScaleY(primaryStage.getHeight() / MIN_HEIGHT);
				controller.getOccupancyListView().setScaleY(primaryStage.getHeight() / MIN_HEIGHT);
				controller.getShuffleControlPane().setScaleY(primaryStage.getHeight() / MIN_HEIGHT);
			}
		});
		
		primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal.doubleValue() < primaryStage.getWidth()) {
				controller.getGlobalCenterFlowPlane().setScaleX(newVal.doubleValue() / MIN_HEIGHT);
				controller.getChessFieldPane().setScaleY(newVal.doubleValue() / MIN_HEIGHT);
				controller.getOccupancyListView().setScaleY(newVal.doubleValue() / MIN_HEIGHT);
				controller.getShuffleControlPane().setScaleY(newVal.doubleValue() / MIN_HEIGHT);
			} else {
				controller.getGlobalCenterFlowPlane().setScaleX(primaryStage.getWidth() / MIN_WIDTH);
				controller.getChessFieldPane().setScaleY(primaryStage.getWidth() / MIN_HEIGHT);
				controller.getOccupancyListView().setScaleY(primaryStage.getWidth() / MIN_HEIGHT);
				controller.getShuffleControlPane().setScaleY(primaryStage.getWidth() / MIN_HEIGHT);
			}
		});

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
