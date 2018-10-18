package web.schach.gruppe6.gui.customComponents;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import static javafx.collections.FXCollections.observableArrayList;

public class OccupancyListView extends ListView<String> {
    public ObservableList data = observableArrayList();

    public OccupancyListView() {
        addItem("test", new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            }
        });

        addItem("test2", new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            }
        });
    }

    public void addItem(String item, ChangeListener e) {
        data.add(item);
        setItems(data);
        getSelectionModel().selectedItemProperty().addListener(e);
    }


}
