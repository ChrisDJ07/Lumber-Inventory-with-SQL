package Controllers;

import Application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CutController {

    @FXML
    private void goToRaw(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/RawLumber.fxml"))));
    }

    @FXML
    private void goToDashBoard(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/Dashboard.fxml"))));
    }

    @FXML
    private void goToHistory(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/History.fxml"))));
    }

    @FXML
    void openSizes(ActionEvent event) throws IOException {
        Stage sizes = new Stage();
        Parent root = FXMLLoader.load(Main.class.getResource("/Views/pop_ups/Sizes.fxml"));
        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        sizes.setTitle("Lumber Sizes");
        sizes.setScene(scene);
        sizes.show();
    }

    @FXML
    void openAddWindow(ActionEvent event) throws IOException{
        Stage add = new Stage();
        Parent root = FXMLLoader.load(Main.class.getResource("/Views/pop_ups/AddCut.fxml"));
        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        add.setTitle("Add Cut Lumber");
        add.setScene(scene);
        add.show();
    }

    @FXML
    void openEditWindow(ActionEvent event) throws IOException{
        Stage add = new Stage();
        Parent root = FXMLLoader.load(Main.class.getResource("/Views/pop_ups/EditCut.fxml"));
        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        add.setTitle("Edit Cut Lumber");
        add.setScene(scene);
        add.show();
    }

    @FXML
    void openSellWindow(ActionEvent event) throws IOException{
        Stage add = new Stage();
        Parent root = FXMLLoader.load(Main.class.getResource("/Views/pop_ups/SellCut.fxml"));
        Scene scene = new Scene(root);

        String css = Main.class.getResource("/CSS/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        add.setTitle("Sell Cut Lumber");
        add.setScene(scene);
        add.show();
    }
}
