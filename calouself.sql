-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 15, 2024 at 10:21 AM
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
-- Database: `calouself`
--

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `item_id` varchar(255) NOT NULL,
  `seller_id` varchar(255) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  `size` varchar(50) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `status` enum('pending','approved','sold','declined') NOT NULL DEFAULT 'pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`item_id`, `seller_id`, `item_name`, `category`, `size`, `price`, `status`) VALUES
('6c2c3d7e-7fa7-4dcd-82a4-cdc8a2df9f49', 'dd2fef21-4630-407c-8947-87a14fad112c', 'Air Jordan', 'Shoes', '40', 3000000.00, 'declined'),
('78ee2711-7f0c-4df7-b7aa-d91d967af573', 'dd2fef21-4630-407c-8947-87a14fad112c', 'Air Jordan', 'Shoes', '45', 2000000.00, 'approved'),
('85471411-7059-4d04-b533-bbde4f59fcbf', 'dd2fef21-4630-407c-8947-87a14fad112c', 'Uniqlo Shirt', 'Shirt', 'XL', 100000.00, 'declined'),
('da646d02-8ecd-4a35-a902-9fe1648d6725', 'dd2fef21-4630-407c-8947-87a14fad112c', 'Air Jordan', 'Shoes', '40', 2000000.00, 'sold'),
('f8d70910-60e8-49f4-a9f6-f2dd8729ebbe', 'dd2fef21-4630-407c-8947-87a14fad112c', 'Nike', 'Shoes', '41', 2000000.00, 'sold'),
('f8e116df-4d73-4fbc-bcef-a6f184613929', 'dd2fef21-4630-407c-8947-87a14fad112c', 'Supreme Hat', 'Hat', 'S', 20000.00, 'sold');

-- --------------------------------------------------------

--
-- Table structure for table `offers`
--

CREATE TABLE `offers` (
  `offer_id` varchar(255) NOT NULL,
  `item_id` varchar(255) NOT NULL,
  `buyer_id` varchar(255) NOT NULL,
  `offer_price` decimal(10,2) NOT NULL,
  `status` enum('pending','accepted','declined') NOT NULL DEFAULT 'pending',
  `decline_reason` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `offers`
--

INSERT INTO `offers` (`offer_id`, `item_id`, `buyer_id`, `offer_price`, `status`, `decline_reason`, `created_at`) VALUES
('72c2d142-7140-4ec8-ba93-cd6265a331ea', '78ee2711-7f0c-4df7-b7aa-d91d967af573', '094bd005-a011-46eb-9376-4500095d0886', 1000000.00, 'declined', 'Terlalu murah', '2024-12-15 07:44:05'),
('905bbb84-4001-4dc1-8bef-7fceeb0ab57f', '78ee2711-7f0c-4df7-b7aa-d91d967af573', '094bd005-a011-46eb-9376-4500095d0886', 1500000.00, 'pending', NULL, '2024-12-15 07:45:02'),
('992b8138-2231-472a-9284-1e68d09a6d71', 'da646d02-8ecd-4a35-a902-9fe1648d6725', '094bd005-a011-46eb-9376-4500095d0886', 2100000.00, 'accepted', NULL, '2024-12-15 07:12:13');

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `transaction_id` varchar(255) NOT NULL,
  `buyer_id` varchar(255) NOT NULL,
  `seller_id` varchar(255) NOT NULL,
  `item_id` varchar(255) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `transaction_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`transaction_id`, `buyer_id`, `seller_id`, `item_id`, `price`, `transaction_date`) VALUES
('43935f50-df40-4f24-8c3e-fca988f52e71', '094bd005-a011-46eb-9376-4500095d0886', 'dd2fef21-4630-407c-8947-87a14fad112c', 'da646d02-8ecd-4a35-a902-9fe1648d6725', 2100000.00, '2024-12-15 07:13:17'),
('94e2bd6a-c270-42b9-b600-9b8efbb94caf', '094bd005-a011-46eb-9376-4500095d0886', 'dd2fef21-4630-407c-8947-87a14fad112c', 'f8d70910-60e8-49f4-a9f6-f2dd8729ebbe', 2000000.00, '2024-12-15 06:50:13');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `User_id` varchar(255) NOT NULL,
  `Username` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `Phone_Number` varchar(255) NOT NULL,
  `Address` text NOT NULL,
  `Role` varchar(50) NOT NULL
) ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`User_id`, `Username`, `Password`, `Phone_Number`, `Address`, `Role`) VALUES
('094bd005-a011-46eb-9376-4500095d0886', 'aldo', '@ldo1234', '+628937465842', 'Bandung', 'Buyer'),
('dd2fef21-4630-407c-8947-87a14fad112c', 'rmbagt', '@ldo1234', '+628746253645', 'Jakarta', 'Seller');

-- --------------------------------------------------------

--
-- Table structure for table `wishlist`
--

CREATE TABLE `wishlist` (
  `wishlist_id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `item_id` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `wishlist`
--

INSERT INTO `wishlist` (`wishlist_id`, `user_id`, `item_id`, `created_at`) VALUES
('fbb6c7af-36df-4088-909e-4ce18bf5f5f8', '094bd005-a011-46eb-9376-4500095d0886', '78ee2711-7f0c-4df7-b7aa-d91d967af573', '2024-12-15 07:45:33');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `seller_id` (`seller_id`);

--
-- Indexes for table `offers`
--
ALTER TABLE `offers`
  ADD PRIMARY KEY (`offer_id`),
  ADD KEY `item_id` (`item_id`),
  ADD KEY `buyer_id` (`buyer_id`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `buyer_id` (`buyer_id`),
  ADD KEY `seller_id` (`seller_id`),
  ADD KEY `item_id` (`item_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`User_id`),
  ADD UNIQUE KEY `Username` (`Username`);

--
-- Indexes for table `wishlist`
--
ALTER TABLE `wishlist`
  ADD PRIMARY KEY (`wishlist_id`),
  ADD UNIQUE KEY `unique_user_item` (`user_id`,`item_id`),
  ADD KEY `item_id` (`item_id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `items`
--
ALTER TABLE `items`
  ADD CONSTRAINT `items_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `users` (`User_id`);

--
-- Constraints for table `offers`
--
ALTER TABLE `offers`
  ADD CONSTRAINT `offers_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`),
  ADD CONSTRAINT `offers_ibfk_2` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`User_id`);

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`User_id`),
  ADD CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`seller_id`) REFERENCES `users` (`User_id`),
  ADD CONSTRAINT `transactions_ibfk_3` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`);

--
-- Constraints for table `wishlist`
--
ALTER TABLE `wishlist`
  ADD CONSTRAINT `wishlist_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`User_id`),
  ADD CONSTRAINT `wishlist_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
