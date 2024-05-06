package Controllers.pop_ups;

import Application.DatabaseManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SizesController implements Initializable{
    @FXML
    TableView<String[]> sizeTable = new TableView<>();
    @FXML
    TableColumn<String[], String> idColumn;
    @FXML
    TableColumn<String[], String> dimensionColumn;

    ObservableList<String[]> dataList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            dataList = FXCollections.observableArrayList(DatabaseManager.readSizes());
            sizeTable.setItems(dataList);

            idColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
            dimensionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
