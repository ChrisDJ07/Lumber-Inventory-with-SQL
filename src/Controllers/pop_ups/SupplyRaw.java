package Controllers.pop_ups;

import Application.DatabaseManager;
import Controllers.RawLumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SupplyRaw implements Initializable {
    @FXML
    private ChoiceBox<String> supplierBox;
    @FXML
    private Label supplyTypeLabel;
    @FXML
    private TextField unitField;
    @FXML
    private TextField priceField;

    RawLumber rawLumber;

    public void setSupplyController(RawLumber rawLumber) {
        this.rawLumber = rawLumber;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            supplyTypeLabel.setText(RawLumber.getSelectedType());
            String[] suppliers = DatabaseManager.getSupplierList();
            supplierBox.getItems().setAll(suppliers);
            unitField.setText("0");
            priceField.setText("0");
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
            if(quantity<0 || price<0){
                throw new RuntimeException("Please enter a positive value for units and/or price.");
            }
            if(supplierBox.getValue() == null){
                throw new RuntimeException("Please choose a supplier.");
            }
            String supplier = supplierBox.getValue();
            String type = supplyTypeLabel.getText();
            DatabaseManager.supplyRawLumber(supplier, type, quantity, price);
            RawLumber.refreshTables();
            rawLumber.setSupplyText(DatabaseManager.getLastSupply());

            ((Stage) unitField.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            alert("Input Error", "Please enter an integer for units and/or price.");
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            alert("Input error", e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        supplierBox.setValue(null);
        unitField.setText("0");
        priceField.setText("0");
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}