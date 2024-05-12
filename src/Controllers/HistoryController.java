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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {
    @FXML
    TableView<String[]> processTable;
    @FXML
    TableColumn<String[], String> processDateColumn;
    @FXML
    TableColumn<String[], String> processInputQuantityColumn;
    @FXML
    TableColumn<String[], String> processOutputQuantityColumn;
    @FXML
    TableColumn<String[], String> processWoodTypeColumn;
    @FXML
    TableColumn<String[], String> processSizeColumn;
    @FXML
    TableView<String[]> soldTable;
    @FXML
    TableColumn<String[], String> soldDateColumn;
    @FXML
    TableColumn<String[], String> soldQuantityColumn;
    @FXML
    TableColumn<String[], String> soldCustomerColumn;
    @FXML
    TableColumn<String[], String> soldLumberColumn;

    ObservableList<String[]> processedList;
    ObservableList<String[]> soldList;

    // User Account
    @FXML
    private Button logout_button;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userRoleLabel;

    // Initialize tables
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLabel.setText(Main.getUser());
        userRoleLabel.setText(Main.getUserRole());
        try {
            // Initialize table - Processed
            processedList = FXCollections.observableArrayList(DatabaseManager.readProcessedInfo());
            processTable.setItems(processedList);

            processDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
            processInputQuantityColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));
            processOutputQuantityColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[2]));
            processWoodTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[3]));
            processSizeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[4]));

            // Initialize table - Sold
            soldList = FXCollections.observableArrayList(DatabaseManager.readSoldTo());
            soldTable.setItems(soldList);

            soldDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
            soldQuantityColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));
            soldCustomerColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[2]));
            soldLumberColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[3]));

//            // Add listener to enable/disable edit button based on selection
//            cutTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//                if (newSelection != null) {
//                    edit_cut_button.setDisable(false); // Enable the "Edit" button
//                } else {
//                    edit_cut_button.setDisable(true); // Disable the "Edit" button
//                }
//            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void goToRaw(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/RawLumber.fxml"))));
    }

    @FXML
    private void goToCut(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/CutLumber.fxml"))));
    }

    @FXML
    private void goToDashBoard(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/Dashboard.fxml"))));
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        Main.logIn();
        ((Stage) userRoleLabel.getScene().getWindow()).close();
    }
}
