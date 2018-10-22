package web.schach.gruppe6.gui.customComponents;

import javafx.scene.layout.GridPane;
import web.schach.gruppe6.gui.util.ColorEnum;
import web.schach.gruppe6.obj.Position;

public class ChessTileField extends GridPane implements TileField {
	
	private Tile[][] chessFieldComponents = new Tile[8][8];
	
	public ChessTileField() {
		setupFillers();
		setupLineCounters();
		setupChessFields();
		setMinSize(300, 300);
	}
	
	private void setupFillers() {
		add(new Tile(ColorEnum.BROWN), 0, 0);
		add(new Tile(ColorEnum.BROWN), 9, 0);
		add(new Tile(ColorEnum.BROWN), 0, 9);
		add(new Tile(ColorEnum.BROWN), 9, 9);
	}
	
	private void setupLineCounters() {
		for (int x = 1; x < 9; x++) {
			add(new LineCountField(x, true), x, 0);
		}
		for (int x = 1; x < 9; x++) {
			add(new LineCountField(x, true), x, 9);
		}
		for (int y = 1; y < 9; y++) {
			add(new LineCountField(y, false), 0, y);
		}
		for (int y = 1; y < 9; y++) {
			add(new LineCountField(y, false), 9, y);
		}
	}
	
	private void setupChessFields() {
		boolean secondRow = false;
		boolean giveColor = false;
		for (int x = 1; x < 9; x++) {
			if (secondRow) {
				giveColor = true;
				secondRow = false;
			} else {
				giveColor = false;
				secondRow = true;
			}
			for (int y = 1; y < 9; y++) {
				Tile tile;
				if (giveColor) {
					tile = new Tile(ColorEnum.BROWN);
					giveColor = false;
				} else {
					tile = new Tile(ColorEnum.WHITE);
					giveColor = true;
				}
				tile.setStyle("-fx-border-color: white ;" + tile.getStyle());
				chessFieldComponents[x - 1][y - 1] = tile;
				add(tile, x, y);
			}
		}
	}
	
	public Tile[][] getFieldComponents() {
		return chessFieldComponents;
	}
	
	public void mark(Position pos) {
		Tile tile = chessFieldComponents[pos.x][pos.y];
		tile.setStyle("-fx-border-color: red; \r\n" + "-fx-border-width: 3; \r\n" + "-fx-background-color: " + tile.getColor() + ";\r\n");
	}
	
	public void unmark(Position pos) {
		Tile tile = chessFieldComponents[pos.x][pos.y];
		tile.setStyle("-fx-border-color: red " + "-fx-border-color: " + tile.getColor().toString());
	}
}
