package web.schach.gruppe6.gui.customComponents;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import static javafx.collections.FXCollections.observableArrayList;

public enum MessageLists {
	ALL(observableArrayList()),
	NO_ERROR(observableArrayList()),
	NO_WARNING(observableArrayList()),
	INFOS_ONLY(observableArrayList());
	
	private final ObservableList<Alert> data;
	
	public ObservableList<Alert> getData() {
		return data;
	}
	
	MessageLists(ObservableList<Alert> data) {
		this.data = data;
	}
	
	public static void add(Alert alert) {
		if (alert.getAlertType() == AlertType.ERROR) {
			ALL.data.add(alert);
			NO_WARNING.data.add(alert);
		} else if (alert.getAlertType() == AlertType.WARNING) {
			ALL.data.add(alert);
			NO_ERROR.data.add(alert);
		} else {
			ALL.data.add(alert);
			INFOS_ONLY.data.add(alert);
			NO_ERROR.data.add(alert);
			NO_WARNING.data.add(alert);
		}
	}
	
	public static boolean remove(Alert key) {
		NO_ERROR.data.remove(key);
		NO_WARNING.data.remove(key);
		INFOS_ONLY.data.remove(key);
		return ALL.data.remove(key);
	}
	
	public static void clear() {
		ALL.data.clear();
		NO_ERROR.data.clear();
		NO_WARNING.data.clear();
		INFOS_ONLY.data.clear();
	}
}
