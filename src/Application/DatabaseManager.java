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
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/wooddynamics";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

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



    /**
     * Create functions
     * Create functions
     * Create functions
     **/
    public static void addCutLumber_Janiola(String type, String unit_price, String quantity, String size) throws SQLException {
        try{
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String query = """
                    INSERT INTO cutlumber (cutlumber_type, unit_price, quantity, size_ID)
                    VALUES (%s, %s, %s, %s);
                """;
            statement.executeUpdate(String.format(query, getRawID_Janiola(type), unit_price, quantity, getSizeID_Janiola(size)));
        }
        catch (SQLException e){
            throw new SQLException("Error adding data to the database", e);
        }
    }
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
    public static void addSupplier_Janiola(String name, String info) throws SQLException {
        try{
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String query = """
                INSERT INTO supplier (supplier_name, supplier_info)
                VALUES("%s","%s");
                """;
            statement.  executeUpdate(String.format(query, name, info));
        }
        catch (SQLException e){
            throw new SQLException("Error adding data to the database", e);
        }
    }



    /**
     * Read functions
     * Read functions
     * Read functions
     **/
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
    // Read Single column data
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

    /*Get Lists from Database*/
    /*Get Lists from Database*/
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
    public static String[] getSupplierList() throws SQLException {
        String query = "SELECT supplier_name\n" + "FROM supplier;";
        supplierList =  getColumn_Janiola(query);
        return supplierList.toArray(new String[0]);
    }

    /*Get Cell Data from Database*/
    /*Get Cell Data from Database*/
    /*Get Cell Data from Database*/
    public static String getRawID_Janiola(String type) throws SQLException {
        String query = "select rawLumber_ID\n" +
                "from rawLumber\n" +
                "where rawLumber_type = \"" + type + "\";";
        return getCell_Janiola(query);
    }
    public static String getSizeID_Janiola(String size) throws SQLException{
        String query = "select size_ID\n" +
                "from size\n" +
                "where size_dimension = \""+size+"\";";
        return getCell_Janiola(query);
    }
    public static String getCutID_Janiola(String type, String size) throws SQLException{
        String query = """
                SELECT cutlumber_ID
                FROM cutlumber
                WHERE cutlumber_type = %s AND size_ID = %s;
                """;
        return getCell_Janiola(String.format(query, getRawID_Janiola(type), getSizeID_Janiola(size)));
    }



    /**
     * Update functions
     * Update functions
     * Update functions
     **/
    // Update raw lumber
    public static void updateRawQuantity(String type, String quantity) throws SQLException {
        try{
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String query = """
                    UPDATE rawlumber
                    SET rawlumber_quantity = %s
                    WHERE rawlumber_ID = %s;
                """;
            statement.executeUpdate(String.format(query, quantity, getRawID_Janiola(type)));
        }
        catch (SQLException e){
            throw new SQLException("Error adding data to the database", e);
        }
    }



    /**
     * Delete functions
     * Delete functions
     * Delete functions
     **/
    // Delete raw lumber
    public static void deleteRawLumber(String type) throws SQLException {
        try{
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String query = """
                    DELETE FROM rawlumber
                    WHERE rawlumber_ID = %s;
                """;
            statement.executeUpdate(String.format(query, getRawID_Janiola(type)));
        }
        catch (SQLException e){
            throw new SQLException("Error adding data to the database", e);
        }
    }
    // Delete raw lumber
    public static void deleteCutLumber(String delete_ID) throws SQLException {
        try {
            Connection con = getConnection();
            String query = "DELETE FROM cutlumber WHERE cutlumber_ID = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, delete_ID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error deleting data from the database", e);
        }
    }

    //TODO: ERASED/COMMENTED OUT - (type did not query as ID, the same for size)
//    public static void processRawLumber(String type, String size, String input_quantity, String output_quantity) throws SQLException {
//        try{
//            Connection con = getConnection();
//            Statement statement = con.createStatement();
//            String subtractFromRaw = """
//                    UPDATE rawlumber
//                    SET rawlumber_quantity = rawlumber_quantity - %s
//                    WHERE rawlumber_ID = %s;
//                """;
//            statement.executeUpdate(String.format(subtractFromRaw, input_quantity, getRawID_Janiola(type)));
//            if(checkCutDuplicate_Janiola(type, size) == 1){
//                System.out.println("Cut lumber exist");
//                String addToCut = """
//                        UPDATE cutlumber
//                        SET quantity = quantity + %s
//                        WHERE cutlumber_ID = %s;
//                        """;
//                statement.executeUpdate(String.format(addToCut, output_quantity, getCutID_Janiola(type,size)));
//            }
//            else{
//                addCutLumber_Janiola(type, "null", output_quantity, size);
//            }
//            String recordProcessInfo = """
//                    INSERT INTO process_info
//                    VALUES(NOW(), %s, %s, "%s", "%s");
//                    """;
//            statement.executeUpdate(String.format(recordProcessInfo, input_quantity, output_quantity, type, size));
//        }
//        catch (SQLException e){
//            throw new SQLException("Error adding data to the database", e);
//        }
//    }



    /*Other functions*/
    /*Other functions*/
    /*Other functions*/
    public static void processRawLumber(String type, String size, String input_quantity, String output_quantity) throws SQLException {
        try{
            Connection con = getConnection();
            Statement statement = con.createStatement();

            // Query to get rawlumber_ID for the given type
            String getRawlumberIDQuery = "SELECT rawlumber_ID FROM rawlumber WHERE rawlumber_Type = '" + type + "'";
            ResultSet rawlumberResult = statement.executeQuery(getRawlumberIDQuery);
            int rawlumberID = 0;
            if (rawlumberResult.next()) {
                rawlumberID = rawlumberResult.getInt("rawlumber_ID");
            } else {
                // Handle if type is not found
                throw new SQLException("Raw lumber type not found");
            }

            // Query to get size_ID for the given size
            String getSizeIDQuery = "SELECT size_ID FROM size WHERE size_dimension = '" + size + "'";
            ResultSet sizeResult = statement.executeQuery(getSizeIDQuery);
            int sizeID = 0;
            if (sizeResult.next()) {
                sizeID = sizeResult.getInt("size_ID");
            } else {
                // Handle if size is not found
                throw new SQLException("Size not found");
            }

            // Update rawlumber_quantity
            String subtractFromRaw = """
                UPDATE rawlumber
                SET rawlumber_quantity = rawlumber_quantity - %s
                WHERE rawlumber_ID = %d;
            """;
            statement.executeUpdate(String.format(subtractFromRaw, input_quantity, rawlumberID));

            // Check if cut lumber exists
            if (checkCutDuplicate_Janiola(type, size) == 1){
                System.out.println("Cut lumber exist"); //TODO: Remove in final output
                String addToCut = """
                    UPDATE cutlumber
                    SET quantity = quantity + %s
                    WHERE cutlumber_ID = %s;
                    """;
                statement.executeUpdate(String.format(addToCut, output_quantity, getCutID_Janiola(type,size)));
            } else {
                addCutLumber_Janiola(type, "null", output_quantity, size);
            }

            // Record process information
            String recordProcessInfo = """
                INSERT INTO process_info
                VALUES(NOW(), %s, %s, "%s", "%s");
                """;
            statement.executeUpdate(String.format(recordProcessInfo, input_quantity, output_quantity, rawlumberID, sizeID));
        } catch (SQLException e){
            throw new SQLException("Error adding data to the database", e);
        }
    }
    // Process Raw Lumber
    public static void supplyRawLumber(String supplier, String type, String input_quantity) throws SQLException {
        try{
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String addToRaw = """
                    UPDATE rawlumber
                    SET rawlumber_quantity = rawlumber_quantity + %s
                    WHERE rawlumber_ID = %s;
                """;
            statement.executeUpdate(String.format(addToRaw, input_quantity, getRawID_Janiola(type)));

            String recordSupplyInfo = """
                    INSERT INTO supplied_by
                    VALUES (NOW(), %s, "%s", "%s");
                    """;
            statement.executeUpdate(String.format(recordSupplyInfo, input_quantity, supplier, type));
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
    // Check Raw lumber quantity limit
    public static int getRawQuantity(String type) throws SQLException {
        String quantity = "";
        String query= """
                SELECT rawlumber_quantity\s
                FROM rawLumber
                WHERE rawlumber_ID = %s;
                """;
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(String.format(query, getRawID_Janiola(type)));
            if (result.next()) {
                quantity = result.getString(1);
            }
        }
        catch (SQLException e){
            throw new SQLException("Error getting quantity data from the database", e);
        }
        return Integer.parseInt(quantity);
    }
    // Check Duplicate instance in a Cut Lumber
    public static int checkCutDuplicate_Janiola(String type, String size) throws SQLException {
        String status = "";
        String query= """
                SELECT EXISTS (SELECT 1 FROM cutlumber WHERE cutlumber_type = %s AND size_ID = %s);
                """;
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(String.format(query,getRawID_Janiola(type), getSizeID_Janiola(size)));
            if (result.next()) {
                status = result.getString(1);
            }
        }
        catch (SQLException e){
            throw new SQLException("Error getting data from the database", e);
        }
        return Integer.parseInt(status);
    }
}