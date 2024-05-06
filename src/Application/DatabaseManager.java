package Application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    public static List<String> usersList;
    public static List<String> customerList;
    public static List<String> rawLumberList;
    public static List<String> cutLumberList;
    public static List<String> sizeList;
    public static List<String> supplierList;


    // JDBC URL, username, and password
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/wooddynamics";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "!DFoYtT7FHFez@rM";

    // Reusable Code for showing errors
    private static void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Establish database connection
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            showErrorAlert("There is an error connecting to the database. Please make sure your SQL Database is running...");
            return null;
        }
    }

    // Read data from the database and store it in lists
    private static List<String[]> readData(String query, int columnCount) throws SQLException {
        List<String[]> dataList = new ArrayList<>();
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                String[] rowData = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = result.getString(i+1);
                }
                dataList.add(rowData);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return dataList;
    }
    private static List<String> readSingleColumnData(String query) throws SQLException {
        List<String> dataList = new ArrayList<>();
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                dataList.add(result.getString(1));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return dataList;
    }

    // Read application_users from the database
    public static List<String[]> readUsers() throws SQLException {
        String query = "SELECT * FROM application_users";
        return readData(query, 2);
    }
    // Read customers from the database
    public static List<String[]> readCustomers() throws SQLException {
        return readData("customer", 3);
    }
    // Read raw lumbers from the database
    public static List<String[]> readRawLumbers() throws SQLException {
        String query = "SELECT * FROM rawLumber";
        return readData(query, 3);
    }
    public static List<String> readRawLumberTypes() throws SQLException {
        String query = "SELECT rawLumber_type FROM rawLumber";
        return readSingleColumnData(query);
    }
    // Read cut lumbers from the database
    public static List<String[]> readCutLumbers() throws SQLException {
        String query =  "SELECT cutLumber_ID, rawLumber_type, size_dimension, unit_price, quantity\n" +
                        "FROM cutLumber\n" +
                        "LEFT JOIN rawLumber\n" +
                        "ON cutLumber.cutLumber_type = rawLumber.rawLumber_ID\n" +
                        "LEFT JOIN size\n" +
                        "ON cutLumber.size_ID = size.size_ID";
        return readData(query, 5);
    }
    // Read sizes from the database
    public static List<String[]> readSizes() throws SQLException {
        String query = "SELECT * FROM size";
        return readData(query, 2);
    }
    public static List<String> readSizeTypes() throws SQLException {
        String query = "SELECT size_dimension FROM size";
        return readSingleColumnData(query);
    }
    // Read suppliers from the database
    public static List<String[]> readSuppliers() throws SQLException {
        return readData("supplier", 3);
    }



    // Create operation
//    public static void createStudentRecord(String name, String id, String yearLevel, String gender, String course) throws SQLException {
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement("INSERT INTO student (StudentName, ID, YearLevel, Gender, CourseID) VALUES (?, ?, ?, ?, ?)")) {
//            stmt.setString(1, name);
//            stmt.setString(2, id);
//            stmt.setString(3, yearLevel);
//            stmt.setString(4, gender);
//            stmt.setString(5, course);
//            stmt.executeUpdate();
//        } catch (MysqlDataTruncation e) {
//            showErrorAlert("Input is more or less than the required amount: " + e.getMessage());
//        }
//    }
//    public static void createCourseRecord(String name, String id) throws SQLException {
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement("INSERT INTO course (CourseName, ID) VALUES (?, ?)")) {
//            stmt.setString(1, name);
//            stmt.setString(2, id);
//            stmt.executeUpdate();
//        }
//    }

//    // TODO: Update and modify for CREATE, UPDTATE, and DELETE actions
//
//    // Update operation
//    public static void updateStudentRecord(String id, String newName, String newId, String newYearLevel, String newGender, String newCourse) throws SQLException {
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement("UPDATE student SET StudentName = ?, ID = ?, YearLevel = ?, Gender = ?, CourseID = ? WHERE ID = ?")) {
//            stmt.setString(1, newName);
//            stmt.setString(2, newId);
//            stmt.setString(3, newYearLevel);
//            stmt.setString(4, newGender);
//            stmt.setString(5, newCourse);
//            stmt.setString(6, id);
//            stmt.executeUpdate();
//        }
//    }
//    public static void updateCourseRecord(String id, String newName, String newId) throws SQLException {
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement("UPDATE course SET CourseName = ?, ID = ? WHERE ID = ?")) {
//            stmt.setString(1, newName);
//            stmt.setString(2, newId);
//            stmt.setString(3, id);
//            stmt.executeUpdate();
//        }
//    }
//
//
//
//    // Delete operation
//    public static void deleteStudentRecord(String id) throws SQLException {
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement("DELETE FROM student WHERE ID = ?")) {
//            stmt.setString(1, id);
//            stmt.executeUpdate();
//        }
//    }
//    public static void deleteCourseRecord(String id) throws SQLException {
//        try (Connection conn = getConnection()) {
//            // Step 1: Update CourseID in student table to null
//            try (PreparedStatement stmtUpdate = conn.prepareStatement("UPDATE student SET CourseID = NULL WHERE CourseID = ?")) {
//                stmtUpdate.setString(1, id);
//                stmtUpdate.executeUpdate();
//            }
//
//            // Step 2: Delete the record from the course table
//            try (PreparedStatement stmtDelete = conn.prepareStatement("DELETE FROM course WHERE ID = ?")) {
//                stmtDelete.setString(1, id);
//                stmtDelete.executeUpdate();
//            }
//        }
//    }

    public static List<String> getUsersList() {return usersList;}
    public static List<String> getCustomerList() {return customerList;}
    public static List<String> getRawLumberList() {return rawLumberList;}
    public static List<String> getCutLumberList() {return cutLumberList;}
    public static List<String> getSizeList() {return sizeList;}
    public static List<String> getSupplierList() {return supplierList;}
}