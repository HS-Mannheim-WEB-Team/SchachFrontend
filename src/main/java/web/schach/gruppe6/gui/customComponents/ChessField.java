package web.schach.gruppe6.gui.customComponents;

import javafx.scene.layout.GridPane;
import web.schach.gruppe6.gui.util.ColorEnum;


public class ChessField extends GridPane {
    Tile[][] chessFieldComponents = new Tile[8][8];

    public ChessField() {
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
        for (int x = 1; x < 9; x++) {
            for (int y = 1; y < 9; y++) {
                Tile tile;
                if (x % 2 == 0 || y % 2 != 0) {
                    tile = new Tile(ColorEnum.WHITE);
                } else {
                    tile = new Tile(ColorEnum.WHITE);
                }
                chessFieldComponents[x - 1][y - 1] = tile;
                add(tile, x, y);
            }
        }
    }

    public Tile[][] getChessFieldComponents() {
        return chessFieldComponents;
    }

}
