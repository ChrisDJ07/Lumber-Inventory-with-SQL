package Controllers;

import Application.DatabaseManager;
import Application.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RawController implements Initializable {
    @FXML
    TableView<String[]> rawTable = new TableView<>();
    @FXML
    TableColumn<String[], String> typeColumn;
    @FXML
    TableColumn<String[], String> quantityColumn;

    static ObservableList<String[]> dataList;

    // Initialize tables
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            dataList = FXCollections.observableArrayList(DatabaseManager.readRawLumbers());
            rawTable.setItems(dataList);

            typeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
            quantityColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

        New.setTitle("New Raw Lumber");
        New.setScene(scene);
        New.show();
    }

    @FXML
    void openEditWindow(ActionEvent event) throws IOException{
        Stage edit = new Stage();
        edit.initOwner(Main.getStage());
        edit.initModality(Modality.WINDOW_MODAL);
        
        Parent root = FXMLLoader.load(Main.class.getResource("/Views/pop_ups/EditRaw.fxml"));
        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        edit.setTitle("Edit Raw Lumber");
        edit.setScene(scene);
        edit.show();
    }

    @FXML
    void openProcessWindow(ActionEvent event) throws IOException{
        Stage process = new Stage();
        process.initOwner(Main.getStage());
        process.initModality(Modality.WINDOW_MODAL);
        
        Parent root = FXMLLoader.load(Main.class.getResource("/Views/pop_ups/ProcessRaw.fxml"));
        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        process.setTitle("Process Raw Lumber");
        process.setScene(scene);
        process.show();
    }
}
