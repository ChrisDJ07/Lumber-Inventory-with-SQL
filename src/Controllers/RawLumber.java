package Controllers;

import Application.DatabaseManager;
import Application.Main;
import Controllers.pop_ups.ProcessRaw;
import Controllers.pop_ups.SupplyRaw;
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

public class RawLumber implements Initializable {
/* Raw Lumber Section */
    @FXML
    private Button process_button;
    @FXML
    private Button raw_edit_button;
    @FXML
    private Button delete_button;
    @FXML
    private Button supply_button;
    @FXML
    private TextField searchField;
    @FXML
    private Button raw_new_button;
    // Raw Lumber Table
    @FXML
    TableView<String[]> rawTable = new TableView<>();
    @FXML
    TableColumn<String[], String> typeColumn;
    @FXML
    TableColumn<String[], String> quantityColumn;
    static ObservableList<String[]> rawLumberList;
    static String[] selectedRawLumber;

/* Navigate */
    @FXML
    private Button dashBoard_button;
    @FXML
    private Button history_button;

/* Supplier Section */
    // Supplier
    @FXML
    private Button delete_supplier_button;
    @FXML
    private Button edit_supplier_button;
    @FXML
    private TextField supplierSearch;
    @FXML
    private Button new_supplier_button;
    // Supplier Table
    @FXML
    private TableView<String[]> supplierTable = new TableView<>();
    @FXML
    TableColumn<String[], String> supplierNameColumn;
    @FXML
    TableColumn<String[], String> supplierInfoColumn;
    static ObservableList<String[]> supplierList;
    static String[] selectedSupplier;

/* History Section */
    @FXML
    private Label lastProcessText;
    @FXML
    private Label lastSupplyText;

/* User Section*/
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
        // Hide buttons based on role access
        switch(userRoleLabel.getText()){
            case "Cashier" -> {
                dashBoard_button.setVisible(false);
                // hide raw buttons
                raw_new_button.setVisible(false);
                raw_edit_button.setVisible(false);
                delete_button.setVisible(false);
                // hide supplier buttons
                edit_supplier_button.setVisible(false);
                delete_supplier_button.setVisible(false);

            }
            case "Employee" -> {
                dashBoard_button.setVisible(false);
                history_button.setVisible(false);
                // hide raw buttons
                raw_new_button.setVisible(false);
                raw_edit_button.setVisible(false);
                delete_button.setVisible(false);
                supply_button.setVisible(false);
                process_button.setVisible(false);
                // hide supplier buttons
                new_supplier_button.setVisible(false);
                edit_supplier_button.setVisible(false);
                delete_supplier_button.setVisible(false);
            }
        }
        try {
            lastProcessText.setText(DatabaseManager.getLastProcess());
            lastSupplyText.setText(DatabaseManager.getLastSupply());
            disableRawButtons();
            disableSupplierButtons();

            rawLumberList = FXCollections.observableArrayList(DatabaseManager.readRawLumbers());
            rawTable.setItems(rawLumberList);
            FilteredList<String[]> filteredRawList = new FilteredList<>(rawLumberList);
            SortedList<String[]> sortedRawList = new SortedList<>(filteredRawList);
            sortedRawList.comparatorProperty().bind(rawTable.comparatorProperty());
            rawTable.setItems(sortedRawList);

            typeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
            quantityColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));

            rawTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    process_button.setDisable(false);
                    supply_button.setDisable(false);
                    raw_edit_button.setDisable(false);
                    delete_button.setDisable(false);
                    selectedRawLumber = rawTable.getSelectionModel().getSelectedItem();
                } else {
                    disableRawButtons();
                }
            });

            supplierList = FXCollections.observableArrayList(DatabaseManager.readSuppliers());
            supplierTable.setItems(supplierList);
            FilteredList<String[]> filteredSupplierList  = new FilteredList<>(supplierList);
            SortedList<String[]> sortedSupplierList = new SortedList<>(filteredSupplierList);
            sortedSupplierList.comparatorProperty().bind(supplierTable.comparatorProperty());
            supplierTable.setItems(sortedSupplierList);

            supplierNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
            supplierInfoColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));

            supplierTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    delete_supplier_button.setDisable(false);
                    edit_supplier_button.setDisable(false);
                    selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
                } else{
                    disableSupplierButtons();
                }
            });

            searchField.setPromptText("Search...");
            supplierSearch.setPromptText("Search...");

        /*Add listener to text property to filter data as user types*/
            // Raw Lumber
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
            // Supplier
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

        String css = Main.class.getResource("/Application/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        New.setTitle("New RawLumber");
        New.setResizable(false);
        New.setScene(scene);
        New.show();
    }
    @FXML
    void openEditWindow(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/pop_ups/EditRaw.fxml"));
        Parent root = loader.load();

        Stage edit = new Stage();
        edit.initOwner(Main.getStage());
        edit.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);

        String css = Main.class.getResource("/Application/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        edit.setTitle("Edit RawLumber");
        edit.setResizable(false);
        edit.setScene(scene);
        edit.show();
    }
    @FXML
    void openProcessWindow(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/pop_ups/ProcessRaw.fxml"));
        Parent root = loader.load();
        ProcessRaw processRawController = loader.getController();
        processRawController.setProcessController(this);

        Stage process = new Stage();
        process.initOwner(Main.getStage());
        process.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);

        String css = Main.class.getResource("/Application/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        process.setTitle("Process RawLumber");
        process.setResizable(false);
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

        String css = Main.class.getResource("/Application/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        supply.setTitle("Supply RawLumber");
        supply.setResizable(false);
        supply.setScene(scene);
        supply.show();
    }
    @FXML
    void openNewSupplierWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/pop_ups/NewSupplier.fxml"));
        Parent root = loader.load();

        Stage supply = new Stage();
        supply.initOwner(Main.getStage());
        supply.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);

        String css = Main.class.getResource("/Application/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        supply.setTitle("Add Supplier");
        supply.setResizable(false);
        supply.setScene(scene);
        supply.show();
    }
    @FXML
    void openEditSupplierWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/pop_ups/EditSupplier.fxml"));
        Parent root = loader.load();

        Stage supply = new Stage();
        supply.initOwner(Main.getStage());
        supply.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);

        String css = Main.class.getResource("/Application/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        supply.setTitle("Edit Supplier");
        supply.setResizable(false);
        supply.setScene(scene);
        supply.show();
    }

/* Delete Functions */
    @FXML
    void delete_rawLumber(ActionEvent event) throws SQLException {
        // check if selected Raw Lumber have any process/supply history
        if(DatabaseManager.checkRawReference(getSelectedType())){
            alert("Deletion Error", "This Raw Lumber already has a" +
                    " Process/Supply transaction and cannot be deleted.");
            return;
        }
        // Confirm Deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("RawLumber Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this Raw Lumber type?");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
        // Proceed Delete
        if(alert.getResult() == ButtonType.OK){
            DatabaseManager.deleteRawLumber(getSelectedType());
            refreshTables();
        }
    }
    @FXML
    void deleteSupplier(ActionEvent event) throws SQLException {
        // check if selected Supplier have any process/supply history
        if(DatabaseManager.checkSupplierReference(getSelectedSupplier())){
            alert("Deletion Error", "This Supplier already has a" +
                    " Supply transaction and cannot be deleted.");
            return;
        }
        // Confirm Deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Supplier Deletion");
        alert.setHeaderText("Are you sure you want to delete this Supplier?");
        alert.setContentText("Deleting this will completely remove it from the database.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
        // Proceed Delete
        if(alert.getResult() == ButtonType.OK){
            DatabaseManager.deleteSupplier(getSelectedSupplier());
            refreshTables();
        }
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

    @FXML
    void logOut(ActionEvent event) throws IOException {
        Main.logIn();
        ((Stage) userRoleLabel.getScene().getWindow()).close();
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    // Get Selected
    public static String getSelectedType(){
        return selectedRawLumber[0];
    }
    public static String getSelectedUnits(){
        return selectedRawLumber[1];
    }
    public static String getSelectedSupplier(){
        return selectedSupplier[0];
    }
    public static String getSelectedSupplierInfo(){
        return selectedSupplier[1];
    }
    // Set text
    public void setProcessText(String text){
        lastProcessText.setText(text);
    }
    public void setSupplyText(String text){
        lastSupplyText.setText(text);
    }
    // Disable buttons
    public void disableRawButtons(){
        process_button.setDisable(true);
        supply_button.setDisable(true);
        raw_edit_button.setDisable(true);
        delete_button.setDisable(true);
    }
    public void disableSupplierButtons(){
        delete_supplier_button.setDisable(true);
        edit_supplier_button.setDisable(true);
    }
    // Clear Search
    @FXML
    void clearSearch(ActionEvent event) {
        searchField.clear();
    }
    @FXML
    void clearSupplierSearch(ActionEvent event) {
        supplierSearch.clear();
    }
}
