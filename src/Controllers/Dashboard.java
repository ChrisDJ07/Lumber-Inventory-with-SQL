package Controllers;

import Application.DatabaseManager;
import Application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {
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
    private Button add_Account_Button;
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
            // RawLumber Lumber
            totalRawCount.setText(DatabaseManager.getTotals("rawlumber_quantity","rawlumber"));
            totalRawSupplied.setText(DatabaseManager.getTotals("supplied_quantity", "supplied_by"));
            totalRawProcessed.setText(DatabaseManager.getTotals("process_input_quantity", "process_info"));
            // CutLumber Lumber
            totalCutCount.setText(DatabaseManager.getTotals("cutlumber_quantity", "cutlumber"));
            totalCutSold.setText(DatabaseManager.getTotals("sold_quantity", "sold_to"));
            // Finance
            totalSpentRaw.setText(DatabaseManager.getTotals("supplied_price", "supplied_by"));
            totalSoldRevenue.setText(DatabaseManager.getTotals("sold_price", "sold_to"));
            int profit = Integer.parseInt(totalSoldRevenue.getText())-Integer.parseInt(totalSpentRaw.getText());
            totalProfit.setText(Integer.toString(profit));
            // Last history
            lastProcess.setText(DatabaseManager.getLastProcess());
            lastSupply.setText(DatabaseManager.getLastSupply());
            lastSold.setText(DatabaseManager.getLastSold());
        } catch (SQLException e) {
            e.printStackTrace();
            //alert("Database Error", "There is an error fetching data from the database.");
        }
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        Main.logIn();
        ((Stage) userRoleLabel.getScene().getWindow()).close();
    }

    @FXML
    void openAddAccountWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/pop_ups/AddAccount.fxml"));
        Parent root = loader.load();

        Stage supply = new Stage();
        supply.initOwner(Main.getStage());
        supply.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);

        String css = Main.class.getResource("/Application/Application.css").toExternalForm();
        scene.getStylesheets().add(css);

        supply.setTitle("Add Account");
        supply.setResizable(false);
        supply.setScene(scene);
        supply.show();
    }

    public void alert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
