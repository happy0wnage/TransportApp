-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: localhost    Database: transport_system
-- ------------------------------------------------------
-- Server version	5.6.24-log

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
-- Table structure for table `arc`
--

DROP TABLE IF EXISTS `arc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `arc` (
  `id_arc` int(11) NOT NULL AUTO_INCREMENT,
  `id_station_from` int(11) NOT NULL,
  `id_station_to` int(11) NOT NULL,
  `travel_time` time NOT NULL,
  PRIMARY KEY (`id_arc`),
  KEY `fk_Arc_Station1_idx` (`id_station_from`),
  KEY `fk_Arc_Station2_idx` (`id_station_to`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `arc`
--

LOCK TABLES `arc` WRITE;
/*!40000 ALTER TABLE `arc` DISABLE KEYS */;
INSERT INTO `arc` VALUES (1,1,2,'00:03:20'),(2,2,3,'00:02:30'),(3,3,4,'00:02:20'),(4,4,5,'00:02:10'),(5,3,6,'00:01:55'),(6,6,7,'00:02:05'),(7,7,8,'00:02:35'),(8,8,9,'00:02:40'),(9,7,10,'00:02:30'),(10,10,11,'00:02:10'),(11,11,12,'00:02:50'),(12,12,1,'00:01:55');
/*!40000 ALTER TABLE `arc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bus`
--

DROP TABLE IF EXISTS `bus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bus` (
  `id_bus` int(11) NOT NULL AUTO_INCREMENT,
  `id_route` int(11) NOT NULL,
  `seat` int(11) NOT NULL,
  PRIMARY KEY (`id_bus`),
  KEY `fk_Bus_Route1_idx` (`id_route`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus`
--

LOCK TABLES `bus` WRITE;
/*!40000 ALTER TABLE `bus` DISABLE KEYS */;
INSERT INTO `bus` VALUES (1,1,25),(2,1,30),(3,1,22),(4,1,24),(5,2,20),(6,2,25),(7,2,30),(8,3,25),(9,3,28),(10,3,30),(11,3,32);
/*!40000 ALTER TABLE `bus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `results`
--

DROP TABLE IF EXISTS `results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `results` (
  `id_results` int(11) NOT NULL AUTO_INCREMENT,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `loading_percent` double NOT NULL,
  `routes` varchar(120) NOT NULL,
  `satisfied_count` int(11) NOT NULL,
  `dissatisfied_count` int(11) NOT NULL,
  PRIMARY KEY (`id_results`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `results`
--

LOCK TABLES `results` WRITE;
/*!40000 ALTER TABLE `results` DISABLE KEYS */;
INSERT INTO `results` VALUES (1,'2016-01-10 23:57:56','2016-01-10 23:57:58',40,'212, 272, 279 ',3534,6540),(2,'2016-01-11 00:07:34','2016-01-11 00:07:36',39,'212, 272, 279 ',3564,6502),(3,'2016-01-11 00:07:45','2016-01-11 00:08:19',39,'212, 272, 279 ',3559,6508),(4,'2016-01-11 00:10:15','2016-01-11 00:10:16',39,'212, 272, 279 ',3553,6500),(5,'2016-01-11 00:10:42','2016-01-11 00:10:43',39,'212, 272, 279 ',3557,6472);
/*!40000 ALTER TABLE `results` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `route`
--

DROP TABLE IF EXISTS `route`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `route` (
  `id_route` int(11) NOT NULL AUTO_INCREMENT,
  `id_start_station` int(11) NOT NULL,
  `id_end_station` int(11) DEFAULT NULL,
  `routing_number` varchar(120) NOT NULL,
  `price` double NOT NULL,
  `depot_stop_time` time NOT NULL,
  `last_bus_time` time NOT NULL,
  `first_bus_time` time NOT NULL,
  PRIMARY KEY (`id_route`),
  KEY `fk_Route_Station_idx` (`id_start_station`),
  KEY `fk_Route_Station1_idx` (`id_end_station`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `route`
--

LOCK TABLES `route` WRITE;
/*!40000 ALTER TABLE `route` DISABLE KEYS */;
INSERT INTO `route` VALUES (1,1,5,'212',3.5,'00:10:00','21:30:00','07:45:00'),(2,1,9,'272',5.5,'00:13:00','21:30:00','07:50:00'),(3,1,1,'279',6,'00:15:00','21:30:00','07:55:00');
/*!40000 ALTER TABLE `route` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `route_arc`
--

DROP TABLE IF EXISTS `route_arc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `route_arc` (
  `id_route` int(11) NOT NULL,
  `id_arc` int(11) NOT NULL,
  PRIMARY KEY (`id_route`,`id_arc`),
  KEY `fk_Route_has_Arc_Arc1_idx` (`id_arc`),
  KEY `fk_Route_has_Arc_Route1_idx` (`id_route`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `route_arc`
--

LOCK TABLES `route_arc` WRITE;
/*!40000 ALTER TABLE `route_arc` DISABLE KEYS */;
INSERT INTO `route_arc` VALUES (1,1),(1,2),(1,3),(1,4),(2,1),(2,2),(2,5),(2,6),(2,7),(2,8),(3,1),(3,2),(3,5),(3,6),(3,9),(3,10),(3,11),(3,12);
/*!40000 ALTER TABLE `route_arc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `station`
--

DROP TABLE IF EXISTS `station`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `station` (
  `id_station` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(120) NOT NULL,
  `stop_time` time NOT NULL,
  PRIMARY KEY (`id_station`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `station`
--

LOCK TABLES `station` WRITE;
/*!40000 ALTER TABLE `station` DISABLE KEYS */;
INSERT INTO `station` VALUES (1,'Svetlaya','00:15:00'),(2,'Traktorostroiteley','00:00:45'),(3,'Shironincev','00:00:35'),(4,'Silpo','00:00:45'),(5,'Heroiv Truda','00:10:45'),(6,'607 Micro-n','00:00:35'),(7,'Blukhera','00:00:40'),(8,'Posad','00:00:36'),(9,'Studencheskaya','00:15:00'),(10,'Mehanizatorskaya','00:01:15'),(11,'Garibaldi','00:01:05'),(12,'Shkola','00:01:00');
/*!40000 ALTER TABLE `station` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-01-11  3:11:47
