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

public class SellCut implements Initializable  {
    @FXML
    ChoiceBox<String> clientCB = new ChoiceBox<>();
    @FXML
    Label typeLabel;
    @FXML
    Label sizeLabel;
    @FXML
    Spinner<Integer> unitsSellSpinner;

    TableView<String[]> cutTable;
    CutLumber cutLumber;
    private static int ID;
    private static String sold_lumber;
    private static int quantity;
    private static String price;

    // Method to set data received from CutLumber
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
        price = rowData[3];
    }

    public void setCutController(CutLumber cutLumber) {
        this.cutLumber = cutLumber;
    }

    public void proceedSelling() throws SQLException {
        try{
            if (clientCB.getValue() == null) {
                // Show an alert to prompt the user to select a client
                alert("Client Not Selected", "Please select a client.");
                return; // Exit the method if no client is selected
            }
            if(unitsSellSpinner.getValue() > quantity){
                throw new RuntimeException("exceedQuantity");
            }
            DatabaseManager.addSold_To(ID, unitsSellSpinner.getValue(), clientCB.getValue(), sold_lumber, quantity,
                    Integer.parseInt(price)*quantity, sizeLabel.getText());
            // Refresh the data table
            CutLumber.refreshCutTable();
            cutLumber.disableRelevantButtons();
            // Close the FXML window
            Stage stage = (Stage) clientCB.getScene().getWindow();
            stage.close();
        } catch (RuntimeException e){
            alert("Unit Quantity Exceeded", "Enter a value not greater than "+quantity+".");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000);
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

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
