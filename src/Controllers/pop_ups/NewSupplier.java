package Controllers.pop_ups;

import Application.DatabaseManager;
import Controllers.RawController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class NewSupplier {

    @FXML
    private TextField nameField;
    @FXML
    private TextField supplier_info_field;

    RawController rawController;

    public void setSupplierController(RawController rawController) {
        this.rawController = rawController;
    }

    @FXML
    void addSupplier(ActionEvent event) {
        try{
            String supplierName = nameField.getText();
            String supplierContactInfo = supplier_info_field.getText();
            if(DatabaseManager.checkDuplicate_Janiola("suppliers", "supplier_info", supplierContactInfo.trim()) == 1
                    && !supplierContactInfo.trim().isEmpty()){
                throw new RuntimeException("Supplier info already exists, please enter a different one.");
            }
            if(DatabaseManager.checkDuplicate_Janiola("suppliers", "supplier_name", supplierName.trim()) == 1){
                throw new RuntimeException("Supplier name already exists, please enter a different one.");
            }
            if(supplierName.trim().isEmpty()){
                throw new RuntimeException("Please enter a supplier name.");
            }
            DatabaseManager.addSupplier_Janiola(supplierName, supplierContactInfo);
            RawController.refreshTables();

            ((Stage) nameField.getScene().getWindow()).close();
        } catch (RuntimeException e){
            alert("Input error", e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        nameField.setText("");
        supplier_info_field.setText("");
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
