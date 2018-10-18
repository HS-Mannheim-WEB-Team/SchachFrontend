package web.schach.gruppe6.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import web.schach.gruppe6.gui.customComponents.BeatenTileField;
import web.schach.gruppe6.gui.customComponents.ChessTileField;
import web.schach.gruppe6.gui.customComponents.TileField;
import web.schach.gruppe6.gui.util.ColorEnum;
import web.schach.gruppe6.obj.FigureType;
import web.schach.gruppe6.obj.PlayerColor;
import web.schach.gruppe6.obj.Position;


/*
 Should be used to add Listeners to to the WEBChess components
 or to add methods and reference to it in the scene Editor
 */
public class Controller {
    private final int TILEWIDTH = 30;
    private final int TILEHEIGHT = 30;

    private boolean menuIsVisible = true;
    private boolean listVisible = true;


    /*
        actual components of the web.schach.gruppe6.gui. Automatically assigned
    */
    @FXML
    private FlowPane menuSocket;


    @FXML
    private ListView listView;

    @FXML
    private Button logButton;

    @FXML
    private Button optionButton;

    @FXML
    private ImageView optionImage;

    @FXML
    private ChessTileField chessField;

    @FXML
    //place icons in here
    private Pane chessFieldPane;

    @FXML
    private Button saveButton;

    @FXML
    private Button newGameButton;


    @FXML
    private Button joinButton;


    @FXML
    private TextField iDTextField;

    @FXML
    private BeatenTileField beatenFiguresTop;

    @FXML
    private BeatenTileField beatenFiguresBot;


    // This method is called by the FXMLLoader when initialization is complete
    @FXML
    void initialize() {

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

    public void setBackgroundColor(Region component, ColorEnum color) {
        component.setStyle("-fx-background-color: " + color.toString());

    }


    public void getBondsOfBeatenFigures(TileField pane, Position pos) {
        pane.getFieldComponents()[pos.x][pos.y].getBounds();
    }


}
