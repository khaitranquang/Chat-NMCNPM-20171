-- MySQL dump 10.13  Distrib 5.7.19, for Win64 (x86_64)
--
-- Host: localhost    Database: cnpmchat
-- ------------------------------------------------------
-- Server version	5.7.19-log

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
-- Table structure for table `icon`
--

DROP TABLE IF EXISTS `icon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `icon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `url` (`url`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `icon`
--

LOCK TABLES `icon` WRITE;
/*!40000 ALTER TABLE `icon` DISABLE KEYS */;
INSERT INTO `icon` VALUES (1,'http://localhost:8080/BTLCNPM/image/dog.png'),(4,'http://localhost:8080/BTLCNPM/image/dogbua.jpg'),(2,'http://localhost:8080/BTLCNPM/image/dogchaitoc.jpg'),(5,'http://localhost:8080/BTLCNPM/image/dogcry.jpg'),(3,'http://localhost:8080/BTLCNPM/image/gif.gif'),(6,'http://localhost:8080/BTLCNPM/image/muggy.jpg'),(7,'http://localhost:8080/BTLCNPM/image/muggyhun.jpg');
/*!40000 ALTER TABLE `icon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(2000) NOT NULL,
  `maxquota` int(11) NOT NULL,
  `logourl` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,'IT','Thảo luận, trao đổi về các vấn đề trong ngành CNTT',100,'http://localhost:8080/BTLCNPM/logo/bk-logo.png'),(2,'Tâm sự sinh viên','Nơi các sinh viên vui vẻ tụ tập',100,'http://localhost:8080/BTLCNPM/logo/sharingan.jpg'),(3,'NEU','Gái xinh NEU',100,'http://localhost:8080/BTLCNPM/logo/kt-logo.png');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_icon_xref`
--

DROP TABLE IF EXISTS `room_icon_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room_icon_xref` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roomid` int(11) NOT NULL,
  `iconid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `roomid` (`roomid`),
  KEY `iconid` (`iconid`),
  CONSTRAINT `room_icon_xref_ibfk_1` FOREIGN KEY (`roomid`) REFERENCES `room` (`id`),
  CONSTRAINT `room_icon_xref_ibfk_2` FOREIGN KEY (`iconid`) REFERENCES `icon` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_icon_xref`
--

LOCK TABLES `room_icon_xref` WRITE;
/*!40000 ALTER TABLE `room_icon_xref` DISABLE KEYS */;
INSERT INTO `room_icon_xref` VALUES (1,1,1),(2,1,2),(3,1,3),(4,2,2),(5,1,4),(6,2,4),(7,3,4),(8,1,5),(9,2,5),(10,3,5),(11,1,6),(12,2,6);
/*!40000 ALTER TABLE `room_icon_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `fullname` varchar(255) NOT NULL,
  `age` int(11) NOT NULL,
  `password` varchar(255) NOT NULL,
  `avatarurl` varchar(255) NOT NULL,
  `role` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'khaiprovipshow','Nguyễn Bá Khải',18,'a2hhaWxpbmgxOTk3','http://localhost:8080/BTLCNPM/avatar/khaiprovipshowgif.gif',0),(2,'khai199733','Ano',18,'a2hhaWxpbmgxOTk3','http://localhost:8080/BTLCNPM/avatar/khai199733muggy.jpg',0),(4,'hadinhkhoe','Hà Đình Khỏe',20,'a2hhaWxpbmgxOTk3','http://localhost:8080/BTLCNPM//logo/bk-logo.png',0),(8,'tranvanan','Trần Văn An',20,'a2hhaWxpbmgxOTk3','http://localhost:8080/BTLCNPM/avatar/tranvanangif.gif',0),(9,'tranquangkhai','Trần Quang Khải',20,'a2hhaWxpbmgxOTk3','http://localhost:8080/BTLCNPM//logo/bk-logo.png',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-14 21:28:49
