package Controllers.pop_ups;

import Application.DatabaseManager;
import Controllers.RawLumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProcessRaw implements Initializable {
    @FXML
    private TextField input_quantity_field;
    @FXML
    private TextField output_quantity_field;
    @FXML
    private ChoiceBox<String> sizeBox;
    @FXML
    private Label type_label;
    @FXML
    private Label quantity_label;

    int rawQuantityLimit;
    RawLumber rawLumber;

    public void setProcessController(RawLumber rawLumber) {
        this.rawLumber = rawLumber;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        type_label.setText(RawLumber.getSelectedType());
        String[] sizes = null;
        try {
            rawQuantityLimit = DatabaseManager.getRawQuantity(RawLumber.getSelectedType());
            quantity_label.setText(Integer.toString(rawQuantityLimit));
            sizes = DatabaseManager.getSizeList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sizeBox.getItems().setAll(sizes);
    }

    @FXML
    void proceedProcess(ActionEvent event) {
        try {
            int input_quantity = Integer.parseInt(input_quantity_field.getText());
            int output_quantity = Integer.parseInt(output_quantity_field.getText());
            if(input_quantity<0 || output_quantity<0){
                throw new NumberFormatException("Negative");
            }
            if(input_quantity==0 || output_quantity==0){
                throw new RuntimeException("zeroUnits");
            }
            if(input_quantity > rawQuantityLimit){
                throw new RuntimeException("exceedQuantity");
            }
            if(sizeBox.getValue()==null){
                throw new RuntimeException("noSize");
            }
            DatabaseManager.processRawLumber(RawLumber.getSelectedType(), sizeBox.getValue(), input_quantity, output_quantity);
            RawLumber.refreshTables();
            rawLumber.setProcessText(DatabaseManager.getLastProcess());

            ((Stage) input_quantity_field.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            alert("Input Error", "Please enter an integer value for units.");
        } catch (RuntimeException e){
            if(e.getMessage().equals("exceedQuantity")){
                alert("Input Quantity Exceeded", "Enter a value not greater than "+ rawQuantityLimit +".");
            } else if (e.getMessage().equals("noSize")) {
                alert("No Size Selected", "Please enter a size.");
            } else if (e.getMessage().equals("zeroUnits")) {
                alert("Zero Units", "Units cannot be zero.");
            } else if (e.getMessage().equals("negative")) {
                alert("Zero Units", "Please enter positive integers for units.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        sizeBox.setValue(null);
        input_quantity_field.setText("");
        output_quantity_field.setText("");
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
