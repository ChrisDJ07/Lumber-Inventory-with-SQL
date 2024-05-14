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

    /**
     * JDBC URL, username, and password
     */
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/wooddynamics";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    /**
     * Reusable Code for showing errors
     */
    private static void showErrorAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setHeaderText(null);
        alert.setContentText("There is an error connecting to the database. Please make sure your SQL Database is running...");
        alert.showAndWait();
    }

    /**
     * Establish database connection
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            showErrorAlert();
            return null;
        }
    }


// CREATE FUNCTIONS
    /**
     * Insert Cut Lumber value in the database
     * @param type - raw lumber type
     * @param unit_price - price per unit
     * @param quantity - quantity of cut lumber
     * @param size - dimension of cut lumber
     */
    public static void addCutLumber_Janiola(String type, int unit_price, int quantity, String size) throws SQLException {
        String query = "INSERT INTO cutlumber (cutlumber_type, cutlumber_unit_price, cutlumber_quantity, cutlumber_size) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setInt(1, getRawID_Janiola(type));
                pstmt.setInt(2, unit_price);
                pstmt.setInt(3, quantity);
                pstmt.setInt(4, getSizeID_Janiola(size));
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException("Error adding data to the database", e);
        }
    }

    /**
     * Same with addCutLumber_Janiola() but no price
     */
    public static void addCutLumberNoPrice_Janiola(String type, int quantity, String size) throws SQLException {
        String query = "INSERT INTO cutlumber (cutlumber_type, cutlumber_quantity, cutlumber_size) VALUES (?, ?, ?)";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setInt(1, getRawID_Janiola(type));
                pstmt.setInt(2, quantity);
                pstmt.setInt(3, getSizeID_Janiola(size));
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException("Error adding data to the database", e);
        }
    }

    /**
     * Insert Raw Lumber value in the database
     * @param type - type of Raw Lumber
     * @param quantity - starting quantity
     */
    public static void addRawLumber_Janiola(String type, String quantity) throws SQLException {
        String query = "INSERT INTO rawlumber (rawlumber_type, rawlumber_quantity) VALUES (?, ?)";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, type);
                pstmt.setString(2, quantity);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException("Error adding data to the database", e);
        }
    }

    /**
     * Insert Supplier value in the database
     * @param name - name of the supplier
     * @param info - contact info of the supplier
     */
    public static void addSupplier_Janiola(String name, String info) throws SQLException {
        String query = "INSERT INTO suppliers (supplier_name, supplier_info) VALUES(?, ?)";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, name);
                pstmt.setString(2, info);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
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

    /**
     * todo: Add Documentation
     */
    public static void addSold_To(int ID, int soldQuantity, String customer_name, String sold_lumber, int currentQuantity, int price, String size) throws SQLException {
        String insertToSold_To = "INSERT INTO sold_to VALUES(NOW(), ?, ?, ?, ?, ?, ?, ?)";
        String updateSpecificLumberQuantity = "UPDATE cutlumber SET cutlumber_quantity = ? WHERE cutlumber_id = ?";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmtInsert = con.prepareStatement(insertToSold_To);
                 PreparedStatement pstmtUpdate = con.prepareStatement(updateSpecificLumberQuantity)) {
                pstmtInsert.setInt(1, soldQuantity);
                pstmtInsert.setInt(2, price);
                pstmtInsert.setString(3, customer_name);
                pstmtInsert.setString(4, sold_lumber);
                pstmtInsert.setString(5, size);
                pstmtInsert.setInt(6, getCustomerID(customer_name));
                pstmtInsert.setInt(7, ID);
                pstmtInsert.executeUpdate();

                currentQuantity -= soldQuantity;
                pstmtUpdate.setInt(1, currentQuantity);
                pstmtUpdate.setInt(2, ID);
                pstmtUpdate.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException("Error adding data to the database", e);
        }
    }


// READ FUNCTIONS
    /**
     * Read table data from the database and store it in lists
     * @param query - String query
     * @param columnCount - how many column to fetch
     * @return - List of String[] data of the table
     */
    private static List<String[]> readData(String query, int columnCount) throws SQLException {
        List<String[]> dataList = new ArrayList<>();
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query);
                 ResultSet result = pstmt.executeQuery()) {
                while (result.next()) {
                    String[] rowData = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        rowData[i] = result.getString(i+1);
                    }
                    dataList.add(rowData);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading data from the database", e);
        }
        return dataList;
    }

    /**
     * Read Single column data
     * @param query - String query
     * @return - List of Specified Column
     */
    private static List<String> readSingleColumnData(String query) throws SQLException {
        List<String> dataList = new ArrayList<>();
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query);
                 ResultSet result = pstmt.executeQuery()) {
                while (result.next()) {
                    dataList.add(result.getString(1));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading data from the database", e);
        }
        return dataList;
    }

    /**
     * Read column data from the database and store it in a list
     * @param query - String query
     * @return List of Column Data
     */
    private static List<String> getColumn_Janiola(String query) throws SQLException {
        List<String> column = new ArrayList<>();
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query);
                 ResultSet result = pstmt.executeQuery()) {
                while (result.next()) {
                    column.add(result.getString(1));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error getting column data from the database", e);
        }
        return column;
    }

    public static List<String[]> readUsers() throws SQLException {
        String query = "SELECT * FROM application_users";
        return readData(query, 3);
    }

    public static List<String[]> readRawLumbers() throws SQLException {
        String query = "SELECT rawlumber_type, rawlumber_quantity FROM rawLumber";
        return readData(query, 2);
    }

    public static List<String> readRawLumberTypes() throws SQLException {
        String query = "SELECT rawLumber_type FROM rawLumber";
        return readSingleColumnData(query);
    }

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
        String query = "SELECT process_date, process_input_quantity, process_output_quantity, process_input_type, process_output_size" +
                       " FROM process_info";
        return readData(query, 5);
    }

    public static List<String[]> readSoldTo() throws SQLException {
        String query = "SELECT sold_date, sold_quantity, sold_price, sold_to_customer, sold_lumber, sold_size" +
                       " FROM sold_to";
        return readData(query, 6);
    }

    public static List<String[]> readSuppliedBy() throws SQLException {
        String query = "SELECT supplied_date, supplied_quantity, supplier_name, supplied_lumber, supplied_price\n" +
                       "FROM supplied_by;";
        return readData(query, 5);
    }

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

    public static List<String[]> readSuppliers() throws SQLException {
        String query = "SELECT supplier_name, supplier_info FROM suppliers";
        return readData(query, 2);
    }
    // Read customers from the database
    public static List<String[]> readCustomers() throws SQLException {
        String query = "SELECT customer_name, customer_info FROM customers";
        return readData(query, 2);
    }


// GET LISTS
    /**
     * @return - List of Raw Lumbers
     */
    public static String[] getRawLumberList_Janiola() throws SQLException{
        String query = "SELECT rawLumber_type FROM rawLumber";
        rawLumberList =  getColumn_Janiola(query);
        return rawLumberList.toArray(new String[0]);
    }

    /**
     * @return - List of Sizes
     */
    public static String[] getSizeList() throws SQLException{
        String query = "SELECT size_dimension FROM size";
        sizeList =  getColumn_Janiola(query);
        return sizeList.toArray(new String[0]);
    }

    /**
     * @return - List of Suppliers
     */
    public static String[] getSupplierList() throws SQLException {
        String query = "SELECT supplier_name FROM suppliers";
        supplierList =  getColumn_Janiola(query);
        return supplierList.toArray(new String[0]);
    }

    // not used
    public static List<String> getUsersList() {return usersList;}
    public static List<String> getCustomerList() {return customerList;}
    public static List<String> getCutLumberList() {return cutLumberList;}


// GET IDs
    /**
     * Fetch ID of specified Raw Lumber
     * @param type - type of raw lumber
     * @return - String ID
     */
    public static int getRawID_Janiola(String type) throws SQLException {
        String query = "select rawLumber_ID from rawLumber where rawLumber_type = ?";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, type);
                try (ResultSet result = pstmt.executeQuery()) {
                    if (result.next()) {
                        return result.getInt(1);
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Fetch ID of specified Cut Lumber
     * @param type - type of cut lumber
     * @param size - dimension of cut lumber
     * @return - String ID
     */
    public static int getCutID_Janiola(String type, String size) throws SQLException{
        String query = "SELECT cutlumber_ID FROM cutlumber WHERE cutlumber_type = ? AND cutlumber_size = ?";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setInt(1, getRawID_Janiola(type));
                pstmt.setInt(2, getSizeID_Janiola(size));
                try (ResultSet result = pstmt.executeQuery()) {
                    if (result.next()) {
                        return result.getInt(1);
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Fetch ID of specified size
     * @param size - dimension
     * @return - String ID
     */
    public static int getSizeID_Janiola(String size) throws SQLException{
        String query = "select size_ID from size where size_dimension = ?";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, size);
                try (ResultSet result = pstmt.executeQuery()) {
                    if (result.next()) {
                        return result.getInt(1);
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Fetch ID of supplier
     * @param name - name of Supplier
     * @return - String ID
     */
    public static int getSupplierID_Janiola(String name) throws SQLException{
        String query = "SELECT supplier_ID FROM suppliers WHERE supplier_name = ?";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, name);
                try (ResultSet result = pstmt.executeQuery()) {
                    if (result.next()) {
                        return result.getInt(1);
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Fetch ID of customer
     * @param name - name of customer
     * @return - String ID
     */
    public static int getCustomerID(String name) throws SQLException{
        String query = "SELECT customer_ID FROM customers WHERE customer_name = ?";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, name);
                try (ResultSet result = pstmt.executeQuery()) {
                    if (result.next()) {
                        return result.getInt(1);
                    }
                }
            }
        }
        return 0;
    }


// UPDATE FUNCTIONS
    /**
     * Updates quantity of specified raw lumber
     * @param type - type of raw lumber
     * @param quantity - quantity to be set
     */
    public static void updateRawQuantity(String type, String quantity) throws SQLException {
        String query = "UPDATE rawlumber SET rawlumber_quantity = ? WHERE rawlumber_ID = ?";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, quantity);
                pstmt.setInt(2, getRawID_Janiola(type));
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * todo: Add Documentation
     */
    public static void updateCutLumber(int ID, String type, int unit_price, int quantity, String size) throws SQLException {
        String query = "UPDATE cutlumber SET cutlumber_type = ?, cutlumber_unit_price = ?, cutlumber_quantity = ?, cutlumber_size = ? WHERE cutlumber_ID = ?";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setInt(1, getRawID_Janiola(type));
                pstmt.setInt(2, unit_price);
                pstmt.setInt(3, quantity);
                pstmt.setInt(4, getSizeID_Janiola(size));
                pstmt.setInt(5, ID);
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * Processes Raw lumber to cut Lumber in a specific size, updates the quantities accordingly and records the transaction
     * @param type - processed Raw Lumber
     * @param size - processed size
     * @param input_quantity - input raw lumber
     * @param output_quantity - output cut lumber
     * @throws SQLException - if there is an error in the SQL query
     */
    public static void processRawLumber(String type, String size, int input_quantity, int output_quantity) throws SQLException {
        String subtractFromRaw = "UPDATE rawlumber SET rawlumber_quantity = rawlumber_quantity - ? WHERE rawlumber_ID = ?";
        String addToCut = "UPDATE cutlumber SET cutlumber_quantity = cutlumber_quantity + ? WHERE cutlumber_ID = ?";
        String recordProcessInfo = "INSERT INTO process_info VALUES(NOW(), ?, ?, ?, ?, ?)";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmtSubtract = con.prepareStatement(subtractFromRaw);
                 PreparedStatement pstmtAdd = con.prepareStatement(addToCut);
                 PreparedStatement pstmtRecord = con.prepareStatement(recordProcessInfo)) {
                pstmtSubtract.setInt(1, input_quantity);
                pstmtSubtract.setInt(2, getRawID_Janiola(type));
                pstmtSubtract.executeUpdate();

                if(checkCutDuplicate_Janiola(type, size) == 1){
                    pstmtAdd.setInt(1, output_quantity);
                    pstmtAdd.setInt(2, getCutID_Janiola(type,size));
                    pstmtAdd.executeUpdate();
                }
                else{
                    addCutLumberNoPrice_Janiola(type, output_quantity, size);
                }

                pstmtRecord.setInt(1, input_quantity);
                pstmtRecord.setInt(2, output_quantity);
                pstmtRecord.setString(3, type);
                pstmtRecord.setString(4, size);
                pstmtRecord.setInt(5, getCutID_Janiola(type, size));
                pstmtRecord.executeUpdate();
            }
        }
    }

    /**
     * Updates Raw Lumber quantity of specified type and records the transaction history
     * @param supplier - name of the Supplier
     * @param type - type of Raw Lumber supplied
     * @param input_quantity - quantity to be added to the Raw Lumber
     * @param price - total price of the transaction
     */
    public static void supplyRawLumber(String supplier, String type, int input_quantity, int price) throws SQLException {
        String addToRaw = "UPDATE rawlumber SET rawlumber_quantity = rawlumber_quantity + ? WHERE rawlumber_ID = ?";
        String recordSupplyInfo = "INSERT INTO supplied_by VALUES (NOW(), ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmtAdd = con.prepareStatement(addToRaw);
                 PreparedStatement pstmtRecord = con.prepareStatement(recordSupplyInfo)) {
                pstmtAdd.setInt(1, input_quantity);
                pstmtAdd.setInt(2, getRawID_Janiola(type));
                pstmtAdd.executeUpdate();

                pstmtRecord.setInt(1, input_quantity);
                pstmtRecord.setString(2, supplier);
                pstmtRecord.setString(3, type);
                pstmtRecord.setInt(4, price);
                pstmtRecord.setInt(5, getSupplierID_Janiola(supplier));
                pstmtRecord.setInt(6, getRawID_Janiola(type));
                pstmtRecord.executeUpdate();
            }
        }
    }


// DELETE FUNCTIONS
    /**
     * Delete a specified type of Raw Lumber in the Database
     * @param type - Raw Lumber type
     */
    public static void deleteRawLumber(String type) throws SQLException {
        String query = "DELETE FROM rawlumber WHERE rawlumber_ID = ?";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setInt(1, getRawID_Janiola(type));
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * Delete a Cut Lumber value in the database
     * @param delete_ID - ID of Cut Lumber to Delete
     */
    public static void deleteCutLumber(String delete_ID) throws SQLException {
        String query = "DELETE FROM cutlumber WHERE cutlumber_ID = ?";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, delete_ID);
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * Delete supplier from the database
     * @param name - name of Supplier
     */
    public static void deleteSupplier(String name) throws SQLException {
        String query = "DELETE FROM suppliers WHERE supplier_ID = ?";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setInt(1, getSupplierID_Janiola(name));
                pstmt.executeUpdate();
            }
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


// EDIT FUNCTIONS
    /**
     * Insert Supplier value in the database
     * @param name - name of the supplier
     * @param info - contact info of the supplier
     */
    public static void editSupplier_Janiola(String name, String info, int supplier_ID) throws SQLException {
        String query = "UPDATE suppliers SET supplier_name = ?, supplier_info = ? WHERE supplier_ID = ?";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, name);
                pstmt.setString(2, info);
                pstmt.setInt(3, supplier_ID);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException("Error adding data to the database", e);
        }
    }

// CHECK DUPLICATES
    /**
     * General function to check existence of a cell value a table in the database
     * @param table - table to check
     * @param column - column to verify
     * @param word - word to authenticate
     * @return - value 1 if it has a duplicate and 0 otherwise
     **/
    public static int checkDuplicate_Janiola(String table, String column, String word) throws SQLException {
        String query= "SELECT EXISTS (SELECT 1 FROM " + table + " WHERE " + column + " = ?)";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, word);
                try (ResultSet result = pstmt.executeQuery()) {
                    if (result.next()) {
                        return result.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    /**
     * Same as checkDuplicate_Janiola() but has an exemption clause
     */
    public static int checkDuplicateForEdit_Janiola(String table, String column, String word, String exemptionColumn, int exemption) throws SQLException {
        String query = "SELECT EXISTS (SELECT 1 FROM " + table + " WHERE " + column + " = ? AND " + exemptionColumn + " != ?)";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, word);
                pstmt.setInt(2, exemption);
                try (ResultSet result = pstmt.executeQuery()) {
                    if (result.next()) {
                        return result.getInt(1);
                    }
                }
            }
        }
        return 0;
    }


    /**
     * Check if a specified Cut Lumber already exists in the Database
     * @param type - type of cut lumber
     * @param size - size of cut lumber
     * @return - value 1 if it has a duplicate and 0 otherwise
     */
    public static int checkCutDuplicate_Janiola(String type, String size) throws SQLException {
        String query= "SELECT EXISTS (SELECT 1 FROM cutlumber WHERE cutlumber_type = ? AND cutlumber_size = ?)";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setInt(1, getRawID_Janiola(type));
                pstmt.setInt(2, getSizeID_Janiola(size));
                try (ResultSet result = pstmt.executeQuery()) {
                    if (result.next()) {
                        return result.getInt(1);
                    }
                }
            }
        }
        return 0;
    }



 // GET HISTORY
    /**
     * Fetch the Record of the last process transaction
     * @return - Record of the last process transaction in a specific format
     */
    public static String getLastProcess() throws SQLException {
        String[] lastProcess = null;
        String processText = """
                    TYPE:   %s
                    
                    SIZE:   %s
                    
                    INPUT:   %s
                    
                    OUTPUT:   %s
                    
                    """;
        String query= "SELECT process_date, process_input_quantity, process_output_quantity, process_input_type, process_output_size" +
                      " FROM process_info ORDER BY process_date DESC LIMIT 1";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query);
                 ResultSet result = pstmt.executeQuery()) {
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
        }
    }

    /**
     * Fetch the Record of the last supply transaction
     * @return - Record of the last supply transaction in a specific format
     */
    public static String getLastSupply() throws SQLException {
        String[] lastSupply = null;
        String supplyText = """
                    SUPPLIER:   %s
                    
                    TYPE:   %s
                    
                    QUANTITY:   %s
                    
                    PRICE:   %s
                    
                    """;
        String query= "SELECT supplied_date, supplied_quantity, supplier_name, supplied_lumber, supplied_price" +
                      " FROM supplied_by ORDER BY supplied_date DESC LIMIT 1";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query);
                 ResultSet result = pstmt.executeQuery()) {
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
        }
    }

    /**
     * Fetch the Record of the last sold transaction
     * @return - Record of the last sold transaction in a specific format
     */
    public static String getLastSold() throws SQLException {
        String[] lastSold = null;
        String supplyText = """
                    CLIENT:   %s
                    
                    TYPE:   %s
                    
                    TYPE:   %s
                    
                    QUANTITY:   %s
                    
                    PRICE:   %s
                    
                    """;
        String query= "SELECT * FROM sold_to ORDER BY sold_date DESC LIMIT 1";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query);
                 ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    lastSold = new String[8];
                    for (int i = 0; i < 8; i++) {
                        lastSold[i] = result.getString(i+1);
                    }
                    return String.format(supplyText, lastSold[3], lastSold[4], lastSold[5], lastSold[1], lastSold[2]);
                }
                else{
                    return String.format(supplyText, "..", "..", "..", "..", "..");
                }
            }
        }
    }


// OTHER FUNCTIONS
    /**
     * Fetch role of user
     * @param name - Username of user
     * @return - role of specified user
     */
    public static String getCurrentUserRole(String name) throws SQLException {
        String query = "SELECT employee_role FROM application_users WHERE employee_userName = ?";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, name);
                try (ResultSet result = pstmt.executeQuery()) {
                    if (result.next()) {
                        return result.getString(1);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns the quantity of specified Raw Lumber type
     * @param type - type of Raw Lumber
     * @return - quantity of specified Raw Lumber type
     */
    public static int getRawQuantity(String type) throws SQLException {
        String query= "SELECT rawlumber_quantity FROM rawLumber WHERE rawlumber_ID = ?";
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setInt(1, getRawID_Janiola(type));
                try (ResultSet result = pstmt.executeQuery()) {
                    if (result.next()) {
                        return result.getInt(1);
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Get total of a column in a specified table in the Database
     * @param column - specified column
     * @param table - specified table
     * @return - total sum of a column
     */
    public static String getTotals(String column, String table) throws SQLException {
        String query= "SELECT SUM(" + column + ") FROM " + table;
        try (Connection con = getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(query);
                 ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    if(result.getString(1) == null){
                        return "0"; // return 0 if result is null (no rows)
                    }
                    return result.getString(1);
                }
                return "0";
            }
        }
    }
}
