package web.schach.gruppe6.gui.customComponents;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import web.schach.gruppe6.gui.util.ColorEnum;
import web.schach.gruppe6.obj.Position;

import java.util.function.Consumer;

public class ChessTileField extends GridPane implements TileField {
	
	private ClickAbleTile[][] chessFieldComponents = new ClickAbleTile[8][8];
	
	private LineCountField[] borderTiles = new LineCountField[32];
	
	private Consumer<Position> onClickCallback;
	
	public ChessTileField() {
		setupFillers();
		setupLineCounters();
		setupChessFields();
		setMinSize(300, 300);
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
			borderTiles[x - 1 + 8] = countField;
		}
		for (int y = 1; y < 9; y++) {
			LineCountField countField = new LineCountField(y, false);
			add(countField, 0, y);
			borderTiles[y - 1 + 16] = countField;
		}
		for (int y = 1; y < 9; y++) {
			LineCountField countField = new LineCountField(y, false);
			add(countField, 9, y);
			borderTiles[y - 1 + 24] = countField;
		}
	}
	
	private void deleteLineCounters() {
		for (int i = 0; i < borderTiles.length; i++) {
			getChildren().remove(borderTiles[i]);
		}
	}
	
	public void setRegularLineCounters() {
		deleteLineCounters();
		for (int x = 7; x >= 0; x--) {
			add(borderTiles[x], x + 1, 0);
		}
		for (int x = 7; x >= 0; x--) {
			add(borderTiles[x + 8], x + 1, 9);
		}
		for (int y = 7; y >= 0; y--) {
			add(borderTiles[y + 16], 0, y + 1);
		}
		for (int y = 7; y >= 0; y--) {
			add(borderTiles[y + 24], 9, y + 1);
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
				ClickAbleTile tile;
				Position pos = new Position(x - 1, y - 1);
				if (giveColor) {
					tile = new ClickAbleTile(ColorEnum.BROWN, pos);
					giveColor = false;
				} else {
					tile = new ClickAbleTile(ColorEnum.WHITE, pos);
					giveColor = true;
				}
				tile.setStyle("-fx-border-color: white ;" + tile.getStyle());
				tile.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
					tile.doClick();
				});
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
		tile.setStyle((color != null ? "-fx-border-color: " + color + ";" : "") + " \r\n" + "-fx-border-width: 3; \r\n" + "-fx-background-color: " + tile.getColor() + ";\r\n");
	}
	
	public void unmark(Position pos) {
		mark(pos, null);
	}
	
	public void doClick(Position pos) {
		chessFieldComponents[pos.x][pos.y].doClick();
	}
	
	public class ClickAbleTile extends Tile {
		
		Position pos;
		
		public ClickAbleTile(ColorEnum color, Position pos) {
			super(color);
			this.pos = pos;
		}
		
		public void doClick() {
			if (onClickCallback != null)
				onClickCallback.accept(pos);
		}
	}
}
