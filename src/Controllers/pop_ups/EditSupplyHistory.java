package Controllers.pop_ups;

import Application.DatabaseManager;
import Controllers.History;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditSupplyHistory implements Initializable {
    @FXML
    private Label dateLabel;

    @FXML
    private Button clearButton;
    @FXML
    private Button editButton;

    @FXML
    private TextField quantityField;
    @FXML
    private TextField priceField;

    @FXML
    private ChoiceBox<String> lumberBox;
    @FXML
    private ChoiceBox<String> supplierBox;

    String[] selected = null;
    String originalDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selected = History.getSelected();
        originalDate = selected[0];
        // Set fields/labels
        dateLabel.setText(selected[0]); // will implement edit later
        quantityField.setText(selected[1]);
        priceField.setText(selected[4]);
        // Initialize boxes
        try {
            lumberBox.getItems().addAll(DatabaseManager.getRawLumberList_Janiola());
            supplierBox.getItems().addAll(DatabaseManager.getSupplierList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Set Boxes
        lumberBox.setValue(selected[3]);
        supplierBox.setValue(selected[2]);
    }

    @FXML
    void editSupply(ActionEvent event) throws SQLException {
        int quantity = Integer.parseInt(quantityField.getText());
        int price = Integer.parseInt(priceField.getText());
        try {
            if(quantity<=0 || price<=0){
                throw new NumberFormatException();
            }
            DatabaseManager.editSupplyHistory(dateLabel.getText(), quantity
                    , price, lumberBox.getValue(), supplierBox.getValue(), originalDate);
        } catch (NumberFormatException e) {
            alert("Input Error", "Quantity and Price should contain positive integers");
            return;
        }
        ((Stage) quantityField.getScene().getWindow()).close();
        History.refreshTables();
    }

    @FXML
    void clearFields(ActionEvent event) {
        quantityField.clear();
        priceField.clear();
        lumberBox.setValue(null);
        supplierBox.setValue(null);
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
