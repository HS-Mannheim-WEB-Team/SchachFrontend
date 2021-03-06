package web.schach.gruppe6.gui.customComponents;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class MessageListView extends ListView<Alert> {
	
	MessageLists curMessageLists = MessageLists.ALL;
	
	public MessageListView() {
		setCellFactory(param -> new ListCell<Alert>() {
			private ImageView imageView = new ImageView();
			
			@Override
			public void updateItem(Alert alert, boolean empty) {
				super.updateItem(alert, empty);
				imageView.setFitHeight(25);
				imageView.setFitWidth(25);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					if (alert.getAlertType() == Alert.AlertType.WARNING)
						imageView.setImage(new Image("web/schach/gruppe6/gui/iconsAndImages/icon-warning.png"));
					else if (alert.getAlertType() == Alert.AlertType.ERROR)
						imageView.setImage(new Image("web/schach/gruppe6/gui/iconsAndImages/icon-error.png"));
					else if (alert.getAlertType() == Alert.AlertType.INFORMATION)
						imageView.setImage(new Image("web/schach/gruppe6/gui/iconsAndImages/icon-info.png"));
					setText(alert.getTitle() + ":   " + alert.getContentText());
					setGraphic(imageView);
				}
			}
		});
		setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				Alert alert = getSelectionModel().getSelectedItem();
				if (alert != null)
					alert.showAndWait();
			}
		});
		setItems(MessageLists.ALL.getData());
	}
	
	public void addItem(Alert item) {
		MessageLists.add(item);
		scrollTo(item);
	}
	
	public void updateList() {
		setItems(curMessageLists.getData());
		scrollTo(curMessageLists.getData().size());
	}
	
	public boolean removeItem(Alert key) {
		return MessageLists.remove(key);
	}
	
	public void clear() {
		MessageLists.clear();
	}
	
	public void switchLists(MessageLists type) {
		curMessageLists = type;
		updateList();
	}
}
