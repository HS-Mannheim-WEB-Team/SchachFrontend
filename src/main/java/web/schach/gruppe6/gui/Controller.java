package web.schach.gruppe6.gui;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
import web.schach.gruppe6.network.exceptions.ParseException;
import web.schach.gruppe6.network.exceptions.ServerErrorException;
import web.schach.gruppe6.obj.Figures;
import web.schach.gruppe6.obj.Layout;
import web.schach.gruppe6.obj.PlayerColor;
import web.schach.gruppe6.util.Grid;
import web.schach.gruppe6.util.Position;
import web.schach.gruppe6.util.Task;
import web.schach.gruppe6.util.Vector;

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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static javafx.collections.FXCollections.observableArrayList;

public class Controller {
	
	//static
	private static final int SHUFFLE_COUNT = 2;
	private static final long TIME_MOVEMENT_TOTAL_NANO = 1000 * 1000000;
	private static final long TIME_MOVEMENT_STEP_NANO = 20 * 1000000;
	public static final int TIME_NETWORK_QUERIES_IN_BETWEEN_MS = 250;
	
	public static final Predicate<String> ONLY_DIGITS = Pattern.compile("^\\d*$").asPredicate();
	public static final ChessConnection CONNECTION = new ChessConnection();
	
	private static Bounds getRelativeBounds(Node child, Node parent) {
		Bounds ret = child.getBoundsInLocal();
		while (!child.equals(parent)) {
			ret = child.localToParent(ret);
			child = child.getParent();
		}
		return ret;
	}
	
	//BOOLEANS VISIBILITY
	private boolean errorsVisible = true;
	private boolean infosVisible = true;
	private boolean messageListIsVisible = true;
	private boolean menuIsVisible = true;
	private boolean layoutListVisible = true;
	
	//object
	private Game game;
	
	private Layout layoutCurrent = Layout.INITIAL_BEATEN;
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
	
	//SOCKETS
	@FXML
	private FlowPane menuSocketLeft;
	
	@FXML
	private FlowPane menuSocketMid;
	
	@FXML
	private FlowPane messageButtonSocket;
	
	@FXML
	private FlowPane messageSocketPane;
	
	//MENU BUTTONS
//	@FXML
//	private Button saveButton;
	
	@FXML
	private Button newGameButton;
	
	@FXML
	private Button joinButton;
	
	@FXML
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
	
	//OTHER PANES
	@FXML
	private Pane chessFieldPane;
	
	@FXML
	private Pane shuffleControlPane;
	
	//GETTER
	
	public boolean isMenuIsVisible() {
		return menuIsVisible;
	}
	
	public Button getNewGameButton() {
		return newGameButton;
	}
	
	public Button getJoinButton() {
		return joinButton;
	}
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
	
	public ColorListView getColorSelectorListView() {
		return colorSelectorListView;
	}
	
	//INITIALIZE
	@FXML
	void initialize() {
		setupGame();
		setupLayoutHandler();
		setupMoveExecution();
		setupShaker();
		setupToolTips();
	}
	
	//TOOLTIP
	private void setupToolTips() {
		joinButton.setTooltip(getToolTip("Shortcut: J/j", "icon-info.png"));
		newGameButton.setTooltip(getToolTip("Shortcut: N/n", "icon-info.png"));
		iDTextField.setTooltip(getToolTip("Numbers only", "icon-info.png"));
		colorSelectorListView.setTooltip(getToolTip("Shortcuts: \r\nWhite: W/w \r\nBlack: B/b\r\nBoth: m/m", "icon-info.png"));
		occupancyListView.setTooltip(getToolTip("Shortcuts: \r\nScroll to First: F/f\r\nScroll to Current: C/c\r\nScroll to Last: L/l", "icon-info.png"));
	}
	
	private Tooltip getToolTip(String content, String iconName) {
		Tooltip tip = new Tooltip();
		tip.setText(content);
		ImageView image = new ImageView("/web/schach/gruppe6/gui/iconsAndImages/" + iconName);
		image.setFitHeight(30);
		image.setFitWidth(30);
		tip.setGraphic(image);
		return tip;
	}
	
	//ROTATION
	public void rotateBoard() {
		if (chessFieldPane.getRotate() == 0) {
			setRotation(180);
			chessField.setReverseLineCounters();
		} else {
			setRotation(0);
			chessField.setRegularLineCounters();
		}
	}
	
	private void setRotation(int rotateValue) {
		for (ImageView icon : figureViewMap.values())
			icon.setRotate(rotateValue);
		for (LineCountField field : chessField.getBorderTiles())
			field.setRotate(rotateValue);
		chessField.setReverseLineCounters();
		chessFieldPane.setRotate(rotateValue);
		curPlayerLabelBot.setRotate(rotateValue);
		curPlayerLabelTop.setRotate(rotateValue);
	}
	
	//VISIBILITY
	public void switchPlayerLabel(PlayerColor color) {
		if (color == PlayerColor.BLACK) {
			curPlayerLabelTop.setVisible(false);
			curPlayerLabelBot.setVisible(true);
		} else {
			curPlayerLabelTop.setVisible(true);
			curPlayerLabelBot.setVisible(false);
		}
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
				messageListView.switchLists(ListType.NO_WARNING);
			else
				messageListView.switchLists(ListType.WARNINGS_ONLY);
		} else {
			setBackgroundColor(showInfosButton, ColorEnum.RED);
			if (errorsVisible)
				messageListView.switchLists(ListType.ALL);
			else
				messageListView.switchLists(ListType.NO_ERROR);
		}
		infosVisible = !infosVisible;
	}
	
	public void switchShowWarnings() {
		if (errorsVisible) {
			setBackgroundColor(showErrorsButton, ColorEnum.GREEN);
			if (infosVisible)
				messageListView.switchLists(ListType.NO_ERROR);
			else
				messageListView.switchLists(ListType.WARNINGS_ONLY);
		} else {
			setBackgroundColor(showErrorsButton, ColorEnum.RED);
			if (infosVisible)
				messageListView.switchLists(ListType.ALL);
			else
				messageListView.switchLists(ListType.NO_WARNING);
		}
		errorsVisible = !errorsVisible;
	}
	
	
	//LOG
	
	/**
	 * @param type should only use ERROR,WARNING or INFORMATION
	 */
	public void logMessageWithJumpToLayout(Alert.AlertType type, String title, String content, int layoutIndex, boolean show) {
		Platform.runLater(() -> messageListView.addItem(getMessageWithJumpToLayout(type, title, content, layoutIndex, show)));
	}
	
	/**
	 * @param type should only use ERROR,WARNING or INFORMATION
	 */
	public Alert getMessageWithJumpToLayout(Alert.AlertType type, String title, String content, int layoutIndex, boolean show) {
		Alert alert = getMessage(type, title, content);
		alert.setOnCloseRequest(value -> {
			occupancyListView.scrollTo(layoutIndex);
			occupancyListView.getSelectionModel().clearAndSelect(layoutIndex);
		});
		if (show)
			alert.showAndWait();
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
	
	//GAME
	public class Game {
		
		public final int id;
		public final PlayerColor color;
		public final ObservableList<Layout> layouts = observableArrayList();
		private final Thread th;
		
		private volatile boolean isRunning = true;
		private Position lastClicked;
		
		public Game(int id, PlayerColor color, boolean newGame) {
			this.id = id;
			this.color = color;
			
			//update requester
			this.th = new Thread(() -> {
				if (newGame) {
					try {
						CONNECTION.newGame(id);
					} catch (IOException | ParseException e) {
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
							List<Layout> newLayoutsWithState = new ArrayList<>();
							
							//download new layouts
							for (int moveId = currCount; moveId < remoteCount; moveId++) {
								Layout change = CONNECTION.getChange(this.id, moveId + 1, newLayout.get(moveId));
								newLayout.add(change);
								if (change.state != null) {
									newLayoutsWithState.add(change);
								}
							}
							
							boolean show = currCount != 0;
							if (isRunning) {
								//apply new layouts
								Task task = new Task(() -> {
									MultipleSelectionModel<Layout> selectionModel = occupancyListView.getSelectionModel();
									boolean selectLast = layouts.isEmpty() || selectionModel.getSelectedItem() == layouts.get(layouts.size() - 1);
									layouts.setAll(newLayout);
									if (selectLast)
										occupancyListView.getSelectionModel().selectLast();
									
									for (Layout layout : newLayoutsWithState) {
										switch (layout.state) {
											case WHITE_CHECK:
											case BLACK_CHECK:
												logMessageWithJumpToLayout(AlertType.WARNING, "Check", layout.state.color + " is in Check!", layout.moveId, show);
												break;
											case WHITE_CHECK_MATE:
											case BLACK_CHECK_MATE:
												logMessageWithJumpToLayout(AlertType.WARNING, "Checkmate", layout.state.color + " is in Checkmate!", layout.moveId, show);
												break;
											case STALEMATE:
												logMessageWithJumpToLayout(AlertType.WARNING, "Stalemate", "Game ended in Stalemate!", layout.moveId, show);
												break;
										}
									}
								});
								Platform.runLater(task);
								task.await();
							}
						}
					} catch (InterruptedException ignore) {
					
					} catch (IOException | ParseException e) {
						logMessage(AlertType.ERROR, "Network", "Network Error, please reconnect!");
						e.printStackTrace();
						return;
					} catch (ServerErrorException e) {
						logMessage(AlertType.ERROR, "Server", "Server error: " + e.getMessage());
					}
				}
			}, "GameUpdateThread" + id);
			th.setDaemon(true);
			th.start();
		}
		
		public void stop() {
			isRunning = false;
			th.interrupt();
			unmarkFields();
		}
		
		public void onTileClick(Position position) {
			if (lastClicked == null) {
				if (game.layouts.get(game.layouts.size() - 1) != layoutCurrent)
					return;
				Figures figure = layoutCurrent.at(position);
				if (figure == null)
					return;
				if (!(game.color == PlayerColor.BOTH || layoutCurrent.getType(figure).color == game.color))
					return;
				
				lastClicked = position;
				
				try {
					Grid<Boolean> permittedMoves = CONNECTION.getPermittedMoves(game.id, position);
					markFields(pos -> permittedMoves.get(pos) == Boolean.TRUE ? ColorEnum.GREEN : null);
					chessField.mark(lastClicked, ColorEnum.RED);
				} catch (IOException | ParseException e) {
					logMessage(AlertType.ERROR, "Network", "Network Error, please reconnect!");
					e.printStackTrace();
				} catch (ServerErrorException e) {
					logMessage(AlertType.ERROR, "Server", "Server error: " + e.getMessage());
				}
			} else if (lastClicked.equals(position)) {
				unmarkFields();
				lastClicked = null;
			} else {
				try {
					CONNECTION.takeMove(game.id, lastClicked, position);
				} catch (IOException | ParseException e) {
					logMessage(AlertType.ERROR, "Network", "Network Error, please reconnect!");
					e.printStackTrace();
				} catch (ServerErrorException e) {
					logMessage(AlertType.ERROR, "Server", "Server error: " + e.getMessage());
				} finally {
					unmarkFields();
					lastClicked = null;
				}
			}
		}
	}
	
	public void setGame(int id, PlayerColor color, boolean newGame) {
		if (game != null)
			game.stop();
		game = new Game(id, color, newGame);
		occupancyListView.setData(game.layouts);
	}
	
	public void setGameFromUI(boolean newGame) {
		setGame(parseInt(iDTextField.getText()), colorSelectorListView.getSelectionModel().getSelectedItem(), newGame);
	}
	
	private void setupGame() {
		occupancyListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null)
				addLayout(newValue);
		});
		
		iDTextField.textProperty().addListener(
				(observable, oldValue, newValue) -> {
					if (!ONLY_DIGITS.test(newValue))
						((StringProperty) observable).setValue(oldValue);
				}
		);
		
		joinButton.setOnAction(event -> {
			try {
				messageListView.clear();
				setGameFromUI(false);
				logMessage(AlertType.INFORMATION, "Connection", "Joining Game " + iDTextField.getText() + " successful!");
				logMessage(AlertType.INFORMATION, "Color", "Your Color is " + colorSelectorListView.getSelectionModel().getSelectedItem().toString());
				switchMenuVisibility();
			} catch (NumberFormatException e) {
				logMessage(AlertType.ERROR, "Connection", "Join failed: Number " + iDTextField.getText() + " not a valid number!");
				shake();
			}
		});
		newGameButton.setOnAction(event -> {
			try {
				messageListView.clear();
				setGameFromUI(true);
				logMessage(AlertType.INFORMATION, "Connection", "Creating new Game " + iDTextField.getText() + " was successful!");
				logMessage(AlertType.INFORMATION, "Color", "Your Color is " + colorSelectorListView.getSelectionModel().getSelectedItem().toString());
				switchMenuVisibility();
			} catch (NumberFormatException e) {
				logMessage(AlertType.ERROR, "Connection", "Creating new Game failed: Number " + iDTextField.getText() + " not a valid number!");
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
		if (game != null) {
			game.onTileClick(position);
		}
	}
	
	private void unmarkFields() {
		markFields(pos -> null);
	}
	
	private void markFields(Function<Position, ColorEnum> color) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Position pos = new Position(x, y);
				chessField.mark(pos, color.apply(pos));
			}
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
					for (Figures figure : Figures.values()) {
						Position position = layoutCurrent.get(figure);
						
						ImageView icon = new ImageView();
						icon.setImage(new Image(layoutCurrent.getType(figure).iconPath));
						icon.setFitWidth(30);
						icon.setFitHeight(30);
						icon.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> chessField.doClick(layoutCurrent.get(figure)));
						
						chessFieldPane.getChildren().add(icon);
						figureViewMap.put(figure, icon);
						
						PositionOnField pof = resolvePositionOnField(layoutCurrent, figure, position);
						Tile node = pof.field.getFieldComponents()[pof.position.x][pof.position.y];
						Bounds bounds = getRelativeBounds(node, chessFieldPane);
						icon.setLayoutX(bounds.getMinX());
						icon.setLayoutY(bounds.getMinY());
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
								
								PositionOnField pofSrc = resolvePositionOnField(layoutSrc, figure, positionSrc);
								PositionOnField pofDest = resolvePositionOnField(layoutDest, figure, positionDest);
								
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
	
	
	//UTIL
	
	/**
	 * removes all other styles. Use only if the Node has no style sheet and the border isn t changed
	 */
	private void setBackgroundColor(Node component, ColorEnum color) {
		component.setStyle("-fx-background-color: " + color);
	}
	
	private PositionOnField resolvePositionOnField(Layout layout, Figures figure, Position position) {
		if (position != null)
			return new PositionOnField(chessField, position);
		return new PositionOnField(layout.getType(figure).color == PlayerColor.WHITE ? beatenFiguresTop : beatenFiguresBot, figure.positionBeaten);
	}
	
	//STATIC CLASSES
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
