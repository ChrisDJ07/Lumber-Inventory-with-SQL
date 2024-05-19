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

public class EditSoldHistory implements Initializable {
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
    private ChoiceBox<String> soldBox;
    @FXML
    private ChoiceBox<String> sizeBox;

    String[] selected = null;
    String originalDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selected = History.getSelected();
        originalDate = selected[0];
        // Set fields/labels
        dateLabel.setText(selected[0]); // will implement edit later
        quantityField.setText(selected[1]);
        priceField.setText(selected[2]);
        // Initialize boxes
        try {
            lumberBox.getItems().addAll(DatabaseManager.getRawLumberList_Janiola());
            soldBox.getItems().addAll(DatabaseManager.getCustomerList());
            sizeBox.getItems().addAll(DatabaseManager.getSizeList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Set Boxes
        lumberBox.setValue(selected[4]);
        soldBox.setValue(selected[3]);
        sizeBox.setValue(selected[5]);
    }

    @FXML
    void editSold(ActionEvent event) throws SQLException {
        if(DatabaseManager.checkCutDuplicate_Janiola(lumberBox.getValue(), sizeBox.getValue()) == 0){
            alert("Input Error", "Cut Lumber doesn't exit, please enter another combination of type and size");
            return;
        }
        if(!DatabaseManager.checkHasPrice(lumberBox.getValue(), sizeBox.getValue())){
            alert("Input Error", "Cut Lumber doesn't have price set, please set the price first or" +
                    " enter another combination of type and size");
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            int price = Integer.parseInt(priceField.getText());
            if(quantity<=0 || price<=0){
                throw new NumberFormatException();
            }
            DatabaseManager.editSoldHistory(
                    dateLabel.getText(),
                    quantity,
                    price,
                    lumberBox.getValue(),
                    soldBox.getValue(),
                    sizeBox.getValue(),
                    originalDate);
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
        soldBox.setValue(null);
        sizeBox.setValue(null);
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
