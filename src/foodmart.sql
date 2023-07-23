-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.11-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for foodmart
DROP DATABASE IF EXISTS `foodmart`;
CREATE DATABASE IF NOT EXISTS `foodmart` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `foodmart`;

-- Dumping structure for table foodmart.admin
DROP TABLE IF EXISTS `admin`;
CREATE TABLE IF NOT EXISTS `admin` (
  `adminID` int(11) NOT NULL AUTO_INCREMENT,
  `adminName` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  PRIMARY KEY (`adminID`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table foodmart.admin: ~2 rows (approximately)
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` (`adminID`, `adminName`, `email`, `password_hash`) VALUES
	(1, 'a', 'a', '356A192B7913B04C54574D18C28D46E6395428AB'),
	(46, 'Thu Anh', 'admin@gmail.com', '2B8FD8541A5FAF512691635F5290493695745BEF');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;

-- Dumping structure for table foodmart.category
DROP TABLE IF EXISTS `category`;
CREATE TABLE IF NOT EXISTS `category` (
  `categoryID` int(11) NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(255) NOT NULL,
  `parent` int(11) DEFAULT NULL,
  PRIMARY KEY (`categoryID`),
  KEY `parent` (`parent`),
  CONSTRAINT `category_ibfk_1` FOREIGN KEY (`parent`) REFERENCES `category` (`categoryID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2002 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table foodmart.category: ~19 rows (approximately)
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`categoryID`, `categoryName`, `parent`) VALUES
	(1, 'Dairy', NULL),
	(2, 'Milk', 1),
	(3, 'Cheese', 1),
	(4, 'Frozen & Refrigerated', NULL),
	(5, 'Frozen Fish & Seafood', 4),
	(6, 'Ice Cream', 4),
	(7, 'Snacks', NULL),
	(8, 'Chips', 7),
	(9, 'Crackers', 7),
	(10, 'Beef Jerky', 7),
	(11, 'Honey, Jam & Syrup', NULL),
	(12, 'Honey', 11),
	(13, 'Jam', 11),
	(14, 'Syrup', 11),
	(15, 'Coffee & Tea', NULL),
	(16, 'Coffee', 15),
	(17, 'Tea', 15),
	(18, 'Fresh Fruit', NULL),
	(19, 'Bakery', NULL);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

-- Dumping structure for table foodmart.customer
DROP TABLE IF EXISTS `customer`;
CREATE TABLE IF NOT EXISTS `customer` (
  `customerID` int(11) NOT NULL AUTO_INCREMENT,
  `fullName` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL DEFAULT '',
  `phone` varchar(12) NOT NULL,
  PRIMARY KEY (`customerID`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table foodmart.customer: ~11 rows (approximately)
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` (`customerID`, `fullName`, `email`, `password_hash`, `address`, `phone`) VALUES
	(1, 'Rachel Green', 'rachel@gmail.com', '17BA0791499DB908433B80F37C5FBC89B870084B', 'New York City', '01234567891'),
	(2, 'Monica Geller', 'monica@gmail.com', '356A192B7913B04C54574D18C28D46E6395428AB', 'New York', '0123456789'),
	(3, 'Ross Geller', 'ross@gmail.com', 'DA4B9237BACCCDF19C0760CAB7AEC4A8359010B0', 'New York', '0123456789'),
	(4, 'Chandler Bing', 'chandler@gmail.com', 'DA4B9237BACCCDF19C0760CAB7AEC4A8359010B0', 'New York', '0123456789'),
	(5, 'Joey Tribbiani', 'joey@gmail.com', 'DA4B9237BACCCDF19C0760CAB7AEC4A8359010B0', 'New York', '0123456789'),
	(6, 'Thu Anh Dang', 'a@gmail.com', '2B8FD8541A5FAF512691635F5290493695745BEF', 'Hoang Mai, Hanoi', '0123456789'),
	(7, 'Cristina Yang', 'yang@gmail.com', '356A192B7913B04C54574D18C28D46E6395428AB', '', '0123456789'),
	(8, 'Meredith Grey', 'grey@gmail.com', '356A192B7913B04C54574D18C28D46E6395428AB', '', '0123456789'),
	(9, 'Jack Dawson', 'jack@gmail.com', '356A192B7913B04C54574D18C28D46E6395428AB', '', '0123456789'),
	(10, 'Thu An Dang', 'an@gmail.com', '356A192B7913B04C54574D18C28D46E6395428AB', 'Hoan Kiem, Ha Noi', '0123456789'),
	(11, 'Rose Dawson', 'rose@gmail.com', '2B8FD8541A5FAF512691635F5290493695745BEF', 'New York', '0123456789'),
	(21, 'Stephen Strange', 'drstrange@gmail.com', '2B8FD8541A5FAF512691635F5290493695745BEF', 'New York City', '0123456789');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;

-- Dumping structure for table foodmart.order
DROP TABLE IF EXISTS `order`;
CREATE TABLE IF NOT EXISTS `order` (
  `orderId` int(11) NOT NULL AUTO_INCREMENT,
  `customerId` int(11) NOT NULL,
  `date` date NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`orderId`),
  KEY `customerId` (`customerId`),
  KEY `status` (`status`),
  CONSTRAINT `order_ibfk_1` FOREIGN KEY (`customerId`) REFERENCES `customer` (`customerID`) ON UPDATE CASCADE,
  CONSTRAINT `order_ibfk_2` FOREIGN KEY (`status`) REFERENCES `status` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table foodmart.order: ~15 rows (approximately)
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` (`orderId`, `customerId`, `date`, `status`) VALUES
	(34, 6, '2020-09-01', 3),
	(35, 2, '2020-09-03', 2),
	(36, 3, '2020-09-03', 2),
	(37, 5, '2020-09-03', 3),
	(38, 6, '2020-09-04', 2),
	(39, 6, '2020-09-04', 3),
	(40, 6, '2020-09-15', 3),
	(41, 10, '2020-09-15', 3),
	(42, 6, '2020-09-18', 3),
	(43, 6, '2020-09-18', 3),
	(47, 11, '2020-09-18', 3),
	(48, 6, '2020-09-18', 3),
	(49, 11, '2020-09-19', 1),
	(50, 11, '2020-09-19', 1),
	(53, 21, '2020-09-19', 3),
	(54, 21, '2020-09-19', 1),
	(55, 2, '2020-09-19', 1);
/*!40000 ALTER TABLE `order` ENABLE KEYS */;

-- Dumping structure for table foodmart.orderdetail
DROP TABLE IF EXISTS `orderdetail`;
CREATE TABLE IF NOT EXISTS `orderdetail` (
  `orderId` int(11) NOT NULL,
  `productId` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `unitPrice` float NOT NULL DEFAULT 0,
  PRIMARY KEY (`orderId`,`productId`),
  KEY `orderdetail_ibfk_2` (`productId`),
  CONSTRAINT `orderdetail_ibfk_1` FOREIGN KEY (`orderId`) REFERENCES `order` (`orderId`) ON UPDATE CASCADE,
  CONSTRAINT `orderdetail_ibfk_2` FOREIGN KEY (`productId`) REFERENCES `product` (`productID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table foodmart.orderdetail: ~56 rows (approximately)
/*!40000 ALTER TABLE `orderdetail` DISABLE KEYS */;
INSERT INTO `orderdetail` (`orderId`, `productId`, `quantity`, `unitPrice`) VALUES
	(34, 19, 1, 280000),
	(35, 7, 2, 155000),
	(35, 8, 2, 175000),
	(35, 9, 3, 125000),
	(36, 15, 1, 105000),
	(36, 16, 2, 115000),
	(36, 17, 4, 205000),
	(36, 18, 3, 105000),
	(36, 20, 6, 105000),
	(37, 48, 1, 48000),
	(37, 49, 3, 65000),
	(37, 50, 5, 48000),
	(38, 10, 31, 195000),
	(38, 51, 1, 150000),
	(38, 56, 1, 380000),
	(38, 57, 2, 105000),
	(39, 10, 9, 195000),
	(39, 21, 6, 35000),
	(39, 36, 6, 65000),
	(39, 57, 11, 105000),
	(40, 8, 1, 175000),
	(40, 42, 1, 265000),
	(40, 55, 20, 160000),
	(41, 39, 3, 680000),
	(41, 41, 20, 130000),
	(41, 42, 1, 265000),
	(41, 52, 1, 110000),
	(41, 54, 1, 220000),
	(42, 1, 2, 46000),
	(42, 2, 1, 68000),
	(42, 3, 1, 98000),
	(42, 4, 1, 98000),
	(42, 5, 1, 260000),
	(43, 39, 3, 680000),
	(43, 41, 20, 130000),
	(43, 42, 1, 265000),
	(43, 48, 2, 48000),
	(47, 9, 2, 130000),
	(47, 41, 3, 130000),
	(47, 52, 2, 110000),
	(48, 11, 3, 205000),
	(48, 12, 1, 20000),
	(48, 32, 1, 75000),
	(48, 33, 4, 75000),
	(49, 58, 3, 96000),
	(49, 59, 1, 84000),
	(50, 58, 1, 96000),
	(50, 59, 1, 84000),
	(50, 60, 1, 65000),
	(50, 61, 1, 45000),
	(50, 62, 1, 15000),
	(53, 7, 1, 155000),
	(53, 8, 2, 175000),
	(53, 52, 1, 110000),
	(53, 61, 1, 45000),
	(54, 58, 1, 96000),
	(54, 59, 1, 84000),
	(54, 60, 1, 65000),
	(54, 61, 1, 45000),
	(54, 62, 1, 15000),
	(55, 59, 1, 84000),
	(55, 60, 1, 65000);
/*!40000 ALTER TABLE `orderdetail` ENABLE KEYS */;

-- Dumping structure for table foodmart.product
DROP TABLE IF EXISTS `product`;
CREATE TABLE IF NOT EXISTS `product` (
  `productID` int(11) NOT NULL AUTO_INCREMENT,
  `productName` varchar(255) NOT NULL,
  `price` float NOT NULL,
  `stock` int(11) NOT NULL,
  `description` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL,
  `categoryID` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`productID`),
  KEY `categoryID` (`categoryID`),
  KEY `FK_product_status` (`status`),
  CONSTRAINT `FK_product_status` FOREIGN KEY (`status`) REFERENCES `status` (`code`) ON UPDATE CASCADE,
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`categoryID`) REFERENCES `category` (`categoryID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table foodmart.product: ~62 rows (approximately)
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`productID`, `productName`, `price`, `stock`, `description`, `image`, `categoryID`, `status`) VALUES
	(1, 'Arborea Latte Semi-Skimmed Milk', 46000, 10, 'Italy - 1L', 'img/Arborea Latte Semi-Skimmed Milk.jpg', 2, 4),
	(2, 'Devondale Our Creamy One Full Cream Pure Milk', 68000, 20, 'Australia 2L', 'img/Devondale Our Creamy One Full Cream Pure Milk.jpg', 2, 4),
	(3, 'Elmhurst Milked Oats Chocolate', 98000, 20, 'USA 325ml', 'img/Elmhurst Milked Oats Chocolate.png', 2, 4),
	(4, 'Elmhurst Milked Oats Vanilla', 98000, 17, 'USA 325ml', 'img/Elmhurst Milked Oats Vanilla.png', 2, 4),
	(5, 'Elmhurst Unsweetened Milked Oats', 260000, 20, 'USA 946ml', 'img/Elmhurst Unsweetened Milked Oats.png', 2, 4),
	(6, 'The Bridge Bio Almond Drink', 140000, 30, 'Italia 1L', 'img/The Bridge Bio Almond Drink.png', 2, 4),
	(7, 'Lemnos Spreadable Ricotta', 155000, 18, 'Australia 250g', 'img/Lemnos Spreadable Ricotta.png', 3, 4),
	(8, 'Lyttos Halloumi Grilled Cheese', 175000, 14, 'Germany 250gr', 'img/Lyttos Halloumi Grilled Cheese.png', 3, 4),
	(9, 'President British Cheddar Mature', 130000, 13, 'UK 200gr', 'img/President British Cheddar Mature.png', 3, 4),
	(10, 'Almare Crispy fish fillet filled with cheese', 195000, 0, 'Germany - 400g', 'img/Almare Crispy fish fillet filled with cheese.png', 5, 4),
	(11, 'Almare Frozen Fish Sticks', 205000, 17, 'Germany 450gr', 'img/Almare Frozen Fish Sticks.png', 5, 4),
	(12, 'Barramundi Sustainable Seabass Fillets', 20000, 20, 'Australia 340gr', 'img/Barramundi Sustainable Seabass Fillets.jpg', 5, 4),
	(13, 'Iglo Backfish Filet Sticks', 190000, 18, 'Germany 405g', 'img/Iglo Backfish Filet Sticks.png', 5, 4),
	(14, 'Primana Wild Salmon Leek', 152000, 20, 'Italy 300g', 'img/Primana Wild Salmon Leek.png', 5, 4),
	(15, 'Daim Cup Ice Cream', 105000, 18, 'Germany 185ml', 'img/Daim Cup Ice Cream.png', 6, 4),
	(16, 'Häagen-Dazs Ice Cream Bar', 115000, 18, 'France 80ml', 'img/Häagen-Dazs Ice Cream Bar.png', 6, 4),
	(17, 'Mucci Funky American Cookie Double Trouble Ice Cream', 205000, 16, 'Germany 500ml', 'img/Mucci Funky American Cookie Double Trouble Ice Cream.png', 6, 4),
	(18, 'Oreo Cup Ice Cream', 105000, 17, 'Germany 185ml', 'img/Oreo Cup Ice Cream.png', 6, 4),
	(19, 'Strawberry Cake And Ice Cream', 280000, 46, 'Germany-600g', 'img/Strawberry Cake And Ice Cream.png', 6, 4),
	(20, 'Toblerone Ice Cream Cup', 105000, 14, 'Germany 185gr', 'img/Toblerone Ice Cream Cup.png', 6, 4),
	(21, 'Bahlsen ABC Russian Bread', 35000, 64, 'Germany - 100g', 'img/Bahlsen ABC Russian Bread.jpg', 9, 4),
	(22, 'Rice Cracker', 26000, 40, 'Thailand - 25g', 'img/Balance Bánh Gạo Hữu Cơ.png', 9, 4),
	(23, 'Soy Waffle', 80000, 40, 'Japan - 16pcs', 'img/Bourbon Bánh Xốp Sữa Đậu Nành.jpg', 9, 4),
	(24, 'Kettle Brand Potato Chips', 88000, 20, 'USA 142gr', 'img/Kettle Brand Potato Chips.jpg', 8, 4),
	(25, 'Lay’s Barbecue Chips', 92000, 13, 'USA 184.2gr', 'img/Lay’s Barbecue Chips.jpg', 8, 4),
	(26, 'Lay’s Classic Chips', 92000, 20, 'USA 184.2gr', 'img/Lay’s Classic Chips.jpg', 8, 4),
	(27, 'Lay’s Salt & Vinegar Chips', 92000, 17, 'USA 184gr', 'img/Lay’s Salt & Vinegar Chips.png', 8, 4),
	(28, 'Lorenz Crunchips Cheese & Onion', 40000, 15, 'Germany 100gr', 'img/Lorenz Crunchips Cheese & Onion.png', 8, 4),
	(29, 'Lorenz Crunchips Salted', 40000, 15, 'Germany 100gr', 'img/Lorenz Crunchips Salted.png', 8, 4),
	(30, 'Lorenz Paprika Crunchips', 40000, 15, 'Germany 100gr', 'img/Lorenz Paprika Crunchips.png', 8, 4),
	(32, 'LU Mini Cracker', 75000, 20, 'France 250g', 'img/LU Mini Cracker.jpg', 9, 4),
	(33, 'Lu Mini Crackers With Salt', 75000, 18, 'France 250g', 'img/Lu Mini Crackers With Salt.jpg', 9, 4),
	(34, 'Orgran Corn Crispibread', 96000, 18, 'Israel 125gr', 'img/Orgran Corn Crispibread.png', 9, 4),
	(35, 'Orgran Essential Rice Crispibread', 96000, 17, 'Australia 125gr', 'img/Orgran Essential Rice Crispibread.jpg', 9, 4),
	(36, 'Jack Link’s Teriyaki Beef Jerky', 68000, 35, 'New Zealand 25gr', 'img/Jack Link’s Teriyaki Beef Jerky.png', 10, 4),
	(37, 'Local Legends Biltong Beef Jerky', 110000, 20, 'Australia 60gr', 'img/Local Legends Biltong Beef Jerky.png', 10, 4),
	(38, 'Local Legends Original Beef Jerky', 120000, 20, 'Australia 75gr', 'img/Local Legends Original Beef Jerky.png', 10, 4),
	(39, 'Kirkland Wildflower Honey', 680000, 19, 'USA 2.27Kg', 'img/Kirkland Wildflower Honey.png', 12, 4),
	(40, 'Lapetite Longan Blossom Honey', 92000, 20, 'Vietnam 240g', 'img/Lapetite Longan Blossom Honey.png', 12, 4),
	(41, 'Smucker’s Red Raspberry Preserves', 130000, 16, 'USA 340gr', 'img/Smucker’s Red Raspberry Preserves.png', 13, 4),
	(42, 'Kirkland Syrup Organic Blue Agave', 265000, 234, 'USA 1.02Kg', 'img/Kirkland Syrup Organic Blue Agave.png', 14, 4),
	(43, 'Davidoff Prestige Espresso Capsules', 150000, 17, 'Germany 10pcs', 'img/Davidoff Prestige Espresso Capsules.png', 16, 4),
	(44, 'Mr. Viet Cappucino Instant Coffee', 109000, 20, 'Vietnam - 15 packs', 'img/Mr. Viet Cappucino Instant Coffee.jpg', 16, 4),
	(45, 'Nespresso Inspiration Genova Livanto', 194000, 22, 'Switzerland 10 cups', 'img/Nespresso Inspiration Genova Livanto.png', 16, 4),
	(46, 'Starbucks Nespresso Capsules', 260000, 20, 'Switzerland 10 capsules', 'img/Starbucks Nespresso Capsules.png', 16, 4),
	(47, 'Tchibo Exclusive Coffee', 142000, 34, 'Germany 100g', 'img/Tchibo Exclusive Coffee.png', 16, 4),
	(48, 'Ahmad Blueberry Brilliance Green Tea', 48000, 17, 'UK - 20bags x 2g', 'img/Ahmad Blueberry Brilliance Green Tea.jpg', 17, 4),
	(49, 'Ahmad Jasmine Green Tea', 65000, 13, 'USA 100gr', 'img/Ahmad Jasmine Green Tea.png', 17, 4),
	(50, 'Ahmad Vanilla Tranquility Black Tea', 48000, 19, 'USA 20 bags', 'img/Ahmad Vanilla Tranquility Black Tea.png', 17, 4),
	(51, 'Dilmah Special Loose Leaf Green Tea', 150000, 19, 'Australia 125gr', 'img/Dilmah Special Loose Leaf Green Tea.png', 17, 4),
	(52, 'Dm Bio Lavender & Verbena Tea', 110000, 18, 'Germany 40gr', 'img/Dm Bio Lavender & Verbena Tea.png', 17, 4),
	(53, 'Elephant Bio Relax Chamomile', 120000, 20, 'Netherlands 20 bags', 'img/Elephant Bio Relax Chamomile.png', 17, 4),
	(54, 'Gardens Ceylon Fruit Tea Selection 4', 220000, 20, 'Japan 100 bags', 'img/Gardens Ceylon Fruit Tea Selection 4.png', 17, 4),
	(55, 'Twinings Cinnamon Orange Tea', 160000, 0, 'UK 25 bags', 'img/Twinings Cinnamon Orange Tea.png', 17, 4),
	(56, 'Andros Frozen Cherry Puree', 380000, 26, 'Vietnam - 1kg', 'img/andros-frozen-cherry-puree.jpg', 18, 4),
	(57, 'Blueberries', 105000, 7, 'Vietnam – 125gr', 'img/blueberries.png', 18, 4),
	(58, 'Firestone Blackberry', 96000, 20, 'USA 300g', 'img/Firestone Blackberry.png', 18, 4),
	(59, 'Firestone Cranberry', 84000, 18, 'USA - 300g', 'img/firestone-franberry.png', 18, 4),
	(60, 'Germany Garlic Butter Baguette', 65000, 149, 'Germany 175g', 'img/Germany Garlic Butter Baguette.png', 19, 4),
	(61, 'Rosemary Kitchen Multi-Grain Bread', 45000, 16, 'Vietnam 300gr', 'img/Rosemary Kitchen Multi-Grain Bread.png', 19, 4),
	(62, 'Saint Honore White Bread Rolls', 15000, 17, 'Vietnam - 60gr x3', 'img/Saint Honore White Bread Rolls.png', 19, 4);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;

-- Dumping structure for table foodmart.status
DROP TABLE IF EXISTS `status`;
CREATE TABLE IF NOT EXISTS `status` (
  `code` int(11) NOT NULL AUTO_INCREMENT,
  `statusName` varchar(255) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- Dumping data for table foodmart.status: ~5 rows (approximately)
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` (`code`, `statusName`) VALUES
	(1, 'Pending'),
	(2, 'Confirmed'),
	(3, 'Cancelled'),
	(4, 'Enabled'),
	(5, 'Disable');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
