-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: pethealth
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `pethealth`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `pethealth` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `pethealth`;

--
-- Table structure for table `__efmigrationshistory`
--

DROP TABLE IF EXISTS `__efmigrationshistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `__efmigrationshistory` (
  `MigrationId` varchar(150) NOT NULL,
  `ProductVersion` varchar(32) NOT NULL,
  PRIMARY KEY (`MigrationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `__efmigrationshistory`
--

LOCK TABLES `__efmigrationshistory` WRITE;
/*!40000 ALTER TABLE `__efmigrationshistory` DISABLE KEYS */;
INSERT INTO `__efmigrationshistory` VALUES ('20261111024342_Inicial','8.0.2');
/*!40000 ALTER TABLE `__efmigrationshistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aplicacionesvacunas`
--

DROP TABLE IF EXISTS `aplicacionesvacunas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aplicacionesvacunas` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `FechaAplicacion` datetime(6) NOT NULL,
  `FechaRefuerzo` datetime(6) NOT NULL,
  `MascotaId` int(11) NOT NULL,
  `VacunaId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_AplicacionesVacunas_MascotaId` (`MascotaId`),
  KEY `IX_AplicacionesVacunas_VacunaId` (`VacunaId`),
  CONSTRAINT `FK_AplicacionesVacunas_Mascotas_MascotaId` FOREIGN KEY (`MascotaId`) REFERENCES `mascotas` (`Id`) ON DELETE CASCADE,
  CONSTRAINT `FK_AplicacionesVacunas_Vacunas_VacunaId` FOREIGN KEY (`VacunaId`) REFERENCES `vacunas` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aplicacionesvacunas`
--

LOCK TABLES `aplicacionesvacunas` WRITE;
/*!40000 ALTER TABLE `aplicacionesvacunas` DISABLE KEYS */;
INSERT INTO `aplicacionesvacunas` VALUES (1,'2026-11-11 00:00:00.000000','2026-11-25 00:00:00.000000',1,1),(3,'2026-11-17 00:00:00.000000','2026-11-24 00:00:00.000000',3,1),(4,'2026-11-17 00:00:00.000000','2026-11-30 00:00:00.000000',3,1),(5,'2026-11-17 00:00:00.000000','2026-11-24 00:00:00.000000',5,2),(7,'2026-11-17 00:00:00.000000','2026-11-19 00:00:00.000000',4,1);
/*!40000 ALTER TABLE `aplicacionesvacunas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consultas`
--

DROP TABLE IF EXISTS `consultas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `consultas` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Fecha` datetime(6) NOT NULL,
  `Motivo` longtext NOT NULL,
  `Tratamiento` longtext NOT NULL,
  `HistoriaClinicaId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_Consultas_HistoriaClinicaId` (`HistoriaClinicaId`),
  CONSTRAINT `FK_Consultas_HistoriasClinicas_HistoriaClinicaId` FOREIGN KEY (`HistoriaClinicaId`) REFERENCES `historiasclinicas` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consultas`
--

LOCK TABLES `consultas` WRITE;
/*!40000 ALTER TABLE `consultas` DISABLE KEYS */;
INSERT INTO `consultas` VALUES (1,'2026-11-11 09:51:44.592635','Vómito y decaimiento','Dieta blanda 3 días',1),(4,'2026-11-11 10:52:35.762214','Control de rutina','Desparasitación',1),(7,'2026-11-17 16:30:37.175395','Inflamación en encías','Limpieza dental y antiinflamatorio por 5 días',6);
/*!40000 ALTER TABLE `consultas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagnosticos`
--

DROP TABLE IF EXISTS `diagnosticos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diagnosticos` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descripcion` longtext NOT NULL,
  `ConsultaId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_Diagnosticos_ConsultaId` (`ConsultaId`),
  CONSTRAINT `FK_Diagnosticos_Consultas_ConsultaId` FOREIGN KEY (`ConsultaId`) REFERENCES `consultas` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagnosticos`
--

LOCK TABLES `diagnosticos` WRITE;
/*!40000 ALTER TABLE `diagnosticos` DISABLE KEYS */;
INSERT INTO `diagnosticos` VALUES (1,'Gastritis leve',1),(2,'Paciente sano',4),(5,'Gingivitis',7);
/*!40000 ALTER TABLE `diagnosticos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historiasclinicas`
--

DROP TABLE IF EXISTS `historiasclinicas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historiasclinicas` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `FechaApertura` datetime(6) NOT NULL,
  `MascotaId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `IX_HistoriasClinicas_MascotaId` (`MascotaId`),
  CONSTRAINT `FK_HistoriasClinicas_Mascotas_MascotaId` FOREIGN KEY (`MascotaId`) REFERENCES `mascotas` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historiasclinicas`
--

LOCK TABLES `historiasclinicas` WRITE;
/*!40000 ALTER TABLE `historiasclinicas` DISABLE KEYS */;
INSERT INTO `historiasclinicas` VALUES (1,'2026-11-11 09:50:33.570249',1),(2,'2026-11-17 13:05:20.405647',3),(3,'2026-11-17 13:21:58.374509',5),(4,'2026-11-17 13:26:32.730316',4),(5,'2026-11-17 16:25:10.746773',2),(6,'2026-11-17 16:29:44.661179',6);
/*!40000 ALTER TABLE `historiasclinicas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mascotas`
--

DROP TABLE IF EXISTS `mascotas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mascotas` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` longtext NOT NULL,
  `Especie` longtext NOT NULL,
  `Raza` longtext NOT NULL,
  `PropietarioId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IX_Mascotas_PropietarioId` (`PropietarioId`),
  CONSTRAINT `FK_Mascotas_Propietarios_PropietarioId` FOREIGN KEY (`PropietarioId`) REFERENCES `propietarios` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mascotas`
--

LOCK TABLES `mascotas` WRITE;
/*!40000 ALTER TABLE `mascotas` DISABLE KEYS */;
INSERT INTO `mascotas` VALUES (1,'Sacha','Perro','Cocker',1),(2,'Ahomi','Gato','Criollo',1),(3,'Gary','Gato','Espartan',1),(4,'Kity','Perro','Chihuahua',3),(5,'Rufus','Perro','San Bernardo',3),(6,'Yersi','Perro','Huski',4);
/*!40000 ALTER TABLE `mascotas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `propietarios`
--

DROP TABLE IF EXISTS `propietarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `propietarios` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Documento` longtext NOT NULL,
  `Nombre` longtext NOT NULL,
  `Telefono` longtext NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `propietarios`
--

LOCK TABLES `propietarios` WRITE;
/*!40000 ALTER TABLE `propietarios` DISABLE KEYS */;
INSERT INTO `propietarios` VALUES (1,'1012345678','Laura Gómez','3104567890'),(2,'1054274849','Yohana Rojas','3294649'),(3,'65644991','Laura Quintana','6265346'),(4,'018000235','Johana Morales','3185846858');
/*!40000 ALTER TABLE `propietarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` longtext NOT NULL,
  `Email` longtext NOT NULL,
  `PasswordHash` longtext NOT NULL,
  `Rol` int(11) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Admin','admin@pethealth.com','$2a$11$nqHefsLcw/FvhvO.zJb39u/yKhKpmDaLCQS7wqcz6bIBp9V.zQTpW',0),(2,'Veterinario','vet@pethealth.com','$2a$11$w9fmWafYiM7XenI4IEHVP.U1FIHVO3BHExtSh98PchPpixhEOeKW6',1),(3,'Auxiliar','aux@pethealth.com','$2a$11$ECUF1D2MNyNrfUlmH9YRnu3i8v1fxzieddOZ2I9DQt1m81XjMHzzW',2);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vacunas`
--

DROP TABLE IF EXISTS `vacunas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vacunas` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` longtext NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vacunas`
--

LOCK TABLES `vacunas` WRITE;
/*!40000 ALTER TABLE `vacunas` DISABLE KEYS */;
INSERT INTO `vacunas` VALUES (1,'Rabia'),(2,'Parvovirus');
/*!40000 ALTER TABLE `vacunas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-17 18:13:27
