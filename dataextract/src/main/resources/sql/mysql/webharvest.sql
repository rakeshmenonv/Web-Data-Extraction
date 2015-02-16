CREATE DATABASE  IF NOT EXISTS `webharvest` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `webharvest`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: webharvest
-- ------------------------------------------------------
-- Server version	5.0.37-community-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Not dumping tablespaces as no INFORMATION_SCHEMA.FILES table on this server
--

--
-- Table structure for table ` sys_area_city`
--

DROP TABLE IF EXISTS `sys_area_city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_area_city` (
  `id` int(11) NOT NULL auto_increment,
  `city_id` varchar(4) NOT NULL,
  `city` varchar(50) NOT NULL,
  `province_id` varchar(2) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ` sys_area_city`
--

LOCK TABLES `sys_area_city` WRITE;
/*!40000 ALTER TABLE ` sys_area_city` DISABLE KEYS */;
/*!40000 ALTER TABLE ` sys_area_city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ` sys_area_county`
--

DROP TABLE IF EXISTS `sys_area_county`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_area_county` (
  `id` int(11) NOT NULL auto_increment,
  `county_id` varchar(6) NOT NULL,
  `county` varchar(50) NOT NULL,
  `city_id` varchar(4) NOT NULL,
  `county_value` varchar(2) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ` sys_area_county`
--

LOCK TABLES `sys_area_county` WRITE;
/*!40000 ALTER TABLE ` sys_area_county` DISABLE KEYS */;
/*!40000 ALTER TABLE ` sys_area_county` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ` sys_area_province`
--

DROP TABLE IF EXISTS `sys_area_province`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_area_province` (
  `id` int(11) NOT NULL auto_increment,
  `province_id` varchar(2) NOT NULL,
  `province` varchar(50) NOT NULL,
  `province_for_short` varchar(20) NOT NULL,
  `province_for_bus_no` varchar(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ` sys_area_province`
--

LOCK TABLES `sys_area_province` WRITE;
/*!40000 ALTER TABLE ` sys_area_province` DISABLE KEYS */;
/*!40000 ALTER TABLE ` sys_area_province` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ` sys_attachment`
--

DROP TABLE IF EXISTS `sys_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_attachment` (
  `id` int(11) NOT NULL auto_increment,
  `file_name` varchar(500) NOT NULL,
  `file_path` varchar(500) NOT NULL,
  `rid` varchar(100) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ` sys_attachment`
--

LOCK TABLES `sys_attachment` WRITE;
/*!40000 ALTER TABLE ` sys_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE ` sys_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ` sys_organization`
--

DROP TABLE IF EXISTS `sys_organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_organization` (
  `id` int(11) NOT NULL auto_increment,
  `unit_code` varchar(100) default NULL,
  `unit_name` varchar(50) NOT NULL,
  `pid` int(11) default NULL,
  `area` char(10) default NULL,
  `remark` varchar(30) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ` sys_organization`
--

LOCK TABLES `sys_organization` WRITE;
/*!40000 ALTER TABLE ` sys_organization` DISABLE KEYS */;
/*!40000 ALTER TABLE ` sys_organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ` sys_parameter`
--

DROP TABLE IF EXISTS `sys_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_parameter` (
  `id` bigint(20) NOT NULL auto_increment,
  `uuid` varchar(255) NOT NULL default '',
  `category` varchar(255) default NULL,
  `subcategory` varchar(255) default NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `remark` varchar(255) default NULL,
  `short_name` varchar(255) default NULL,
  `sort` int(11) default NULL,
  `parent_id` int(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ` sys_parameter`
--

LOCK TABLES `sys_parameter` WRITE;
/*!40000 ALTER TABLE ` sys_parameter` DISABLE KEYS */;
/*!40000 ALTER TABLE ` sys_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ` sys_permission`
--

DROP TABLE IF EXISTS `sys_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_permission` (
  `id` bigint(20) NOT NULL auto_increment,
  `pid` bigint(20) NOT NULL,
  `ckey` varchar(255) NOT NULL,
  `pkey` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `permission_type` varchar(255) default NULL,
  `sort` int(11) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ` sys_permission`
--

LOCK TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE ` sys_permission` DISABLE KEYS */;
INSERT INTO `sys_permission` VALUES (1,0,'bc914d00-6837-492c-8f99-471b2176bdb4','0','ç³»ç»Ÿè®¾ç½®','menu:setting','2',0),(2,1,'d2274a49-74a6-4d0c-b7a8-0f16d125b4c0','bc914d00-6837-492c-8f99-471b2176bdb4','ç”¨æˆ·ç®¡ç†','menu:user','2',0),(3,1,'d271d733-4e57-4181-9f5f-730b53b4edc8','bc914d00-6837-492c-8f99-471b2176bdb4','è§’è‰²ç®¡ç†','menu:role','2',0),(4,1,'92c2b77c-6e77-4eaf-aa43-cfba4c3da583','bc914d00-6837-492c-8f99-471b2176bdb4','æƒé™ç®¡ç†','menu:permisson','2',0),(5,1,'c66f2469-ce0e-4e52-9d31-c9940df79ebe','bc914d00-6837-492c-8f99-471b2176bdb4','å‚æ•°ç®¡ç†','menu:parameter','2',0);
/*!40000 ALTER TABLE ` sys_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ` sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  `role_type` varchar(255) default NULL,
  `pid` bigint(20) default NULL,
  `remark` text,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ` sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE ` sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'è¶…çº§ç®¡ç†å‘˜','cb6e6022-955e-4978-ae2c-afa392be5ebf',0,'æ‹¥æœ‰æœ€é«˜æƒé™');
/*!40000 ALTER TABLE ` sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ` sys_role_permission`
--

DROP TABLE IF EXISTS `sys_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_permission` (
  `role_id` bigint(20) NOT NULL,
  `permission` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ` sys_role_permission`
--

LOCK TABLES `sys_role_permission` WRITE;
/*!40000 ALTER TABLE ` sys_role_permission` DISABLE KEYS */;
INSERT INTO `sys_role_permission` VALUES (1,'menu:parameter'),(1,'menu:permisson'),(1,'menu:role'),(1,'menu:user'),(1,'menu:setting'),(1,'menu:setting'),(1,'menu:setting');
/*!40000 ALTER TABLE ` sys_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ` sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL auto_increment,
  `login_name` varchar(18) NOT NULL,
  `name` varchar(200) default NULL,
  `password` varchar(100) default NULL,
  `salt` varchar(64) default NULL,
  `area` varchar(10) default NULL,
  `user_type` int(11) NOT NULL,
  `register_date` varchar(100) default NULL,
  `user_status` int(11) default '0',
  `theme` varchar(100) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ` sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE ` sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','è¶…çº§ç®¡ç†å‘˜','af27c8816506bf1b6705b50866f9cde0a67401cb','eb69619a75232f52','371401',0,'0',0,'bootstrap'),(2,'user','user','fc4b2d83355003aa9649ddcb47a38a4f957636e4','cba97144a760fd39',NULL,0,NULL,0,'bootstrap');
/*!40000 ALTER TABLE ` sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table ` sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `user_id_index` (`user_id`),
  KEY `role_id_index` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table ` sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE ` sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1);
/*!40000 ALTER TABLE ` sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `page_data_info`
--

DROP TABLE IF EXISTS `page_data_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `page_data_info` (
  `id` bigint(20) NOT NULL,
  `page_url_info_id` bigint(20) NOT NULL,
  `table_group_key` varchar(20) default NULL,
  `row_group_key` varchar(20) default NULL,
  `content` text,
  `type` varchar(20) default NULL,
  `extracted_time` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `page_data_info`
--

LOCK TABLES `page_data_info` WRITE;
/*!40000 ALTER TABLE `page_data_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `page_data_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `page_url_info`
--

DROP TABLE IF EXISTS `page_url_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `page_url_info` (
  `id` bigint(20) NOT NULL auto_increment,
  `url` text,
  `element` varchar(50) default NULL,
  `attribute` varchar(50) default NULL,
  `value` varchar(50) default NULL,
  `extracted_time` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `page_url_info`
--

LOCK TABLES `page_url_info` WRITE;
/*!40000 ALTER TABLE `page_url_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `page_url_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-02-16 14:54:43
