package Controllers;

import Application.DatabaseManager;
import Application.Main;
import Controllers.pop_ups.EditCut;
import Controllers.pop_ups.SellCut;
import Controllers.pop_ups.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class CutLumber implements Initializable{

    // CutLumber Lumber
    @FXML
    private TextField searchField;
    @FXML
    private Button clear_search_button;
    @FXML
    private ChoiceBox<?> sizeFilter;

    // Navigate
    @FXML
    private Button dashBoard_button;
    @FXML
    private Button history_button;
    @FXML
    private Button raw_button;
    @FXML
    private Label lastSoldLabel;


    // Customer
    @FXML
    private TextField customerSearch;
    @FXML
    private Button delete_customer_button;
    @FXML
    private Button new_customer_button;
    @FXML
    private Button clear_customer_search_button;

    @FXML
    private TableView<String[]> customerTable;
    @FXML
    private TableColumn<String[], String> customerInfoColumn;
    @FXML
    private TableColumn<String[], String> customerNameColumn;

    // CutLumber table
    @FXML
    private Button add_cut_button;
    @FXML
    private Button delete_button;
    @FXML
    private Button edit_cut_button;
    @FXML
    private Button sell_button;
    @FXML
    private Button sizes_button;

    @FXML
    TableView<String[]> cutTable = new TableView<>();
    @FXML
    TableColumn<String[], String> typeColumn;
    @FXML
    TableColumn<String[], String> sizeColumn;
    @FXML
    TableColumn<String[], String> priceColumn ;
    @FXML
    TableColumn<String[], String> quantityColumn;

    static ObservableList<String[]> dataList;
    static ObservableList<String[]> customerList;

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
            disableRelevantButtons();

            // Initialize table - CutLumber Lumber
            dataList = FXCollections.observableArrayList(DatabaseManager.readCutLumbers());
            cutTable.setItems(dataList);

            typeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));
            sizeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[2]));
            priceColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[3]));
            quantityColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[4]));

            // Initialize table - Customer
            customerList = FXCollections.observableArrayList(DatabaseManager.readCustomers());
            customerTable.setItems(customerList);

            customerNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
            customerInfoColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));

            // Add listener to enable/disable relevant buttons based on selection
            cutTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    sell_button.setDisable(false);
                    edit_cut_button.setDisable(false);
                    delete_button.setDisable(false);
                }
            });
            customerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    delete_customer_button.setDisable(false);
                }
            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Navigate to other scenes
    @FXML
    private void goToRaw(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/RawLumber.fxml"))));
    }

    @FXML
    private void goToDashBoard(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/Dashboard.fxml"))));
    }

    @FXML
    private void goToHistory(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/History.fxml"))));
    }

    // Open Pop-up windows
    @FXML
    void openSizes(ActionEvent event) throws IOException {
        Stage sizes = new Stage();
        sizes.initOwner(Main.getStage());
        sizes.initModality(Modality.WINDOW_MODAL);

        Parent root = FXMLLoader.load(Main.class.getResource("/Views/pop_ups/Sizes.fxml"));
        Scene scene = new Scene(root);

        String css = Main.class.getResource("/Application/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        sizes.setTitle("Lumber Sizes");
        sizes.setScene(scene);
        sizes.show();
    }

    @FXML
    void openAddWindow(ActionEvent event) throws IOException{
        Stage add = new Stage();
        add.initOwner(Main.getStage());
        add.initModality(Modality.WINDOW_MODAL);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/pop_ups/AddCut.fxml"));
        Parent root = loader.load();
        AddCut addCut = loader.getController();
        addCut.setCutController(this);

        Scene scene = new Scene(root);

        String css = Main.class.getResource("/Application/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        add.setTitle("Add CutLumber Lumber");
        add.setScene(scene);
        add.show();
    }

    @FXML
    void openEditWindow(ActionEvent event) throws IOException{
        // Ensure this method is triggered only when a row is selected
        String[] rowData = cutTable.getSelectionModel().getSelectedItem();
            Stage edit = new Stage();
            edit.initOwner(Main.getStage());
            edit.initModality(Modality.WINDOW_MODAL);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/pop_ups/EditCut.fxml"));
            Parent root = loader.load();
            EditCut editCut = loader.getController();
            editCut.setData(cutTable, rowData);
            editCut.setCutController(this);

            Scene scene = new Scene(root);

            String css = Main.class.getResource("/Application/Application.css").toExternalForm();
            scene.getStylesheets().add(css);

            edit.setTitle("Edit CutLumber Lumber");
            edit.setScene(scene);
            edit.show();
    }

    @FXML
    void openDeleteConfirmationWindow(ActionEvent event) throws SQLException {
        String[] rowData = cutTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cut Lumber Detection");
        alert.setHeaderText("Are you sure you want to delete this Cut Lumber type?");
        alert.setContentText("Deleting this will also affect Cut Lumber with this type.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.OK){
            DatabaseManager.deleteCutLumber(rowData[0]);
            refreshCutTable();
        }
        disableRelevantButtons();
    }


    @FXML
    void openSellWindow(ActionEvent event) throws IOException{
        String[] rowData = cutTable.getSelectionModel().getSelectedItem();
        if (rowData[3] == null) {
            // Show an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Field");
            alert.setHeaderText(null);
            alert.setContentText("Unit price is not set. Please fill it before proceeding.");
            alert.showAndWait();
        } else {
            Stage sell = new Stage();
            sell.initOwner(Main.getStage());
            sell.initModality(Modality.WINDOW_MODAL);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/pop_ups/SellCut.fxml"));
            Parent root = loader.load();
            SellCut sellCutController = loader.getController();
            sellCutController.setData(cutTable, rowData);
            sellCutController.setCutController(this);

            Scene scene = new Scene(root);

            String css = Main.class.getResource("/Application/Application.css").toExternalForm();
            scene.getStylesheets().add(css);

            sell.setTitle("SellCut CutLumber Lumber");
            sell.setScene(scene);
            sell.show();
        }
    }

    @FXML
    void clearCustomerSearch(ActionEvent event) {

    }

    @FXML
    void deleteCustomer(ActionEvent event) throws SQLException {
        String[] rowData = customerTable.getSelectionModel().getSelectedItem();
        if (rowData != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Customer Detection");
            alert.setHeaderText("Are you sure you want to delete this Customer?");
            alert.setContentText("Deleting this will completely remove it from the database.");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();

            if(alert.getResult() == ButtonType.OK){
                DatabaseManager.deleteCustomer(rowData[0]);
                refreshCutTable();
            }
            disableRelevantButtons();
        }
    }

    @FXML
    void openNewCustomerWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/pop_ups/NewCustomer.fxml"));
        Parent root = loader.load();
        NewCustomer customerController = loader.getController();
        customerController.setCustomerController(this);

        Stage supply = new Stage();
        supply.initOwner(Main.getStage());
        supply.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        supply.setResizable(false);
        supply.setTitle("Add Customer");
        supply.setScene(scene);
        supply.show();
    }

    @FXML
    void clearSearch(ActionEvent event) {

    }

    public void disableRelevantButtons(){
        sell_button.setDisable(true);
        edit_cut_button.setDisable(true);
        delete_button.setDisable(true);
        delete_customer_button.setDisable(true);
    }

    public static void refreshCutTable() throws SQLException {
        try {
            // Clear the existing data
            dataList.clear();
            dataList.addAll(DatabaseManager.readCutLumbers());
            customerList.clear();
            customerList.addAll(DatabaseManager.readCustomers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        Main.logIn();
        ((Stage) userRoleLabel.getScene().getWindow()).close();
    }
}