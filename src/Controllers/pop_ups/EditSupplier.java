package Controllers.pop_ups;

import Application.DatabaseManager;
import Controllers.RawLumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditSupplier implements Initializable {
    @FXML
    private TextField nameField;
    @FXML
    private TextField supplier_info_field;

    String originalName, originalInfo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        originalName = RawLumber.getSelectedSupplier();
        originalInfo = RawLumber.getSelectedSupplierInfo();
        nameField.setText(originalName);
        supplier_info_field.setText(originalInfo);
    }

    @FXML
    void editSupplier(ActionEvent event) {
        try{
            String supplierName = nameField.getText();
            String supplierContactInfo = supplier_info_field.getText();
            if(DatabaseManager.checkDuplicateForEdit_Janiola("suppliers", "supplier_info", supplierContactInfo.trim(),
                    "supplier_ID", DatabaseManager.getSupplierID_Janiola(originalName)) == 1
                    && !supplierContactInfo.trim().isEmpty()){
                throw new RuntimeException("Supplier info already exists, please enter a different one.");
            }
            if(DatabaseManager.checkDuplicateForEdit_Janiola("suppliers", "supplier_name", supplierName.trim(),
                    "supplier_ID", DatabaseManager.getSupplierID_Janiola(originalName)) == 1){
                throw new RuntimeException("Supplier name already exists, please enter a different one.");
            }
            if(supplierName.trim().isEmpty()){
                throw new RuntimeException("Please enter a supplier name.");
            }
            DatabaseManager.editSupplier_Janiola(supplierName, supplierContactInfo, DatabaseManager.getSupplierID_Janiola(originalName));
            RawLumber.refreshTables();

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
