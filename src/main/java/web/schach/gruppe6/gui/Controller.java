package web.schach.gruppe6.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import web.schach.gruppe6.gui.customComponents.*;
import web.schach.gruppe6.gui.util.ColorEnum;
import web.schach.gruppe6.obj.*;
import web.schach.gruppe6.util.Task;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller {

    private static final int SHUFFLE_COUNT = 2;
    private static final long TIME_MOVEMENT_TOTAL_NANO = 1000 * 1000000;
    private static final long TIME_MOVEMENT_STEP_NANO = 20 * 1000000;

    private boolean messageListIsVisible = true;
    private boolean menuIsVisible = true;
    private boolean listVisible = true;

    private Layout layoutCurrent = Layout.INITIAL_LAYOUT;
    private Map<Figures, ImageView> figureViewMap = new EnumMap<>(Figures.class);
    private BlockingQueue<Layout> layoutQueue = new LinkedBlockingQueue<>();

    //HIDE&SHOW BUTTONS

    @FXML
    private Button logButton;

    @FXML
    private Button messageButton;

    @FXML
    private Button optionButton;


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
    @SuppressWarnings("unused")
    private TextField iDTextField;


    //LIST VIEW

    @FXML
    private OccupancyListView occupancyListView;

    @FXML
    private MessageListView messageListView;

    //TILE FIELDS
    @FXML
    private FlowPane globalCenterFlowPlane;

    @FXML
    private BeatenTileField beatenFiguresTop;

    @FXML
    private ChessTileField chessField;

    @FXML
    private BeatenTileField beatenFiguresBot;

    @FXML
    private Pane chessFieldPane;

    @FXML
    private Pane shuffleControlPane;

    public BeatenTileField getBeatenFiguresTop() {
        return beatenFiguresTop;
    }

    public ChessTileField getChessField() {
        return chessField;
    }

    public BeatenTileField getBeatenFiguresBot() {
        return beatenFiguresBot;
    }

    public OccupancyListView getOccupancyListView() {
        return occupancyListView;
    }

    public MessageListView getMessageListView() {
        return messageListView;
    }

    @FXML
    void initialize() {
        globalCenterFlowPlane.setScaleX(ChessGUI.SCALE_FACTOR);
        chessFieldPane.setScaleY(ChessGUI.SCALE_FACTOR);
        occupancyListView.setScaleY(ChessGUI.SCALE_FACTOR);
        setupListeners();
        launchLayoutHandler();

        //testing
        occupancyListView.addItem("test");
        occupancyListView.addItem("test2");
        joinButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            Alert message = getMessage(AlertType.ERROR, "Test Connection", "Results:", "Connect successfully!");
            messageListView.addItem(message);
            shake();
        });
        newGameButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            Alert message = getMessage(AlertType.WARNING, "Test Connection", "Results:", "WARNING");
            messageListView.addItem(message);
            shake();
        });

        saveButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            Alert message = getMessage(AlertType.INFORMATION, "Test Connection", "Results:", "Game NOT saved!");
            messageListView.addItem(message);
        });
    }

    private void setupListeners() {
        occupancyListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> System.out.println(newValue));
        messageListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> newValue.showAndWait());
    }

    private void switchVisibility(Node node, Button button, boolean curVisible) {
        if (curVisible) {
            node.setVisible(false);
            setBackgroundColor(button, ColorEnum.GREEN);
        } else {
            node.setVisible(true);
            setBackgroundColor(button, ColorEnum.RED);
        }
    }

    public void switchEventLogVisibility() {
        switchVisibility(messageListView, messageButton, messageListIsVisible);
        messageListIsVisible = !messageListIsVisible;
    }

    public void switchMenuVisibility() {
        switchVisibility(menuSocket, optionButton, menuIsVisible);
        menuIsVisible = !menuIsVisible;
    }

    public void switchListVisibility() {
        switchVisibility(occupancyListView, logButton, listVisible);
        listVisible = !listVisible;
    }

    public static ImageView getFigureIcon(Figures figures) {
        ImageView newImage = new ImageView();
        newImage.setImage(new Image(figures.getIconPath()));
        newImage.setFitWidth(30);
        newImage.setFitHeight(30);
        return newImage;
    }

    /**
     * removes all other styles. Use only if the Node has no style sheet and the border isn t changed
     */
    private void setBackgroundColor(Node component, ColorEnum color) {
        component.setStyle("-fx-background-color: " + color.toString());
    }

    /**
     * @param type should only use ERROR,WARNING or INFORMATION
     */
    public Alert getMessage(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    public void shake() {
        new Thread(() -> {
            //FIXME: needs updating
            for (int i = 0; i < SHUFFLE_COUNT; i++) {
                shuffleControlPane.setPrefSize(shuffleControlPane.getPrefWidth() - 10, shuffleControlPane.getPrefHeight());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
                shuffleControlPane.setPrefSize(shuffleControlPane.getPrefWidth() + 20, shuffleControlPane.getPrefHeight());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
                shuffleControlPane.setPrefSize(shuffleControlPane.getPrefWidth() - 10, shuffleControlPane.getPrefHeight());
            }
        }, "Shaker").start();
    }

    private static Bounds getRelativeBounds(Node child, Node parent) {
        Bounds ret = child.getBoundsInLocal();
        while (!child.equals(parent)) {
            ret = child.localToParent(ret);
            child = child.getParent();
        }
        return ret;
    }

    //layout handling
    public void addLayout(Layout layout) {
        layoutQueue.add(layout);
    }

    private void launchLayoutHandler() {
        Thread th = new Thread(() -> {
            try {
                Task setInitialLayout = new Task(() -> {
                    for (Entry<Figures, Position> entry : layoutCurrent.entrySet()) {
                        Figures figure = entry.getKey();
                        Position position = entry.getValue();

                        ImageView icon = getFigureIcon(figure);
                        chessFieldPane.getChildren().add(icon);
                        figureViewMap.put(figure, icon);

                        Tile desTile = ((TileField) chessField).getFieldComponents()[position.x][position.y];
                        Bounds rel = getRelativeBounds(desTile, chessFieldPane);
                        icon.setLayoutX(rel.getMinX());
                        icon.setLayoutY(rel.getMinY());
                    }
                });
                Platform.runLater(setInitialLayout);
                setInitialLayout.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
                return;
            }

            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    Layout layoutDest = layoutQueue.take();
                    Layout layoutSrc = layoutCurrent;
                    layoutCurrent = layoutDest;

                    long timeStart = System.nanoTime();
                    long timeEnd = timeStart + TIME_MOVEMENT_TOTAL_NANO;
                    EnumMap<Figures, Movement> movements = new EnumMap<>(Figures.class);

                    Task taskCalcMovement = new Task(() -> {
                        for (Figures figure : Figures.values()) {
                            Position positionSrc = layoutSrc.get(figure);
                            Position positionDest = layoutDest.get(figure);
                            if (!Objects.equals(positionSrc, positionDest)) {

                                PositionOnField pofSrc = resolvePositionOnField(figure, positionSrc);
                                PositionOnField pofDest = resolvePositionOnField(figure, positionDest);

                                Tile nodeSrc = pofSrc.field.getFieldComponents()[pofSrc.position.x][pofSrc.position.y];
                                Tile nodeDest = pofDest.field.getFieldComponents()[pofDest.position.x][pofDest.position.y];

                                Bounds boundsSrc = getRelativeBounds(nodeSrc, chessFieldPane);
                                Bounds boundsDest = getRelativeBounds(nodeDest, chessFieldPane);

                                movements.put(figure, new Movement(
                                        new Vector(boundsSrc.getMinX(), boundsSrc.getMinY()),
                                        timeStart,
                                        new Vector(boundsDest.getMinX(), boundsDest.getMinY()),
                                        timeEnd));
                            }
                        }
                    });
                    Platform.runLater(taskCalcMovement);
                    taskCalcMovement.await();

                    long timeNextUpdate = timeStart;
                    while (true) {
                        //timer
                        long timeCurr;
                        while (true) {
                            timeCurr = System.nanoTime();
                            long timeUntilNextUpdate = timeNextUpdate - timeCurr;
                            if (timeUntilNextUpdate <= 0)
                                break;
                            Thread.sleep(timeUntilNextUpdate / 1000000);
                        }
                        timeNextUpdate += TIME_MOVEMENT_STEP_NANO;
                        if (timeNextUpdate >= timeEnd)
                            break;

                        updateMovement(movements, timeCurr);
                    }

                    updateMovement(movements, timeEnd);
                } catch (InterruptedException ignore) {

                }
            }
        }, "LayoutHandler");
        th.setDaemon(true);
        th.start();
    }

    private void updateMovement(EnumMap<Figures, Movement> movements, long timeCurr) throws InterruptedException {
        Task taskUpdatePosition = new Task(() -> {
            for (Entry<Figures, Movement> entry : movements.entrySet()) {
                Movement movement = entry.getValue();
                if (movement == null)
                    continue;

                Figures figure = entry.getKey();
                Vector currPosition = movement.getPosition(timeCurr);

                ImageView imageView = figureViewMap.get(figure);
                imageView.setLayoutX(currPosition.x);
                imageView.setLayoutY(currPosition.y);
            }
        });
        Platform.runLater(taskUpdatePosition);
        taskUpdatePosition.await();
    }

    private PositionOnField resolvePositionOnField(Figures figure, Position position) {
        if (position != null)
            return new PositionOnField(chessField, position);
        return new PositionOnField(figure.color == PlayerColor.WHITE ? beatenFiguresTop : beatenFiguresBot, figure.positionBeaten);
    }

    private static class PositionOnField {

        final TileField field;
        final Position position;

        public PositionOnField(TileField field, Position position) {
            this.field = field;
            this.position = position;
        }
    }

    private static class Movement {

        final Vector from;
        final long fromTime;
        final Vector to;
        final long toTime;
        final Vector delta;
        final long deltaTime;

        public Movement(Vector from, long fromTime, Vector to, long toTime) {
            this.from = from;
            this.fromTime = fromTime;
            this.to = to;
            this.toTime = toTime;
            this.delta = to.sub(from);
            this.deltaTime = toTime - fromTime;
        }

        public Vector getPosition(long currTime) {
            if (currTime <= fromTime)
                return from;
            if (currTime >= toTime)
                return to;

            float d = (float) (currTime - fromTime) / deltaTime;
            d = d * d * (3 - 2 * d);
            return from.add(delta.multiply(d));
        }

        @Override
        public String toString() {
            return "Movement{" +
                    "from=" + from +
                    ", fromTime=" + fromTime +
                    ", to=" + to +
                    ", toTime=" + toTime +
                    '}';
        }
    }
}
