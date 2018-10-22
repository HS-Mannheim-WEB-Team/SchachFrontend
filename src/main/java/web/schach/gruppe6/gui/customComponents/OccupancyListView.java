package web.schach.gruppe6.gui.customComponents;

import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import web.schach.gruppe6.obj.Layout;

public class OccupancyListView extends ListView<Layout> {
	
	public OccupancyListView() {
		setCellFactory(param -> new ListCell<Layout>() {
			@Override
			protected void updateItem(Layout item, boolean empty) {
				super.updateItem(item, empty);
				
				setText(empty || item == null ? null : item.name);
			}
		});
	}
	
	public void setData(ObservableList<Layout> data) {
		setItems(data);
	}
}
