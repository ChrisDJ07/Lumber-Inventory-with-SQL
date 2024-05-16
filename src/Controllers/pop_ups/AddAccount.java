package Controllers.pop_ups;

import Application.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddAccount implements Initializable {

    @FXML
    private Button addButton;
    @FXML
    private Button clearButton;

    @FXML
    private PasswordField passField1;
    @FXML
    private PasswordField passField2;
    @FXML
    private TextField userNameField;

    @FXML
    private ChoiceBox<String> roleBox;

    String userName;
    String password1;
    String password2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addButton.setDisable(true);
        roleBox.getItems().addAll("Employee", "Admin", "Cashier");
        roleBox.setValue(null);
        // Add listener for input fields
        passField1.textProperty().addListener((observable, oldValue, newValue) -> checkInputs());
        passField2.textProperty().addListener((observable, oldValue, newValue) -> checkInputs());
        userNameField.textProperty().addListener((observable, oldValue, newValue) -> checkInputs());
        roleBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            checkInputs();
        });
    }

    @FXML
    void addAccount(ActionEvent event) {
        try{
            if(DatabaseManager.checkDuplicate_Janiola("application_users", "employee_userName"
            , userName) == 1){
                throw new RuntimeException("UserName is already taken, choose a different one.");
            }
            if(!password1.equals(password2)){
                throw new RuntimeException("Repeated password does not match.");
            }
            DatabaseManager.addAccount(userName, password1, roleBox.getValue());
            ((Stage) userNameField.getScene().getWindow()).close();
        } catch (RuntimeException e){
            alert("Input Error", e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        userNameField.clear();
        passField1.clear();
        passField2.clear();
        roleBox.setValue(null);
    }

    private void checkInputs() {
        userName = userNameField.getText().trim();
        password1 = passField1.getText().trim();
        password2 = passField2.getText().trim();

        boolean allFieldsHaveText = !userName.isEmpty() && !password1.isEmpty() && !password2.isEmpty() && !(roleBox.getValue() == null);
        addButton.setDisable(!allFieldsHaveText);
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
}
