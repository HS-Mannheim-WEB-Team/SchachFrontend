package web.schach.gruppe6.gui.customComponents;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import static javafx.collections.FXCollections.observableArrayList;

public enum ListType {
	ALL(observableArrayList()),
	INFOONLY(observableArrayList()),
	ERRORSONLY(observableArrayList()),
	WARNINGSONLY(observableArrayList());
	
	private final ObservableList<Alert> data;
	
	public ObservableList<Alert> getData() {
		return data;
	}
	
	ListType(ObservableList<Alert> data) {
		this.data = data;
	}
	
	public static void add(Alert alert) {
		if (alert.getAlertType() == AlertType.ERROR) {
			ALL.data.add(alert);
			ERRORSONLY.data.add(alert);
		} else if (alert.getAlertType() == AlertType.INFORMATION) {
			ALL.data.add(alert);
			INFOONLY.data.add(alert);
		} else {
			ALL.data.add(alert);
			WARNINGSONLY.data.add(alert);
			INFOONLY.data.add(alert);
			ERRORSONLY.data.add(alert);
		}
	}
	
	public static boolean remove(Alert key) {
		INFOONLY.data.remove(key);
		ERRORSONLY.data.remove(key);
		WARNINGSONLY.data.remove(key);
		return ALL.data.remove(key);
	}
	
	public static void clear() {
		ALL.data.clear();
		INFOONLY.data.clear();
		ERRORSONLY.data.clear();
		WARNINGSONLY.data.clear();
	}
}
