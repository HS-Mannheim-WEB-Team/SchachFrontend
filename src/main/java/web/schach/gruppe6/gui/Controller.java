package web.schach.gruppe6.gui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import web.schach.gruppe6.gui.customComponents.BeatenTileField;
import web.schach.gruppe6.gui.customComponents.ChessTileField;
import web.schach.gruppe6.gui.customComponents.ColorListView;
import web.schach.gruppe6.gui.customComponents.MessageListView;
import web.schach.gruppe6.gui.customComponents.OccupancyListView;
import web.schach.gruppe6.gui.customComponents.Tile;
import web.schach.gruppe6.gui.customComponents.TileField;
import web.schach.gruppe6.gui.util.ColorEnum;
import web.schach.gruppe6.network.ChessConnection;
import web.schach.gruppe6.obj.Figures;
import web.schach.gruppe6.obj.Layout;
import web.schach.gruppe6.obj.PlayerColor;
import web.schach.gruppe6.obj.Position;
import web.schach.gruppe6.obj.Vector;
import web.schach.gruppe6.util.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static javafx.collections.FXCollections.observableArrayList;

public class Controller {
	
	//static
	private static final int SHUFFLE_COUNT = 2;
	private static final long TIME_MOVEMENT_TOTAL_NANO = 1000 * 1000000;
	private static final long TIME_MOVEMENT_STEP_NANO = 20 * 1000000;
	public static final int TIME_NETWORK_QUERIES_IN_BETWEEN_MS = 250;
	
	public static final ChessConnection CONNECTION = new ChessConnection();
	
	private static Bounds getRelativeBounds(Node child, Node parent) {
		Bounds ret = child.getBoundsInLocal();
		while (!child.equals(parent)) {
			ret = child.localToParent(ret);
			child = child.getParent();
		}
		return ret;
	}
	
	//object
	private boolean messageListIsVisible = true;
	private boolean menuIsVisible = true;
	private boolean listVisible = true;
	
	private Game game;
	
	private Layout layoutCurrent = Layout.INITIAL_LAYOUT;
	private Map<Figures, ImageView> figureViewMap = new EnumMap<>(Figures.class);
	private BlockingQueue<Layout> layoutQueue = new LinkedBlockingQueue<>();
	
	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	private BlockingQueue<Boolean> shakeQueue = new LinkedBlockingQueue<>();
	
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

//	@FXML
//	private Button saveButton;
	
	@FXML
	private Button newGameButton;
	
	@FXML
	private Button joinButton;
	
	@FXML
	@SuppressWarnings("unused")
	private TextField iDTextField;
	
	@FXML
	private ColorListView colorSelectorListView;
	
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
		
		setupNetwork();
		setupLayoutHandler();
		setupShaker();
		
		setupListeners();
	}
	
	private void setupListeners() {
		messageListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> newValue.showAndWait());
	}
	
	//element visibility
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
	
	/**
	 * removes all other styles. Use only if the Node has no style sheet and the border isn t changed
	 */
	private void setBackgroundColor(Node component, ColorEnum color) {
		component.setStyle("-fx-background-color: " + color.toString());
	}
	
	//log
	
	/**
	 * @param type should only use ERROR,WARNING or INFORMATION
	 */
	public void logMessageWithJumpToLayout(Alert.AlertType type, String title, String content, int layoutIndex) {
		Platform.runLater(() -> messageListView.addItem(getMessageWithJumpToLayout(type, title, content, layoutIndex)));
	}
	
	/**
	 * @param type should only use ERROR,WARNING or INFORMATION
	 */
	public Alert getMessageWithJumpToLayout(Alert.AlertType type, String title, String content, int layoutIndex) {
		Alert alert = getMessage(type, title, content);
		alert.setOnCloseRequest(value -> {
			occupancyListView.scrollTo(layoutIndex);
			occupancyListView.getSelectionModel().clearAndSelect(layoutIndex);
		});
		return alert;
	}
	
	/**
	 * @param type should only use ERROR,WARNING or INFORMATION
	 */
	public void logMessage(Alert.AlertType type, String title, String content) {
		Platform.runLater(() -> messageListView.addItem(getMessage(type, title, content)));
	}
	
	/**
	 * @param type should only use ERROR,WARNING or INFORMATION
	 */
	private Alert getMessage(Alert.AlertType type, String title, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		String name = type.name();
		alert.setHeaderText(Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase());
		alert.setContentText(content);
		return alert;
	}
	
	//game
	public class Game {
		
		public final int id;
		public final PlayerColor color;
		public final ObservableList<Layout> layouts = observableArrayList();
		
		private Thread th;
		private volatile boolean isRunning = true;
		
		public Game(int id, PlayerColor color, boolean newGame) {
			this.id = id;
			this.color = color;
			this.th = new Thread(() -> {
				if (newGame) {
					try {
						CONNECTION.newGame(id);
					} catch (IOException e) {
						logMessage(AlertType.ERROR, "Network", "Network Error, please reconnect!");
						e.printStackTrace();
					}
				}
				
				while (isRunning) {
					try {
						Thread.sleep(TIME_NETWORK_QUERIES_IN_BETWEEN_MS);
						
						//get layout count
						int remoteCount = CONNECTION.moveCount(this.id);
						int currCount = layouts.size() - 1;
						
						//has changed
						if (currCount != remoteCount) {
							
							//check for resets
							List<Layout> newLayout;
							if (currCount == -1 || currCount > remoteCount) {
								newLayout = new ArrayList<>(Collections.singleton(Layout.INITIAL_LAYOUT));
								currCount = 0;
							} else {
								newLayout = new ArrayList<>(layouts);
							}
							
							//download new layouts
							for (int moveId = currCount; moveId < remoteCount; moveId++) {
								Layout layout = new Layout("After move " + (moveId + 1), newLayout.get(moveId));
								layout.apply(CONNECTION.getChange(this.id, moveId + 1, layout));
								newLayout.add(layout);
							}
							
							//apply new layouts
							Task task = new Task(() -> {
								MultipleSelectionModel<Layout> selectionModel = occupancyListView.getSelectionModel();
								int last = newLayout.size() - 1;
								boolean selectLast = layouts.isEmpty() || selectionModel.getSelectedItem() == newLayout.get(last);
								
								layouts.setAll(newLayout);
								if (selectLast)
									selectionModel.clearAndSelect(last);
							});
							Platform.runLater(task);
							task.await();
						}
					} catch (InterruptedException ignore) {
					
					} catch (IOException e) {
						logMessage(AlertType.ERROR, "Network", "Network Error, please reconnect!");
						e.printStackTrace();
						break;
					}
				}
			});
			th.setDaemon(true);
			th.start();
		}
		
		public void stop() {
			isRunning = false;
			th.interrupt();
		}
	}
	
	public void setGame(int id, PlayerColor color, boolean newGame) {
		if (game != null)
			game.stop();
		game = new Game(id, color, newGame);
		occupancyListView.setData(game.layouts);
	}
	
	//network and occupancyListView
	public void setupNetwork() {
		occupancyListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null)
				addLayout(newValue);
		});
		
		joinButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			try {
				setGame(Integer.parseInt(iDTextField.getText()), colorSelectorListView.getSelectionModel().getSelectedItem(), false);
				logMessage(AlertType.INFORMATION, "Connection", "Join successful!");
			} catch (NumberFormatException e) {
				logMessage(AlertType.ERROR, "Connection", "Join failed: Number " + iDTextField.getText() + " not a valid number!");
				shake();
			}
		});
		newGameButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			try {
				setGame(Integer.parseInt(iDTextField.getText()), colorSelectorListView.getSelectionModel().getSelectedItem(), true);
				logMessage(AlertType.INFORMATION, "Connection", "New Game successful!");
			} catch (NumberFormatException e) {
				logMessage(AlertType.ERROR, "Connection", "New Game failed: Number " + iDTextField.getText() + " not a valid number!");
				shake();
			}
		});
//		saveButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
//			Alert message = getMessage(AlertType.INFORMATION, "Test Connection", "Results:", "Game NOT saved!");
//			messageListView.addItem(message);
//		});
	}
	
	//marking
	public void mark(Position pos) {
		chessField.mark(pos);
	}
	
	public void unmark(Position pos) {
		chessField.unmark(pos);
	}
	
	//shake
	public void shake() {
		shakeQueue.add(Boolean.TRUE);
	}
	
	public void setupShaker() {
		Thread th = new Thread(() -> {
			//noinspection InfiniteLoopStatement
			while (true) {
				try {
					shakeQueue.take();
					
					for (int i = 0; i < SHUFFLE_COUNT; i++) {
						Platform.runLater(() -> shuffleControlPane.setPrefSize(shuffleControlPane.getPrefWidth() - 10, shuffleControlPane.getPrefHeight()));
						try {
							Thread.sleep(30);
						} catch (InterruptedException ignored) {
						
						}
						Platform.runLater(() -> shuffleControlPane.setPrefSize(shuffleControlPane.getPrefWidth() + 20, shuffleControlPane.getPrefHeight()));
						try {
							Thread.sleep(30);
						} catch (InterruptedException ignored) {
						
						}
						Platform.runLater(() -> shuffleControlPane.setPrefSize(shuffleControlPane.getPrefWidth() - 10, shuffleControlPane.getPrefHeight()));
						try {
							Thread.sleep(30);
						} catch (InterruptedException ignored) {
						
						}
					}
				} catch (InterruptedException ignored) {
				
				}
			}
		}, "ShakerThread");
		th.setDaemon(true);
		th.start();
	}
	
	//layout handling
	public void addLayout(Layout layout) {
		layoutQueue.add(layout);
	}
	
	private void setupLayoutHandler() {
		Thread th = new Thread(() -> {
			try {
				Task setInitialLayout = new Task(() -> {
					for (Entry<Figures, Position> entry : layoutCurrent.entrySet()) {
						Figures figure = entry.getKey();
						Position position = entry.getValue();
						
						ImageView icon = new ImageView();
						icon.setImage(new Image(figure.getIconPath()));
						icon.setFitWidth(30);
						icon.setFitHeight(30);
						icon.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> chessField.doClick(layoutCurrent.get(figure)));
						
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
