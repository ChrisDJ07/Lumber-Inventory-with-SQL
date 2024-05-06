package Controllers.pop_ups;

import Application.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AddController implements Initializable {
    @FXML
    ChoiceBox<String> typeAddCB = new ChoiceBox<>();
    @FXML
    ChoiceBox<String> sizeAddCB = new ChoiceBox<>();
    @FXML
    Spinner<Integer> unitsAddSpinner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999);
        valueFactory.setValue(1);

        unitsAddSpinner.setValueFactory(valueFactory);
        unitsAddSpinner.setEditable(true);

        try {
            List<String> rawLumberTypes = DatabaseManager.readRawLumberTypes();
            List<String> sizeTypes = DatabaseManager.readSizeTypes();
            typeAddCB.getItems().addAll(rawLumberTypes);
            sizeAddCB.getItems().addAll(sizeTypes);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
