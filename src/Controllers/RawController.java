package Controllers;

import Application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RawController {

    @FXML
    private void goToDashBoard(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/Dashboard.fxml"))));
    }

    @FXML
    private void goToCut(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/CutLumber.fxml"))));
    }

    @FXML
    private void goToHistory(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/History.fxml"))));
    }

    @FXML
    void openNewWindow(ActionEvent event) throws IOException{
        Stage add = new Stage();
        Parent root = FXMLLoader.load(Main.class.getResource("/Views/pop_ups/NewRaw.fxml"));
        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        add.setTitle("New Raw Lumber");
        add.setScene(scene);
        add.show();
    }

    @FXML
    void openEditWindow(ActionEvent event) throws IOException{
        Stage add = new Stage();
        Parent root = FXMLLoader.load(Main.class.getResource("/Views/pop_ups/EditRaw.fxml"));
        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        add.setTitle("Edit Raw Lumber");
        add.setScene(scene);
        add.show();
    }

    @FXML
    void openProcessWindow(ActionEvent event) throws IOException{
        Stage add = new Stage();
        Parent root = FXMLLoader.load(Main.class.getResource("/Views/pop_ups/ProcessRaw.fxml"));
        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        add.setTitle("Process Raw Lumber");
        add.setScene(scene);
        add.show();
    }
}
