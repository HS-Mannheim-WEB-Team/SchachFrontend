package web.schach.gruppe6.gui.customComponents;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import web.schach.gruppe6.obj.PlayerColor;

import static javafx.collections.FXCollections.observableArrayList;

public class ColorListView extends ListView<PlayerColor> {
	
	public ColorListView() {
		setOrientation(Orientation.HORIZONTAL);
		setCellFactory(new Callback<ListView<PlayerColor>,
							   ListCell<PlayerColor>>() {
						   @Override
						   public ListCell<PlayerColor> call(ListView<PlayerColor> list) {
							   return new ColorRectCell();
						   }
					   }
		);
		ObservableList<PlayerColor> data = observableArrayList();
		data.add(PlayerColor.WHITE);
		data.add(PlayerColor.BLACK);
		setItems(data);
		getSelectionModel().selectFirst();
	}
	
	static class ColorRectCell extends ListCell<PlayerColor> {
		
		@Override
		public void updateItem(PlayerColor item, boolean empty) {
			super.updateItem(item, empty);
			Rectangle rect = new Rectangle(15, 15);
			if (item != null) {
				rect.setFill(Color.web(item.toString()));
				setGraphic(rect);
			}
		}
	}
}
