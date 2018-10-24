package web.schach.gruppe6.gui.customComponents;

import javafx.geometry.Bounds;
import javafx.scene.layout.StackPane;
import web.schach.gruppe6.gui.util.ColorEnum;

public class Tile extends StackPane {
	
	private ColorEnum color;
	
	public Tile() {
		this(ColorEnum.BROWN);
	}
	
	public Tile(ColorEnum color) {
		this.color = color;
		setStyle("-fx-background-color: " + color);
	}
	
	public Bounds getBounds() {
		return this.localToScene(this.getBoundsInLocal());
	}
	
	public ColorEnum getColor() {
		return color;
	}
}
