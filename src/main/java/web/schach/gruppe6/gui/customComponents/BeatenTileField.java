package web.schach.gruppe6.gui.customComponents;

import javafx.scene.layout.GridPane;
import web.schach.gruppe6.gui.util.ColorEnum;

public class BeatenTileField extends GridPane implements TileField {
    private Tile[][] beatenFieldComponents = new Tile[8][2];


    public BeatenTileField() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 2; y++) {
                Tile tile = new Tile(ColorEnum.LIGHT_BROWN);
                tile.setStyle("-fx-border-color: white ;" + tile.getStyle());
                beatenFieldComponents[x][y] = tile;
                add(tile, x, y);
            }
        }

    }

    public Tile[][] getFieldComponents() {
        return beatenFieldComponents;
    }


}
