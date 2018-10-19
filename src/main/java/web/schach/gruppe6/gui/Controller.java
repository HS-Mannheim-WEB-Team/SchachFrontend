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
import web.schach.gruppe6.gui.customComponents.BeatenTileField;
import web.schach.gruppe6.gui.customComponents.ChessTileField;
import web.schach.gruppe6.gui.customComponents.OccupancyListView;
import web.schach.gruppe6.gui.customComponents.TileField;
import web.schach.gruppe6.gui.util.ColorEnum;
import web.schach.gruppe6.obj.FigureType;
import web.schach.gruppe6.obj.PlayerColor;
import web.schach.gruppe6.obj.Position;


public class Controller {
    private final int TILEWIDTH = 30;
    private final int TILEHEIGHT = 30;

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






    // This method is called by the FXMLLoader when initialization is complete
    @FXML
    void initialize() {
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
                showMessage(Alert.AlertType.ERROR, "saving", "Results:", "ERROR");
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
        return newImage;
    }

    /**
     * removes all other styles. Use only if the Region has no style sheet and the border isn t changed
     */
    public void setBackgroundColor(Region component, ColorEnum color) {
        component.setStyle("-fx-background-color: " + color.toString());

    }


    public void getBondsOfBeatenFigures(TileField pane, Position pos) {
        pane.getFieldComponents()[pos.x][pos.y].getBounds();
    }

    /**
     * @param type should only use ERROR,WARNING or INFORMATION
     */
    private void showMessage(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
