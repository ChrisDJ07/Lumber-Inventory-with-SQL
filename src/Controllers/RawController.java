package Controllers;

import Application.DatabaseManager;
import Application.Main;
import Controllers.pop_ups.EditRaw;
import Controllers.pop_ups.ProcessRaw;
import Controllers.pop_ups.SupplyRaw;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RawController implements Initializable {
    @FXML
    private Button process_button;
    @FXML
    private Button raw_edit_button;
    @FXML
    private Button delete_button;

    @FXML
    private TextField searchField;

    @FXML
    TableView<String[]> rawTable = new TableView<>();
    @FXML
    TableColumn<String[], String> typeColumn;
    @FXML
    TableColumn<String[], String> quantityColumn;

    static ObservableList<String[]> dataList;
    static String[] selectedItem;

    // Initialize tables
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            disableRelevantButtons();

            dataList = FXCollections.observableArrayList(DatabaseManager.readRawLumbers());
            rawTable.setItems(dataList);

            FilteredList<String[]> filteredList = new FilteredList<>(dataList);
            rawTable.setItems(filteredList);

            typeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
            quantityColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));

            rawTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    process_button.setDisable(false);
                    raw_edit_button.setDisable(false);
                    delete_button.setDisable(false);
                    selectedItem = rawTable.getSelectionModel().getSelectedItem();
                }
            });

            searchField.setPromptText("Search...");

            // Add listener to text property to filter data as user types
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(item -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true; // Show all items if the filter is empty
                    }
                    for (String value : item) {
                        if (value.toLowerCase().contains(newValue.toLowerCase())) {
                            return true;
                        }
                    }
                    return false;
                });
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Navigate scenes
    @FXML
    private void goToDashBoard(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/Dashboard.fxml"))));
    }

    @FXML
    private void goToCut(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/CutLumber.fxml"))));
    }

    @FXML
    private void goToHistory(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/History.fxml"))));
    }

    // Open Pop-ups
    @FXML
    void openNewWindow(ActionEvent event) throws IOException{
        Stage New = new Stage();
        New.initOwner(Main.getStage());
        New.initModality(Modality.WINDOW_MODAL);
        
        Parent root = FXMLLoader.load(Main.class.getResource("/Views/pop_ups/NewRaw.fxml"));
        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        New.setResizable(false);
        New.setTitle("New Raw Lumber");
        New.setScene(scene);
        New.show();
    }

    @FXML
    void openEditWindow(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/pop_ups/EditRaw.fxml"));
        Parent root = loader.load();
        EditRaw editController = loader.getController();
        editController.setRawController(this);

        Stage edit = new Stage();
        edit.initOwner(Main.getStage());
        edit.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        edit.setResizable(false);
        edit.setTitle("Edit Raw Lumber");
        edit.setScene(scene);
        edit.show();
    }

    @FXML
    void openProcessWindow(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/pop_ups/ProcessRaw.fxml"));
        Parent root = loader.load();
        ProcessRaw processController = loader.getController();
        processController.setProcessController(this);

        Stage process = new Stage();
        process.initOwner(Main.getStage());
        process.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        process.setResizable(false);
        process.setTitle("Process Raw Lumber");
        process.setScene(scene);
        process.show();
    }

    @FXML
    void openSupplyWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/pop_ups/SupplyRaw.fxml"));
        Parent root = loader.load();
        SupplyRaw supplyController = loader.getController();
        supplyController.setSupplyController(this);

        Stage supply = new Stage();
        supply.initOwner(Main.getStage());
        supply.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        supply.setResizable(false);
        supply.setTitle("Supply Raw Lumber");
        supply.setScene(scene);
        supply.show();
    }

    @FXML
    void delete_rawLumber(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Raw Lumber Detetion");
        alert.setHeaderText("Are you sure you want to delete this Raw Lumber type?");
        alert.setContentText("Deleting this will also affect Cut Lumber with this type.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();

        // Delete if ok
        if(alert.getResult() == ButtonType.OK){
            DatabaseManager.deleteRawLumber(getSelectedType());
            refreshTable();
        }
        disableRelevantButtons();
    }

    // Method to refresh the TableView with the latest data from the database
    public static void refreshTable() {
        try {
            // Update the ObservableList with the latest data from the database
            dataList.clear(); // Clear the existing data
            dataList.addAll(DatabaseManager.readRawLumbers()); // Add the latest data
        } catch (SQLException e) {
            // Handle the exception (show error message, log, etc.)
            e.printStackTrace();
        }
    }

    public static String getSelectedType(){
        return selectedItem[0];
    }

    public void disableRelevantButtons(){
        process_button.setDisable(true);
        raw_edit_button.setDisable(true);
        delete_button.setDisable(true);
    }

    @FXML
    void clearSearch(ActionEvent event) {
        searchField.clear();
    }
}
