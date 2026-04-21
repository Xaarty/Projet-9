# notes-service

## Description

Microservice responsable de la gestion des notes médicales.

Stockage dans MongoDB.

---

##  Stack technique

- Java 17
- Spring Boot 3.2.5
- Spring Data MongoDB
- MongoDB

---

## Port

8083

---

## Base de données

- Type : MongoDB
- Base : `medilabo-notes`

---

## Endpoints principaux

| Méthode | Endpoint                     | Description              |
|--------|------------------------------|--------------------------|
| GET    | /notes/patient/{id}          | Notes d’un patient       |
| POST   | /notes                       | Création                 |
| GET    | /notes/{id}                  | Détail note              |
| DELETE | /notes/{id}                  | Suppression              |

---

## Lancement en local

mvn spring-boot:run