module Lumber.Inventory {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens Application;
    opens Views;
    opens Controllers;

    exports Controllers.pop_ups;
    opens Controllers.pop_ups to javafx.fxml;
}