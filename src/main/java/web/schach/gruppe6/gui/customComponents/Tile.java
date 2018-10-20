package web.schach.gruppe6.gui.customComponents;

import javafx.geometry.Bounds;
import javafx.scene.layout.StackPane;
import web.schach.gruppe6.gui.util.ColorEnum;

public class Tile extends StackPane {
	
	public Tile() {
		this(ColorEnum.BROWN);
	}
	
	public Tile(ColorEnum color) {
		setStyle("-fx-background-color: " + color.toString());
	}
	
	public Bounds getBounds() {
		return this.localToScene(this.getBoundsInLocal());
	}
}
