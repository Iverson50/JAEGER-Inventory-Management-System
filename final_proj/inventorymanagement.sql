-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 21, 2023 at 05:21 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `inventorymanagement`
--

-- --------------------------------------------------------

--
-- Table structure for table `tblcustomreport`
--

CREATE TABLE `tblcustomreport` (
  `report_ID` int(11) NOT NULL,
  `product` varchar(255) NOT NULL,
  `customreport` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tblcustomreport`
--

INSERT INTO `tblcustomreport` (`report_ID`, `product`, `customreport`) VALUES
(4, 'wawa', 'wawa');

-- --------------------------------------------------------

--
-- Table structure for table `tblorderfulfillment`
--

CREATE TABLE `tblorderfulfillment` (
  `order_ID` int(11) NOT NULL,
  `product_ID` int(11) NOT NULL,
  `orderItem` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tblorderfulfillment`
--

INSERT INTO `tblorderfulfillment` (`order_ID`, `product_ID`, `orderItem`) VALUES
(1, 8, 'qqqq'),
(2, 7, 'vvvv'),
(3, 5, 'asdadasd'),
(4, 9, 'eee');

-- --------------------------------------------------------

--
-- Table structure for table `tblproduct`
--

CREATE TABLE `tblproduct` (
  `product_ID` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `quantity` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tblproduct`
--

INSERT INTO `tblproduct` (`product_ID`, `name`, `description`, `quantity`, `category`, `location`) VALUES
(5, 'asdadasd', 'asdasdas', '12', 'qqqw', 'asdasdas'),
(7, 'vvvv', 'vvvv', '11', 'vvvv', 'vvvv'),
(8, 'qqqq', 'qqqq', '11', 'qqq', 'qqq'),
(9, 'eee', 'ee', '1', 'ee', 'ee');

-- --------------------------------------------------------

--
-- Table structure for table `tblreorder`
--

CREATE TABLE `tblreorder` (
  `reorder_ID` int(11) NOT NULL,
  `product_ID` int(11) NOT NULL,
  `reorderItem` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tblreorder`
--

INSERT INTO `tblreorder` (`reorder_ID`, `product_ID`, `reorderItem`) VALUES
(1, 9, 'eee'),
(2, 9, 'eee');

-- --------------------------------------------------------

--
-- Table structure for table `tblshipped`
--

CREATE TABLE `tblshipped` (
  `ship_ID` int(11) NOT NULL,
  `product_ID` int(11) NOT NULL,
  `isShipped` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tblshipped`
--

INSERT INTO `tblshipped` (`ship_ID`, `product_ID`, `isShipped`) VALUES
(1, 9, 1),
(2, 8, 1),
(3, 9, 1),
(4, 9, 1);

-- --------------------------------------------------------

--
-- Table structure for table `tblstock`
--

CREATE TABLE `tblstock` (
  `stock_ID` int(11) NOT NULL,
  `product_ID` int(11) NOT NULL,
  `isReceived` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tblstock`
--

INSERT INTO `tblstock` (`stock_ID`, `product_ID`, `isReceived`) VALUES
(1, 9, 1),
(2, 9, 1);

-- --------------------------------------------------------

--
-- Table structure for table `tbluseracc`
--

CREATE TABLE `tbluseracc` (
  `user_ID` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbluseracc`
--

INSERT INTO `tbluseracc` (`user_ID`, `username`, `password`) VALUES
(8, 'asda', 'dada');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tblcustomreport`
--
ALTER TABLE `tblcustomreport`
  ADD PRIMARY KEY (`report_ID`);

--
-- Indexes for table `tblorderfulfillment`
--
ALTER TABLE `tblorderfulfillment`
  ADD PRIMARY KEY (`order_ID`),
  ADD KEY `fk_orderfulfillment_product` (`product_ID`);

--
-- Indexes for table `tblproduct`
--
ALTER TABLE `tblproduct`
  ADD PRIMARY KEY (`product_ID`);

--
-- Indexes for table `tblreorder`
--
ALTER TABLE `tblreorder`
  ADD PRIMARY KEY (`reorder_ID`);

--
-- Indexes for table `tblshipped`
--
ALTER TABLE `tblshipped`
  ADD PRIMARY KEY (`ship_ID`),
  ADD KEY `product_ID` (`product_ID`);

--
-- Indexes for table `tblstock`
--
ALTER TABLE `tblstock`
  ADD PRIMARY KEY (`stock_ID`),
  ADD KEY `product_ID` (`product_ID`);

--
-- Indexes for table `tbluseracc`
--
ALTER TABLE `tbluseracc`
  ADD PRIMARY KEY (`user_ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tblcustomreport`
--
ALTER TABLE `tblcustomreport`
  MODIFY `report_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tblorderfulfillment`
--
ALTER TABLE `tblorderfulfillment`
  MODIFY `order_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tblproduct`
--
ALTER TABLE `tblproduct`
  MODIFY `product_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `tblreorder`
--
ALTER TABLE `tblreorder`
  MODIFY `reorder_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tblshipped`
--
ALTER TABLE `tblshipped`
  MODIFY `ship_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tblstock`
--
ALTER TABLE `tblstock`
  MODIFY `stock_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tbluseracc`
--
ALTER TABLE `tbluseracc`
  MODIFY `user_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tblorderfulfillment`
--
ALTER TABLE `tblorderfulfillment`
  ADD CONSTRAINT `fk_orderfulfillment_product` FOREIGN KEY (`product_ID`) REFERENCES `tblproduct` (`product_ID`);

--
-- Constraints for table `tblshipped`
--
ALTER TABLE `tblshipped`
  ADD CONSTRAINT `tblshipped_ibfk_1` FOREIGN KEY (`product_ID`) REFERENCES `tblproduct` (`product_ID`);

--
-- Constraints for table `tblstock`
--
ALTER TABLE `tblstock`
  ADD CONSTRAINT `tblstock_ibfk_1` FOREIGN KEY (`product_ID`) REFERENCES `tblproduct` (`product_ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
