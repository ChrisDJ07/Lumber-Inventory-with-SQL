package Controllers;

import Application.DatabaseManager;
import Application.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

public class History implements Initializable {
    @FXML
    private TabPane tab;

    @FXML
    private Button delete_button;
    @FXML
    private Button edit_button;

    // Navigate
    @FXML
    private Button dashBoard_button;

    // Setting table
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
    TableColumn<String[], String> soldPriceColumn;
    @FXML
    TableColumn<String[], String> soldCustomerColumn;
    @FXML
    TableColumn<String[], String> soldLumberColumn;
    @FXML
    TableColumn<String[], String> soldSizeColumn;
    @FXML
    TableView<String[]> supplyTable;
    @FXML
    TableColumn<String[], String> supplyDateColumn;
    @FXML
    TableColumn<String[], String> supplySupplierColumn;
    @FXML
    TableColumn<String[], String> supplyLumberColumn;
    @FXML
    TableColumn<String[], String> supplyQuantityColumn;
    @FXML
    TableColumn<String[], String> supplyPriceColumn;

    static ObservableList<String[]> processedList;
    static ObservableList<String[]> soldList;
    static ObservableList<String[]> supplyList;

    // User Account
    @FXML
    private Button logout_button;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userRoleLabel;

    // Search
    @FXML
    private Button clear_button;
    @FXML
    private TextField searchField;

    // Tabs
    @FXML
    private Tab soldTab;
    @FXML
    private Tab processTab;
    @FXML
    private Tab supplyTab;

    static String[] selectedSoldInfo;
    static String[] selectedProcessInfo;
    static String[] selectedSupplyInfo;

    static String currentTab = "";

    @FXML
    private Label versionLabel; // version label

    // Initialize tables
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        versionLabel.setText("v.1.6.8");
        currentTab = "process";
        userNameLabel.setText(Main.getUser());
        userRoleLabel.setText(Main.getUserRole());
        disableButtons();
        // Hide buttons based on role access
        if (userRoleLabel.getText().equals("Cashier")) {
            dashBoard_button.setVisible(false);
            // hide history buttons
            edit_button.setVisible(false);
            delete_button.setVisible(false);
        }

        try {
            // Initialize table - Processed
            processedList = FXCollections.observableArrayList(DatabaseManager.readProcessedInfo());
            processTable.setItems(processedList);
            FilteredList<String[]> filteredProcessedList  = new FilteredList<>(processedList);
            SortedList<String[]> sortedProcessedList = new SortedList<>(filteredProcessedList);
            sortedProcessedList.comparatorProperty().bind(processTable.comparatorProperty());
            processTable.setItems(sortedProcessedList);

            processDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
            processInputQuantityColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));
            processOutputQuantityColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[2]));
            processWoodTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[3]));
            processSizeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[4]));

            processTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    edit_button.setDisable(false);
                    delete_button.setDisable(false);
                    selectedProcessInfo = processTable.getSelectionModel().getSelectedItem();
                } else {
                    disableButtons();
                }
            });

            // Initialize table - Sold
            soldList = FXCollections.observableArrayList(DatabaseManager.readSoldTo());
            soldTable.setItems(soldList);
            FilteredList<String[]> filteredSoldList  = new FilteredList<>(soldList);
            SortedList<String[]> sortedSoldList = new SortedList<>(filteredSoldList);
            sortedSoldList.comparatorProperty().bind(soldTable.comparatorProperty());
            soldTable.setItems(sortedSoldList);

            soldDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
            soldQuantityColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));
            soldPriceColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[2]));
            soldCustomerColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[3]));
            soldLumberColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[4]));
            soldSizeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[5]));

            soldTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    edit_button.setDisable(false);
                    delete_button.setDisable(false);
                    selectedSoldInfo = soldTable.getSelectionModel().getSelectedItem();
                } else {
                    disableButtons();
                }
            });


            // Initialize table - Supply
            supplyList = FXCollections.observableArrayList(DatabaseManager.readSuppliedBy());
            supplyTable.setItems(supplyList);
            FilteredList<String[]> filteredSupplyList  = new FilteredList<>(supplyList);
            SortedList<String[]> sortedSupplyList = new SortedList<>(filteredSupplyList);
            sortedSupplyList.comparatorProperty().bind(supplyTable.comparatorProperty());
            supplyTable.setItems(sortedSupplyList);

            supplyDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
            supplyQuantityColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));
            supplySupplierColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[2]));
            supplyLumberColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[3]));
            supplyPriceColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[4]));

            supplyTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    edit_button.setDisable(false);
                    delete_button.setDisable(false);
                    selectedSupplyInfo = supplyTable.getSelectionModel().getSelectedItem();
                } else {
                    disableButtons();
                }
            });

            // Adding filters
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredProcessedList.setPredicate(item -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true; // Show all items if the filter is empty
                    }
                    for (String value : item) {
                        if (value.toLowerCase().contains(newValue.toLowerCase())) {
                            return true;
                        }
                    }
                    return false;
                }
                );
                filteredSoldList.setPredicate(item -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true; // Show all items if the filter is empty
                    }
                    for (String value : item) {
                        if (value.toLowerCase().contains(newValue.toLowerCase())) {
                            return true;
                        }
                    }
                    return false;
                }
                );
                filteredSupplyList.setPredicate(item -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true; // Show all items if the filter is empty
                    }
                    for (String value : item) {
                        if (value.toLowerCase().contains(newValue.toLowerCase())) {
                            return true;
                        }
                    }
                    return false;
                }
                );
            });

            tab.getSelectionModel().selectedItemProperty().addListener((Observable, oldTab, newTab) -> {
                disableButtons();
                clear();
                if (newTab == soldTab) {
                    currentTab = "sold";
                    if (selectedSoldInfo != null) {
                        edit_button.setDisable(false);
                        delete_button.setDisable(false);
                    }
                } else if (newTab == processTab) {
                    currentTab = "process";
                    if (selectedProcessInfo != null) {
                        edit_button.setDisable(false);
                        delete_button.setDisable(false);
                    }
                } else if (newTab == supplyTab) {
                    currentTab = "supply";
                    if (selectedSupplyInfo != null) {
                        edit_button.setDisable(false);
                        delete_button.setDisable(false);
                    }
                }
            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void openEditHistoryWindows(ActionEvent event) throws IOException {
        String fxmlFile = "";
        String title = "";

        switch (currentTab) {
            case "process" -> {
                fxmlFile = "/Views/pop_ups/EditProcessHistory.fxml";
                title = "Edit Process History";
            }
            case "sold" -> {
                fxmlFile = "/Views/pop_ups/EditSoldHistory.fxml";
                title = "Edit Sold History";
            }
            case "supply" -> {
                fxmlFile = "/Views/pop_ups/EditSupplyHistory.fxml";
                title = "Edit Supply History";
            }
        }

        if (!fxmlFile.isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage edit = new Stage();
            edit.setTitle(title);
            edit.initOwner(Main.getStage());
            edit.initModality(Modality.WINDOW_MODAL);
            edit.setResizable(false);

            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Main.class.getResource("/Application/Application.css").toExternalForm());
            edit.setResizable(false);
            edit.setScene(scene);
            edit.show();
        }
    }


    @FXML
    void deleteHistory(ActionEvent event) throws SQLException {
        // Confirm Deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("History Deletion");
        alert.setHeaderText("Are you sure you want to delete this Transaction History?");
        alert.setContentText("Deleting this will completely remove it from the database.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
        // Proceed Delete
        if(alert.getResult() == ButtonType.OK){
            switch (currentTab) {
                case "process" -> {
                    DatabaseManager.deleteHistory("process_info", "process_date", selectedProcessInfo[0]);
                    selectedProcessInfo = null;
                }
                case "sold" -> {
                    DatabaseManager.deleteHistory("sold_to", "sold_date", selectedSoldInfo[0]);
                    selectedSoldInfo = null;
                }
                case "supply" -> {
                    DatabaseManager.deleteHistory("supplied_by", "supplied_date", selectedSupplyInfo[0]);
                    selectedSupplyInfo = null;
                }
            }
            refreshTables();
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
    void clearSearch(ActionEvent event) {
        clear();
    }
    public void clear(){
        searchField.clear();
    }


    @FXML
    void logOut(ActionEvent event) throws IOException {
        Main.logIn();
        ((Stage) userRoleLabel.getScene().getWindow()).close();
    }

    public static void refreshTables() throws SQLException {
        processedList.clear();
        soldList.clear();
        supplyList.clear();

        processedList.addAll(DatabaseManager.readProcessedInfo());
        soldList.setAll(DatabaseManager.readSoldTo());
        supplyList.addAll(DatabaseManager.readSuppliedBy());
    }

    public static String[] getSelected(){
        switch (currentTab) {
            case "process" -> {
                return selectedProcessInfo;
            }
            case "sold" -> {
                //fxmlFile = "/Views/pop_ups/EditSoldHistory.fxml";
                return selectedSoldInfo;
            }
            case "supply" -> {
                //fxmlFile = "/Views/pop_ups/EditSupplyHistory.fxml";
                return selectedSupplyInfo;
            }
        }
        return null;
    }

    void disableButtons(){
        edit_button.setDisable(true);
        delete_button.setDisable(true);
    }
}
