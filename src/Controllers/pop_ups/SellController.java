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

public class SellController implements Initializable  {
    @FXML
    ChoiceBox<String> clientCB = new ChoiceBox<>();
    @FXML
    Label typeLabel;
    @FXML
    Label sizeLabel;
    @FXML
    Spinner<Integer> unitsSellSpinner;

    TableView<String[]> cutTable;
    CutController cutController;
    private static int ID;
    private static String sold_lumber;
    private static int quantity;

    // Method to set data received from CutController
    public void setData(TableView<String[]> cutTable, String[] rowData) {
        // Assuming the rowData array contains the following elements in order:
        // rowData[0]: ID
        // rowData[1]: Type
        // rowData[2]: Size
        // rowData[3]: Price
        // rowData[4]: Quantity
        this.cutTable = cutTable;
        // Set the values of the ChoiceBoxes, TextField, and Spinner
        typeLabel.setText(rowData[1]);
        sizeLabel.setText(rowData[2]);
        ID = Integer.parseInt(rowData[0]);
        sold_lumber = rowData[1];
        quantity = Integer.parseInt(rowData[4]);
    }

    public void setCutController(CutController cutController) {
        this.cutController = cutController;
    }

    public void proceedSelling() throws SQLException {
        if (clientCB.getValue() == null) {
            // Show an alert to prompt the user to select a client
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Client Not Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a client.");
            alert.showAndWait();
            return; // Exit the method if no client is selected
        }

        DatabaseManager.addSold_To(ID, unitsSellSpinner.getValue(), clientCB.getValue(), sold_lumber, quantity);

        // Refresh the data table
        cutController.refreshCutTable();
        cutController.disableRelevantButtons();

        // Close the FXML window
        Stage stage = (Stage) clientCB.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999);
        valueFactory.setValue(1);

        try {
            List<String> allClients = DatabaseManager.readAllCostumers();
            clientCB.getItems().addAll(allClients);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        unitsSellSpinner.setValueFactory(valueFactory);
        unitsSellSpinner.setEditable(true);
    }
}
