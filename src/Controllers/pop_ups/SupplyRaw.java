package Controllers.pop_ups;

import Application.DatabaseManager;
import Controllers.RawController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SupplyRaw implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton1;

    @FXML
    private Button clearButton2;

    @FXML
    private TextField nameField;

    @FXML
    private ChoiceBox<String> supplierBox;

    @FXML
    private TextField supplier_info_field;

    @FXML
    private Button supplyButton;

    @FXML
    private ChoiceBox<String> typeBox;

    @FXML
    private TextField unitField;

    @FXML
    private TextField priceField;

    RawController rawController;

    public void setSupplyController(RawController rawController) {
        this.rawController = rawController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            String[] types = DatabaseManager.getRawLumberList_Janiola();
            typeBox.getItems().setAll(types);
            refreshSupplierBox();
            unitField.setText("0");
            priceField.setText("0");
            supplier_info_field.setText("");
        } catch (SQLException e) {
            alert("Database Error", "There is an error connecting to the database.");
        }
    }

    @FXML
    void supplyRaw(ActionEvent event) {
        try {
            int quantity = Integer.parseInt(unitField.getText());
            int price = Integer.parseInt(priceField.getText());
            if (quantity == 0){
                throw new RuntimeException("Units supplied cannot be zero.");
            }
            if(supplierBox.getValue() == null){
                throw new RuntimeException("Please choose a supplier.");
            }
            if(typeBox.getValue() ==  null){
                throw new RuntimeException("Please choose a type.");
            }
            String supplier = supplierBox.getValue();
            String type = typeBox.getValue();
            DatabaseManager.supplyRawLumber(supplier, type, Integer.toString(quantity), Integer.toString(price));
            RawController.refreshTable();

            ((Stage) unitField.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            alert("Input Error", "Please enter an integer for units and/or price.");
        } catch (RuntimeException e){
            alert("Input error", e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void addSupplier(ActionEvent event) {
        try{
            String supplierName = nameField.getText();
            String supplierContactInfo = supplier_info_field.getText();
            if(DatabaseManager.checkDuplicate_Janiola("supplier", "supplier_info", supplierContactInfo.trim()) == 1
                    && !supplierContactInfo.trim().isEmpty()){
                throw new RuntimeException("Supplier info already exists, please enter a different one.");
            }
            if(DatabaseManager.checkDuplicate_Janiola("supplier", "supplier_name", supplierName.trim()) == 1){
                throw new RuntimeException("Supplier name already exists, please enter a different one.");
            }
            if(supplierName.trim().isEmpty()){
                throw new RuntimeException("Please enter a supplier name.");
            }
            DatabaseManager.addSupplier_Janiola(supplierName, supplierContactInfo);
            refreshSupplierBox();
            supplierBox.setValue(supplierName);
        } catch (RuntimeException e){
            alert("Input error", e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clearFields1(ActionEvent event) {
        typeBox.setValue(null);
        supplierBox.setValue(null);
        unitField.setText("0");
        priceField.setText("0");
    }

    @FXML
    void clearFields2(ActionEvent event) {
        nameField.setText("");
        supplier_info_field.setText("");
    }

    void refreshSupplierBox() throws SQLException{
        supplierBox.getItems().clear();
        try{
        String[] suppliers = DatabaseManager.getSupplierList();
        supplierBox.getItems().setAll(suppliers);
        } catch (SQLException e) {
            alert("Database Error", "There is an error connecting to the database.");
        }
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}