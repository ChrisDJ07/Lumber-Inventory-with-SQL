-- Sample data for application_users table
INSERT INTO application_users (employee_userName, employee_Password, employee_Role) VALUES
('admin', 'pass', 'Admin'),
('cashier', 'pass', 'Cashier'),
('employee', 'pass', 'Employee'),
('user4', 'password4', 'Employee'),
('user5', 'password5', 'Employee');

-- Sample data for supplier table
INSERT INTO suppliers (supplier_name, supplier_info) VALUES
('Alice Johnson', '09123456789'),
('John Smith', '09876543210'),
('Emily Davis', '09765432109'),
('Michael Brown', '09543210987'),
('Emma Wilson', '09432109876'),
('Mike Black', '09232414244'),
('Marc Angelo', '09443436321'),
('Sean Gohqou', '09442526624'),
('Jack Black', '09636788224'),
('Honey Anderson', '09295743324');

-- Sample data for rawlumber table
INSERT INTO rawlumber (rawlumber_type, rawlumber_quantity) VALUES
('Acacia', 400),
('Oak', 250),
('Narra', 200),
('Birch', 250),
('Pine', 320),
('Spruce', 420);

-- Sample data for size table
INSERT INTO size (size_dimension) VALUES
('2x2'),
('2x4'),
('2x8'),
('2x12'),
('4x4'),
('4x8'),
('4x12');

-- Sample data for customer table
INSERT INTO customers (customer_name, customer_info) VALUES
('Sophia Garcia', '09321456789'),
('Daniel Martinez', '09871234567'),
('Olivia Rodriguez', '09123765432'),
('William Hernandez', '09234567890'),
('Isabella Lopez', '09785634120'),
('Emma Sanchez', '09341267890'),
('James Rivera', '09876543210'),
('Mia Gonzales', '09123456789'),
('Benjamin Torres', '09217654321'),
('Ava Ramirez', '09781234567');

-- We probably just needs the sample data above to make sure its working


-- Sample data for cutlumber table
INSERT INTO cutlumber (cutlumber_type, unit_price, quantity, size_ID) VALUES
--(1, 50, 10, 1),
--(2, 60, 15, 2),
--(3, 70, 20, 3),
--(4, 80, 25, 4),
--(5, 90, 30, 5);

-- Sample data for supplied_by table
INSERT INTO supplied_by (supplier_name, supplied_lumber, supplied_date, quantity, price) VALUES
--('Wood Supplier Inc.', 'Acacia', '2024-05-10', 100, 2000),
--('Lumber Emporium', 'Oak', '2024-05-11', 150, 3000),
--('Timberland Enterprises', 'Nara', '2024-05-12', 200, 4000),
--('IIT Wood Inc.', 'Birch', '2024-05-13', 250, 5000),
--('Cagayan de Oro, Wood Inc.', 'Pine', '2024-05-14', 300, 6000);

-- Sample data for sold_to table
INSERT INTO sold_to (sold_date, sold_quantity, price, customer_name, sold_lumber) VALUES
--('2024-05-15', 50, 2500, 'ABC Construction', 'Acacia'),
--('2024-05-16', 40, 2000, 'XYZ Carpentry', 'Oak'),
--('2024-05-17', 30, 1500, 'Customer3', 'Nara'),
--('2024-05-18', 20, 1000, 'Customer4', 'Birch'),
--('2024-05-19', 10, 500, 'Customer5', 'Pine');

-- Sample data for process_info table
INSERT INTO process_info (process_date, process_input_quantity, process_output_quantity, process_input_type, process_output_size) VALUES
--('2024-05-20', 100, 50, 'Acacia', '2x4'),
--('2024-05-21', 150, 40, 'Oak', '2x6'),
--('2024-05-22', 200, 30, 'Nara', '4x4'),
--('2024-05-23', 250, 20, 'Birch', '6x6'),
--('2024-05-24', 300, 10, 'Pine', '8x8');