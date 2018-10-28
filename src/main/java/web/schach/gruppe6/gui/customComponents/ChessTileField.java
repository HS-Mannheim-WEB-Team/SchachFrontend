package web.schach.gruppe6.gui.customComponents;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import web.schach.gruppe6.gui.util.ColorEnum;
import web.schach.gruppe6.util.Position;

import java.util.function.Consumer;

public class ChessTileField extends GridPane implements TileField {
	
	private ClickAbleTile[][] chessFieldComponents = new ClickAbleTile[8][8];
	
	private LineCountField[] borderTiles = new LineCountField[32];
	
	private Consumer<Position> onClickCallback;
	
	public ChessTileField() {
		setupFillers();
		setupLineCounters();
		setupChessFields();
	}
	
	public LineCountField[] getBorderTiles() {
		return borderTiles;
	}
	
	private void setupFillers() {
		add(new Tile(ColorEnum.BROWN), 0, 0);
		add(new Tile(ColorEnum.BROWN), 9, 0);
		add(new Tile(ColorEnum.BROWN), 0, 9);
		add(new Tile(ColorEnum.BROWN), 9, 9);
	}
	
	private void setupLineCounters() {
		for (int x = 1; x < 9; x++) {
			LineCountField countField = new LineCountField(x, true);
			add(countField, x, 0);
			borderTiles[x - 1] = countField;
		}
		for (int x = 1; x < 9; x++) {
			LineCountField countField = new LineCountField(x, true);
			add(countField, x, 9);
			borderTiles[x + 7] = countField;
		}
		for (int y = 1; y < 9; y++) {
			LineCountField countField = new LineCountField(y, false);
			add(countField, 0, y);
			borderTiles[y + 15] = countField;
		}
		for (int y = 1; y < 9; y++) {
			LineCountField countField = new LineCountField(y, false);
			add(countField, 9, y);
			borderTiles[y + 23] = countField;
		}
	}
	
	private void deleteLineCounters() {
		for (LineCountField borderTile : borderTiles) {
			getChildren().remove(borderTile);
		}
	}
	
	public void setRegularLineCounters() {
		deleteLineCounters();
		for (int x = 8; x > 0; x--) {
			add(borderTiles[x - 1], x, 0);
		}
		for (int x = 8; x > 0; x--) {
			add(borderTiles[x + 7], x, 9);
		}
		for (int y = 8; y > 0; y--) {
			add(borderTiles[y + 15], 0, y);
		}
		for (int y = 8; y > 0; y--) {
			add(borderTiles[y + 23], 9, y);
		}
	}
	
	public void setReverseLineCounters() {
		deleteLineCounters();
		for (int x = 8; x > 0; x--) {
			add(borderTiles[8 - x], x, 0);
		}
		for (int x = 8; x > 0; x--) {
			add(borderTiles[16 - x], x, 9);
		}
		for (int y = 8; y > 0; y--) {
			add(borderTiles[24 - y], 0, y);
		}
		for (int y = 8; y > 0; y--) {
			add(borderTiles[32 - y], 9, y);
		}
	}
	
	private void setupChessFields() {
		boolean giveColor = false;
		for (int x = 1; x < 9; x++) {
			giveColor = !giveColor;
			for (int y = 1; y < 9; y++) {
				ClickAbleTile tile;
				Position pos = new Position(x - 1, y - 1);
				if (giveColor) {
					tile = new ClickAbleTile(ColorEnum.BROWN, pos);
				} else {
					tile = new ClickAbleTile(ColorEnum.WHITE, pos);
				}
				giveColor = !giveColor;
				tile.setStyle("-fx-border-color: white ;" + tile.getStyle());
				chessFieldComponents[pos.x][pos.y] = tile;
				add(tile, x, y);
			}
		}
	}
	
	public void setOnClickCallback(Consumer<Position> onClickCallback) {
		this.onClickCallback = onClickCallback;
	}
	
	public Tile[][] getFieldComponents() {
		return chessFieldComponents;
	}
	
	public void mark(Position pos, ColorEnum color) {
		Tile tile = chessFieldComponents[pos.x][pos.y];
		if (color != null)
			tile.setStyle("-fx-border-color: " + color + ";" + "-fx-border-width: 3; \r\n" + " \r\n" + "-fx-background-color: " + tile.getColor() + ";\r\n");
		else
			tile.setStyle("-fx-border-color: white;" + " \r\n" + "-fx-background-color: " + tile.getColor() + ";\r\n");
	}
	
	public void doClick(Position pos) {
		chessFieldComponents[pos.x][pos.y].doClick();
	}
	
	public class ClickAbleTile extends Tile {
		
		Position pos;
		
		public ClickAbleTile(ColorEnum color, Position pos) {
			super(color);
			this.pos = pos;
			addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
				doClick();
			});
		}
		
		public void doClick() {
			if (onClickCallback != null)
				onClickCallback.accept(pos);
		}
	}
}
