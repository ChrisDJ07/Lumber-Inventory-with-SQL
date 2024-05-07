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
    private Button clearButton;

    @FXML
    private TextField supplierField;

    @FXML
    private TextField supplier_info_field;

    @FXML
    private ChoiceBox<String> typeBox;

    @FXML
    private TextField unitField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        try {
//            String[] types = DatabaseManager.getRawLumberList_Janiola();
//            typeBox.getItems().setAll(types);
//            // Now that typeBox is set up, get the selected type
//            String selectedType = RawController.getSelectedType();
//            typeBox.setValue(selectedType);
//            unitField.setText("0");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    @FXML
    void supplyRaw(ActionEvent event) {
        try {
            int quantity = Integer.parseInt(unitField.getText());
            String supplierName = supplierField.getText();
            String supplierContactInfo = supplier_info_field.getText();
            if(supplierName.trim().isEmpty()){
                throw new RuntimeException("Please enter a supplier name.");
            }
            // to be implemented furtherr
            ((Stage) unitField.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            alert("Input Error", "Please enter a valid integer for units.");
        } catch (RuntimeException e){
            alert("Input error", e.getMessage());
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        typeBox.setValue(null);
        unitField.setText("0");
        supplierField.setText("");
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
