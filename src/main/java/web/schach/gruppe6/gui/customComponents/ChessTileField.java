package web.schach.gruppe6.gui.customComponents;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import web.schach.gruppe6.gui.util.ColorEnum;
import web.schach.gruppe6.obj.Position;

import java.util.function.Consumer;

public class ChessTileField extends GridPane implements TileField {
	
	private ClickAbleTile[][] chessFieldComponents = new ClickAbleTile[8][8];
	private Consumer<Position> onClickCallback;
	
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
	
	public void mark(Position pos) {
		Tile tile = chessFieldComponents[pos.x][pos.y];
		tile.setStyle("-fx-border-color: red; \r\n" + "-fx-border-width: 3; \r\n" + "-fx-background-color: " + tile.getColor() + ";\r\n");
	}
	
	public void unmark(Position pos) {
		Tile tile = chessFieldComponents[pos.x][pos.y];
		tile.setStyle("-fx-border-color: white ;\r\n" + "-fx-background-color: " + tile.getColor().toString());
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
