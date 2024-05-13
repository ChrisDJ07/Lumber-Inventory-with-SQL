package Controllers.pop_ups;

import Application.DatabaseManager;
import Controllers.RawLumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditRaw implements Initializable {
    @FXML
    private Label typeLabel;
    @FXML
    private TextField unitField;

    @FXML
    void editRaw(ActionEvent event) {
        try {
            int quantity = Integer.parseInt(unitField.getText());
            if(quantity<0){
                throw new NumberFormatException();
            }
            DatabaseManager.updateRawQuantity(typeLabel.getText(), Integer.toString(quantity));
            RawLumber.refreshTables();
            ((Stage) unitField.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            alert("Input Error", "Please enter a valid positive integer for units.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        unitField.setText("");
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeLabel.setText(RawLumber.getSelectedType());
        unitField.setText(RawLumber.getSelectedUnits());
    }
}
