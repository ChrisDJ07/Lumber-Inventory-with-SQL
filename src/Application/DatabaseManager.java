package Application;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    public static List<String> usersList;
    public static List<String> customerList;
    public static List<String> rawLumberList;
    public static List<String> cutLumberList;
    public static List<String> sizeList;
    public static List<String> supplierList;


    // JDBC URL, username, and password
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/lumberstore";
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

    // Read table data from the database and store it in lists
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
            throw new SQLException("Error reading data from the database", e);
        }
        return dataList;
    }

    // Read column data from the database and store it in a list
    private static List<String> getColumn_Janiola(String query) throws SQLException {
        List<String> column = new ArrayList<>();
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                column.add(result.getString(1));
            }
        }
        catch (SQLException e){
            throw new SQLException("Error getting column data from the database", e);
        }
        return column;
    }

    // Read cell data from the database and store it in a String
    private static String getCell_Janiola(String query) throws SQLException {
        String data = "";
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                data = result.getString(1);
            }
        }
        catch (SQLException e){
            throw new SQLException("Error getting cell data from the database", e);
        }
        return data;
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
        String query = "SELECT rawlumber_type, rawlumber_quantity FROM rawLumber";
        return readData(query, 2);
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
    // Read suppliers from the database
    public static List<String[]> readSuppliers() throws SQLException {
        return readData("supplier", 3);
    }

    /*Get Lists from Database*/
    public static List<String> getUsersList() {return usersList;}
    public static List<String> getCustomerList() {return customerList;}
    public static String[] getRawLumberList_Janiola() throws SQLException{
        String query = "SELECT rawLumber_type\n" + "FROM rawLumber;";
        rawLumberList =  getColumn_Janiola(query);
        return rawLumberList.toArray(new String[0]);
    }
    public static List<String> getCutLumberList() {return cutLumberList;}
    public static String[] getSizeList() throws SQLException{
        String query = "SELECT size_dimension\n" + "FROM size;";
        sizeList =  getColumn_Janiola(query);
        return sizeList.toArray(new String[0]);
    }
    public static List<String> getSupplierList() {return supplierList;}

    /*Get Cell Data from Database*/
    public static String getRawID_Janiola(String type) throws SQLException{
        String query = "select rawLumber_ID\n" +
                "from rawLumber\n" +
                "where rawLumber_type = \""+type+"\";";
        return getCell_Janiola(query);
    }
    public static String getSizeID_Janiola(String size) throws SQLException{
        String query = "select size_ID\n" +
                "from size\n" +
                "where size_dimension = \""+size+"\";";
        return getCell_Janiola(query);
    }

    // Add cut lumber
    public static void addCutLumber_Janiola(String type, String unit_price, String quantity, String size) throws SQLException {
        String cutID = getRawID_Janiola(type)+ getSizeID_Janiola(size);
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO cutLumber VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, cutID);
            stmt.setString(2, getRawID_Janiola(type));
            stmt.setString(3, unit_price);
            stmt.setString(4, quantity);
            stmt.setString(5, getSizeID_Janiola(size));
            stmt.executeUpdate();
        } catch (MysqlDataTruncation e) {
            showErrorAlert("Input is more or less than the required amount: " + e.getMessage());
        }
    }

    // Add raw lumber
    public static void addRawLumber_Janiola(String type, String quantity) throws SQLException {
        try{
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String query = """
                INSERT INTO rawlumber (rawlumber_type, rawlumber_quantity)
                VALUES ("%s",%s);
                """;
            statement.executeUpdate(String.format(query, type, quantity));
        }
        catch (SQLException e){
            throw new SQLException("Error adding data to the database", e);
        }
    }

    // Check Duplicate instance of a string in a table
    public static int checkDuplicate_Janiola(String table, String column, String word) throws SQLException {
        String status = "";
        String query= """
                SELECT EXISTS (SELECT 1 FROM %s WHERE %s = "%s");
                """;
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(String.format(query, table, column, word));
            if (result.next()) {
                status = result.getString(1);
            }
        }
        catch (SQLException e){
            throw new SQLException("Error getting cell data from the database", e);
        }
        return Integer.parseInt(status);
    }

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
}