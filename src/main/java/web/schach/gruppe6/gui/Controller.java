package web.schach.gruppe6.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import web.schach.gruppe6.gui.customComponents.BeatenTileField;
import web.schach.gruppe6.gui.customComponents.ChessTileField;
import web.schach.gruppe6.gui.customComponents.MessageListView;
import web.schach.gruppe6.gui.customComponents.OccupancyListView;
import web.schach.gruppe6.gui.customComponents.Tile;
import web.schach.gruppe6.gui.customComponents.TileField;
import web.schach.gruppe6.gui.util.ColorEnum;
import web.schach.gruppe6.obj.FigureType;
import web.schach.gruppe6.obj.PlayerColor;
import web.schach.gruppe6.obj.Position;

public class Controller {
	
	private static final int SHUFFLECOUNT = 2;
	private final int MOVINGPARTS = 20;
	private boolean messageListIsVisible = true;
	private boolean menuIsVisible = true;
	private boolean listVisible = true;
	
	
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
	private TextField iDTextField;
	
	
	//LIST VIEW
	
	@FXML
	private OccupancyListView occupancyListView;
	
	@FXML
	private MessageListView messageListView;
	
	//TILE FIELDS
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
		setupListeners();
		
		
		//testing
		occupancyListView.addItem("test");
		occupancyListView.addItem("test2");
		joinButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Alert message = getMessage(Alert.AlertType.INFORMATION, "Test Connection", "Results:", "Connect successfully!");
				messageListView.addItem(message);
				shuffle();
			}
		});
		newGameButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Alert message = getMessage(Alert.AlertType.WARNING, "Test Connection", "Results:", "WARNING");
				messageListView.addItem(message);
				shuffle();
			}
		});
		
		saveButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				new Thread() {
					public void run() {
						moveIcon(beatenFiguresBot, new Position(4, 0), chessField, new Position(3, 1));
						try {
							Thread.sleep(100);
						} catch (InterruptedException ignored) {
						}
						moveIcon(chessField, new Position(3, 1), beatenFiguresBot, new Position(1, 0));
						try {
							Thread.sleep(100);
						} catch (InterruptedException ignored) {
						}
						moveIcon(beatenFiguresBot, new Position(1, 0), beatenFiguresBot, new Position(4, 0));
					}
				}.start();
				new Thread() {
					public void run() {
						moveIcon(beatenFiguresBot, new Position(0, 0), beatenFiguresBot, new Position(4, 0));
						try {
							Thread.sleep(100);
						} catch (InterruptedException ignored) {
						}
						moveIcon(beatenFiguresBot, new Position(4, 0), beatenFiguresBot, new Position(2, 1));
						try {
							Thread.sleep(100);
						} catch (InterruptedException ignored) {
						}
						moveIcon(beatenFiguresBot, new Position(2, 1), beatenFiguresBot, new Position(0, 0));
					}
				}.start();
			}
		});
	}
	
	private void setupListeners() {
		occupancyListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				System.out.println(newValue);
			}
		});
		messageListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Alert>() {
			@Override
			public void changed(ObservableValue<? extends Alert> observable, Alert oldValue, Alert newValue) {
				newValue.showAndWait();
			}
		});
	}
	
	public void switchEventLogVisibility() {
		if (messageListIsVisible) {
			messageListView.setVisible(false);
			messageListIsVisible = false;
			setBackgroundColor(messageButton, ColorEnum.GREEN);
		} else {
			messageListView.setVisible(true);
			messageListIsVisible = true;
			setBackgroundColor(messageButton, ColorEnum.RED);
		}
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
			occupancyListView.setVisible(false);
			listVisible = false;
			setBackgroundColor(logButton, ColorEnum.GREEN);
		} else {
			occupancyListView.setVisible(true);
			listVisible = true;
			setBackgroundColor(logButton, ColorEnum.RED);
		}
	}
	
	public static ImageView getFigureIcon(FigureType icon, PlayerColor color) {
		ImageView newImage = new ImageView();
		if (color == PlayerColor.BLACK)
			newImage.setImage(new Image(icon.iconPathBlack));
		else
			newImage.setImage(new Image(icon.iconPathWhite));
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
	
	private Bounds getBoundsRelativeToScene(Node node) {
		return node.localToScene(node.getBoundsInLocal());
	}
	
	private Step getStep(Bounds src, Bounds des) {
		return new Step((des.getMinX() - src.getMinX()) / MOVINGPARTS, (des.getMinY() - src.getMinY()) / MOVINGPARTS);
	}
	
	private void doMove(ImageView icon, Step step) {
		double x = icon.getLayoutX();
		double y = icon.getLayoutY();
		for (int stepCount = 0; stepCount < MOVINGPARTS; stepCount++) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			x += step.x;
			y += step.y;
			synchronized (this) {
				icon.setLayoutX(x);
				icon.setLayoutY(y);
			}
		}
	}
	
	public void moveIcon(TileField srcField, Position srcPos, TileField desField, Position desPos) {
		Tile srcTile = srcField.getFieldComponents()[srcPos.x][srcPos.y];
		Tile desTile = desField.getFieldComponents()[desPos.x][desPos.y];
		ImageView icon = srcTile.getIcon();
		Bounds srcBounds = getBoundsRelativeToScene(srcTile);
		Bounds desBounds = getBoundsRelativeToScene(desTile);
		Step step = getStep(srcBounds, desBounds);
		doMove(icon, step);
		desTile.setIcon(icon);
	}
	
	public void placeIcon(TileField desField, Position desPos, FigureType iconType, PlayerColor color) {
		ImageView icon = getFigureIcon(iconType, color);
		Tile desTile = desField.getFieldComponents()[desPos.x][desPos.y];
		desTile.setIcon(icon);
		chessFieldPane.getChildren().add(icon);
		Bounds paneBoundsInScene = getBoundsRelativeToScene(chessFieldPane);
		Bounds tileBoundsInScene = getBoundsRelativeToScene(desTile);
		icon.setLayoutX(tileBoundsInScene.getMinX() - paneBoundsInScene.getMinX());
		icon.setLayoutY(tileBoundsInScene.getMinY() - paneBoundsInScene.getMinY());
	}
	
	public void shuffle() {
		new Thread() {
			public void run() {
				for (int i = 0; i < SHUFFLECOUNT; i++) {
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
			}
		}.start();
	}
	
	private class Step {
		
		double x;
		double y;
		
		public Step(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
}
