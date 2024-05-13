package Controllers.pop_ups;

import Application.DatabaseManager;
import Controllers.RawLumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewRaw implements Initializable {
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
            if(quantity<0){
                throw new NumberFormatException();
            }
            if (DatabaseManager.checkDuplicate_Janiola("rawlumber", "rawlumber_type", type) == 1) {
                throw new IllegalArgumentException("Duplicate");
            }
            if(type.trim().isEmpty()){
                throw  new IllegalArgumentException("Others");
            }
            DatabaseManager.addRawLumber_Janiola(type, Integer.toString(quantity));
            RawLumber.refreshTables();
            ((Stage) unitField.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            alert("Input Error", "Please enter a valid positive integer for units.");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Duplicate")) {
                alert("Duplicate type", "Type is already taken.");
            } else {
                alert("No type", "Please enter a type.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        typeField.setText("");
        unitField.setText("0");
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
        unitField.setText("0");
        typeField.setPromptText("Required");
    }
}
