package Controllers.pop_ups;

import Application.DatabaseManager;
import Controllers.CutLumber;
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

public class EditCustomer implements Initializable {
    @FXML
    private TextField nameField;
    @FXML
    private TextField customer_info_field;

    String originalName, originalInfo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        originalName = CutLumber.getSelectedCustomer()[0];
        originalInfo = CutLumber.getSelectedCustomer()[1];
        nameField.setText(originalName);
        customer_info_field.setText(originalInfo);
    }

    @FXML
    void editCustomer(ActionEvent event) {
        try{
            String customerName = nameField.getText();
            String customerContactInfo = customer_info_field.getText();
            if(DatabaseManager.checkDuplicateForEdit_Janiola("customers", "customer_info", customerContactInfo.trim(),
                    "customer_ID", DatabaseManager.getCustomerID(originalName)) == 1
                    && !customerContactInfo.trim().isEmpty()){
                throw new RuntimeException("Customer info already exists, please enter a different one.");
            }
            if(DatabaseManager.checkDuplicateForEdit_Janiola("customers", "customer_name", customerName.trim(),
                    "customer_ID", DatabaseManager.getCustomerID(originalName)) == 1){
                throw new RuntimeException("Customer name already exists, please enter a different one.");
            }
            if(customerName.trim().isEmpty()){
                throw new RuntimeException("Please enter a customer name.");
            }
            DatabaseManager.editCustomer_Janiola(customerName, customerContactInfo, DatabaseManager.getCustomerID(originalName));
            CutLumber.refreshCutTable();

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
        customer_info_field.setText("");
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
