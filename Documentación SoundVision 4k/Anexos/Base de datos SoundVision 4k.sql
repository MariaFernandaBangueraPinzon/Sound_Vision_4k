/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.5.5-10.1.30-MariaDB : Database - proyectoreserva
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`proyectoreserva` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `proyectoreserva`;

/*Table structure for table `ambienteesp` */

DROP TABLE IF EXISTS `ambienteesp`;

CREATE TABLE `ambienteesp` (
  `codigoAmbEsp` int(11) NOT NULL AUTO_INCREMENT,
  `nombreAmbEsp` varchar(15) NOT NULL,
  `administradorAmbEsp` varchar(10) NOT NULL,
  PRIMARY KEY (`codigoAmbEsp`),
  KEY `administradorAmbEsp` (`administradorAmbEsp`),
  CONSTRAINT `ambienteesp_ibfk_1` FOREIGN KEY (`administradorAmbEsp`) REFERENCES `usuarioreser` (`cedulaUsu`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `ambienteesp` */

insert  into `ambienteesp`(`codigoAmbEsp`,`nombreAmbEsp`,`administradorAmbEsp`) values (1,'Audiovisual 1','1234567890'),(2,'Audiovisual 2','1234567890'),(3,'Auditorio','1094975459'),(5,'jajaj','1234567890');

/*Table structure for table `informe` */

DROP TABLE IF EXISTS `informe`;

CREATE TABLE `informe` (
  `nis` int(11) NOT NULL AUTO_INCREMENT,
  `tipoInforme` int(2) NOT NULL,
  `destinatario` varchar(10) NOT NULL,
  `destino` varchar(10) NOT NULL,
  `descripcion` varchar(50) NOT NULL,
  `reserva` int(11) NOT NULL,
  PRIMARY KEY (`nis`),
  KEY `tipoInforme` (`tipoInforme`),
  KEY `destinatario` (`destinatario`),
  KEY `destino` (`destino`),
  KEY `reserva` (`reserva`),
  CONSTRAINT `informe_ibfk_1` FOREIGN KEY (`tipoInforme`) REFERENCES `tipoinforme` (`nisInformeTipo`),
  CONSTRAINT `informe_ibfk_2` FOREIGN KEY (`destinatario`) REFERENCES `usuarioreser` (`cedulaUsu`),
  CONSTRAINT `informe_ibfk_3` FOREIGN KEY (`destino`) REFERENCES `usuarioreser` (`cedulaUsu`),
  CONSTRAINT `reserva` FOREIGN KEY (`reserva`) REFERENCES `solicitudreserva` (`codReser`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `informe` */

insert  into `informe`(`nis`,`tipoInforme`,`destinatario`,`destino`,`descripcion`,`reserva`) values (1,1,'1094975459','1234567890','fkjjeñlgjrl',1),(2,1,'12345','1234567890','1lkasnd',1),(3,1,'12345','1234567890','jajajajajajajaj :V',1);

/*Table structure for table `inventario` */

DROP TABLE IF EXISTS `inventario`;

CREATE TABLE `inventario` (
  `codInventario` int(10) NOT NULL AUTO_INCREMENT,
  `objeto` varchar(20) NOT NULL,
  `cantidad` int(6) NOT NULL,
  `ambiente` int(2) NOT NULL,
  PRIMARY KEY (`codInventario`),
  KEY `ambiente` (`ambiente`),
  CONSTRAINT `inventario_ibfk_1` FOREIGN KEY (`ambiente`) REFERENCES `ambienteesp` (`codigoAmbEsp`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=latin1;

/*Data for the table `inventario` */

insert  into `inventario`(`codInventario`,`objeto`,`cantidad`,`ambiente`) values (34,'mesas',122,2);

/*Table structure for table `reservaamb` */

DROP TABLE IF EXISTS `reservaamb`;

CREATE TABLE `reservaamb` (
  `codReser` int(11) NOT NULL AUTO_INCREMENT,
  `usuarioReser` varchar(10) NOT NULL,
  `fechaReser` date NOT NULL,
  `horaLlegadaReser` time NOT NULL,
  `motivoReser` varchar(60) NOT NULL,
  `ambienteReser` int(2) NOT NULL,
  `horaSalidaReser` time NOT NULL,
  PRIMARY KEY (`codReser`),
  KEY `usuarioReser` (`usuarioReser`),
  KEY `ambienteReser` (`ambienteReser`),
  CONSTRAINT `reservaamb_ibfk_1` FOREIGN KEY (`usuarioReser`) REFERENCES `usuarioreser` (`cedulaUsu`),
  CONSTRAINT `reservaamb_ibfk_2` FOREIGN KEY (`ambienteReser`) REFERENCES `ambienteesp` (`codigoAmbEsp`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `reservaamb` */

insert  into `reservaamb`(`codReser`,`usuarioReser`,`fechaReser`,`horaLlegadaReser`,`motivoReser`,`ambienteReser`,`horaSalidaReser`) values (1,'123','2018-02-28','06:00:00','dcgsjkfws',1,'07:00:00');

/*Table structure for table `solicitudreserva` */

DROP TABLE IF EXISTS `solicitudreserva`;

CREATE TABLE `solicitudreserva` (
  `codReser` int(11) NOT NULL AUTO_INCREMENT,
  `usuarioReser` varchar(10) NOT NULL,
  `fechaReser` date NOT NULL,
  `horaLlegadaReser` time NOT NULL,
  `motivoReser` varchar(60) NOT NULL,
  `ambienteReser` int(2) NOT NULL,
  `horaSalidaReser` time NOT NULL,
  PRIMARY KEY (`codReser`),
  KEY `usuarioReser` (`usuarioReser`),
  KEY `ambienteReser` (`ambienteReser`),
  CONSTRAINT `solicitudreserva_ibfk_1` FOREIGN KEY (`usuarioReser`) REFERENCES `usuarioreser` (`cedulaUsu`),
  CONSTRAINT `solicitudreserva_ibfk_2` FOREIGN KEY (`ambienteReser`) REFERENCES `ambienteesp` (`codigoAmbEsp`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `solicitudreserva` */

insert  into `solicitudreserva`(`codReser`,`usuarioReser`,`fechaReser`,`horaLlegadaReser`,`motivoReser`,`ambienteReser`,`horaSalidaReser`) values (1,'1094975459','2018-02-19','03:00:00','gfhjyu',1,'04:00:00');

/*Table structure for table `tiporeserva` */

DROP TABLE IF EXISTS `tiporeserva`;

CREATE TABLE `tiporeserva` (
  `codReser` int(1) NOT NULL,
  `nombre` varchar(15) NOT NULL,
  PRIMARY KEY (`codReser`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `tiporeserva` */

/*Table structure for table `usuarioreser` */

DROP TABLE IF EXISTS `usuarioreser`;

CREATE TABLE `usuarioreser` (
  `cedulaUsu` varchar(10) NOT NULL,
  `nombreUsu` varchar(70) NOT NULL,
  `telUsu` varchar(11) DEFAULT NULL,
  `rolUsu` varchar(15) NOT NULL,
  `contraseniaUsu` varchar(100) DEFAULT NULL,
  `emailUsu` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`cedulaUsu`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `usuarioreser` */

insert  into `usuarioreser`(`cedulaUsu`,`nombreUsu`,`telUsu`,`rolUsu`,`contraseniaUsu`,`emailUsu`) values ('1094975459','Juan carlos','3128298018','Instructor','301','juan@gmail.com'),('11111','Hugo Hernan','3012788005','Mesa De Ayuda','12','Hugo@gmail.com'),('123','Karem','7372978','Administrador','123','karem@gmail.com'),('1234','Juan Diego','13','Instructor','12','juandiego@gmail.com'),('12345','Jaison Quiroga','3142355','Instructor','12345','sonvisiosos@hotma.com'),('1234567890','Palomino','7985421','Administrador','123','palomino@gmail.com'),('12399','Erika','5482145','Administrador','1234','erika@gmail.com'),('9910190874','Cristian David Henao ','30121212','Instructor','12','CdHenao@gmail.com');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

/*Table structure for table `horarioreser` */

DROP TABLE IF EXISTS `horarioreser`;

CREATE TABLE `horarioreser` (
  `codigo` INT NOT NULL AUTO_INCREMENT,
  `reserva` INT NOT NULL,
  `hora` TIME NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `reserva` (`reserva`),
  CONSTRAINT `horarioreser_ibfk_1` FOREIGN KEY (`reserva`) REFERENCES `reservaamb` (`codReser`)
 ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Table structure for table `solicitudreserva` */

/*Table structure for table `horariosolicitud` */

DROP TABLE IF EXISTS `horariosolicitud`;

CREATE TABLE `horariosolicitud` (
  `codigo` INT NOT NULL AUTO_INCREMENT,
  `reserva` INT NOT NULL,
  `hora` TIME NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `reserva` (`reserva`),
  CONSTRAINT `horariosolicitud_ibfk_1` FOREIGN KEY (`reserva`) REFERENCES `solicitudreserva` (`codReser`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=latin1;

/*Table structure for table `tipoinforme` */
