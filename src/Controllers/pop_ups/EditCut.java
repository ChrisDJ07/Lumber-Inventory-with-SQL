package Controllers.pop_ups;

import Application.DatabaseManager;
import Controllers.CutController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class EditCut implements Initializable {
    @FXML
    ChoiceBox<String> typeEditCB = new ChoiceBox<>();
    @FXML
    ChoiceBox<String> sizeEditCB = new ChoiceBox<>();
    @FXML
    TextField priceEditTF;
    @FXML
    Spinner<Integer> unitsEditSpinner;

    TableView<String[]> cutTable;
    CutController cutController;
    String ID;

    // Method to set data received from CutController
    public void setData(TableView<String[]> cutTable, String[] rowData) {
        // Assuming the rowData array contains the following elements in order:
        // rowData[0]: ID
        // rowData[1]: Type
        // rowData[2]: Size
        // rowData[3]: Price
        // rowData[4]: Quantity

        // Set the values of the ChoiceBoxes, TextField, and Spinner
        ID = rowData[0];
        typeEditCB.setValue(rowData[1]);
        sizeEditCB.setValue(rowData[2]);
        priceEditTF.setText(rowData[3]);
        unitsEditSpinner.getValueFactory().setValue(Integer.parseInt(rowData[4]));
    }

    public void setCutController(CutController cutController) {
        this.cutController = cutController;
    }

    @FXML
    void submitEdit() throws SQLException {
        // Get the values of the ChoiceBoxes, TextField, and Spinner
        String type = typeEditCB.getValue();
        String size = sizeEditCB.getValue();
        String price = priceEditTF.getText();
        int quantity = unitsEditSpinner.getValue();

        // Assuming the rowData array contains the following elements in order:
        // rowData[0]: ID
        // rowData[1]: Type
        // rowData[2]: Size
        // rowData[3]: Price
        // rowData[4]: Quantity
        // Update the values of the rowData array
//        rowData[1] = type;
//        rowData[2] = size;
//        rowData[3] = price;
//        rowData[4] = String.valueOf(units);
//
//        // Update the row in the TableView
//        // Assuming the TableView is named cutTable
//        cutTable.getItems().set(cutTable.getSelectionModel().getSelectedIndex(), rowData);

        DatabaseManager.updateCutLumber(ID, type, price, quantity, size);

        // Refresh the data table
        cutController.refreshTables();
        cutController.disableRelevantButtons();

        // Close the FXML window
        Stage stage = (Stage) typeEditCB.getScene().getWindow();
        stage.close();
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
