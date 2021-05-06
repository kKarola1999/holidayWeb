-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

create database holiday;
use holiday;







-- -----------------------------------------------------
-- Table `mydb`.`Pracownicy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `holiday`.`Pracownicy` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `imie_nazwisko` VARCHAR(45) NULL,
  `staz` INT NULL,
  `dodatkowe_dni` INT NULL,
  `etat` ENUM('1', '3/4', '2/3', '1/2', '1/4') NULL,
  `password` VARCHAR(45) NOT NULL,
  `email` VARCHAR(60) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Urlopy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `holiday`.`Urlopy` (
  `idUrlopy` INT NOT NULL AUTO_INCREMENT,
  `start_urlopu` DATETIME NULL,
  `end_urlopu` DATETIME NULL,
  `akceptacja` boolean NULL,
  `Pracownicy_id` INT NOT NULL,
  `email` VARCHAR(60) NOT NULL,
  
  PRIMARY KEY (`idUrlopy`, `Pracownicy_id`),
  INDEX `fk_Urlopy_Pracownicy_idx` (`Pracownicy_id` ASC) VISIBLE,
  CONSTRAINT `fk_Urlopy_Pracownicy`
    FOREIGN KEY (`Pracownicy_id`)
    REFERENCES `holiday`.`Pracownicy` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

insert into Urlopy values (1,'2020-01-02','2020-01-03',true,1,'karoli@wp.pl');
insert into Pracownicy values(1,'hahala',5,4,1,'elo','karoli@wp.pl');
select*from urlopy  ;
update Urlopy set akceptacja=false where idUrlopy=1;

CREATE USER 'karoli@wp.pl'@'localhost' IDENTIFIED BY 'elo'; 
GRANT SELECT ON `Urlopy` TO 'karoli@wp.pl'@'localhost';
GRANT SELECT ON `Pracownicy` TO 'karoli@wp.pl'@'localhost';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
