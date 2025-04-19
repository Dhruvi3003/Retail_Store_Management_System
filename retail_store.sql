-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 23, 2024 at 06:33 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `retail_store`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`` PROCEDURE `insert_orders` (IN `order_id1` INT, IN `product_id1` INT, IN `quantity1` INT)   BEGIN
INSERT INTO OrderDetails (OrderID, ProductID, Quantity)
  VALUES (order_id1,product_id1,quantity1);
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `CustomerID` int(11) NOT NULL,
  `Name` varchar(100) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `Phone` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`CustomerID`, `Name`, `Email`, `Phone`) VALUES
(1, 'Rahul Kumar', 'rahulkumar@gmail.com', '9999999999'),
(2, 'Priya Sharma', 'priyasharma@gamil.com', '8888888888'),
(3, 'Rajesh Gupta', 'rajeshgupta@gmail.com', '1236549870'),
(4, 'Sonia Jain', 'soniajain@gmail.com', '5236149870'),
(5, 'Vikas Singh', 'vikassingh@gmail.com', '9863257410'),
(6, 'Neha Patel', 'nehapatel@gmail.com', '9632587415'),
(7, 'Amit Shah', 'amitshah@gmail.com', '3333333333'),
(8, 'Nitin Parmar', 'nitin@gmail.com', '9876549876');

-- --------------------------------------------------------

--
-- Table structure for table `orderdetails`
--

CREATE TABLE `orderdetails` (
  `OrderID` int(11) NOT NULL,
  `ProductID` int(11) NOT NULL,
  `Quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orderdetails`
--

INSERT INTO `orderdetails` (`OrderID`, `ProductID`, `Quantity`) VALUES
(1, 1, 2),
(1, 2, 1),
(1, 3, 2),
(2, 3, 3),
(2, 4, 2),
(3, 5, 1),
(4, 7, 2),
(4, 8, 3),
(5, 9, 1),
(5, 10, 2),
(6, 1, 3),
(6, 5, 2),
(7, 2, 4),
(7, 9, 1),
(8, 1, 1),
(8, 11, 2),
(9, 1, 1),
(10, 6, 1);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `OrderID` int(11) NOT NULL,
  `CustomerID` int(11) DEFAULT NULL,
  `StaffID` int(11) DEFAULT NULL,
  `OrderDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`OrderID`, `CustomerID`, `StaffID`, `OrderDate`) VALUES
(1, 1, 1, '2024-08-18'),
(2, 2, 2, '2024-08-18'),
(3, 3, 3, '2024-08-18'),
(4, 4, 1, '2024-08-18'),
(5, 5, 2, '2024-08-18'),
(6, 6, 3, '2024-08-18'),
(7, 7, 1, '2024-08-18'),
(8, 8, 1, '2024-08-20'),
(9, 1, 1, '2024-08-20'),
(10, 2, 2, '2024-08-20');

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `PaymentID` int(11) NOT NULL,
  `OrderID` int(11) DEFAULT NULL,
  `PaymentMethod` varchar(100) DEFAULT NULL,
  `PaymentDate` date DEFAULT NULL,
  `Amount` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`PaymentID`, `OrderID`, `PaymentMethod`, `PaymentDate`, `Amount`) VALUES
(1, 1, 'Credit Card', '2024-08-19', 90.00),
(2, 2, 'Cash', '2024-08-20', 90.00),
(3, 3, 'UPI', '2024-08-20', 20.00),
(4, 4, 'Credit Card', '2024-08-21', 106.00),
(5, 5, 'Cash', '2024-08-21', 64.00),
(6, 6, 'Cash', '2024-08-22', 115.00),
(7, 8, 'Cash', '2024-08-21', 6025.00),
(11, 9, 'UPI', '2024-08-23', 25.00),
(12, 10, 'Credit Card', '2024-08-22', 15.00);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `ProductID` int(11) NOT NULL,
  `Name` varchar(100) DEFAULT NULL,
  `Price` decimal(10,2) DEFAULT NULL,
  `QuantityInStock` int(11) DEFAULT NULL,
  `Status` varchar(20) DEFAULT 'Available',
  `dateFrom` date DEFAULT current_timestamp(),
  `dateTo` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`ProductID`, `Name`, `Price`, `QuantityInStock`, `Status`, `dateFrom`, `dateTo`) VALUES
(1, 'Aashirvaad Atta', 25.00, 43, 'Available', '2024-08-18', NULL),
(2, 'Amul Milk', 40.00, 25, 'Available', '2024-08-18', NULL),
(3, 'Britannia Biscuits', 10.00, 72, 'Available', '2024-08-18', NULL),
(4, 'Pepsi Soft Drink', 30.00, 38, 'Available', '2024-08-18', NULL),
(5, 'Nescafe Coffee', 20.00, 57, 'Available', '2024-08-18', NULL),
(6, 'Lays Chips', 15.00, 79, 'Discontinued', '2024-08-18', '2024-08-22'),
(7, 'Kellogg\'s Cereal', 35.00, 43, 'Available', '2024-08-18', NULL),
(8, 'Parle Biscuits', 12.00, 87, 'Available', '2024-08-18', NULL),
(9, 'Tata Tea', 28.00, 53, 'Available', '2024-08-18', NULL),
(10, 'Dettol Soap', 18.00, 68, 'Available', '2024-08-18', NULL),
(11, 'Oil Can', 2000.00, 8, 'Available', '2024-08-20', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `staff`
--

CREATE TABLE `staff` (
  `staffID` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `position` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `staff`
--

INSERT INTO `staff` (`staffID`, `name`, `email`, `phone`, `position`) VALUES
(1, 'Suresh Birla', 'sureshpatel@gmail.com', '9874563210', 'Store Manager'),
(2, 'Neha Singh', 'nehasingh@gmail.com', '9879876540', 'Sales Associate'),
(3, 'Vikas Jain', 'vikasjain@gmail.com', '9999999999', 'Customer service'),
(4, 'Rajesh Kumar', 'rajeshkumar@gmail.com', '6666666666', 'Customer Service'),
(5, 'Priya Sharma', 'priyasharma@gamil.com', '9876540213', 'Store Manager'),
(6, 'Manoj Sojitra', 'manoj@gmail.com', '9865327410', 'Sales Associate');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`CustomerID`);

--
-- Indexes for table `orderdetails`
--
ALTER TABLE `orderdetails`
  ADD PRIMARY KEY (`OrderID`,`ProductID`),
  ADD KEY `ProductID` (`ProductID`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`OrderID`),
  ADD KEY `StaffID` (`StaffID`),
  ADD KEY `CustomerID` (`CustomerID`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`PaymentID`),
  ADD KEY `OrderID` (`OrderID`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`ProductID`);

--
-- Indexes for table `staff`
--
ALTER TABLE `staff`
  ADD PRIMARY KEY (`staffID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `OrderID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `PaymentID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `orderdetails`
--
ALTER TABLE `orderdetails`
  ADD CONSTRAINT `orderdetails_ibfk_1` FOREIGN KEY (`OrderID`) REFERENCES `orders` (`OrderID`),
  ADD CONSTRAINT `orderdetails_ibfk_2` FOREIGN KEY (`ProductID`) REFERENCES `products` (`ProductID`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`StaffID`) REFERENCES `staff` (`staffID`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`CustomerID`) REFERENCES `customers` (`CustomerID`);

--
-- Constraints for table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`OrderID`) REFERENCES `orders` (`OrderID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
