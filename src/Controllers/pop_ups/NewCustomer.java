package Controllers.pop_ups;

import Application.DatabaseManager;
import Controllers.CutLumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class NewCustomer {

    @FXML
    private TextField nameField;
    @FXML
    private TextField customer_info_field;

    CutLumber cutController;

    public void setCustomerController(CutLumber cutController) {
        this.cutController = cutController;
    }

    @FXML
    void addCustomer(ActionEvent event) {
        try{
            String customerName = nameField.getText();
            String customerContactInfo = customer_info_field.getText();
            if(DatabaseManager.checkDuplicate_Janiola("customers", "customer_info", customerContactInfo.trim()) == 1
                    && !customerContactInfo.trim().isEmpty()){
                throw new RuntimeException("Customer info already exists, please enter a different one.");
            }
            if(DatabaseManager.checkDuplicate_Janiola("customers", "customer_name", customerName.trim()) == 1){
                throw new RuntimeException("Customer name already exists, please enter a different one.");
            }
            if(customerName.trim().isEmpty()){
                throw new RuntimeException("Please enter a customer name.");
            }
            DatabaseManager.addCustomer(customerName, customerContactInfo);
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
