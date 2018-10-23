package web.schach.gruppe6.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ChessGUI extends Application {
	
	private static final int MIN_WIDTH = 800;
	private static final int MIN_HIGHT = 600;
	private static final float GENEREL_GROW_FACTOR = 1 / 600f;
	public static final float MESSAGE_GROW_FACTOR = 0.125f;
	public static final int DISTANCE_TO_WINDOW_BORDER = 15;
	
	public static final double INITIALSCALE = 1;
	public static final boolean OP_MODE_ALLOW_MOVE_BOTH = false;
	
	public Controller controller;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChessGUI.fxml"));
		Parent root = loader.load();
		root.setId("pane");
		Scene scene = new Scene(root, INITIALSCALE * MIN_WIDTH, INITIALSCALE * MIN_HIGHT);
		scene.getStylesheets().addAll(this.getClass().getResource("styles/BackgroundStyle.css").toExternalForm());
		primaryStage.getIcons().add(new Image("/web/schach/gruppe6/gui/iconsAndImages/iconset1/queen_white.png"));
		primaryStage.setMinWidth(MIN_WIDTH);
		primaryStage.setMinHeight(MIN_HIGHT);
		primaryStage.setTitle("WEB-Chess");
		primaryStage.setScene(scene);
		primaryStage.show();
		controller = loader.getController();
		//set current messageListView Scale
		controller.getMessageListView().setPrefSize(primaryStage.getWidth() - DISTANCE_TO_WINDOW_BORDER, MIN_HIGHT * MESSAGE_GROW_FACTOR);
		//add rescale on window size changes
		primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal.doubleValue() < primaryStage.getHeight())
				rescale(newVal);
			controller.getMessageButtonSocket().setPrefSize(newVal.doubleValue() - DISTANCE_TO_WINDOW_BORDER, 15);
			controller.getMessageListView().setPrefSize(newVal.doubleValue() - DISTANCE_TO_WINDOW_BORDER, primaryStage.getHeight() * MESSAGE_GROW_FACTOR);
		});
		
		primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal.doubleValue() < primaryStage.getWidth())
				rescale(newVal);
			controller.getMessageListView().setPrefSize(primaryStage.getWidth() - DISTANCE_TO_WINDOW_BORDER, newVal.doubleValue() * MESSAGE_GROW_FACTOR);
		});
	}
	
	private void rescale(Number newVal) {
		controller.getGlobalCenterFlowPlane().setScaleX(newVal.doubleValue() * GENEREL_GROW_FACTOR);
		controller.getChessFieldPane().setScaleY(newVal.doubleValue() * GENEREL_GROW_FACTOR);
		controller.getOccupancyListView().setScaleY(newVal.doubleValue() * GENEREL_GROW_FACTOR);
		controller.getShuffleControlPane().setScaleY(newVal.doubleValue() * GENEREL_GROW_FACTOR);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
