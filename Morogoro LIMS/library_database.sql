-- phpMyAdmin SQL Dump
-- version 4.4.15.8
-- https://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 22, 2018 at 07:07 AM
-- Server version: 5.6.31
-- PHP Version: 5.5.38

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `library_database`
--

-- --------------------------------------------------------

--
-- Table structure for table `author`
--

CREATE TABLE IF NOT EXISTS `author` (
  `id` int(11) NOT NULL,
  `firstname` varchar(100) NOT NULL,
  `middlename` varchar(100) DEFAULT NULL,
  `lastname` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE IF NOT EXISTS `book` (
  `id` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `category_id` int(11) DEFAULT NULL,
  `edition` varchar(50) DEFAULT NULL,
  `publisher_id` int(11) DEFAULT NULL,
  `isbn` bigint(20) DEFAULT NULL,
  `copies` int(11) DEFAULT NULL,
  `reference` enum('0','1') NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `book_author`
--

CREATE TABLE IF NOT EXISTS `book_author` (
  `id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `author_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `book_status`
--

CREATE TABLE IF NOT EXISTS `book_status` (
  `id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `available` enum('0','1') NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE IF NOT EXISTS `category` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `lending`
--

CREATE TABLE IF NOT EXISTS `lending` (
  `id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `member_reg_number` varchar(50) NOT NULL,
  `librarian_id` int(11) DEFAULT NULL,
  `lend_date` date NOT NULL,
  `return_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `librarian`
--

CREATE TABLE IF NOT EXISTS `librarian` (
  `id` int(11) NOT NULL,
  `reg_number` varchar(20) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `middlename` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `postal_addr` varchar(100) DEFAULT NULL,
  `phone1` varchar(15) NOT NULL,
  `phone2` varchar(15) DEFAULT NULL,
  `street` varchar(100) DEFAULT NULL,
  `region` varchar(15) DEFAULT 'Morogoro',
  `password` varchar(50) NOT NULL,
  `status` enum('0','1') NOT NULL DEFAULT '0',
  `photo` blob
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `member`
--

CREATE TABLE IF NOT EXISTS `member` (
  `id` int(11) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `middlename` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `postal_addr` varchar(100) NOT NULL,
  `phone1` varchar(15) NOT NULL,
  `phone2` varchar(15) DEFAULT NULL,
  `type_of_id` varchar(100) NOT NULL,
  `id_number` varchar(50) NOT NULL,
  `street` varchar(100) NOT NULL,
  `region` varchar(50) NOT NULL DEFAULT 'Morogoro',
  `status` enum('0','1') NOT NULL DEFAULT '0',
  `photo` blob
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `member_adult`
--

CREATE TABLE IF NOT EXISTS `member_adult` (
  `member_id` int(11) NOT NULL,
  `house_number` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `member_primary`
--

CREATE TABLE IF NOT EXISTS `member_primary` (
  `member_id` int(11) NOT NULL,
  `school_address` varchar(100) NOT NULL,
  `school_name` varchar(100) NOT NULL,
  `class` enum('I','II','III','IV','V','VI','VII') NOT NULL,
  `p_firstname` varchar(50) NOT NULL,
  `p_middlename` varchar(50) NOT NULL,
  `p_lastname` varchar(50) NOT NULL,
  `p_photo` blob
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `member_sec`
--

CREATE TABLE IF NOT EXISTS `member_sec` (
  `member_id` int(11) NOT NULL,
  `school_post_address` varchar(100) NOT NULL,
  `school_name` varchar(100) NOT NULL,
  `class` enum('I','II','III','IV','V','VI') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `publisher`
--

CREATE TABLE IF NOT EXISTS `publisher` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `reference_lend`
--

CREATE TABLE IF NOT EXISTS `reference_lend` (
  `id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `member_id` varchar(50) NOT NULL,
  `librarian_id` int(11) DEFAULT NULL,
  `lend_time` datetime NOT NULL,
  `return_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `registered`
--

CREATE TABLE IF NOT EXISTS `registered` (
  `id` int(11) NOT NULL,
  `member_id` int(11) NOT NULL,
  `registration_number` varchar(20) NOT NULL,
  `librarian_id` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `registration`
--

CREATE TABLE IF NOT EXISTS `registration` (
  `id` int(11) NOT NULL,
  `member_reg_number` varchar(50) NOT NULL,
  `receipt` varchar(50) NOT NULL,
  `reg_date` date NOT NULL,
  `end_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `renew`
--

CREATE TABLE IF NOT EXISTS `renew` (
  `id` int(11) NOT NULL,
  `member_reg_number` varchar(50) NOT NULL,
  `librarian_id` int(11) DEFAULT NULL,
  `renew_date` date NOT NULL,
  `end_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `returning`
--

CREATE TABLE IF NOT EXISTS `returning` (
  `id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `member_reg_number` varchar(50) NOT NULL,
  `librarian_id` int(11) DEFAULT NULL,
  `lend_date` date NOT NULL,
  `return_date` date NOT NULL,
  `late_ontime` enum('0','1') NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sponsor`
--

CREATE TABLE IF NOT EXISTS `sponsor` (
  `id` int(11) NOT NULL,
  `member_reg_number` varchar(40) NOT NULL,
  `office_name` varchar(100) NOT NULL,
  `sponsor_fname` varchar(50) NOT NULL,
  `sponsor_lname` varchar(50) NOT NULL,
  `title` varchar(50) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `author`
--
ALTER TABLE `author`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `fk_book_category_1` (`category_id`),
  ADD KEY `fk_book_publisher_1` (`publisher_id`);

--
-- Indexes for table `book_author`
--
ALTER TABLE `book_author`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_bookauthor_book` (`book_id`),
  ADD KEY `fk_bookauthor_author_1` (`author_id`);

--
-- Indexes for table `book_status`
--
ALTER TABLE `book_status`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_bookstatus_book_1` (`book_id`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `lending`
--
ALTER TABLE `lending`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_lending_librarian_1` (`librarian_id`),
  ADD KEY `fk_lending_book_1` (`book_id`),
  ADD KEY `fk_lending_regnumber_1` (`member_reg_number`);

--
-- Indexes for table `librarian`
--
ALTER TABLE `librarian`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `reg_number` (`reg_number`),
  ADD KEY `reg_number_2` (`reg_number`);

--
-- Indexes for table `member`
--
ALTER TABLE `member`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `member_adult`
--
ALTER TABLE `member_adult`
  ADD PRIMARY KEY (`member_id`),
  ADD KEY `member_id` (`member_id`);

--
-- Indexes for table `member_primary`
--
ALTER TABLE `member_primary`
  ADD PRIMARY KEY (`member_id`),
  ADD KEY `member_id` (`member_id`);

--
-- Indexes for table `member_sec`
--
ALTER TABLE `member_sec`
  ADD PRIMARY KEY (`member_id`),
  ADD UNIQUE KEY `reg_number` (`member_id`),
  ADD KEY `member_id` (`member_id`);

--
-- Indexes for table `publisher`
--
ALTER TABLE `publisher`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reference_lend`
--
ALTER TABLE `reference_lend`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_referencelend_book_1` (`book_id`),
  ADD KEY `fk_referencelend_regnumber_1` (`member_id`);

--
-- Indexes for table `registered`
--
ALTER TABLE `registered`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `member_id` (`member_id`),
  ADD UNIQUE KEY `registration_number` (`registration_number`),
  ADD KEY `librarian_id` (`librarian_id`);

--
-- Indexes for table `registration`
--
ALTER TABLE `registration`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_registration_regnumber_1` (`member_reg_number`) USING BTREE;

--
-- Indexes for table `renew`
--
ALTER TABLE `renew`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_renew_regnumber_1` (`member_reg_number`),
  ADD KEY `fk_renew_librarian_1` (`librarian_id`);

--
-- Indexes for table `returning`
--
ALTER TABLE `returning`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_returning_book_1` (`book_id`) USING BTREE,
  ADD KEY `fk_returning_librarian_1` (`librarian_id`) USING BTREE,
  ADD KEY `fk_returning_regnumber_1` (`member_reg_number`) USING BTREE;

--
-- Indexes for table `sponsor`
--
ALTER TABLE `sponsor`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_sponsor_regnumber_1` (`member_reg_number`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `author`
--
ALTER TABLE `author`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `book`
--
ALTER TABLE `book`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `book_author`
--
ALTER TABLE `book_author`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `book_status`
--
ALTER TABLE `book_status`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `lending`
--
ALTER TABLE `lending`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `librarian`
--
ALTER TABLE `librarian`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `member`
--
ALTER TABLE `member`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `publisher`
--
ALTER TABLE `publisher`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `reference_lend`
--
ALTER TABLE `reference_lend`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `registered`
--
ALTER TABLE `registered`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `registration`
--
ALTER TABLE `registration`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `renew`
--
ALTER TABLE `renew`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `returning`
--
ALTER TABLE `returning`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `sponsor`
--
ALTER TABLE `sponsor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `fk_book_category_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_book_publisher_1` FOREIGN KEY (`publisher_id`) REFERENCES `publisher` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `book_author`
--
ALTER TABLE `book_author`
  ADD CONSTRAINT `fk_bookauthor_author_1` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_bookauthor_book_1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `book_status`
--
ALTER TABLE `book_status`
  ADD CONSTRAINT `fk_bookstatus_book_1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `lending`
--
ALTER TABLE `lending`
  ADD CONSTRAINT `fk_lending_book_1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_lending_librarian_1` FOREIGN KEY (`librarian_id`) REFERENCES `librarian` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `lending_ibfk_1` FOREIGN KEY (`member_reg_number`) REFERENCES `registered` (`registration_number`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `member_adult`
--
ALTER TABLE `member_adult`
  ADD CONSTRAINT `member_adult_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `member_primary`
--
ALTER TABLE `member_primary`
  ADD CONSTRAINT `member_primary_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `member_sec`
--
ALTER TABLE `member_sec`
  ADD CONSTRAINT `member_sec_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `reference_lend`
--
ALTER TABLE `reference_lend`
  ADD CONSTRAINT `fk_referencelend_book_1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `registered`
--
ALTER TABLE `registered`
  ADD CONSTRAINT `registered_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `registered_ibfk_2` FOREIGN KEY (`librarian_id`) REFERENCES `librarian` (`reg_number`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `registration`
--
ALTER TABLE `registration`
  ADD CONSTRAINT `registration_ibfk_1` FOREIGN KEY (`member_reg_number`) REFERENCES `registered` (`registration_number`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `renew`
--
ALTER TABLE `renew`
  ADD CONSTRAINT `fk_renew_librarian_1` FOREIGN KEY (`librarian_id`) REFERENCES `librarian` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `renew_ibfk_1` FOREIGN KEY (`member_reg_number`) REFERENCES `registered` (`registration_number`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `returning`
--
ALTER TABLE `returning`
  ADD CONSTRAINT `fk_returning_book_1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_returning_librarian_1` FOREIGN KEY (`librarian_id`) REFERENCES `librarian` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `returning_ibfk_1` FOREIGN KEY (`member_reg_number`) REFERENCES `registered` (`registration_number`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `sponsor`
--
ALTER TABLE `sponsor`
  ADD CONSTRAINT `sponsor_ibfk_1` FOREIGN KEY (`member_reg_number`) REFERENCES `registered` (`registration_number`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
