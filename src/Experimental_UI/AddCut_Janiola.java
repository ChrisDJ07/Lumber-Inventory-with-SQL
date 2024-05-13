package Experimental_UI;

import Application.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCut_Janiola implements Initializable {

    @FXML
    private TextField priceField;

    @FXML
    private TextField unitField;

    @FXML
    private ChoiceBox<String> sizeBox;

    @FXML
    private ChoiceBox<String> typeBox;


    SpinnerValueFactory<Integer> valueFactory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            String[] types = DatabaseManager.getRawLumberList_Janiola();
            String[] sizes = DatabaseManager.getSizeList();
            typeBox.getItems().setAll(types);
            sizeBox.getItems().setAll(sizes);
            typeBox.setValue(types[0]);
            sizeBox.setValue(sizes[0]);

            priceField.setText("0");
            unitField.setText("0");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        priceField.setText("0");
        unitField.setText("0");
    }

    @FXML
    void submitAdd(ActionEvent event) {
        String typeID = typeBox.getValue();
        String sizeID = sizeBox.getValue();
        try{
            int quantity = Integer.parseInt(unitField.getText());
            int unit_price = Integer.parseInt(priceField.getText());
            DatabaseManager.addCutLumber_Janiola(typeID, unit_price, quantity, sizeID);
        }
        catch (Exception e){
            alert("Input Error", "Please enter a valid unit or price.");
        }
        ((Stage) typeBox.getScene().getWindow()).close();
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
