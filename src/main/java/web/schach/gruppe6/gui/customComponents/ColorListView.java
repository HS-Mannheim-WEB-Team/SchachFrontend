package web.schach.gruppe6.gui.customComponents;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import web.schach.gruppe6.gui.util.ColorEnum;
import web.schach.gruppe6.obj.PlayerColor;

import static javafx.collections.FXCollections.observableArrayList;

public class ColorListView extends ListView<PlayerColor> {
	
	private static final int RECTANGLE_WIDTH = 15;
	private static final int RECTANGLE_HIGHT = 15;
	
	public ColorListView() {
		setOrientation(Orientation.HORIZONTAL);
		setCellFactory(list -> new ColorRectCell());
		ObservableList<PlayerColor> data = observableArrayList();
		data.add(PlayerColor.WHITE);
		data.add(PlayerColor.BLACK);
		data.add(PlayerColor.BOTH);
		setItems(data);
		getSelectionModel().selectFirst();
	}
	
	static class ColorRectCell extends ListCell<PlayerColor> {
		
		@Override
		public void updateItem(PlayerColor item, boolean empty) {
			super.updateItem(item, empty);
			if (item != null) {
				if (item != PlayerColor.BOTH) {
					Rectangle rect = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HIGHT);
					rect.setFill(Color.web(item.toString()));
					setGraphic(rect);
				} else {
					GridPane pane = new GridPane();
					pane.setAlignment(Pos.CENTER);
					pane.setVgap(2);
					Rectangle rect = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HIGHT / 2.5);
					Rectangle rect2 = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HIGHT / 2.5);
					rect.setFill(Color.web(ColorEnum.WHITE.toString()));
					rect2.setFill(Color.web(ColorEnum.BLACK.toString()));
					pane.add(rect, 0, 0);
					pane.add(rect2, 0, 1);
					setGraphic(pane);
				}
			}
		}
	}
}
