-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Dec 26, 2025 at 07:50 PM
-- Server version: 8.3.0
-- PHP Version: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `springinvoicemanager2`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
CREATE TABLE IF NOT EXISTS `admin` (
  `user_id` int NOT NULL,
  `user_email` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `user_password` varchar(255) DEFAULT NULL,
  `user_role` varchar(20) NOT NULL DEFAULT 'admin',
  UNIQUE KEY `unique_user_name` (`user_name`(100))
) ;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`user_id`, `user_email`, `user_name`, `user_password`, `user_role`) VALUES
(1253, 'achrafsaadalii@gmail.com', 'Achraf_23', '$2a$10$kAqewQYRHx0Xw15/NkJKZez8HsfH/ERULfO4fXN9F5rDQEI4t2H72', 'ADMIN');

-- --------------------------------------------------------

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
CREATE TABLE IF NOT EXISTS `client` (
  `user_id` int NOT NULL,
  `user_email` varchar(200) NOT NULL,
  `user_name` varchar(200) NOT NULL,
  `user_password` varchar(255) DEFAULT NULL,
  `user_role` varchar(20) NOT NULL DEFAULT 'client',
  `client_code` varchar(200) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_email` (`user_email`),
  UNIQUE KEY `user_name` (`user_name`),
  UNIQUE KEY `client_code` (`client_code`),
  UNIQUE KEY `user_name_2` (`user_name`),
  UNIQUE KEY `user_name_3` (`user_name`),
  UNIQUE KEY `unique_user_name` (`user_name`(100))
) ;

--
-- Dumping data for table `client`
--

INSERT INTO `client` (`user_id`, `user_email`, `user_name`, `user_password`, `user_role`, `client_code`) VALUES
(1295, 'achrafsaadalii@gmail.com', 'achraf', '$2a$10$ggYMjRwU7.kgNdYownbKhuxjn.l7dHM/Fy8eZAx83.VuKuB6/4e.y', 'CLIENT', 'CL-59057'),
(1399, 'amrousaadalii@gmail.com', 'Amrou_23', '$2a$10$q97WgptRvg7lQcKeUUj0COneXYNE9aFcYE.JQ0d8q9504lH6.zDe6', 'CLIENT', 'Cl-2006'),
(1186, 'pfacours@gmail.com', 'pfa', '$2a$10$cJXyMXkM0PrT4mF4k4uRfO0lkdU4BCjb5CHzOVylY84659V.YhxDC', 'CLIENT', 'CL-92914'),
(1243, 'amrou@gmail.com', 'amrou', '$2a$10$uqvTmr.4NJFdAUMAHfAJpO0uJ7a2wbvLv2U3HzFIgQSZqZR9Ta0yO', 'CLIENT', 'CL-97412');

-- --------------------------------------------------------

--
-- Table structure for table `comptable`
--

DROP TABLE IF EXISTS `comptable`;
CREATE TABLE IF NOT EXISTS `comptable` (
  `user_id` int NOT NULL,
  `user_email` varchar(200) NOT NULL,
  `user_name` varchar(200) NOT NULL,
  `user_password` varchar(255) DEFAULT NULL,
  `user_role` varchar(20) NOT NULL DEFAULT 'comptable',
  `comptable_code` varchar(200) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_email` (`user_email`),
  UNIQUE KEY `user_name` (`user_name`),
  UNIQUE KEY `comptable_code` (`comptable_code`),
  UNIQUE KEY `user_name_2` (`user_name`),
  UNIQUE KEY `unique_user_name` (`user_name`(100))
) ;

--
-- Dumping data for table `comptable`
--

INSERT INTO `comptable` (`user_id`, `user_email`, `user_name`, `user_password`, `user_role`, `comptable_code`) VALUES
(1186, 'zinesaadalii@gmail.com', 'ZineEddine_89', '$2a$10$r3TlvNPdBtUWpe3w6F6pueyoN6VuTCrtqd.4i3xVz7j5EDsjfDx/G', 'COMPTABLE', 'Co-1999'),
(1151, 'comptable1@gmail.com', 'comptable1', '$2a$10$j.43p9iyX.CxNetOlAV6jOioBSGdUpgMhFlh4VTTPPwKH3kt3pzdW', 'COMPTABLE', 'Co-58573'),
(1184, 'achraf@gmail.com', 'amrou', '$2a$10$ScTSW8jO2S/7lBvJAXxRf.CgDMkBVPbIsENURDnYwNjhcOSKDd2sO', 'COMPTABLE', 'CO-35336'),
(1176, 'ammm@gmail.com', 'amrou11', '$2a$10$koL5uejWo2ih188lifrI2ekostwDYY4hJboScV4Zh9E4b96zR08wy', 'COMPTABLE', 'CO-35630');

-- --------------------------------------------------------

--
-- Table structure for table `facture`
--

DROP TABLE IF EXISTS `facture`;
CREATE TABLE IF NOT EXISTS `facture` (
  `id_facture` int NOT NULL,
  `created_at` date DEFAULT NULL,
  `prix_facture` double NOT NULL,
  `etat` enum('ATTENTE','PAYE') DEFAULT NULL,
  `code_facturation` varchar(255) NOT NULL,
  `type_facture` enum('ADMIN','CLIENT','COMPTABLE') DEFAULT NULL,
  PRIMARY KEY (`id_facture`)
) ;

--
-- Dumping data for table `facture`
--

INSERT INTO `facture` (`id_facture`, `created_at`, `prix_facture`, `etat`, `code_facturation`, `type_facture`) VALUES
(1153, '2025-12-26', 9.99, 'ATTENTE', 'CL-92914', 'CLIENT'),
(1180, '2025-12-26', 62.5, 'ATTENTE', 'Cl-2006', 'CLIENT'),
(1390, '2025-12-26', 12.5, 'ATTENTE', 'CL-97412', 'CLIENT');

-- --------------------------------------------------------

--
-- Table structure for table `forgotpasswordtoken`
--

DROP TABLE IF EXISTS `forgotpasswordtoken`;
CREATE TABLE IF NOT EXISTS `forgotpasswordtoken` (
  `id_token` int NOT NULL AUTO_INCREMENT,
  `token` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_token`),
  UNIQUE KEY `token` (`token`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ligne_facture`
--

DROP TABLE IF EXISTS `ligne_facture`;
CREATE TABLE IF NOT EXISTS `ligne_facture` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantite_commandes` int DEFAULT NULL,
  `id_facture` int DEFAULT NULL,
  `id_produit` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpd4amvd8ym45aj461rjawx6pm` (`id_facture`),
  KEY `FKqxxcxnu5n5u1hvatr4od4cp9k` (`id_produit`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `ligne_facture`
--

INSERT INTO `ligne_facture` (`id`, `quantite_commandes`, `id_facture`, `id_produit`) VALUES
(8, 5, 1180, 1),
(9, 1, 1153, 9),
(10, 1, 1390, 1);

--
-- Triggers `ligne_facture`
--
DROP TRIGGER IF EXISTS `quantite_produits`;
DELIMITER $$
CREATE TRIGGER `quantite_produits` AFTER INSERT ON `ligne_facture` FOR EACH ROW BEGIN
    UPDATE produit
    SET quantite_stock = quantite_stock - NEW.quantite_commandes
    WHERE id_produit = NEW.id_produit;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `produit`
--

DROP TABLE IF EXISTS `produit`;
CREATE TABLE IF NOT EXISTS `produit` (
  `id_produit` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `prix_unitaire` double DEFAULT NULL,
  `quantite_stock` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_produit`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `produit`
--

INSERT INTO `produit` (`id_produit`, `name`, `prix_unitaire`, `quantite_stock`, `description`) VALUES
(1, 'Produit A', 12.5, 94, 'Produit A offre un excellent rapport qualité-prix avec un stock disponible de 100 unités.'),
(2, 'Produit B', 8.99, 50, 'Produit B offre un excellent rapport qualité-prix avec un stock disponible de 50 unités.'),
(3, 'Produit C', 5, 200, 'Produit C offre un excellent rapport qualité-prix avec un stock disponible de 200 unités.'),
(4, 'Produit D', 15, 75, 'Produit D offre un excellent rapport qualité-prix avec un stock disponible de 75 unités.'),
(5, 'Produit E', 20, 150, 'Produit E offre un excellent rapport qualité-prix avec un stock disponible de 150 unités.'),
(6, 'Produit F', 7.5, 60, 'Produit F offre un excellent rapport qualité-prix avec un stock disponible de 60 unités.'),
(7, 'Produit G', 10, 120, 'Produit G offre un excellent rapport qualité-prix avec un stock disponible de 120 unités.'),
(8, 'Produit H', 18.75, 80, 'Produit H offre un excellent rapport qualité-prix avec un stock disponible de 80 unités.'),
(9, 'Produit I', 9.99, 89, 'Produit I offre un excellent rapport qualité-prix avec un stock disponible de 90 unités.'),
(10, 'Produit J', 14, 110, 'Produit J offre un excellent rapport qualité-prix avec un stock disponible de 110 unités.'),
(11, 'Produit K', 6.5, 70, 'Produit K offre un excellent rapport qualité-prix avec un stock disponible de 70 unités.'),
(12, 'Produit L', 11.25, 95, 'Produit L offre un excellent rapport qualité-prix avec un stock disponible de 95 unités.'),
(13, 'Produit M', 16.5, 130, 'Produit M offre un excellent rapport qualité-prix avec un stock disponible de 130 unités.'),
(14, 'Produit N', 13.75, 85, 'Produit N offre un excellent rapport qualité-prix avec un stock disponible de 85 unités.'),
(15, 'Produit O', 19.99, 140, 'Produit O offre un excellent rapport qualité-prix avec un stock disponible de 140 unités.'),
(16, 'Produit P', 4.99, 55, 'Produit P offre un excellent rapport qualité-prix avec un stock disponible de 55 unités.'),
(17, 'Produit Q', 21.5, 160, 'Produit Q offre un excellent rapport qualité-prix avec un stock disponible de 160 unités.'),
(18, 'Produit R', 3.75, 45, 'Produit R offre un excellent rapport qualité-prix avec un stock disponible de 45 unités.'),
(19, 'Produit S', 12, 100, 'Produit S offre un excellent rapport qualité-prix avec un stock disponible de 100 unités.'),
(20, 'Produit T', 17.25, 75, 'Produit T offre un excellent rapport qualité-prix avec un stock disponible de 75 unités.'),
(21, 'Produit U', 9.5, 90, 'Produit U offre un excellent rapport qualité-prix avec un stock disponible de 90 unités.'),
(22, 'Produit V', 8.75, 120, 'Produit V offre un excellent rapport qualité-prix avec un stock disponible de 120 unités.'),
(23, 'Produit W', 15.5, 110, 'Produit W offre un excellent rapport qualité-prix avec un stock disponible de 110 unités.'),
(24, 'Produit X', 6.25, 80, 'Produit X offre un excellent rapport qualité-prix avec un stock disponible de 80 unités.'),
(25, 'Produit Y', 18, 130, 'Produit Y offre un excellent rapport qualité-prix avec un stock disponible de 130 unités.'),
(26, 'Produit Z', 14.75, 95, 'Produit Z offre un excellent rapport qualité-prix avec un stock disponible de 95 unités.'),
(27, 'Produit AA', 22.5, 150, 'Produit AA offre un excellent rapport qualité-prix avec un stock disponible de 150 unités.'),
(28, 'Produit AB', 7.99, 60, 'Produit AB offre un excellent rapport qualité-prix avec un stock disponible de 60 unités.'),
(29, 'Produit AC', 13, 100, 'Produit AC offre un excellent rapport qualité-prix avec un stock disponible de 100 unités.'),
(30, 'Produit AD', 10.5, 70, 'Produit AD offre un excellent rapport qualité-prix avec un stock disponible de 70 unités.'),
(31, 'Produit AE', 11.5, 100, 'Produit AE offre un excellent rapport qualité-prix avec un stock disponible de 100 unités.'),
(32, 'Produit AF', 9.75, 80, 'Produit AF offre un excellent rapport qualité-prix avec un stock disponible de 80 unités.'),
(33, 'Produit AG', 14.25, 120, 'Produit AG offre un excellent rapport qualité-prix avec un stock disponible de 120 unités.'),
(34, 'Produit AH', 7.5, 60, 'Produit AH offre un excellent rapport qualité-prix avec un stock disponible de 60 unités.'),
(35, 'Produit AI', 12.99, 90, 'Produit AI offre un excellent rapport qualité-prix avec un stock disponible de 90 unités.'),
(36, 'Produit AJ', 15.5, 110, 'Produit AJ offre un excellent rapport qualité-prix avec un stock disponible de 110 unités.'),
(37, 'Produit AK', 5.25, 50, 'Produit AK offre un excellent rapport qualité-prix avec un stock disponible de 50 unités.'),
(38, 'Produit AL', 18.75, 140, 'Produit AL offre un excellent rapport qualité-prix avec un stock disponible de 140 unités.'),
(39, 'Produit AM', 13.4, 75, 'Produit AM offre un excellent rapport qualité-prix avec un stock disponible de 75 unités.'),
(40, 'Produit AN', 19.9, 130, 'Produit AN offre un excellent rapport qualité-prix avec un stock disponible de 130 unités.'),
(41, 'Produit AO', 16.25, 95, 'Produit AO offre un excellent rapport qualité-prix avec un stock disponible de 95 unités.'),
(42, 'Produit AP', 8.99, 85, 'Produit AP offre un excellent rapport qualité-prix avec un stock disponible de 85 unités.'),
(43, 'Produit AQ', 21, 150, 'Produit AQ offre un excellent rapport qualité-prix avec un stock disponible de 150 unités.'),
(44, 'Produit AR', 10.5, 70, 'Produit AR offre un excellent rapport qualité-prix avec un stock disponible de 70 unités.'),
(45, 'Produit AS', 12.3, 100, 'Produit AS offre un excellent rapport qualité-prix avec un stock disponible de 100 unités.'),
(46, 'Produit AT', 9.8, 65, 'Produit AT offre un excellent rapport qualité-prix avec un stock disponible de 65 unités.'),
(47, 'Produit AU', 17.5, 120, 'Produit AU offre un excellent rapport qualité-prix avec un stock disponible de 120 unités.'),
(48, 'Produit AV', 14.99, 90, 'Produit AV offre un excellent rapport qualité-prix avec un stock disponible de 90 unités.'),
(49, 'Produit AW', 11.75, 110, 'Produit AW offre un excellent rapport qualité-prix avec un stock disponible de 110 unités.'),
(50, 'Produit AX', 6.5, 80, 'Produit AX offre un excellent rapport qualité-prix avec un stock disponible de 80 unités.'),
(51, 'Produit AY', 20.25, 130, 'Produit AY offre un excellent rapport qualité-prix avec un stock disponible de 130 unités.'),
(52, 'Produit AZ', 13.9, 75, 'Produit AZ offre un excellent rapport qualité-prix avec un stock disponible de 75 unités.'),
(53, 'Produit BA', 22.5, 140, 'Produit BA offre un excellent rapport qualité-prix avec un stock disponible de 140 unités.'),
(54, 'Produit BB', 7.25, 55, 'Produit BB offre un excellent rapport qualité-prix avec un stock disponible de 55 unités.'),
(55, 'Produit BC', 15.8, 100, 'Produit BC offre un excellent rapport qualité-prix avec un stock disponible de 100 unités.'),
(56, 'Produit BD', 16.99, 95, 'Produit BD offre un excellent rapport qualité-prix avec un stock disponible de 95 unités.'),
(57, 'Produit BE', 10, 85, 'Produit BE offre un excellent rapport qualité-prix avec un stock disponible de 85 unités.'),
(58, 'Produit BF', 18.45, 120, 'Produit BF offre un excellent rapport qualité-prix avec un stock disponible de 120 unités.'),
(59, 'Produit BG', 9.5, 60, 'Produit BG offre un excellent rapport qualité-prix avec un stock disponible de 60 unités.'),
(60, 'Produit BH', 23.75, 150, 'Produit BH offre un excellent rapport qualité-prix avec un stock disponible de 150 unités.'),
(61, 'Produit BI', 12.1, 100, 'Produit BI offre un excellent rapport qualité-prix avec un stock disponible de 100 unités.'),
(62, 'Produit BJ', 9.95, 85, 'Produit BJ offre un excellent rapport qualité-prix avec un stock disponible de 85 unités.'),
(63, 'Produit BK', 14.8, 120, 'Produit BK offre un excellent rapport qualité-prix avec un stock disponible de 120 unités.'),
(64, 'Produit BL', 7.75, 60, 'Produit BL offre un excellent rapport qualité-prix avec un stock disponible de 60 unités.'),
(65, 'Produit BM', 13.5, 90, 'Produit BM offre un excellent rapport qualité-prix avec un stock disponible de 90 unités.'),
(66, 'Produit BN', 15.2, 110, 'Produit BN offre un excellent rapport qualité-prix avec un stock disponible de 110 unités.'),
(67, 'Produit BO', 5.8, 50, 'Produit BO offre un excellent rapport qualité-prix avec un stock disponible de 50 unités.'),
(68, 'Produit BP', 18.3, 140, 'Produit BP offre un excellent rapport qualité-prix avec un stock disponible de 140 unités.'),
(69, 'Produit BQ', 13.9, 75, 'Produit BQ offre un excellent rapport qualité-prix avec un stock disponible de 75 unités.'),
(70, 'Produit BR', 19.6, 130, 'Produit BR offre un excellent rapport qualité-prix avec un stock disponible de 130 unités.'),
(71, 'Produit BS', 16.1, 95, 'Produit BS offre un excellent rapport qualité-prix avec un stock disponible de 95 unités.'),
(72, 'Produit BT', 8.75, 85, 'Produit BT offre un excellent rapport qualité-prix avec un stock disponible de 85 unités.'),
(73, 'Produit BU', 21.5, 150, 'Produit BU offre un excellent rapport qualité-prix avec un stock disponible de 150 unités.'),
(74, 'Produit BV', 10.9, 70, 'Produit BV offre un excellent rapport qualité-prix avec un stock disponible de 70 unités.'),
(75, 'Produit BW', 12.45, 100, 'Produit BW offre un excellent rapport qualité-prix avec un stock disponible de 100 unités.'),
(76, 'Produit BX', 9.6, 65, 'Produit BX offre un excellent rapport qualité-prix avec un stock disponible de 65 unités.'),
(77, 'Produit BY', 17.8, 120, 'Produit BY offre un excellent rapport qualité-prix avec un stock disponible de 120 unités.'),
(78, 'Produit BZ', 15.25, 90, 'Produit BZ offre un excellent rapport qualité-prix avec un stock disponible de 90 unités.'),
(79, 'Produit CA', 11.9, 110, 'Produit CA offre un excellent rapport qualité-prix avec un stock disponible de 110 unités.'),
(80, 'Produit CB', 6.95, 80, 'Produit CB offre un excellent rapport qualité-prix avec un stock disponible de 80 unités.'),
(81, 'Produit CC', 20.1, 130, 'Produit CC offre un excellent rapport qualité-prix avec un stock disponible de 130 unités.'),
(82, 'Produit CD', 14.35, 75, 'Produit CD offre un excellent rapport qualité-prix avec un stock disponible de 75 unités.'),
(83, 'Produit CE', 22.2, 140, 'Produit CE offre un excellent rapport qualité-prix avec un stock disponible de 140 unités.'),
(84, 'Produit CF', 7.8, 55, 'Produit CF offre un excellent rapport qualité-prix avec un stock disponible de 55 unités.'),
(85, 'Produit CG', 15.9, 100, 'Produit CG offre un excellent rapport qualité-prix avec un stock disponible de 100 unités.'),
(86, 'Produit CH', 16.75, 95, 'Produit CH offre un excellent rapport qualité-prix avec un stock disponible de 95 unités.'),
(87, 'Produit CI', 10.5, 85, 'Produit CI offre un excellent rapport qualité-prix avec un stock disponible de 85 unités.'),
(88, 'Produit CJ', 18.9, 120, 'Produit CJ offre un excellent rapport qualité-prix avec un stock disponible de 120 unités.'),
(89, 'Produit CK', 9.2, 60, 'Produit CK offre un excellent rapport qualité-prix avec un stock disponible de 60 unités.'),
(90, 'Produit CL', 23.1, 150, 'Produit CL offre un excellent rapport qualité-prix avec un stock disponible de 150 unités.'),
(91, 'Produit CM', 12.75, 100, 'Produit CM offre un excellent rapport qualité-prix avec un stock disponible de 100 unités.'),
(92, 'Produit CN', 9.4, 85, 'Produit CN offre un excellent rapport qualité-prix avec un stock disponible de 85 unités.'),
(93, 'Produit CO', 14.6, 120, 'Produit CO offre un excellent rapport qualité-prix avec un stock disponible de 120 unités.'),
(94, 'Produit CP', 7.95, 60, 'Produit CP offre un excellent rapport qualité-prix avec un stock disponible de 60 unités.'),
(95, 'Produit CQ', 13.2, 90, 'Produit CQ offre un excellent rapport qualité-prix avec un stock disponible de 90 unités.'),
(96, 'Produit CR', 15.7, 110, 'Produit CR offre un excellent rapport qualité-prix avec un stock disponible de 110 unités.'),
(97, 'Produit CS', 5.6, 50, 'Produit CS offre un excellent rapport qualité-prix avec un stock disponible de 50 unités.'),
(98, 'Produit CT', 18.6, 140, 'Produit CT offre un excellent rapport qualité-prix avec un stock disponible de 140 unités.'),
(99, 'Produit CU', 13.8, 75, 'Produit CU offre un excellent rapport qualité-prix avec un stock disponible de 75 unités.'),
(100, 'Produit CV', 19.9, 130, 'Produit CV offre un excellent rapport qualité-prix avec un stock disponible de 130 unités.'),
(101, 'Produit CW', 16.4, 95, 'Produit CW offre un excellent rapport qualité-prix avec un stock disponible de 95 unités.'),
(102, 'Produit CX', 8.85, 85, 'Produit CX offre un excellent rapport qualité-prix avec un stock disponible de 85 unités.'),
(103, 'Produit CY', 21.2, 150, 'Produit CY offre un excellent rapport qualité-prix avec un stock disponible de 150 unités.'),
(104, 'Produit CZ', 10.6, 70, 'Produit CZ offre un excellent rapport qualité-prix avec un stock disponible de 70 unités.'),
(105, 'Produit DA', 12.65, 100, 'Produit DA offre un excellent rapport qualité-prix avec un stock disponible de 100 unités.'),
(106, 'Produit DB', 9.85, 65, 'Produit DB offre un excellent rapport qualité-prix avec un stock disponible de 65 unités.'),
(107, 'Produit DC', 17.95, 120, 'Produit DC offre un excellent rapport qualité-prix avec un stock disponible de 120 unités.'),
(108, 'Produit DD', 15.3, 90, 'Produit DD offre un excellent rapport qualité-prix avec un stock disponible de 90 unités.'),
(109, 'Produit DE', 11.8, 110, 'Produit DE offre un excellent rapport qualité-prix avec un stock disponible de 110 unités.'),
(110, 'Produit DF', 7.1, 80, 'Produit DF offre un excellent rapport qualité-prix avec un stock disponible de 80 unités.'),
(111, 'Produit DG', 20.4, 130, 'Produit DG offre un excellent rapport qualité-prix avec un stock disponible de 130 unités.'),
(112, 'Produit DH', 14.2, 75, 'Produit DH offre un excellent rapport qualité-prix avec un stock disponible de 75 unités.'),
(113, 'Produit DI', 22.5, 140, 'Produit DI offre un excellent rapport qualité-prix avec un stock disponible de 140 unités.'),
(114, 'Produit DJ', 7.5, 55, 'Produit DJ offre un excellent rapport qualité-prix avec un stock disponible de 55 unités.'),
(115, 'Produit DK', 15.7, 100, 'Produit DK offre un excellent rapport qualité-prix avec un stock disponible de 100 unités.'),
(116, 'Produit DL', 16.85, 95, 'Produit DL offre un excellent rapport qualité-prix avec un stock disponible de 95 unités.'),
(117, 'Produit DM', 10.9, 85, 'Produit DM offre un excellent rapport qualité-prix avec un stock disponible de 85 unités.'),
(118, 'Produit DN', 18.7, 120, 'Produit DN offre un excellent rapport qualité-prix avec un stock disponible de 120 unités.'),
(119, 'Produit DO', 9.4, 60, 'Produit DO offre un excellent rapport qualité-prix avec un stock disponible de 60 unités.'),
(120, 'Produit DP', 23.5, 150, 'Produit DP offre un excellent rapport qualité-prix avec un stock disponible de 150 unités.'),
(121, 'Produit DQ', 12.8, 100, 'Produit DQ offre un excellent rapport qualité-prix avec un stock disponible de 100 unités.'),
(122, 'Produit DR', 9.65, 85, 'Produit DR offre un excellent rapport qualité-prix avec un stock disponible de 85 unités.'),
(123, 'Produit DS', 14.9, 120, 'Produit DS offre un excellent rapport qualité-prix avec un stock disponible de 120 unités.'),
(124, 'Produit DT', 7.85, 60, 'Produit DT offre un excellent rapport qualité-prix avec un stock disponible de 60 unités.'),
(125, 'Produit DU', 13.45, 90, 'Produit DU offre un excellent rapport qualité-prix avec un stock disponible de 90 unités.'),
(126, 'Produit DV', 15.95, 110, 'Produit DV offre un excellent rapport qualité-prix avec un stock disponible de 110 unités.');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ligne_facture`
--
ALTER TABLE `ligne_facture`
  ADD CONSTRAINT `FKpd4amvd8ym45aj461rjawx6pm` FOREIGN KEY (`id_facture`) REFERENCES `facture` (`id_facture`),
  ADD CONSTRAINT `FKqxxcxnu5n5u1hvatr4od4cp9k` FOREIGN KEY (`id_produit`) REFERENCES `produit` (`id_produit`);

DELIMITER $$
--
-- Events
--
DROP EVENT IF EXISTS `token_manipulation`$$
CREATE DEFINER=`root`@`localhost` EVENT `token_manipulation` ON SCHEDULE EVERY 2 MINUTE STARTS '2025-12-19 14:22:42' ON COMPLETION NOT PRESERVE ENABLE DO DELETE FROM  forgotpasswordtoken  WHERE NOW() >= created_at + INTERVAL 10 MINUTE$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
