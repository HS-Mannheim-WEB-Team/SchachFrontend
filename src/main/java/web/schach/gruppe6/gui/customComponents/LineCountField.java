package web.schach.gruppe6.gui.customComponents;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import web.schach.gruppe6.gui.util.ColorEnum;

public class LineCountField extends StackPane {

    IntegerProperty pos = new SimpleIntegerProperty(this, "pos", 0);
    BooleanProperty inLetters = new SimpleBooleanProperty(this, "inLetters");

    Label label;

    public LineCountField(int pos, boolean inLetters) {
        this();
        this.pos.set(pos);
        this.inLetters.set(inLetters);
    }

    public LineCountField() {
        setStyle("-fx-background-color: " + ColorEnum.BROWN);
        this.pos.addListener(observable -> this.update());
        this.inLetters.addListener(observable -> this.update());

        label = new Label();
        label.setAlignment(Pos.CENTER);
        getChildren().add(label);
    }

    public void update() {
        if (inLetters.get())
            label.setText("" + (char) ('A' + pos.get() - 1));
        else
            label.setText("" + pos.get());
    }

    public IntegerProperty posProperty() {
        return pos;
    }

    public int getPos() {
        return pos.get();
    }

    public void setPos(int value) {
        pos.set(value);
    }

    public BooleanProperty inLettersProperty() {
        return inLetters;
    }

    public boolean getInLetters() {
        return inLetters.get();
    }

    public void setInLetters(boolean value) {
        inLetters.set(value);
    }

}
