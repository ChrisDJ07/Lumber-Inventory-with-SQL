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

public class EditProcessHistory implements Initializable {
    @FXML
    private Label dateLabel;

    @FXML
    private Button clearButton;
    @FXML
    private Button editButton;

    @FXML
    private TextField inputField;
    @FXML
    private TextField outputField;

    @FXML
    private ChoiceBox<String> sizeBox;
    @FXML
    private ChoiceBox<String> typeBox;

    String[] selected = null;
    String originalDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selected = History.getSelected();
        originalDate = selected[0];
        // Set fields/labels
        dateLabel.setText(selected[0]); // will implement edit later
        inputField.setText(selected[1]);
        outputField.setText(selected[2]);
        // Initialize boxes
        try {
            typeBox.getItems().addAll(DatabaseManager.getRawLumberList_Janiola());
            sizeBox.getItems().addAll(DatabaseManager.getSizeList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Set Boxes
        typeBox.setValue(selected[3]);
        sizeBox.setValue(selected[4]);
    }

    @FXML
    void editProcess(ActionEvent event) throws SQLException {
        if(DatabaseManager.checkCutDuplicate_Janiola(typeBox.getValue(), sizeBox.getValue()) == 0){
            alert("Input Error", "Cut Lumber doesn't exit, please enter another combination of type and size");
            return;
        }
        int input = Integer.parseInt(inputField.getText());
        int output = Integer.parseInt(outputField.getText());
        try {
            if(input<=0 || output<=0){
                throw new NumberFormatException();
            }
            DatabaseManager.editProcessHistory(dateLabel.getText(), input
                    , output, typeBox.getValue(), sizeBox.getValue(), originalDate);
        } catch (NumberFormatException e) {
            alert("Input Error", "Input and Output should contain positive integers");
            return;
        }
        ((Stage) inputField.getScene().getWindow()).close();
        History.refreshTables();
    }

    @FXML
    void clearFields(ActionEvent event) {
        inputField.clear();
        outputField.clear();
        typeBox.setValue(null);
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
