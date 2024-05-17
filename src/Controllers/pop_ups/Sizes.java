package Controllers.pop_ups;

import Application.DatabaseManager;
import Application.Main;
import Controllers.RawLumber;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Sizes implements Initializable {
    @FXML
    private Button deleteButton;

    // Add
    @FXML
    private Button addButton;
    @FXML
    private TextField addX;
    @FXML
    private TextField addY;

    // Edit
    @FXML
    private Button editButton;
    @FXML
    private TextField editX;
    @FXML
    private TextField editY;

    ObservableList<String[]> dataList;
    static String selectedSize = null;

    // Table
    @FXML
    TableView<String[]> sizeTable = new TableView<>();
    @FXML
    TableColumn<String[], String> dimensionColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        disableSizeButtons();
        // Hide buttons based on role access
        switch(Main.getUserRole()){
            case "Cashier" -> {
                // hide buttons
                deleteButton.setVisible(false);
                editButton.setVisible(false);

            }
            case "Employee" -> {
                // hide buttons
                deleteButton.setVisible(false);
                editButton.setVisible(false);
                addButton.setVisible(false);
            }
        }
        try {
            dataList = FXCollections.observableArrayList(DatabaseManager.readSizes());
            sizeTable.setItems(dataList);

            sizeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    editButton.setDisable(false);
                    deleteButton.setDisable(false);
                    selectedSize = sizeTable.getSelectionModel().getSelectedItem()[1];
                    editX.setText(selectedSize.split("x")[0]);
                    editY.setText(selectedSize.split("x")[1]);
                } else{
                    disableSizeButtons();
                }
            });

            dimensionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void addSize(ActionEvent event) {
        try {
            int X = Integer.parseInt(addX.getText());
            int Y = Integer.parseInt(addY.getText());
            String dimension = "%dx%d";
            if(X<=0 || Y<=0){
                throw new NumberFormatException();
            }
            if(DatabaseManager.checkDuplicate_Janiola("size", "size_dimension", String.format(dimension,X,Y)) == 1){
                throw new RuntimeException();
            }
            DatabaseManager.addSize(String.format(dimension,X,Y));
            addX.clear();
            addY.clear();
            refreshTable();
        } catch (NumberFormatException e){
            alert("Input error", "Please enter a valid positive size dimension");
        } catch (RuntimeException e) {
            alert("Input Error", "Dimension already exists, please enter a different one.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void deleteSize(ActionEvent event) throws SQLException {
        // check if selected size have any process/supply history
        if(DatabaseManager.checkSizeReference(selectedSize)){
            alert("Deletion Error", "This Size already has a" +
                    " Process transaction reference and cannot be deleted.");
            return;
        }
        // Confirm Deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Size Detetion");
        alert.setHeaderText("Are you sure you want to delete this Size?");
        alert.setContentText("Deleting this will affect Cut Lumber with this size and remove it from the database.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
        // Proceed Delete
        if(alert.getResult() == ButtonType.OK){
            DatabaseManager.deleteSize(selectedSize);
            refreshTable();
        }
    }

    @FXML
    void editSize(ActionEvent event) {
        try {
            int X = Integer.parseInt(editX.getText());
            int Y = Integer.parseInt(editY.getText());
            String dimension = "%dx%d";
            if(X<=0 || Y<=0){
                throw new NumberFormatException();
            }
            if(DatabaseManager.checkDuplicateForEdit_Janiola("size", "size_dimension",
                    String.format(dimension,X,Y), "size_ID", DatabaseManager.getSizeID_Janiola(selectedSize)) == 1){
                throw new RuntimeException();
            }
            DatabaseManager.updateSize(String.format(dimension,X,Y), DatabaseManager.getSizeID_Janiola(selectedSize));
            editX.clear();
            editY.clear();
            refreshTable();
        } catch (NumberFormatException e){
            alert("Input error", "Please enter a valid positive size dimension");
        } catch (RuntimeException e) {
            alert("Input Error", "Dimension already exists, please enter a different one.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void refreshTable() throws SQLException {
        dataList.clear();
        dataList = FXCollections.observableArrayList(DatabaseManager.readSizes());
        sizeTable.setItems(dataList);
    }

    void disableSizeButtons(){
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
}