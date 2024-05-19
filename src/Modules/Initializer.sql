-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: wooddynamics
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `application_users`
--

DROP TABLE IF EXISTS `application_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `application_users` (
  `employee_ID` int NOT NULL AUTO_INCREMENT,
  `employee_userName` char(29) NOT NULL,
  `employee_Password` varchar(199) NOT NULL,
  `employee_Role` varchar(35) NOT NULL,
  PRIMARY KEY (`employee_ID`),
  UNIQUE KEY `employee_userName` (`employee_userName`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_users`
--

LOCK TABLES `application_users` WRITE;
/*!40000 ALTER TABLE `application_users` DISABLE KEYS */;
INSERT INTO `application_users` VALUES (1,'admin','pass','Admin'),(2,'cashier','pass','Cashier'),(3,'employee','pass','Employee'),(4,'user4','password4','Employee'),(5,'user5','password5','Employee');
/*!40000 ALTER TABLE `application_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `customer_ID` int NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(50) NOT NULL,
  `customer_info` varchar(75) DEFAULT NULL,
  PRIMARY KEY (`customer_ID`),
  UNIQUE KEY `customer_name` (`customer_name`),
  UNIQUE KEY `customer_info` (`customer_info`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'Sophia Garcia','09321456789'),(2,'Daniel Martinez','09871234567'),(3,'Olivia Rodriguez','09123765432'),(4,'William Hernandez','09234567890'),(5,'Isabella Lopez','09785634120'),(6,'Emma Sanchez','09341267890'),(7,'James Rivera','09876543210'),(8,'Mia Gonzales','09123456789'),(9,'Benjamin Torres','09217654321'),(10,'Ava Ramirez','09781234567');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cutlumber`
--

DROP TABLE IF EXISTS `cutlumber`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cutlumber` (
  `cutlumber_ID` int NOT NULL AUTO_INCREMENT,
  `cutlumber_type` int NOT NULL,
  `cutlumber_unit_price` int DEFAULT NULL,
  `cutlumber_quantity` int DEFAULT NULL,
  `cutlumber_size` int NOT NULL,
  PRIMARY KEY (`cutlumber_ID`),
  KEY `fk_cut_from_rawlumber` (`cutlumber_type`),
  KEY `fk_description_of_size` (`cutlumber_size`),
  CONSTRAINT `fk_cut_from_rawlumber` FOREIGN KEY (`cutlumber_type`) REFERENCES `rawlumber` (`rawlumber_ID`) ON DELETE CASCADE,
  CONSTRAINT `fk_description_of_size` FOREIGN KEY (`cutlumber_size`) REFERENCES `size` (`size_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cutlumber`
--

LOCK TABLES `cutlumber` WRITE;
/*!40000 ALTER TABLE `cutlumber` DISABLE KEYS */;
INSERT INTO `cutlumber` VALUES (1,1,20,945,1),(2,1,35,500,2),(3,1,45,750,6),(4,4,100,300,7),(5,4,50,90,4),(6,3,22,250,2),(7,3,28,250,6),(8,2,75,50,7),(9,5,15,164,1),(10,6,200,100,2);
/*!40000 ALTER TABLE `cutlumber` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process_info`
--

DROP TABLE IF EXISTS `process_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `process_info` (
  `process_date` varchar(35) NOT NULL,
  `process_input_quantity` int NOT NULL,
  `process_output_quantity` int NOT NULL,
  `process_from_rawlumber` int NOT NULL,
  `process_to_cutlumber` int NOT NULL,
  KEY `fk_process_from_rawlumber` (`process_from_rawlumber`),
  KEY `fk_process_into_cutlumber` (`process_to_cutlumber`),
  CONSTRAINT `fk_process_from_rawlumber` FOREIGN KEY (`process_from_rawlumber`) REFERENCES `rawlumber` (`rawlumber_ID`) ON DELETE CASCADE,
  CONSTRAINT `fk_process_into_cutlumber` FOREIGN KEY (`process_to_cutlumber`) REFERENCES `cutlumber` (`cutlumber_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process_info`
--

LOCK TABLES `process_info` WRITE;
/*!40000 ALTER TABLE `process_info` DISABLE KEYS */;
INSERT INTO `process_info` VALUES ('2024-05-19 20:34:17',25,1000,1,1),('2024-05-19 20:34:35',25,500,1,2),('2024-05-19 20:34:59',50,750,1,3),('2024-05-19 20:35:39',50,500,4,4),('2024-05-19 20:35:55',25,100,4,5),('2024-05-19 20:36:05',25,250,3,6),('2024-05-19 20:36:22',50,250,3,7),('2024-05-19 20:36:38',25,100,2,8),('2024-05-19 20:36:46',20,200,5,9),('2024-05-19 20:37:04',35,400,6,10);
/*!40000 ALTER TABLE `process_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rawlumber`
--

DROP TABLE IF EXISTS `rawlumber`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rawlumber` (
  `rawlumber_ID` int NOT NULL AUTO_INCREMENT,
  `rawlumber_type` varchar(39) NOT NULL,
  `rawlumber_quantity` int DEFAULT NULL,
  PRIMARY KEY (`rawlumber_ID`),
  UNIQUE KEY `rawlumber_type` (`rawlumber_type`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rawlumber`
--

LOCK TABLES `rawlumber` WRITE;
/*!40000 ALTER TABLE `rawlumber` DISABLE KEYS */;
INSERT INTO `rawlumber` VALUES (1,'Acacia',350),(2,'Oak',300),(3,'Narra',300),(4,'Birch',325),(5,'Pine',300),(6,'Spruce',400);
/*!40000 ALTER TABLE `rawlumber` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `size`
--

DROP TABLE IF EXISTS `size`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `size` (
  `size_ID` int NOT NULL AUTO_INCREMENT,
  `size_dimension` varchar(15) NOT NULL,
  PRIMARY KEY (`size_ID`),
  UNIQUE KEY `size_dimension` (`size_dimension`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `size`
--

LOCK TABLES `size` WRITE;
/*!40000 ALTER TABLE `size` DISABLE KEYS */;
INSERT INTO `size` VALUES (4,'2x12'),(1,'2x2'),(2,'2x4'),(3,'2x8'),(7,'4x12'),(5,'4x4'),(6,'4x8');
/*!40000 ALTER TABLE `size` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sold_to`
--

DROP TABLE IF EXISTS `sold_to`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sold_to` (
  `sold_date` varchar(35) NOT NULL,
  `sold_quantity` int NOT NULL,
  `sold_price` int NOT NULL,
  `sold_to` int NOT NULL,
  `sold_cutlumber` int NOT NULL,
  KEY `fk_sold_to_customer` (`sold_to`),
  KEY `fk_sold_cutlumber` (`sold_cutlumber`),
  CONSTRAINT `fk_sold_cutlumber` FOREIGN KEY (`sold_cutlumber`) REFERENCES `cutlumber` (`cutlumber_ID`) ON DELETE CASCADE,
  CONSTRAINT `fk_sold_to_customer` FOREIGN KEY (`sold_to`) REFERENCES `customers` (`customer_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sold_to`
--

LOCK TABLES `sold_to` WRITE;
/*!40000 ALTER TABLE `sold_to` DISABLE KEYS */;
INSERT INTO `sold_to` VALUES ('2024-05-19 20:41:22',20,20000,10,1),('2024-05-19 20:41:28',35,19600,9,1),('2024-05-19 20:41:35',10,5000,2,5),('2024-05-19 20:41:48',200,50000,6,4),('2024-05-19 20:41:59',300,80000,5,10),('2024-05-19 20:42:20',50,7500,7,8),('2024-05-19 20:42:34',36,3000,9,9);
/*!40000 ALTER TABLE `sold_to` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplied_by`
--

DROP TABLE IF EXISTS `supplied_by`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplied_by` (
  `supplied_date` varchar(35) NOT NULL,
  `supplied_quantity` int NOT NULL,
  `supplied_price` int NOT NULL,
  `supplied_by` int NOT NULL,
  `supplied_lumber` int NOT NULL,
  KEY `fk_supplied_from_supplier` (`supplied_by`),
  KEY `fk_supplied_rawlumber` (`supplied_lumber`),
  CONSTRAINT `fk_supplied_from_supplier` FOREIGN KEY (`supplied_by`) REFERENCES `suppliers` (`supplier_ID`) ON DELETE CASCADE,
  CONSTRAINT `fk_supplied_rawlumber` FOREIGN KEY (`supplied_lumber`) REFERENCES `rawlumber` (`rawlumber_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplied_by`
--

LOCK TABLES `supplied_by` WRITE;
/*!40000 ALTER TABLE `supplied_by` DISABLE KEYS */;
INSERT INTO `supplied_by` VALUES ('2024-05-19 20:37:26',50,5000,1,1),('2024-05-19 20:37:49',125,3000,3,4),('2024-05-19 20:38:04',100,10000,5,3),('2024-05-19 20:38:32',75,5000,10,3),('2024-05-19 20:38:59',75,4500,10,2),('2024-05-19 20:39:19',25,2000,9,4),('2024-05-19 20:39:30',15,500,2,6);
/*!40000 ALTER TABLE `supplied_by` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `supplier_ID` int NOT NULL AUTO_INCREMENT,
  `supplier_name` varchar(50) NOT NULL,
  `supplier_info` varchar(75) DEFAULT NULL,
  PRIMARY KEY (`supplier_ID`),
  UNIQUE KEY `supplier_name` (`supplier_name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES (1,'Alice Johnson','09123456789'),(2,'John Smith','09876543210'),(3,'Emily Davis','09765432109'),(4,'Michael Brown','09543210987'),(5,'Emma Wilson','09432109876'),(6,'Mike Black','09232414244'),(7,'Marc Angelo','09443436321'),(8,'Sean Gohqou','09442526624'),(9,'Jack Black','09636788224'),(10,'Honey Anderson','09295743324');
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-19 20:44:33
