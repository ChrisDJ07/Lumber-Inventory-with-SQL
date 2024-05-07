CREATE DATABASE IF NOT EXISTS WoodDynamics;

USE WoodDynamics;


CREATE TABLE if NOT EXISTS application_users
(
	employee_ID				CHAR (29) NOT NULL,
	employee_Password		VARCHAR (199) NOT NULL
);

CREATE TABLE if NOT EXISTS supplier
(
	supplier_ID 			CHAR (9) NOT NULL PRIMARY KEY ,
	supplier_name			VARCHAR (199) NOT NULL ,
	supplier_info			VARCHAR (299) NULL
);

CREATE TABLE if NOT EXISTS rawlumber
(
	rawlumber_ID			INT (4) NOT NULL PRIMARY KEY ,
	rawlumber_type			VARCHAR (39) NOT NULL ,
	rawlumber_quantity	INT NULL
);

CREATE TABLE if NOT EXISTS size
(
	size_ID					INT (4) NOT NULL PRIMARY KEY ,
	size_dimension			VARCHAR (29) NOT NULL
);

CREATE TABLE if NOT EXISTS cutlumber
(
	cutlumber_ID			INT (4) NOT NULL PRIMARY KEY ,
	cutlumber_type			INT (4) NOT NULL ,
	CONSTRAINT fk_cut_from_rawlumber FOREIGN KEY (cutlumber_type) REFERENCES rawlumber (rawlumber_ID),
	unit_price				INT NULL ,
	quantity					INT NULL ,
	size_ID					INT (4) NOT NULL ,
	CONSTRAINT fk_description_of_size FOREIGN KEY (size_ID) REFERENCES size (size_ID)
);

CREATE TABLE if NOT EXISTS customer
(
	customer_ID				INT (4) NOT NULL PRIMARY KEY ,
	customer_name			VARCHAR (199) NOT NULL ,
	customer_info			VARCHAR (299) NULL
);

CREATE TABLE if NOT EXISTS supplied_by
(
	supplier_ID				CHAR (9) NOT NULL ,
	CONSTRAINT fk_supplier_receipt FOREIGN KEY (supplier_ID) REFERENCES supplier (supplier_ID),
	supplied_lumber		INT (4) NOT NULL ,
	CONSTRAINT fk_supplied_lumber FOREIGN KEY (supplied_lumber) REFERENCES rawlumber (rawlumber_ID),
	supplied_date			CHAR (10) NOT NULL ,
	quantity					INT NULL ,
	price						INT NULL

);

CREATE TABLE if NOT EXISTS process_info
(
	process_date			CHAR (10) NOT NULL ,
	process_input_quantity			INT NOT NULL ,
	process_output_quantity			INT NOT NULL ,
	process_input			INT (4) NOT NULL ,
	CONSTRAINT fk_process_input_receipt FOREIGN KEY (process_input) REFERENCES rawlumber (rawlumber_ID),
	process_output			INT (4) NOT NULL ,
	CONSTRAINT fk_process_output_receipt FOREIGN KEY (process_output) REFERENCES cutlumber (cutlumber_ID)
);

CREATE TABLE if NOT EXISTS sold_to
(
	sold_date				CHAR (10) NOT NULL ,
	sold_quantity			INT NULL ,
	customer_ID				INT (4) NOT NULL ,
	CONSTRAINT fk_customer_receipt FOREIGN KEY (customer_ID) REFERENCES customer (customer_ID),
	sold_lumber				INT (4) NOT NULL ,
	CONSTRAINT fk_customer_bought FOREIGN KEY (sold_lumber) REFERENCES cutlumber (cutlumber_ID)
);

-- Note: You can optionally add PRIMARY KEY or UNIQUE constraints to the columns if needed.