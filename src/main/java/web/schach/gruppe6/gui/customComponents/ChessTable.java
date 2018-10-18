package web.schach.gruppe6.gui.customComponents;

import javafx.scene.layout.BorderPane;


public class ChessTable extends BorderPane {

    ChessField chessField = new ChessField();
    BeatenField beatenAreaWhite = new BeatenField();
    BeatenField beatenAreaBlack = new BeatenField();

    public ChessTable() {
        super();
        setTop(beatenAreaBlack);
        setCenter(chessField);
        setBottom(beatenAreaWhite);
        setMinSize(300, 420);
    }

    public Tile[][] getChessFieldTiles() {
        return chessField.getChessFieldComponents();
    }

    public Tile[][] getBeatenAreaWhiteTiles() {
        return beatenAreaWhite.getBeatenFieldComponents();
    }

    public Tile[][] getBeatenAreaBlackTiles() {
        return beatenAreaBlack.getBeatenFieldComponents();
    }


}
