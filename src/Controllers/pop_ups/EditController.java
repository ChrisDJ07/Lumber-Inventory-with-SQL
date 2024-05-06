package Controllers.pop_ups;

import Application.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class EditController implements Initializable {
    @FXML
    ChoiceBox<String> typeEditCB = new ChoiceBox<>();
    @FXML
    ChoiceBox<String> sizeEditCB = new ChoiceBox<>();
    @FXML
    TextField priceEditTF;
    @FXML
    Spinner<Integer> unitsEditSpinner;

    // Method to set data received from CutController
    public void setData(String[] rowData) {
        // Assuming the rowData array contains the following elements in order:
        // rowData[0]: ID
        // rowData[1]: Type
        // rowData[2]: Size
        // rowData[3]: Price
        // rowData[4]: Quantity

        // Set the values of the ChoiceBoxes, TextField, and Spinner
        typeEditCB.setValue(rowData[1]);
        sizeEditCB.setValue(rowData[2]);
        priceEditTF.setText(rowData[3]);
        unitsEditSpinner.getValueFactory().setValue(Integer.parseInt(rowData[4]));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999);
        valueFactory.setValue(1);

        unitsEditSpinner.setValueFactory(valueFactory);
        unitsEditSpinner.setEditable(true);
        try {
            List<String> rawLumberTypes = DatabaseManager.readRawLumberTypes();
            List<String> sizeTypes = DatabaseManager.readSizeTypes();
            typeEditCB.getItems().addAll(rawLumberTypes);
            sizeEditCB.getItems().addAll(sizeTypes);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
