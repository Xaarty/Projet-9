# patient-service

## Description

Microservice responsable de la gestion des patients.

Il expose une API REST permettant :
- la création de patients
- la mise à jour
- la suppression
- la recherche

---

##  Stack technique

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- MySQL
- Hibernate

---

## Port

-8081


## Base de données

- Type : MySQL
- Nom : `medilabo`

## Configuration

yaml

spring:
  datasource:
    url: jdbc:mysql://patient-db:3306/medilabo
    username: medilabo
    password: medilabo

## Endpoints principaux

| Méthode | Endpoint         | Description         |
|---------|------------------|---------------------|
| GET     | /patients        | Liste des patients  |
| GET     | /patients/{id}   | Détail d’un patient |
| POST    | /patients        | Création            |
| PUT     | /patients/{id}   | Mise à jour         |
| DELETE  | /patients/{id}   | Suppression         |
| GET     | /patients/search | Recherche par nom   |


## Lancement en locaL

mvn spring-boot:run