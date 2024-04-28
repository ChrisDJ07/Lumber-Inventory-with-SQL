package Application;/*
* This is the main class for the Application
* @author Christian Dave J. Janiola
* @Created 4/28/24
* */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/Dashboard.fxml"));
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        Image icon = new Image("/icon.png");

        stage.setResizable(false);
        stage.setTitle("Lumber Inventory");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }
}