package web.schach.gruppe6.gui.customComponents;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import static javafx.collections.FXCollections.observableArrayList;

public class OccupancyListView extends ListView<String> {
    public ObservableList data = observableArrayList();

    public OccupancyListView() {
    }

    public void addItem(String item, ChangeListener e) {
        data.add(item);
        setItems(data);
        getSelectionModel().selectedItemProperty().addListener(e);
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
