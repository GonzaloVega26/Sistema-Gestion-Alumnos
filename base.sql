CREATE DATABASE  IF NOT EXISTS sga_vega;
USE sga_vega;

/* Se dropean todas las tablas primero*/
DROP TABLE IF EXISTS `cursado`;
DROP TABLE IF EXISTS `materia`;
DROP TABLE IF EXISTS `profesor`;
DROP TABLE IF EXISTS `alumno`;
DROP TABLE IF EXISTS `inscripcion`;
DROP TABLE IF EXISTS `carrera`;

CREATE TABLE `carrera` (
  `car_cod` int NOT NULL,
  `car_nombre` varchar(45) DEFAULT NULL,
  `car_duracion` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`car_cod`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* Insertando valores a Carrera */
INSERT INTO `carrera` VALUES (32,'Ciencias De La Computacion','5 Años'),(323,'Ingenieria Civil','5 Años'),(546,'Licenciatura en Higiene y Seguridad','2 Años'),(5464,'Licenciatura en Enología','2 Años'),(35464,'Tec. Superior en Programación','2 Años');

CREATE TABLE `inscripcion` (
  `insc_cod` int NOT NULL,
  `insc_nombre` varchar(45) DEFAULT NULL,
  `insc_fecha` date DEFAULT NULL,
  `insc_car_cod` int DEFAULT NULL,
  PRIMARY KEY (`insc_cod`),
  KEY `FK_carrera_idx` (`insc_car_cod`),
  CONSTRAINT `FK_carrera` FOREIGN KEY (`insc_car_cod`) REFERENCES `carrera` (`car_cod`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `inscripcion` VALUES (1123,'Joseba Infante','2020-06-12',5464),(4432,'Rut Romera','2020-06-12',32);

CREATE TABLE `profesor` (
  `prof_dni` int NOT NULL,
  `prof_nombre` varchar(45) DEFAULT NULL,
  `prof_apellido` varchar(45) DEFAULT NULL,
  `prof_fec_nac` varchar(45) DEFAULT NULL,
  `prof_domicilio` varchar(45) DEFAULT NULL,
  `prof_telefono` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`prof_dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `profesor` VALUES (123456789,'Maximino','De La Fuente','2020-06-17','Neuquen','+235854732'),(987654321,'Geronimo','Chen','2020-06-17','Neuquen','+235854732'),(456789123,'Ernesto','Vera','2020-06-17','Neuquen','+235854732'),(321654987,'Eloi','Arjona','2020-06-17','Neuquen','+235854732'),(789123456,'Marcos','Dionisio','2020-06-17','Neuquen','+235854732');

CREATE TABLE `materia` (
  `mat_cod` int NOT NULL AUTO_INCREMENT,
  `mat_nombre` varchar(45) DEFAULT NULL,
  `mat_prof_dni` int DEFAULT NULL,
  `cant_horas` int,
  PRIMARY KEY (`mat_cod`),
  KEY `FK_profesor_idx` (`mat_prof_dni`),
  CONSTRAINT `FK_profesor` FOREIGN KEY (`mat_prof_dni`) REFERENCES `profesor` (`prof_dni`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO materia (mat_cod, mat_nombre, mat_prof_dni, cant_horas)
VALUES 
  (1, 'Matemáticas', 123456789, 5),
  (2, 'Física', 987654321, 4),
  (3, 'Química', 456789123, 3),
  (4, 'Historia', 789123456, 2),
  (5, 'Literatura', 321654987, 6);

CREATE TABLE `alumno` (
  `alu_dni` int NOT NULL,
  `alu_nombre` varchar(45) DEFAULT NULL,
  `alu_apellido` varchar(45) DEFAULT NULL,
  `alu_fec_nac` date DEFAULT NULL,
  `alu_domicilio` varchar(45) DEFAULT NULL,
  `alu_telefono` varchar(45) DEFAULT NULL,
  `alu_insc_cod` int DEFAULT NULL,
  PRIMARY KEY (`alu_dni`),
  UNIQUE KEY `alu_dni_UNIQUE` (`alu_dni`),
  KEY `FK_inscripcion_idx` (`alu_insc_cod`),
  CONSTRAINT `FK_inscripcion` FOREIGN KEY (`alu_insc_cod`) REFERENCES `inscripcion` (`insc_cod`) ON DELETE CASCADE ON UPDATE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* Insertando valores a Alumno */
INSERT INTO `alumno` VALUES (9564857,'Rut','Romera','2012-06-21','San Juan','+2647564657',4432),(13457345,'Carmelo','Del Rio','2012-06-21','San Juan','+2647564657',NULL),(43678645,'Joseba','Infante','2020-06-11','Mendoza','+2617564657',1123),(45678345,'Maria','Freire','2020-06-11','Mendoza','+2617564657',NULL),(65678345,'Jaume','Polo','2020-06-11','Mendoza','+2617564657',NULL),(78465756,'Erika','Poveda','2020-06-11','Mendoza','+2617564657',NULL),(84293483,'Amadeo','Romera','2012-06-21','San Juan','+2647564657',NULL),(85351098,'Geronimo','Del Castillo','2012-06-21','San Juan','+2647564657',NULL),(85647564,'Iñigo','Barbera','2020-06-11','Mendoza','+2617564657',NULL),(94536574,'Nicolasa','Padilla','2012-06-21','Mendoza','+2617564657',NULL);

CREATE TABLE cursado (
    cur_alu_dni int NOT NULL,
    cur_mat_cod int NOT NULL,
    cur_nota DOUBLE DEFAULT NULL,
    PRIMARY KEY (cur_alu_dni, cur_mat_cod),
    FOREIGN KEY (cur_alu_dni) REFERENCES alumno(alu_dni) ON DELETE CASCADE,
    FOREIGN KEY (cur_mat_cod) REFERENCES materia(mat_cod) ON DELETE CASCADE

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `cursado`(cur_alu_dni,cur_mat_cod,cur_nota) VALUES (13457345,1,8),(13457345,2,10),(45678345,1,8),(85647564,2,10),
(45678345,2,8),(85647564,3,10),
(85647564,4,8),(84293483,4,10);



