package web.schach.gruppe6.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import web.schach.gruppe6.obj.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ChessGUI extends Application {

    public static final double SCALE_FACTOR = 1;

	public Controller controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
        try {
            resize("out/production/resources/web/schach/gruppe6/gui/iconsAndImages/backgroundPicture.jpg", "out/production/resources/web/schach/gruppe6/gui/iconsAndImages/backgroundPicture.jpg", (int) SCALE_FACTOR * 800, (int) SCALE_FACTOR * 600);
        }catch(IOException e) {
            e.printStackTrace();
        }
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChessGUI.fxml"));
		Parent root = loader.load();
		root.setId("pane");
		Scene scene = new Scene(root, SCALE_FACTOR * 800, SCALE_FACTOR * 600);
		primaryStage.setResizable(false);
        primaryStage.setTitle("WEB-Schach");
        scene.getStylesheets().addAll(this.getClass().getResource("styles/BackgroundStyle.css").toExternalForm());
        primaryStage.setScene(scene);
		primaryStage.show();
		controller = loader.getController();
		scene.widthProperty().addListener((obs, oldVal, newVal) -> {

		});

		scene.heightProperty().addListener((obs, oldVal, newVal) -> {
			// Do whatever you want
		});

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

	public static void resize(String inputImagePath, String outputImagePath, int scaledWidth, int scaledHeight)
			throws IOException {
		// reads input image
		File inputFile = new File(inputImagePath);
		BufferedImage inputImage = ImageIO.read(inputFile);

		// creates output image
		BufferedImage outputImage = new BufferedImage(scaledWidth,
				scaledHeight, inputImage.getType());

		// scales the input image to the output image
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
		g2d.dispose();

		// extracts extension of output file
		String formatName = outputImagePath.substring(outputImagePath
				.lastIndexOf(".") + 1);

		// writes to output file
		ImageIO.write(outputImage, formatName, new File(outputImagePath));
        outputImage.flush();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
