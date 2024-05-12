package Controllers;

import Application.DatabaseManager;
import Application.Main;
import Controllers.pop_ups.EditController;
import Controllers.pop_ups.SellController;
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

public class CutController implements Initializable{
    // Customer
    @FXML
    private TableView<?> customerTable;
    @FXML
    private TableColumn<?, ?> customerInfoColumn;
    @FXML
    private TableColumn<?, ?> customerNameColumn;
    @FXML
    private TextField customerSearch;
    @FXML
    private Button delete_customer_button;
    @FXML
    private Button new_customer_button;
    @FXML
    private Button clear_customer_search_button;

    // Cut Lumber
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


    // Cut table
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

    ObservableList<String[]> dataList;

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

            dataList = FXCollections.observableArrayList(DatabaseManager.readCutLumbers());
            cutTable.setItems(dataList);

            typeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));
            sizeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[2]));
            priceColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[3]));
            quantityColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[4]));

            // Add listener to enable/disable relevant buttons based on selection
            cutTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    sell_button.setDisable(false);
                    edit_cut_button.setDisable(false);
                    delete_button.setDisable(false);
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

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
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

        Parent root = FXMLLoader.load(Main.class.getResource("/Views/pop_ups/AddCut.fxml"));
        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        add.setTitle("Add Cut Lumber");
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
            EditController editController = loader.getController();
            editController.setData(rowData);

            Scene scene = new Scene(root);

            String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
            scene.getStylesheets().add(css);

            edit.setTitle("Edit Cut Lumber");
            edit.setScene(scene);
            edit.show();
    }

    @FXML
    void openDeleteConfirmationWindow(ActionEvent event) {
        // TODO: There will be a confirmation window (Soon) so far wala pa...
        // Get the selected item
        String[] selectedItem = cutTable.getSelectionModel().getSelectedItem();
            // Remove the selected item from the dataList
            dataList.remove(selectedItem);
            // Optionally, you can also delete the selected item from the database using DatabaseManager
            try {
                DatabaseManager.deleteCutLumber(selectedItem[0]); // Assuming the first column is the ID
            } catch (SQLException e) {
                // Handle SQL exception
                e.printStackTrace();
            }
            cutTable.refresh(); // Refresh the TableView
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
            SellController sellController = loader.getController();
            sellController.setData(cutTable, rowData);
            sellController.setCutController(this);

            Scene scene = new Scene(root);

            String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
            scene.getStylesheets().add(css);

            sell.setTitle("Sell Cut Lumber");
            sell.setScene(scene);
            sell.show();
        }
    }

    @FXML
    void clearCustomerSearch(ActionEvent event) {

    }

    @FXML
    void deleteCustomer(ActionEvent event) {

    }

    @FXML
    void openNewCustomerWindow(ActionEvent event) {

    }

    @FXML
    void clearSearch(ActionEvent event) {

    }

    public void disableRelevantButtons(){
        sell_button.setDisable(true);
        edit_cut_button.setDisable(true);
        delete_button.setDisable(true);
    }

    @FXML
    public void refreshCutTable() throws SQLException {
        // Clear the existing data
        dataList.clear();
        // Reload data from the database
        dataList.addAll(DatabaseManager.readCutLumbers());
        cutTable.refresh();
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        Main.logIn();
        ((Stage) userRoleLabel.getScene().getWindow()).close();
    }
}