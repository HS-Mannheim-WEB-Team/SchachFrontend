package web.schach.gruppe6.gui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import web.schach.gruppe6.gui.customComponents.BeatenTileField;
import web.schach.gruppe6.gui.customComponents.ChessTileField;
import web.schach.gruppe6.gui.customComponents.ColorListView;
import web.schach.gruppe6.gui.customComponents.LineCountField;
import web.schach.gruppe6.gui.customComponents.ListType;
import web.schach.gruppe6.gui.customComponents.MessageListView;
import web.schach.gruppe6.gui.customComponents.OccupancyListView;
import web.schach.gruppe6.gui.customComponents.Tile;
import web.schach.gruppe6.gui.customComponents.TileField;
import web.schach.gruppe6.gui.util.ColorEnum;
import web.schach.gruppe6.network.ChessConnection;
import web.schach.gruppe6.network.exceptions.ServerErrorException;
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
	private boolean errorsVisible = true;
	private boolean infosVisible = true;
	private boolean messageListIsVisible = true;
	private boolean menuIsVisible = true;
	private boolean layoutListVisible = true;
	
	private Game game;
	
	private Position lastClicked;
	
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
	
	@FXML
	private Button showInfosButton;
	
	@FXML
	private Button showErrorsButton;
	
	//MENU
	@FXML
	private FlowPane menuSocketLeft;
	
	@FXML
	private FlowPane menuSocketMid;
	
	@FXML
	private FlowPane messageButtonSocket;
	
	@FXML
	private FlowPane messageSocketPane;


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
	
	//PLAYER DISPLAY
	@FXML
	private Label curPlayerLabelBot;
	
	@FXML
	private Label curPlayerLabelTop;
	
	@FXML
	private Pane chessFieldPane;
	
	@FXML
	private BorderPane borderPane;
	
	@FXML
	private Pane shuffleControlPane;
	
	public FlowPane getMessageButtonSocket() {
		return messageButtonSocket;
	}
	
	public Pane getShuffleControlPane() {
		return shuffleControlPane;
	}
	
	public Pane getChessFieldPane() {
		return chessFieldPane;
	}
	
	public FlowPane getGlobalCenterFlowPlane() {
		return globalCenterFlowPlane;
	}
	
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
		setupPlayerLabels();
		setupGame();
		setupLayoutHandler();
		setupMoveExecution();
		setupShaker();
	}
	
	private void setupPlayerLabels() {
		curPlayerLabelTop.setStyle("-fx-border-color:" + ColorEnum.BLACK + ";\n -fx-background-color: " + ColorEnum.WHITE + ";\n -fx-text-fill: " + ColorEnum.BLACK);
		curPlayerLabelTop.setText(String.valueOf((ColorEnum.WHITE.toString().charAt(0))));
		curPlayerLabelBot.setStyle("-fx-border-color:" + ColorEnum.BLACK + ";\n -fx-background-color: " + ColorEnum.BLACK + ";\n -fx-text-fill: " + ColorEnum.WHITE);
		curPlayerLabelBot.setText(String.valueOf((ColorEnum.BLACK.toString().charAt(0))));
	}
	
	public void rotateBoard() {
		if (chessFieldPane.getRotate() == 0) {
			for (ImageView icon : figureViewMap.values())
				icon.setRotate(-180);
			for (LineCountField field : chessField.getBorderTiles())
				field.setRotate(-180);
			chessField.setReverseLineCounters();
			chessFieldPane.setRotate(180);
			curPlayerLabelBot.setRotate(180);
			curPlayerLabelTop.setRotate(180);
		} else {
			for (ImageView icon : figureViewMap.values())
				icon.setRotate(0);
			for (LineCountField field : chessField.getBorderTiles())
				field.setRotate(0);
			chessField.setRegularLineCounters();
			chessFieldPane.setRotate(0);
			curPlayerLabelBot.setRotate(0);
			curPlayerLabelTop.setRotate(0);
		}
	}
	
	public void switchPlayerLabel(PlayerColor color) {
		if (color == PlayerColor.BLACK) {
			curPlayerLabelTop.setVisible(false);
			curPlayerLabelBot.setVisible(true);
		} else {
			curPlayerLabelTop.setVisible(true);
			curPlayerLabelBot.setVisible(false);
		}
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
		switchVisibility(messageSocketPane, messageButton, messageListIsVisible);
		messageListIsVisible = !messageListIsVisible;
	}
	
	public void switchMenuVisibility() {
		switchVisibility(menuSocketLeft, optionButton, menuIsVisible);
		switchVisibility(menuSocketMid, optionButton, menuIsVisible);
		menuIsVisible = !menuIsVisible;
	}
	
	public void switchListVisibility() {
		switchVisibility(occupancyListView, logButton, layoutListVisible);
		layoutListVisible = !layoutListVisible;
	}
	
	public void switchShowInfos() {
		if (infosVisible) {
			setBackgroundColor(showInfosButton, ColorEnum.GREEN);
			if (errorsVisible)
				messageListView.switchLists(ListType.ERRORSONLY);
			else
				messageListView.switchLists(ListType.WARNINGSONLY);
		} else {
			setBackgroundColor(showInfosButton, ColorEnum.RED);
			if (errorsVisible)
				messageListView.switchLists(ListType.ALL);
			else
				messageListView.switchLists(ListType.INFOONLY);
		}
		infosVisible = !infosVisible;
	}
	
	public void switchShowWarnings() {
		if (errorsVisible) {
			setBackgroundColor(showErrorsButton, ColorEnum.GREEN);
			if (infosVisible)
				messageListView.switchLists(ListType.INFOONLY);
			else
				messageListView.switchLists(ListType.WARNINGSONLY);
		} else {
			setBackgroundColor(showErrorsButton, ColorEnum.RED);
			if (infosVisible)
				messageListView.switchLists(ListType.ALL);
			else
				messageListView.switchLists(ListType.ERRORSONLY);
		}
		errorsVisible = !errorsVisible;
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
			
			//update requester
			this.th = new Thread(() -> {
				if (newGame) {
					try {
						CONNECTION.newGame(id);
					} catch (IOException e) {
						logMessage(AlertType.ERROR, "Network", "Network Error, please reconnect!");
						e.printStackTrace();
						return;
					} catch (ServerErrorException e) {
						logMessage(AlertType.ERROR, "Server", "Server error: " + e.getMessage());
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
								newLayout.add(CONNECTION.getChange(this.id, moveId + 1, newLayout.get(moveId)));
							}
							
							//apply new layouts
							Task task = new Task(() -> {
								MultipleSelectionModel<Layout> selectionModel = occupancyListView.getSelectionModel();
								boolean selectLast = layouts.isEmpty() || selectionModel.getSelectedItem() == layouts.get(layouts.size() - 1);
								layouts.setAll(newLayout);
								if (selectLast)
									occupancyListView.getSelectionModel().selectLast();
							});
							Platform.runLater(task);
							task.await();
						}
					} catch (InterruptedException ignore) {
					
					} catch (IOException e) {
						logMessage(AlertType.ERROR, "Network", "Network Error, please reconnect!");
						e.printStackTrace();
						return;
					} catch (ServerErrorException e) {
						logMessage(AlertType.ERROR, "Server", "Server error: " + e.getMessage());
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
	
	public void setGameFromUI(boolean newGame) {
		setGame(Integer.parseInt(iDTextField.getText()), colorSelectorListView.getSelectionModel().getSelectedItem(), newGame);
	}
	
	private void setupGame() {
		occupancyListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null)
				addLayout(newValue);
		});
		
		joinButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			try {
				setGameFromUI(false);
				logMessage(AlertType.INFORMATION, "Connection", "Join successful!");
				switchMenuVisibility();
			} catch (NumberFormatException e) {
				logMessage(AlertType.ERROR, "Connection", "Join failed: Number " + iDTextField.getText() + " not a valid number!");
				shake();
			}
		});
		newGameButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			try {
				setGameFromUI(true);
				logMessage(AlertType.INFORMATION, "Connection", "New Game successful!");
				switchMenuVisibility();
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
	
	//move execution
	private void setupMoveExecution() {
		chessField.setOnClickCallback(this::onTileClick);
	}
	
	public void onTileClick(Position position) {
		if (game == null)
			return;
		
		if (lastClicked == null) {
			if (game.layouts.get(game.layouts.size() - 1) != layoutCurrent)
				return;
			Figures figure = layoutCurrent.at(position);
			if (figure == null)
				return;
			if (!(game.color == PlayerColor.BOTH || figure.type.color == game.color))
				return;
			
			lastClicked = position;
			chessField.mark(lastClicked);
		} else if (lastClicked.equals(position)) {
			chessField.unmark(lastClicked);
			lastClicked = null;
		} else {
			try {
				CONNECTION.takeMove(game.id, lastClicked, position);
			} catch (IOException e) {
				logMessage(AlertType.ERROR, "Network", "Network Error, please reconnect!");
				e.printStackTrace();
				shake();
				return;
			} catch (ServerErrorException e) {
				logMessage(AlertType.ERROR, "Server", "Server error: " + e.getMessage());
				shake();
			}
			
			chessField.unmark(lastClicked);
			lastClicked = null;
		}
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
					switchPlayerLabel(layoutDest.playerColorCurrent());
					
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
					switchPlayerLabel(layoutDest.playerColorNext());
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
		return new PositionOnField(figure.type.color == PlayerColor.WHITE ? beatenFiguresTop : beatenFiguresBot, figure.positionBeaten);
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
