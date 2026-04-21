# assessment-service

## Description

Microservice responsable du calcul du risque de diabète.

Il consomme :
- patient-service
- notes-service

---

##  Stack technique

- Java 17
- Spring Boot 3.2.5

---

## Port

8084

---

## Dépendances

- http://patient-service:8081
- http://notes-service:8083

---

## Endpoint principal

| Méthode | Endpoint                | Description              |
|--------|-------------------------|--------------------------|
| GET    | /assessment/{id}        | Calcul du risque         |

---

## Logique

Le calcul est basé sur :
- l’âge
- le sexe
- le nombre de déclencheurs dans les notes

---

## Lancement en local

mvn spring-boot:run