package Controllers.pop_ups;

import Application.DatabaseManager;
import Controllers.CutLumber;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AddCut implements Initializable {
    @FXML
    ChoiceBox<String> typeAddCB = new ChoiceBox<>();
    @FXML
    ChoiceBox<String> sizeAddCB = new ChoiceBox<>();
    @FXML
    TextField priceAddTF;
    @FXML
    Spinner<Integer> unitsAddSpinner;

    TableView<String[]> cutTable;
    CutLumber cutController;
    String ID;

    public void setCutController(CutLumber cutController) {
        this.cutController = cutController;
    }

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

    @FXML
    void createLumber() throws SQLException {
        String typeID = typeAddCB.getValue();
        String sizeID = sizeAddCB.getValue();
        try{
            int quantity = unitsAddSpinner.getValue();
            int unit_price = Integer.parseInt(priceAddTF.getText());
            DatabaseManager.addCutLumber_Janiola(typeID, unit_price, quantity, sizeID);
        }
        catch (Exception e){
            alert("Input Error", "Please enter a valid unit or price.");
        }

        cutController.refreshCutTable();
        ((Stage) typeAddCB.getScene().getWindow()).close();
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
