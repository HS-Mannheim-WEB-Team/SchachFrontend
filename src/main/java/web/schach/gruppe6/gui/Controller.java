package web.schach.gruppe6.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import web.schach.gruppe6.gui.customComponents.*;
import web.schach.gruppe6.gui.util.ColorEnum;
import web.schach.gruppe6.obj.FigureType;
import web.schach.gruppe6.obj.PlayerColor;
import web.schach.gruppe6.obj.Position;


public class Controller {
    private final int MOVINGPARTS = 20;
    private boolean menuIsVisible = true;
    private boolean listVisible = true;


    //HIDE&SHOW BUTTONS

    @FXML
    private Button logButton;

    @FXML
    private Button optionButton;

    @FXML
    private ImageView optionImage;


    //MENU

    @FXML
    private FlowPane menuSocket;

    @FXML
    private Button saveButton;

    @FXML
    private Button newGameButton;

    @FXML
    private Button joinButton;

    @FXML
    private TextField iDTextField;


    //LIST VIEW

    @FXML
    private OccupancyListView listView;


    //TILE FIELDS

    @FXML
    private BeatenTileField beatenFiguresTop;

    @FXML
    private ChessTileField chessField;


    @FXML
    private BeatenTileField beatenFiguresBot;

    @FXML
    //place icons in here
    private Pane chessFieldPane;

    public BeatenTileField getBeatenFiguresTop() {
        return beatenFiguresTop;
    }

    public ChessTileField getChessField() {
        return chessField;
    }

    public BeatenTileField getBeatenFiguresBot() {
        return beatenFiguresBot;
    }

    @FXML
    void initialize() {
        //testing
        listView.addItem("test", new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            }
        });
        listView.addItem("test2", new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            }
        });
        joinButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                showMessage(Alert.AlertType.INFORMATION, "Test Connection", "Results:", "Connect successfully!");
            }
        });
        newGameButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                showMessage(Alert.AlertType.WARNING, "Test Connection", "Results:", "WARNING");
            }
        });

        saveButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new Thread() {
                    public void run() {
                        moveIcon(beatenFiguresBot, new Position(1, 1), beatenFiguresBot, new Position(6, 1));
                        moveIcon(beatenFiguresBot, new Position(6, 1), beatenFiguresBot, new Position(2, 0));
                        moveIcon(beatenFiguresBot, new Position(2, 0), beatenFiguresBot, new Position(0, 0));
                    }
                }.start();
            }

        });

    }

    public void switchMenuVisibility() {
        if (menuIsVisible) {
            menuSocket.setVisible(false);
            menuIsVisible = false;
            setBackgroundColor(optionButton, ColorEnum.GREEN);


        } else {
            menuSocket.setVisible(true);
            menuIsVisible = true;
            setBackgroundColor(optionButton, ColorEnum.RED);
        }
    }

    public void switchListVisibility() {
        if (listVisible) {
            listView.setVisible(false);
            listVisible = false;
            setBackgroundColor(logButton, ColorEnum.GREEN);


        } else {
            listView.setVisible(true);
            listVisible = true;
            setBackgroundColor(logButton, ColorEnum.RED);
        }
    }

    public static ImageView getFigureIcon(FigureType icon, PlayerColor color) {
        ImageView newImage = new ImageView();
        if (color == PlayerColor.BLACK)
            newImage.setImage(new Image(icon.iconPathBlack));
        else newImage.setImage(new Image(icon.iconPathWhite));
        newImage.setFitWidth(30);
        newImage.setFitHeight(30);
        return newImage;
    }

    /**
     * removes all other styles. Use only if the Region has no style sheet and the border isn t changed
     */
    private void setBackgroundColor(Region component, ColorEnum color) {
        component.setStyle("-fx-background-color: " + color.toString());

    }


    /**
     * @param type should only use ERROR,WARNING or INFORMATION
     */
    public void showMessage(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void moveIcon(TileField srcField, Position srcPos, TileField desField, Position desPos) {
        Tile srcTile = srcField.getFieldComponents()[srcPos.x][srcPos.y];
        Tile desTile = desField.getFieldComponents()[desPos.x][desPos.y];
        ImageView icon = srcTile.getIcon();
        double distanceX = (icon.getLayoutX() - 47.5) - desTile.getLayoutX();
        double distanceY = icon.getLayoutY() - desTile.getLayoutY();
        double stepX = distanceX / MOVINGPARTS;
        double stepY = distanceY / MOVINGPARTS;
        double startX = icon.getLayoutX();
        double startY = icon.getLayoutY();
        for (int stepCount = 0; stepCount < MOVINGPARTS; stepCount++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
            startX = startX - stepX;
            startY = startY - stepY;
            icon.setLayoutX(startX);
            icon.setLayoutY(startY);
        }

        srcTile.setIcon(null);
        desTile.setIcon(icon);
    }

    public void placeIcon(TileField desField, Position desPos, FigureType iconType, PlayerColor color) {
        ImageView icon = getFigureIcon(iconType, color);
        Tile desTile = desField.getFieldComponents()[desPos.x][desPos.y];
        desTile.setIcon(icon);
        chessFieldPane.getChildren().add(icon);
        icon.setLayoutX(47.5 + desTile.getLayoutX());
        icon.setLayoutY(desTile.getLayoutY());


    }


}
