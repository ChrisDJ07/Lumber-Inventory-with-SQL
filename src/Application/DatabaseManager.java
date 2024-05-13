package Application;

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
     **/
    public static void addCutLumber_Janiola(String type, String unit_price, String quantity, String size) throws SQLException {
        try{
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String query = """
                    INSERT INTO cutlumber (cutlumber_type, cutlumber_unit_price, cutlumber_quantity, cutlumber_size)
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
            String query = String.format("""
                INSERT INTO suppliers (supplier_name, supplier_info)
                VALUES("%s","%s");
                """, name, info);
            statement.executeUpdate(query);
        }
        catch (SQLException e){
            throw new SQLException("Error adding data to the database", e);
        }
    }
    public static void addCustomer(String name, String info) throws SQLException {
        try{
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String query = String.format("""
                INSERT INTO customers (customer_name, customer_info)
                VALUES("%s","%s");
                """, name, info);
            statement.executeUpdate(query);
        }
        catch (SQLException e){
            throw new SQLException("Error adding data to the database", e);
        }
    }
    public static void addSold_To(int ID, int soldQuantity, String costumer_name, String sold_lumber, int currentQuantity, String price, String size) throws SQLException {
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String insertToSold_To = String.format("""
                INSERT INTO sold_to
                VALUES(NOW(), %d, %s,'%s', '%s', '%s');
                """, soldQuantity, price, costumer_name, sold_lumber, size);
            statement.executeUpdate(insertToSold_To);

            currentQuantity -= soldQuantity;

            String updateSpecificLumberQuantity = String.format("""
                UPDATE cutlumber
                SET cutlumber_quantity = %d
                WHERE cutlumber_id = %d
                """, currentQuantity, ID);
            statement.executeUpdate(updateSpecificLumberQuantity);

        } catch (SQLException e) {
            throw new SQLException("Error adding data to the database", e);
        }
    }



    /**
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
        return readData(query, 3);
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
        String query = """
                SELECT cutLumber_ID, rawLumber_type, size_dimension, cutlumber_unit_price, cutlumber_quantity
                FROM cutLumber
                LEFT JOIN rawLumber
                ON cutLumber.cutLumber_type = rawLumber.rawLumber_ID
                LEFT JOIN size
                ON cutLumber.cutlumber_size = size.size_ID""";
        return readData(query, 5);
    }
    public static List<String[]> readProcessedInfo() throws SQLException {
        String query = "SELECT * FROM process_info";
        return readData(query, 5);
    }
    public static List<String[]> readSoldTo() throws SQLException {
        String query = "SELECT * FROM sold_to";
        return readData(query, 6);
    }
    public static List<String[]> readSuppliedBy() throws SQLException {
        String query = "SELECT * FROM supplied_by";
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
    public static List<String> readAllCostumers() throws SQLException {
        String query = "SELECT customer_name FROM customers";
        return readSingleColumnData(query);
    }
    // Read suppliers from the database
    public static List<String[]> readSuppliers() throws SQLException {
        String query = "SELECT supplier_name, supplier_info\n" + "FROM suppliers;";
        return readData(query, 2);
    }
    // Read customers from the database
    public static List<String[]> readCustomers() throws SQLException {
        String query = "SELECT customer_name, customer_info FROM customers";
        return readData(query, 2);
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
        String query = "SELECT supplier_name\n" + "FROM suppliers;";
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
    public static String getCutID_Janiola(String type, String size) throws SQLException{
        String query = """
                SELECT cutlumber_ID
                FROM cutlumber
                WHERE cutlumber_type = %s AND cutlumber_size = %s;
                """;
        return getCell_Janiola(String.format(query, getRawID_Janiola(type), getSizeID_Janiola(size)));
    }
    public static String getSizeID_Janiola(String size) throws SQLException{
        String query = "select size_ID\n" +
                "from size\n" +
                "where size_dimension = \""+size+"\";";
        return getCell_Janiola(query);
    }
    public static String getSupplierID_Janiola(String name) throws SQLException{
        String query = "SELECT supplier_ID\n" +
                "FROM suppliers\n" +
                "WHERE supplier_name = \""+name+"\";";
        return getCell_Janiola(query);
    }
    public static String getCustomerID(String name) throws SQLException{
        String query = "SELECT customer_ID\n" +
                "FROM customers\n" +
                "WHERE customer_name = \""+name+"\";";
        return getCell_Janiola(query);
    }
    // Get the role of current user from the database
    public static String getCurrentUserRole(String name) throws SQLException {
        String query = "SELECT employee_role\n" +
                "FROM application_users\n" +
                "WHERE employee_userName = \""+name+"\";";
        return getCell_Janiola(query);
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
    public static void updateCutLumber(String ID, String type, String unit_price, int quantity, String size) throws SQLException {
        try{
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String query = """
                    UPDATE cutlumber
                    SET cutlumber_type = %d, cutlumber_unit_price = %d, cutlumber_quantity = %d, cutlumber_size = %d
                    WHERE cutlumber_ID = %d;
                """;
            statement.executeUpdate(String.format(query, Integer.parseInt(getRawID_Janiola(type)), Integer.parseInt(unit_price), quantity, Integer.parseInt(getSizeID_Janiola(size)), Integer.parseInt(ID)));
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
    // Delete Supplier
    public static void deleteSupplier(String name) throws SQLException {
        try{
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String query = """
                    DELETE FROM suppliers
                    WHERE supplier_ID = %s;
                """;
            statement.executeUpdate(String.format(query, getSupplierID_Janiola(name)));
        }
        catch (SQLException e){
            throw new SQLException("Error adding data to the database", e);
        }
    }
    // Delete Supplier
    public static void deleteCustomer(String name) throws SQLException {
        try{
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String query = """
                    DELETE FROM customers
                    WHERE customer_ID = %s;
                """;
            statement.executeUpdate(String.format(query, getCustomerID(name)));
        }
        catch (SQLException e){
            throw new SQLException("Error adding data to the database", e);
        }
    }




    /*Other functions*/
    public static void processRawLumber(String type, String size, String input_quantity, String output_quantity) throws SQLException {
        try{
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String subtractFromRaw = """
                    UPDATE rawlumber
                    SET rawlumber_quantity = rawlumber_quantity - %s
                    WHERE rawlumber_ID = %s;
                """;
            statement.executeUpdate(String.format(subtractFromRaw, input_quantity, getRawID_Janiola(type)));
            if(checkCutDuplicate_Janiola(type, size) == 1){
                System.out.println("Cut lumber exist");
                String addToCut = """
                        UPDATE cutlumber
                        SET cutlumber_quantity = cutlumber_quantity + %s
                        WHERE cutlumber_ID = %s;
                        """;
                statement.executeUpdate(String.format(addToCut, output_quantity, getCutID_Janiola(type,size)));
            }
            else{
                addCutLumber_Janiola(type, "null", output_quantity, size);
            }
            String recordProcessInfo = """
                    INSERT INTO process_info
                    VALUES(NOW(), %s, %s, "%s", "%s");
                    """;
            statement.executeUpdate(String.format(recordProcessInfo, input_quantity, output_quantity, type, size));
        }
        catch (SQLException e){
            throw new SQLException("Error adding data to the database", e);
        }
    }

    // Process Raw Lumber
    public static void supplyRawLumber(String supplier, String type, int input_quantity, int price) throws SQLException {
        try{
            Connection con = getConnection();
            Statement statement = con.createStatement();
            String addToRaw = """
                    UPDATE rawlumber
                    SET rawlumber_quantity = rawlumber_quantity + %d
                    WHERE rawlumber_ID = %s;
                """;
            statement.executeUpdate(String.format(addToRaw, input_quantity, getRawID_Janiola(type)));

            String recordSupplyInfo = """
                INSERT INTO supplied_by
                VALUES (NOW(), %d, '%s', '%s',  %d);
                """;
            statement.executeUpdate(String.format(recordSupplyInfo, input_quantity, supplier, type, price));
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
                SELECT EXISTS (SELECT 1 FROM cutlumber WHERE cutlumber_type = %s AND cutlumber_size = %s);
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
    // Get Last Process in History
    public static String getLastProcess() throws SQLException {
        String[] lastProcess = null;
        String processText = """
                    TYPE:   %s
                    
                    SIZE:   %s
                    
                    INPUT:   %s
                    
                    OUTPUT:   %s
                    
                    """;
        String query= """
                SELECT * FROM process_info
                ORDER BY process_date DESC
                LIMIT 1;
                """;
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                lastProcess = new String[5];
                for (int i = 0; i < 5; i++) {
                    lastProcess[i] = result.getString(i+1);
                }
                return String.format(processText, lastProcess[3], lastProcess[4], lastProcess[1], lastProcess[2]);
            }
            else{
                return String.format(processText, "..", "..", "..", "..");
            }
        }
        catch (SQLException e){
            throw new SQLException("Error getting quantity data from the database", e);
        }
    }
    // Get Last Supply in History
    public static String getLastSupply() throws SQLException {
        String[] lastSupply = null;
        String supplyText = """
                    SUPPLIER:   %s
                    
                    TYPE:   %s
                    
                    QUANTITY:   %s
                    
                    PRICE:   %s
                    
                    """;
        String query= """
                SELECT * FROM supplied_by
                ORDER BY supplied_date DESC
                LIMIT 1;
                """;
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                lastSupply = new String[5];
                for (int i = 0; i < 5; i++) {
                    lastSupply[i] = result.getString(i+1);
                }
                return String.format(supplyText, lastSupply[2], lastSupply[3], lastSupply[1], lastSupply[4]);
            }
            else{
                return String.format(supplyText, "..", "..", "..", "..");
            }
        }
        catch (SQLException e){
            throw new SQLException("Error getting quantity data from the database", e);
        }
    }
    // Get Last Sold in History
    public static String getLastSold() throws SQLException {
        String[] lastSupply = null;
        String supplyText = """
                    CLIENT:   %s
                    
                    TYPE:   %s
                    
                    TYPE:   %s
                    
                    QUANTITY:   %s
                    
                    PRICE:   %s
                    
                    """;
        String query= """
                SELECT * FROM sold_to
                ORDER BY sold_date DESC
                LIMIT 1;
                """;
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                lastSupply = new String[6];
                for (int i = 0; i < 6; i++) {
                    lastSupply[i] = result.getString(i+1);
                }
                return String.format(supplyText, lastSupply[3], lastSupply[4], lastSupply[5], lastSupply[1], lastSupply[2]);
            }
            else{
                return String.format(supplyText, "..", "..", "..", "..", "..");
            }
        }
        catch (SQLException e){
            throw new SQLException("Error getting quantity data from the database", e);
        }
    }
    // Get table totals
    public static String getTotals(String column, String table) throws SQLException {
        String query= """
                SELECT SUM(%s)
                FROM %s;
                """;
        try {
            Connection con = getConnection();
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(String.format(query, column, table));
            if (result.next()) {
                if(result.getString(1) == null){
                    return "0";
                }
                return result.getString(1);
            }
            return "0";
        }
        catch (SQLException e){
            throw new SQLException("Error getting quantity data from the database", e);
        }
    }
}
