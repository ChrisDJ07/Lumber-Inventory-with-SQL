package Controllers;

import Application.DatabaseManager;
import Application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class LoginForm_controller {
    @FXML
    private TextField usernameTF;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private void login(ActionEvent e) throws SQLException {
        String username = usernameTF.getText();
        String password = passwordTF.getText();

        // Validate credentials (replace this with your actual validation logic)
        if (isValidCredentials(username, password)) {
            try {
                Main.showDashboard((Stage) usernameTF.getScene().getWindow());
            } catch (Exception error) {
                error.printStackTrace();
            }
        } else {
            // Show error message if credentials are invalid
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        }
    }

    @FXML
    void up(ActionEvent event) {
        System.out.println("UP");
    }

    // Dummy validation method (replace with your actual validation logic)
    private boolean isValidCredentials(String username, String password) throws SQLException {
        List<String[]> usersList;
        try {
            // Retrieve users list from the database
            usersList = (List<String[]>) DatabaseManager.readUsers();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // Unable to fetch user data, authentication failed
        }

        // Check if the provided username and password match any user in the list
        for (String[] userData : usersList) {
            String userID = userData[0];
            String storedPassword = userData[1];
            if (userID.equals(username) && storedPassword.equals(password)) {
                return true; // Authentication successful
            }
        }

        return false; // No matching user found, authentication failed
    }
}
