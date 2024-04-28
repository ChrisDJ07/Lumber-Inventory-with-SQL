package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class cut_controller {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button cut_button;

    @FXML
    private Button dashBoard_button;

    @FXML
    private Button history_button;

    @FXML
    private Button raw_button;

    @FXML
    private TableView<String> cutTable;


    @FXML
    void goToDashBoard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Views/Dashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        String css = this.getClass().getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        Image icon = new Image("/icon.png");

        stage.setResizable(false);
        stage.setTitle("Lumber Inventory");
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goToHistory(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Views/History.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        String css = this.getClass().getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        Image icon = new Image("/icon.png");

        stage.setResizable(false);
        stage.setTitle("Lumber Inventory");
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goToRaw(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Views/Raw Lumber.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        String css = this.getClass().getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        Image icon = new Image("/icon.png");

        stage.setResizable(false);
        stage.setTitle("Lumber Inventory");
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goToCut(ActionEvent event) throws IOException {
        //..don't implement
    }

}
