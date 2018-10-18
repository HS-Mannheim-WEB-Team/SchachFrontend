package web.schach.gruppe6.gui.customComponents;

import javafx.scene.layout.GridPane;
import web.schach.gruppe6.gui.util.ColorEnum;

public class BeatenField extends GridPane {
    Tile[][] beatenFieldComponents = new Tile[8][2];

    public BeatenField() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 2; y++) {
                Tile tile = new Tile(ColorEnum.BROWN);
                beatenFieldComponents[x][y] = tile;
                add(tile, x, y);
            }
        }
    }

    public Tile[][] getBeatenFieldComponents() {
        return beatenFieldComponents;
    }


}
