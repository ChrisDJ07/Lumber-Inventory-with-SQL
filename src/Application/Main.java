package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * This is the main class for the Application
 * @author Christian Dave J. Janiola
 * @contributors Chriscent Louis June M. Pingol, ---
 * @Created 4/28/24
 **/

public class Main extends Application {
    private static Stage inventoryStage; // Create a separate stage for the dashboard, gets rid of the centering issue
    private static String user;
    private static String role;
    private static Stage logInStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        logInStage = stage;
        logIn();
    }

    /**
     * Method to show the dashboard after successful login
     */
    public static void showDashboard() throws Exception {
        inventoryStage = new Stage();
        inventoryStage.setResizable(false);
        // Load the dashboard
        loadScene(new Scene(FXMLLoader.load(Main.class.getResource("/Views/Dashboard.fxml"))));
    }

    /**
     * Initializes Log-in
     */
    public static void logIn() throws IOException {
        // Design Files
        String css = Main.class.getResource("/Application/Application.css").toExternalForm();

        // Load the login form
        Parent loginRoot = FXMLLoader.load(Main.class.getResource("/Views/pop_ups/LoginForm.fxml"));
        Scene loginScene = new Scene(loginRoot);
        loginScene.getStylesheets().add(css);

        logInStage.setResizable(false);
        logInStage.setTitle("Login - Lumber Inventory");
        logInStage.setScene(loginScene);
        logInStage.getIcons().add(new Image("/icon.png"));
        logInStage.show();
    }

    /**
     * Method to load scenes
     * @param scene - prepared scene to be loaded
     */
    public static void loadScene(Scene scene){
        String css = Main.class.getResource("/Application/Application.css").toExternalForm();
        scene.getStylesheets().add(css);
        inventoryStage.setTitle("Lumber Inventory");
        inventoryStage.getIcons().add(new Image("/icon.png"));
        inventoryStage.setScene(scene);
        inventoryStage.show();
    }

    /**
     * Returns the Stage instance of Main
     * @return - inventoryStage
     */
    public static Stage getStage(){
        return inventoryStage;
    }

    /**
     * Set the name and role of the current user
     * @param name - Username of current user
     */
    public static void setUser(String name) throws SQLException {
        user = name;
        role = DatabaseManager.getCurrentUserRole(name); // Fetch role of user from the database
    }

    /**
     * @return - name of current user
     */
    public static String getUser(){
        return user;
    }

    /**
     * @return - role of current user
     */
    public static String getUserRole(){
        return role;
    }
}