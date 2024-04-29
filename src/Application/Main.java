package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/**
 * This is the main class for the Application
 * @author Christian Dave J. Janiola
 * @contributors Chriscent Louis June M. Pingol, ---
 * @Created 4/28/24
 **/


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Design Files
        String css = this.getClass().getResource("/CSS/Application.css").toExternalForm();

        // Load the login form
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/Views/LoginForm.fxml"));
        Scene loginScene = new Scene(loginRoot);
        loginScene.getStylesheets().add(css);

        stage.setResizable(false);
        stage.setTitle("Login - Lumber Inventory");
        stage.setScene(loginScene);
        stage.getIcons().add(new Image("/icon.png"));
        stage.show();
    }

    // Method to show the dashboard after successful login
    public static void showDashboard(Stage stage) throws Exception {
        // Load the dashboard
        Parent root = FXMLLoader.load(Main.class.getResource("/Views/Dashboard.fxml"));
        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Lumber Inventory");
        stage.setScene(scene);
        stage.show();
    }
}