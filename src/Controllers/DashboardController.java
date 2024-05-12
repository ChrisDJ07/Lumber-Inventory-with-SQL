package Controllers;

import Application.DatabaseManager;
import Application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Label totalCutCount;
    @FXML
    private Label totalCutSold;
    @FXML
    private Label totalProfit;
    @FXML
    private Label totalRawCount;
    @FXML
    private Label totalRawProcessed;
    @FXML
    private Label totalRawSupplied;
    @FXML
    private Label totalSoldRevenue;
    @FXML
    private Label totalSpentRaw;

    @FXML
    private Label lastProcess;
    @FXML
    private Label lastSold;
    @FXML
    private Label lastSupply;

    // User Account
    @FXML
    private Button logout_button;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userRoleLabel;

    private static String userRole = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLabel.setText(Main.getUser());
        userRoleLabel.setText(Main.getUserRole());
        setLabels();
    }

    @FXML
    private void goToRaw(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/RawLumber.fxml"))));
    }

    @FXML
    private void goToCut(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/CutLumber.fxml"))));
    }

    @FXML
    private void goToHistory(ActionEvent event) throws IOException {
        Main.loadScene(new Scene(FXMLLoader.load(getClass().getResource("/Views/History.fxml"))));
    }

    public void setLabels(){
        try {
            // Raw Lumber
            totalRawCount.setText(DatabaseManager.getTotals("rawlumber_quantity","rawlumber"));
            totalRawSupplied.setText(DatabaseManager.getTotals("quantity", "supplied_by"));
            totalRawProcessed.setText(DatabaseManager.getTotals("process_input_quantity", "process_info"));
            // Cut Lumber
            totalCutCount.setText(DatabaseManager.getTotals("quantity", "cutlumber"));
            totalCutSold.setText(DatabaseManager.getTotals("sold_quantity", "sold_to"));
            // Finance
            totalSpentRaw.setText(DatabaseManager.getTotals("price", "supplied_by"));
            totalSoldRevenue.setText(DatabaseManager.getTotals("price", "sold_to"));
            int profit = Integer.parseInt(totalSoldRevenue.getText())-Integer.parseInt(totalSpentRaw.getText());
            totalProfit.setText(Integer.toString(profit));
            // Last history
            lastProcess.setText(DatabaseManager.getLastProcess());
            lastSupply.setText(DatabaseManager.getLastSupply());
            lastSold.setText(DatabaseManager.getLastSold());
        } catch (SQLException e) {
            alert("Database Error", "There is an error fetching data from the database.");
        }
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        Main.logIn();
        ((Stage) userRoleLabel.getScene().getWindow()).close();
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
