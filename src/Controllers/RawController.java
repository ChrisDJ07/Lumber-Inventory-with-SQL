package Controllers;

import Application.DatabaseManager;
import Application.Main;
import Controllers.pop_ups.EditRaw;
import Controllers.pop_ups.NewSupplier;
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
    /*Raw Lumber Section*/
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
    static ObservableList<String[]> rawLumberList;
    static String[] selectedRawLumber;

    /*Supplier Section*/
    @FXML
    private Button delete_supplier_button;
    @FXML
    private TextField supplierSearch;

    @FXML
    private Label lastProcessText;
    @FXML
    private Label lastSupplyText;

    @FXML
    private TableView<String[]> supplierTable = new TableView<>();
    @FXML
    TableColumn<String[], String> supplierNameColumn;
    @FXML
    TableColumn<String[], String> supplierInfoColumn;
    static ObservableList<String[]> supplierList;
    static String[] selectedSupplier;

    // User Account
    @FXML
    private Button logout_button;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userRoleLabel;


    /* Initialize tables */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLabel.setText(Main.getUser());
        userRoleLabel.setText(Main.getUserRole());
        try {
            lastProcessText.setText(DatabaseManager.getLastProcess());
            lastSupplyText.setText(DatabaseManager.getLastSupply());
            disableRelevantButtons();

            rawLumberList = FXCollections.observableArrayList(DatabaseManager.readRawLumbers());
            rawTable.setItems(rawLumberList);
            FilteredList<String[]> filteredRawList = new FilteredList<>(rawLumberList);
            rawTable.setItems(filteredRawList);
            typeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
            quantityColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));

            rawTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    process_button.setDisable(false);
                    raw_edit_button.setDisable(false);
                    delete_button.setDisable(false);
                    selectedRawLumber = rawTable.getSelectionModel().getSelectedItem();
                }
            });

            supplierList = FXCollections.observableArrayList(DatabaseManager.readSuppliers());
            supplierTable.setItems(supplierList);
            FilteredList<String[]> filteredSupplierList  = new FilteredList<>(supplierList);
            supplierTable.setItems(filteredSupplierList);
            supplierNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
            supplierInfoColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));

            supplierTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    delete_supplier_button.setDisable(false);
                    selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
                }
            });

            searchField.setPromptText("Search...");
            supplierSearch.setPromptText("Search...");

            /*Add listener to text property to filter data as user types*/
            // Raw Lumbers
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredRawList.setPredicate(item -> {
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
            // Suppliers
            supplierSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredSupplierList.setPredicate(item -> {
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

    /* Navigate scenes */
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

    /*Open Pop-ups*/
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
    void openNewSupplierWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/pop_ups/NewSupplier.fxml"));
        Parent root = loader.load();
        NewSupplier supplierController = loader.getController();
        supplierController.setSupplierController(this);

        Stage supply = new Stage();
        supply.initOwner(Main.getStage());
        supply.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        supply.setResizable(false);
        supply.setTitle("Add Supplier");
        supply.setScene(scene);
        supply.show();
    }

    /* Delete Functions */
    @FXML
    void delete_rawLumber(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Raw Lumber Detetion");
        alert.setHeaderText("Are you sure you want to delete this Raw Lumber type?");
        alert.setContentText("Deleting this will also affect Cut Lumber with this type.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.OK){
            DatabaseManager.deleteRawLumber(getSelectedType());
            refreshTables();
        }
        disableRelevantButtons();
    }
    @FXML
    void deleteSupplier(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Supplier Detetion");
        alert.setHeaderText("Are you sure you want to delete this Supplier?");
        alert.setContentText("Deleting this will completely remove it from the database.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.OK){
            DatabaseManager.deleteSupplier(getSelectedSupplier());
            refreshTables();
        }
        disableRelevantButtons();
    }

    /* Other Functions */
    public static void refreshTables() {
        try {
            rawLumberList.clear();
            rawLumberList.addAll(DatabaseManager.readRawLumbers());
            supplierList.clear();
            supplierList.addAll(DatabaseManager.readSuppliers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String getSelectedType(){
        return selectedRawLumber[0];
    }
    public static String getSelectedUnits(){
        return selectedRawLumber[1];
    }
    public static String getSelectedSupplier(){
        return selectedSupplier[0];
    }
    public void setProcessText(String text){
        lastProcessText.setText(text);
    }
    public void setSupplyText(String text){
        lastSupplyText.setText(text);
    }
    public void disableRelevantButtons(){
        process_button.setDisable(true);
        raw_edit_button.setDisable(true);
        delete_button.setDisable(true);
        delete_supplier_button.setDisable(true);
    }
    @FXML
    void clearSearch(ActionEvent event) {
        searchField.clear();
    }
    @FXML
    void clearSupplierSearch(ActionEvent event) {
        supplierSearch.clear();
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        Main.logIn();
        ((Stage) userRoleLabel.getScene().getWindow()).close();
    }
}
