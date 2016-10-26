# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: philiplaine.com (MySQL 5.7.15-0ubuntu0.16.04.1)
# Database: backend
# Generation Time: 2016-10-26 15:57:02 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table Game
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Game`;

CREATE TABLE `Game` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player1` int(11) unsigned NOT NULL,
  `player2` int(11) unsigned NOT NULL,
  `type` int(11) NOT NULL,
  `state` int(11) NOT NULL DEFAULT '0',
  `player1_score` int(11) NOT NULL DEFAULT '0',
  `player2_score` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `Game` WRITE;
/*!40000 ALTER TABLE `Game` DISABLE KEYS */;

INSERT INTO `Game` (`id`, `player1`, `player2`, `type`, `state`, `player1_score`, `player2_score`)
VALUES
	(69,1,70,2,0,0,0),
	(70,71,1,1,0,0,0),
	(71,72,73,1,3,16,14),
	(72,73,72,1,3,16,16),
	(73,73,72,1,3,16,16),
	(74,73,70,1,0,0,0),
	(75,73,72,1,3,14,15),
	(76,73,72,1,3,16,13),
	(77,73,72,1,3,14,16),
	(78,72,71,1,0,0,0),
	(79,72,73,1,1,0,0),
	(80,73,72,1,3,16,16),
	(81,72,73,1,1,0,0),
	(82,73,72,1,2,12,0),
	(83,73,72,1,2,7,0),
	(84,73,1,1,0,0,0),
	(85,73,72,1,2,15,0),
	(86,73,72,1,2,15,0),
	(87,74,72,1,2,15,15),
	(88,75,76,1,3,15,15),
	(89,77,72,1,0,0,0),
	(90,77,76,1,3,16,15),
	(91,77,72,1,0,0,0),
	(92,77,76,1,3,14,16),
	(93,78,76,1,0,0,0);

/*!40000 ALTER TABLE `Game` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Memory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Memory`;

CREATE TABLE `Memory` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `image` int(11) unsigned NOT NULL DEFAULT '0',
  `text` varchar(200) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `Memory` WRITE;
/*!40000 ALTER TABLE `Memory` DISABLE KEYS */;

INSERT INTO `Memory` (`id`, `image`, `text`)
VALUES
	(12,1,'Bil'),
	(13,2,'Melon'),
	(14,3,'Äpple'),
	(15,31,'Dansa'),
	(16,32,'Hoppa'),
	(17,33,'Ko'),
	(18,34,'Klättra'),
	(19,35,'Krama'),
	(20,36,'Måla'),
	(21,37,'Prata'),
	(22,38,'Simma'),
	(23,39,'Hand'),
	(24,40,'Skriva'),
	(25,41,'Gitarr'),
	(26,42,'Fotboll'),
	(27,43,'Springa'),
	(28,62,'Sjunga'),
	(29,63,'Badkar'),
	(30,64,'Pengar'),
	(31,65,'Bebis'),
	(32,66,'Cykel'),
	(33,67,'Natur'),
	(34,68,'Kök'),
	(35,69,'Duscha'),
	(36,70,'Fiska'),
	(37,71,'Kasta'),
	(38,72,'Grilla'),
	(39,73,'Blommor'),
	(40,74,'Hörlurar'),
	(41,75,'Brevlåda'),
	(42,76,'Träd'),
	(43,77,'Regn'),
	(44,79,'Rita'),
	(45,80,'Ropa'),
	(46,81,'Skrika'),
	(47,82,'Matte'),
	(48,83,'Dator'),
	(49,84,'Mat'),
	(50,86,'Snö'),
	(51,87,'Schack'),
	(52,88,'Sand'),
	(53,89,'Katt'),
	(54,90,'Tvätta');

/*!40000 ALTER TABLE `Memory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Rounds
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Rounds`;

CREATE TABLE `Rounds` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `game_id` int(11) NOT NULL,
  `round_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `Rounds` WRITE;
/*!40000 ALTER TABLE `Rounds` DISABLE KEYS */;

INSERT INTO `Rounds` (`id`, `game_id`, `round_id`)
VALUES
	(1,69,34),
	(2,69,53),
	(3,69,15),
	(4,69,42),
	(5,69,52),
	(6,69,23),
	(8,70,25),
	(9,70,55),
	(10,70,34),
	(11,70,52),
	(15,71,48),
	(16,71,20),
	(17,71,24),
	(18,71,21),
	(22,72,35),
	(23,72,21),
	(24,72,20),
	(25,72,41),
	(29,73,34),
	(30,73,43),
	(31,73,14),
	(32,73,45),
	(36,74,33),
	(37,74,38),
	(38,74,24),
	(39,74,17),
	(43,75,52),
	(44,75,49),
	(45,75,20),
	(46,75,21),
	(50,76,49),
	(51,76,13),
	(52,76,18),
	(53,76,47),
	(57,77,54),
	(58,77,10),
	(59,77,17),
	(60,77,55),
	(64,78,23),
	(65,78,54),
	(66,78,14),
	(67,78,18),
	(71,79,13),
	(72,79,46),
	(73,79,27),
	(74,79,14),
	(78,80,13),
	(79,80,16),
	(80,80,32),
	(81,80,49),
	(85,81,33),
	(86,81,53),
	(87,81,29),
	(88,81,24),
	(92,82,24),
	(93,82,35),
	(94,82,11),
	(95,82,38),
	(99,83,55),
	(100,83,30),
	(101,83,34),
	(102,83,36),
	(106,84,20),
	(107,84,45),
	(108,84,16),
	(109,84,14),
	(113,85,10),
	(114,85,43),
	(115,85,19),
	(116,85,53),
	(120,86,20),
	(121,86,47),
	(122,86,29),
	(123,86,34),
	(127,87,13),
	(128,87,14),
	(129,87,34),
	(130,87,25),
	(134,88,12),
	(135,88,42),
	(136,88,28),
	(137,88,21),
	(141,89,32),
	(142,89,55),
	(143,89,19),
	(144,89,24),
	(148,90,44),
	(149,90,35),
	(150,90,32),
	(151,90,21),
	(155,91,41),
	(156,91,12),
	(157,91,27),
	(158,91,33),
	(162,92,16),
	(163,92,18),
	(164,92,32),
	(165,92,45),
	(169,93,34),
	(170,93,55),
	(171,93,40),
	(172,93,48);

/*!40000 ALTER TABLE `Rounds` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Sentence
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Sentence`;

CREATE TABLE `Sentence` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `text` varchar(300) NOT NULL DEFAULT '',
  `answer_a` varchar(11) NOT NULL DEFAULT '' COMMENT 'Allways correct answer',
  `answer_b` varchar(11) NOT NULL DEFAULT '',
  `answer_c` varchar(11) NOT NULL DEFAULT '',
  `answer_d` varchar(11) NOT NULL DEFAULT '',
  `image` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `Sentence` WRITE;
/*!40000 ALTER TABLE `Sentence` DISABLE KEYS */;

INSERT INTO `Sentence` (`id`, `text`, `answer_a`, `answer_b`, `answer_c`, `answer_d`, `image`)
VALUES
	(10,'Kvinnan * bil','kör','äter','springer','ser',1),
	(11,'Flickan * melon','äter','skriker','spelar','åker',2),
	(12,'Hon * i äpplet','biter','boll','röd','hoppar',3),
	(13,'Dem * tillsammans','dansar','målar','trollar','sitter',31),
	(14,'Hon * på stranden','hoppar','tittar','pratar','sover',32),
	(15,'Hon * kon','klappar','badar','heter','knuffar',33),
	(16,'Barnet * i trädet','klättrar','dansar','sjunger','kör',34),
	(17,'Dem * varandra','kramar','heter','spelar','målar',35),
	(18,'Han * en bild','målar','bygger','knuffar','lägger',36),
	(19,'Dem * med varandra','pratar','springer','hoppar','ritar',37),
	(20,'Han * i bassängen','simmar','skjuter','trollar','kittlas',38),
	(21,'Dem * hand','skakar','ritar','lägger','sitter',39),
	(22,'Hon * i boken','skriver','hoppar','pratar','ligger',40),
	(23,'Han * guitar','spelar','rider','kastar','räknar',41),
	(24,'Dem * fotboll','spelar','ritar','bygger','talar',42),
	(25,'Han * längs gatan','springer','kör','simmar','sitter',43),
	(26,'Killen * inför provet','studerar','katt','hoppar','orange',61),
	(27,'Tjejen * högt','sjunger','målar','blå','siffra',62),
	(28,'Pojken * i karet','badar','kvinna','gul','fisk',63),
	(29,'Han * med pengar','betalar','bil','ruta','solsken',64),
	(30,'Barnet *','gråter','elefant','dator','snabb',65),
	(32,'Flickan * på ängen','dansar','vit','katt','skola',67),
	(33,'Killen *','diskar','springer','ishockey','sport',68),
	(34,'Personen * för att bli ren','duschar','ler','fisk','simhall',69),
	(35,'Mannen * vid sjön','fiskar','väder','hjul','grå',70),
	(36,'Mannen * bollen','kastar','svänga','slå','randig',71),
	(38,'Kvinnan * på en blomma','luktar','eluttag','målar','röd',73),
	(39,'Tjejen * på musik','lyssnar','tittar','upp','leende',74),
	(40,'Personen * ett brev','postar','dricker','handlar','lila',75),
	(41,'Personer * i parken','promenerar','vatten','räknar','markis',76),
	(42,'Det * utomhus','regnar','brinner','tomt','afton',77),
	(43,'Pojken * en tavla','målar','vänster','krattar','hund',78),
	(44,'Ett barn * en bild','ritar','grön','sjunger','tiger',79),
	(45,'Killen *','ropar','sov','tavla','blad',80),
	(46,'Pojken *','skriker','regnar','skiner','turkos',81),
	(47,'Eleven * matte','räknar','sängkläder','pizza','varm',82),
	(48,'Pojkarna * vid datorn','samarbetar','kall','blöt','musik',83),
	(49,'Kvinnan * mat','serverar','tråkig','trött','äter',84),
	(50,'Pojken * i poolen','simmar','letar','bokstäver','vatten',85),
	(51,'Det * utomhus','snöar','väder','element','kemi',86),
	(52,'Killarna * schack','spelar','fysik','stöt','kol',87),
	(53,'Pojkarna * i sandlådan','leker','köper','kläder','brun',88),
	(54,'Katten * sött','sover','skriver','byrå','gul',89),
	(55,'Mannen * sin bil','tvättar','kartong','köpa','virus',90);

/*!40000 ALTER TABLE `Sentence` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table User
# ------------------------------------------------------------

DROP TABLE IF EXISTS `User`;

CREATE TABLE `User` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(15) NOT NULL DEFAULT '',
  `score` int(11) unsigned NOT NULL DEFAULT '0',
  `password` varchar(40) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;

INSERT INTO `User` (`id`, `username`, `score`, `password`)
VALUES
	(1,'phillebaba',2323,'5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8'),
	(70,'testusername',0,'aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d'),
	(71,'fred',0,'8418a59a686d05fd4936e5161deff693cb50319d'),
	(72,'William',0,'7c4a8d09ca3762af61e59520943dc26494f8941b'),
	(73,'fred2',0,'919ceb0b11496e90ece17aca191e921bab5ddedf'),
	(74,'kohage',0,'ecdff4eaaee8fa954568f16831f246b0b13f5061'),
	(75,'Test',0,'7c4a8d09ca3762af61e59520943dc26494f8941b'),
	(76,'Kalle',0,'7c4a8d09ca3762af61e59520943dc26494f8941b'),
	(77,'jml',0,'7c4a8d09ca3762af61e59520943dc26494f8941b'),
	(78,'apa',0,'92429d82a41e930486c6de5ebda9602d55c39986');

/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
