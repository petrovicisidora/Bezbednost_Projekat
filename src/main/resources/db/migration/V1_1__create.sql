DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `skill`;
DROP TABLE IF EXISTS `korisnik_skills`;
DROP TABLE IF EXISTS `document`;
DROP TABLE IF EXISTS `korisnik_document`;
DROP TABLE IF EXISTS `employee_in_project`;
DROP TABLE IF EXISTS `projekat`;
DROP TABLE IF EXISTS `notification`;



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
DROP TABLE IF EXISTS `korisnik`;
DROP TABLE IF EXISTS `projekat`;
DROP TABLE IF EXISTS `employee_in_project`;

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
    `status` varchar(255) NOT NULL,
    `blocked` BOOLEAN DEFAULT FALSE,
     token BINARY(16),
     expiration_date DATETIME,
     is_used TINYINT(1),
    PRIMARY KEY (`id`)
);

INSERT INTO `korisnik` (`id`, `email`, `password`, `first_name`, `last_name`, `address`, `city`, `state`, `phone_number`, `job_title`, `status`, `blocked`, `token`, `expiration_date`, `is_used` ) VALUES

    ('1', 'admin@gmail.com', '123', 'Admin', 'Admin', 'Admin', 'Admin', 'fesef','111',
     'ADMIN', 'APPROVED', 0, X'0123456789ABCDEF0123456789ABCDEF', '2023-05-31 12:00:00', 1);

CREATE TABLE `projekat` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    `start_date` date NOT NULL,
    `end_date` date NOT NULL,
    PRIMARY KEY (`id`)
);


INSERT INTO `projekat` (`id`, `name`, `start_date`, `end_date`) VALUES ('1', 'Java Project', '2023-01-01', '2023-06-01');


CREATE TABLE `employee_in_project` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `worker_id` bigint(20) NOT NULL,
    `project_id` bigint(20) NOT NULL,
    `job_description` varchar(255) DEFAULT NULL,
    `job_start_time` date DEFAULT NULL,
    `job_end_time` date DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_worker_id` FOREIGN KEY (`worker_id`) REFERENCES `korisnik` (`id`),
    CONSTRAINT `fk_project_id` FOREIGN KEY (`project_id`) REFERENCES `projekat` (`id`)
);

INSERT INTO employee_in_project (worker_id, project_id, job_description, job_start_time, job_end_time)
VALUES (1, 1, 'Opis posla 1', '2023-01-01', '2023-12-31');


CREATE TABLE `skill` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(191) NOT NULL,
    `level` int NOT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO `skill` (`id`, `name`, `level`) VALUES
    ('1', 'Java', '3'),
    ('2', 'Python', '4'),
    ('3', 'SQL', '2');

CREATE TABLE `korisnik_skills` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT,
      `korisnik_id` bigint(20) NOT NULL,
      `skill_id` bigint(20) NOT NULL,
      PRIMARY KEY (`id`),
      CONSTRAINT `fk_korisnik_id` FOREIGN KEY (`korisnik_id`) REFERENCES `korisnik` (`id`),
      CONSTRAINT `fk_skill_id` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`)
);

INSERT INTO `korisnik_skills` (`id`, `korisnik_id`, `skill_id`) VALUES
    ('1', '1', '1');

CREATE TABLE `document` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    `doc` longblob NOT NULL,
     PRIMARY KEY (`id`)
);



CREATE TABLE `korisnik_document` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT,
      `korisnik_id` bigint(20) NOT NULL,
      `document_id` bigint(20) NOT NULL,
      PRIMARY KEY (`id`),
      CONSTRAINT `fk_korisnik_document_korisnik` FOREIGN KEY (`korisnik_id`) REFERENCES `korisnik` (`id`),
      CONSTRAINT `fk_korisnik_document_document` FOREIGN KEY (`document_id`) REFERENCES `document` (`id`)
);

CREATE TABLE `notification` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                        `message` varchar(255) NOT NULL,
                                        `email` varchar(255) NOT NULL,
                                        `time` datetime NOT NULL,
                                        `count` int NOT NULL,
                                        `critical` BOOLEAN DEFAULT FALSE,

                                        PRIMARY KEY (`id`)
);


