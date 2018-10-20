package web.schach.gruppe6.gui.customComponents;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static javafx.collections.FXCollections.observableArrayList;

public class MessageListView extends ListView<Alert> {
	
	public ObservableList data = observableArrayList();
	
	public MessageListView() {
		setCellFactory(param -> new ListCell<Alert>() {
			private ImageView imageView = new ImageView();
			
			@Override
			public void updateItem(Alert alert, boolean empty) {
				super.updateItem(alert, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					if (alert.getAlertType() == Alert.AlertType.WARNING)
						imageView.setImage(new Image("web/schach/gruppe6/gui/iconsAndImages/icon-warning.png"));
					else {
						if (alert.getAlertType() == Alert.AlertType.ERROR)
							imageView.setImage(new Image("web/schach/gruppe6/gui/iconsAndImages/icon-error.png"));
					}
					setText(alert.getTitle() + "  " + alert.getHeaderText() + "   " + alert.getContentText());
					setGraphic(imageView);
				}
			}
		});
	}
	
	public void addItem(Alert item) {
		data.add(item);
		setItems(data);
		scrollTo(item);
	}
	
	public boolean removeItem(String key) {
		boolean success = data.remove(key);
		if (success)
			setItems(data);
		return success;
	}
	
	public void clear() {
		data.clear();
		setItems(data);
	}
}
