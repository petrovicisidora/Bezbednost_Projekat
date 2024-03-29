DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_created` datetime NOT NULL,
  `date_deleted` datetime DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) NOT NULL,
  `deleted` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
);


INSERT INTO `user` (`id`, `email`, `password`, `first_name`, `last_name`, `role`, `deleted`, `date_created`, `date_deleted`, `date_updated`) VALUES

('1', 'test1@gmail.com', '$2a$10$gDqfRHdeFeiopzJ3MSNiLOdYsTDU/RJWhTr5OJvGHE3355C5OLDDm', 'ADMIN', 'ADMIN', 'ADMIN', 0, '2011-11-11 00:00:00','2011-11-11 00:00:00',
'2011-11-11 00:00:00');

CREATE TABLE `korisnik` (
                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                      `email` varchar(255) NOT NULL,
                      `password` varchar(255) NOT NULL,
                      `first_name` varchar(255) NOT NULL,
                      `last_name` varchar(255) NOT NULL,
                      `address` varchar(255) NOT NULL,
                      `city` varchar(255) NOT NULL,
                      `state` varchar(255) NOT NULL,
                      `phone_number` varchar(255) NOT NULL,
                      `job_title` varchar(255) NOT NULL,
                      PRIMARY KEY (`id`)
);
INSERT INTO `korisnik` (`id`, `email`, `password`, `first_name`, `last_name`, `address`, `city`, `state`, `phone_number`, `job_title`) VALUES

    ('1', 'test1@gmail.com', 'sjvh', 'akdbh', 'ekbh', 'efw', 'efw', 'fesef','03151',
     'fekwh');


CREATE TABLE `cert` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_created` datetime NOT NULL,
  `date_deleted` datetime DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `issuer_name` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `serial_number` varchar(255) NOT NULL,
  `start_date` varchar(255) DEFAULT NULL,
  `end_date` varchar(255) NOT NULL,
  `cert` varchar(3000) NOT NULL,
  `cert_type` varchar(3000) NOT NULL,
  `deleted` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `alarm` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_created` datetime NOT NULL,
  `date_deleted` datetime DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `date` varchar(255) NOT NULL,
  `alarm` varchar(255) NOT NULL,
  `agent` varchar(255) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
);