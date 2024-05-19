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
    TextField unitsAddTF;

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
            int quantity = Integer.parseInt(unitsAddTF.getText());
            int unit_price = Integer.parseInt(priceAddTF.getText());
            if(quantity<0 || unit_price<0){
                throw new NumberFormatException();
            }
            if(unit_price == 0){
                alert("Input error", "Price cannot be zero.");
                return;
            }
            if(typeID == null || sizeID == null){
                alert("Input error", "Please enter a Type and Size.");
                return;
            }
            if(DatabaseManager.checkCutDuplicate_Janiola(typeID, sizeID) == 1){
                alert("Input error", "Cut Lumber already exists.");
                return;
            }
            DatabaseManager.addCutLumber_Janiola(typeID, unit_price, quantity, sizeID);
            CutLumber.refreshCutTable();
            ((Stage) typeAddCB.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            alert("Input Error", "Please enter a valid unit or price.");
        }
    }

    @FXML
    void clearInput() {
        typeAddCB.setValue(null);
        sizeAddCB.setValue(null);
        unitsAddTF.clear();
        priceAddTF.clear();
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
