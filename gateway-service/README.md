# gateway-service

## Description

Point d’entrée de l’application.

Routage des requêtes vers les microservices.

---

## ⚙Stack technique

- Java 17
- Spring Boot 3.2.5
- Spring Cloud Gateway

---

## Port

8080

---

## Routes

yaml

routes:
  - id: patient-service
    uri: http://patient-service:8081
    predicates:
      - Path=/patients/**

  - id: notes-service
    uri: http://notes-service:8083
    predicates:
      - Path=/notes/**


## Lancement

mvn spring-boot:run
