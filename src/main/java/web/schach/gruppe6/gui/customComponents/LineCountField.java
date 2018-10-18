package web.schach.gruppe6.gui.customComponents;

import javafx.scene.control.Label;
import web.schach.gruppe6.gui.util.ColorEnum;

public class LineCountField extends Tile {

    public LineCountField(int pos, boolean inLetters) {
        super(ColorEnum.BROWN);
        Label label = new Label();
        if (inLetters)
            label.setText("" + (char) ('A' + pos - 1));
        else
            label.setText("" + pos);
    }

}
