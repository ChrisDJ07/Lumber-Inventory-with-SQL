package Controllers.pop_ups;

import Application.DatabaseManager;
import Controllers.RawController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class NewRaw {

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton;

    @FXML
    private TextField typeField;

    @FXML
    private TextField unitField;

    @FXML
    void addRaw(ActionEvent event) {
        String type = typeField.getText();
        try {
            int quantity = Integer.parseInt(unitField.getText());
            if (DatabaseManager.checkDuplicate_Janiola("rawlumber", "rawlumber_type", type) == 0) {
                DatabaseManager.addRawLumber_Janiola(type, Integer.toString(quantity));
                RawController.refreshTable();
                ((Stage) unitField.getScene().getWindow()).close();
            } else {
                throw new IllegalArgumentException("Duplicate");
            }
        } catch (NumberFormatException e) {
            alert("Input Error", "Please enter a valid integer for units.");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Duplicate")) {
                alert("Input Error", "Type is already taken.");
            } else {
                alert("Input Error", "An unexpected error occurred.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        typeField.setText("");
        unitField.setText("");
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}