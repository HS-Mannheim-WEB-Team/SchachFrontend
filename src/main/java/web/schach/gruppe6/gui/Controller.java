package web.schach.gruppe6.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import web.schach.gruppe6.gui.util.ColorEnum;
import web.schach.gruppe6.gui.util.FigureIcons;
import web.schach.gruppe6.obj.Position;


/*
 Should be used to add Listeners to to the WEBChess components
 or to add methods and reference to it in the scene Editor
 */
public class Controller {
    private boolean menuIsVisible = true;
    private boolean logIsVisible = true;


    /*
        actual components of the web.schach.gruppe6.gui. Automatically assigned
    */
    @FXML
    private FlowPane menuSocket;


    @FXML
    private TextArea logArea;

    @FXML
    private Button logButton;

    @FXML
    private Button optionButton;

    @FXML
    private ImageView optionImage;

    @FXML
    private Button saveButton;

    @FXML
    private Button newGameButton;


    @FXML
    private Button joinButton;

    @FXML
    private Pane chessFieldPane;

    @FXML
    private TextField iDTextField;

    @FXML
    private GridPane beatenFiguresTop;

    @FXML
    private GridPane beatenFiguresBot;


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

    public void switchLogVisibility() {
        if (logIsVisible) {
            logArea.clear();
            logArea.setVisible(false);
            logIsVisible = false;
            setBackgroundColor(logButton, ColorEnum.GREEN);


        } else {
            logArea.setVisible(true);
            logIsVisible = true;
            setBackgroundColor(logButton, ColorEnum.RED);
        }
    }

    public static ImageView getFigureIcon(FigureIcons icon, boolean isWhite) {
        ImageView newImage = new ImageView();
        newImage.setImage(new Image(icon.getPath(isWhite)));
        return newImage;
    }

    public void removeFigureIcon(ImageView tile) {
        tile.setImage(null);
    }

    public void setBackgroundColor(Region component, ColorEnum color) {
        component.setStyle("-fx-background-color: " + color.toString());
    }

    public void addBeatenFigure(GridPane pane, FigureIcons icon) {
        ImageView figureIcon = Controller.getFigureIcon(icon, false);
        figureIcon.setFitHeight(30);
        figureIcon.setFitWidth(30);
        int i = pane.getChildren().size() - 10;
        if (i < 9)
            pane.add(figureIcon, pane.getChildren().size() - 16, 0);
        else pane.add(figureIcon, pane.getChildren().size() - 16, 1);
    }


    public void getBondsOfBeatenFigures(GridPane pane, Position pos) {

    }


}
