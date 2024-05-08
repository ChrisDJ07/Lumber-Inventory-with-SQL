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
    // Create a separate stage for the dashboard, gets rid of the centering issue
    private static Stage inventoryStage;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        // Design Files
        String css = this.getClass().getResource("/CSS/Application.css").toExternalForm();

        // Load the login form
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/Views/pop_ups/LoginForm.fxml"));
        Scene loginScene = new Scene(loginRoot);
        loginScene.getStylesheets().add(css);

        stage.setResizable(false);
        stage.setTitle("Login - Lumber Inventory");
        stage.setScene(loginScene);
        stage.getIcons().add(new Image("/icon.png"));
        stage.show();
    }

    // Method to show the dashboard after successful login
    public static void showDashboard() throws Exception {
        inventoryStage = new Stage();
        inventoryStage.setResizable(false);
        // Load the dashboard
        loadScene(new Scene(FXMLLoader.load(Main.class.getResource("/Views/Dashboard.fxml"))));
    }

    // Method to load scenes
    public static void loadScene(Scene scene){
        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);
        inventoryStage.setTitle("Lumber Inventory");
        inventoryStage.getIcons().add(new Image("/icon.png"));
        inventoryStage.setScene(scene);
        inventoryStage.show();
    }

    // Returns the Stage instance of Main
    public static Stage getStage(){
        return inventoryStage;
    }
}