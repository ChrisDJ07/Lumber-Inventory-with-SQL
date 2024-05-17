package Controllers.pop_ups;

import Application.DatabaseManager;
import Controllers.CutLumber;
import Controllers.RawLumber;
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
    CutLumber cutLumber;
    String ID, originalType, originalSize;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000);
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

    // Method to set data received from CutLumber
    public void setData(TableView<String[]> cutTable, String[] rowData) {
        // Assuming the rowData array contains the following elements in order:
        // rowData[0]: ID
        // rowData[1]: Type
        // rowData[2]: Size
        // rowData[3]: Price
        // rowData[4]: Quantity

        originalType = rowData[1];
        originalSize = rowData[2];

        // Set the values of the ChoiceBoxes, TextField, and Spinner
        ID = rowData[0];
        typeEditCB.setValue(rowData[1]);
        sizeEditCB.setValue(rowData[2]);
        priceEditTF.setText(rowData[3]);
        unitsEditSpinner.getValueFactory().setValue(Integer.parseInt(rowData[4]));
    }

    public void setCutController(CutLumber cutLumber) {
        this.cutLumber = cutLumber;
    }

    @FXML
    void submitEdit() throws SQLException {
        // Get the values of the ChoiceBoxes, TextField, and Spinner
        String type = typeEditCB.getValue();
        String size = sizeEditCB.getValue();
        String price = priceEditTF.getText();
        int quantity = unitsEditSpinner.getValue();

        try {
            if(DatabaseManager.checkDuplicateForEditCut_Janiola(DatabaseManager.getRawID_Janiola(type),
                    DatabaseManager.getSizeID_Janiola(size), Integer.parseInt(ID)) == 1){
                throw new RuntimeException("Cut Lumber already exists, please enter a different combination of type and size.");
            }
            if(quantity<0 || Integer.parseInt(price)<0){
                throw new NumberFormatException();
            }
            DatabaseManager.updateCutLumber(Integer.parseInt(ID), type, Integer.parseInt(price), quantity, size);
            // Refresh the data table
            CutLumber.refreshCutTable();
            cutLumber.disableRelevantButtons();
            // Close the FXML window
            Stage stage = (Stage) typeEditCB.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            alert("Input Error", "Please enter a valid positive integer for units.");
        } catch (RuntimeException e){
            alert("Input Error", e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        /*
        // Assuming the rowData array contains the following elements in order:
         rowData[0]: ID
         rowData[1]: Type
         rowData[2]: Size
         rowData[3]: Price
         rowData[4]: Quantity
         Update the values of the rowData array
        rowData[1] = type;
        rowData[2] = size;
        rowData[3] = price;
        rowData[4] = String.valueOf(units);

        // Update the row in the TableView
        // Assuming the TableView is named cutTable
        cutTable.getItems().set(cutTable.getSelectionModel().getSelectedIndex(), rowData);
         */
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
