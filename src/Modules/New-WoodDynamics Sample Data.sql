-- Sample data for application_users table
INSERT INTO application_users (employee_userName, employee_Password) VALUES
('emp001', 'password123'),
('emp002', 'securePassword'),
('emp003', 'qwerty');

-- Sample data for supplier table
INSERT INTO supplier (supplier_name, supplier_info) VALUES
('Wood Supplier Inc.', 'Specializes in high-quality wood materials'),
('Lumber Emporium', 'Offers a wide range of lumber products'),
('Timberland Enterprises', 'Supplier of timber products for construction');

-- Sample data for rawlumber table
INSERT INTO rawlumber (rawlumber_ID, rawlumber_type, rawlumber_quantity) VALUES
(1, 'Pine', 100),
(2, 'Oak', 75),
(3, 'Cedar', 50);

-- Sample data for size table
INSERT INTO size ( size_dimension) VALUES
('2x4'),
('4x4'),
('2x6');

-- Sample data for cutlumber table
INSERT INTO cutlumber (cutlumber_type, unit_price, quantity, size_ID) VALUES
( 1, 10, 50, 1),
( 2, 15, 30, 2),
( 3, 20, 20, 3);

-- Sample data for customer table
INSERT INTO customer (customer_ID, customer_name, customer_info) VALUES
(1, 'ABC Construction', 'Specializes in residential construction'),
(2, 'XYZ Carpentry', 'Provides custom woodworking services');
