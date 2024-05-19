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
    private TextField unitField;
    @FXML
    private TextField typeField;

    String originalType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        originalType = RawLumber.getSelectedType();
        typeField.setText(originalType);
        unitField.setText(RawLumber.getSelectedUnits());
    }

    @FXML
    void editRaw(ActionEvent event) {
        try {
            String type = typeField.getText();
            if(type.trim().isEmpty()){
                throw new RuntimeException("Please enter a type.");
            }
            if(DatabaseManager.checkDuplicateForEdit_Janiola("rawlumber", "rawlumber_type", type.trim(),
                    "rawlumber_ID", DatabaseManager.getRawID_Janiola(originalType)) == 1){
                throw new RuntimeException("Raw Lumber already exists, please enter a different one.");
            }
            int quantity = Integer.parseInt(unitField.getText());
            if(quantity<0){
                throw new NumberFormatException();
            }
            DatabaseManager.updateRaw(type, quantity, DatabaseManager.getRawID_Janiola(originalType));
            RawLumber.refreshTables();
            ((Stage) unitField.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            alert("Input Error", "Please enter a valid positive integer for units.");
        } catch (RuntimeException e){
            alert("Input Error", e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        unitField.setText("");
        typeField.setText("");
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
