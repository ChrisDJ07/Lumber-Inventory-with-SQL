CREATE DATABASE IF NOT EXISTS WoodDynamics;

USE WoodDynamics;


CREATE TABLE if NOT EXISTS application_users
(
    employee_ID             INT PRIMARY KEY AUTO_INCREMENT,
	employee_userName		CHAR (29) NOT NULL UNIQUE,
	employee_Password		VARCHAR (199) NOT NULL,
	employee_Role           VARCHAR (35) NOT NULL
);

CREATE TABLE if NOT EXISTS suppliers
(
	supplier_ID 			INT PRIMARY KEY AUTO_INCREMENT,
	supplier_name			VARCHAR (50) NOT NULL UNIQUE,
	supplier_info			VARCHAR (75) NULL
);

CREATE TABLE if NOT EXISTS rawlumber
(
	rawlumber_ID			INT PRIMARY KEY AUTO_INCREMENT,
	rawlumber_type			VARCHAR (39) NOT NULL UNIQUE,
	rawlumber_quantity	    INT NULL
);

CREATE TABLE if NOT EXISTS size
(
	size_ID					INT PRIMARY KEY AUTO_INCREMENT,
	size_dimension			VARCHAR (15) NOT NULL UNIQUE
);

CREATE TABLE if NOT EXISTS cutlumber
(
	cutlumber_ID			INT PRIMARY KEY AUTO_INCREMENT,
	cutlumber_type			INT NOT NULL,
	cutlumber_unit_price	INT NULL,
	cutlumber_quantity		INT NULL,
	cutlumber_size			INT NOT NULL,
	CONSTRAINT fk_cut_from_rawlumber FOREIGN KEY (cutlumber_type) REFERENCES rawlumber (rawlumber_ID) ON DELETE CASCADE,
	CONSTRAINT fk_description_of_size FOREIGN KEY (cutlumber_size) REFERENCES size (size_ID) ON DELETE CASCADE
);

CREATE TABLE if NOT EXISTS customers
(
	customer_ID				INT PRIMARY KEY AUTO_INCREMENT,
	customer_name			VARCHAR (50) NOT NULL UNIQUE,
	customer_info			VARCHAR (75) NULL UNIQUE
);
 
CREATE TABLE if NOT EXISTS supplied_by
(
    supplied_date			VARCHAR (35) NOT NULL,
    supplied_quantity		INT NOT NULL,
	supplier_name			VARCHAR (35) NOT NULL,
	supplied_lumber		    VARCHAR (35) NOT NULL,
	supplied_price          INT NOT NULL
);

CREATE TABLE if NOT EXISTS process_info
(
	process_date			        VARCHAR (35) NOT NULL,
	process_input_quantity			INT NOT NULL,
	process_output_quantity			INT NOT NULL,
	process_input_type			    VARCHAR (25) NOT NULL,
	process_output_size			    VARCHAR (25) NOT NULL
);

CREATE TABLE if NOT EXISTS sold_to
(
	sold_date				VARCHAR (35) NOT NULL,
	sold_quantity			INT NOT NULL,
	sold_price              INT NOT NULL,
	sold_to_customer		VARCHAR (50) NOT NULL,
	sold_lumber				VARCHAR (39) NOT NULL,
	sold_size               VARCHAR (25) NOT NULL
);

-- Note: You can optionally add PRIMARY KEY or UNIQUE constraints to the columns if needed.