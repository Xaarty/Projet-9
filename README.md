# 🩺 Medilabo Solutions

## 🧠 Présentation

Medilabo Solutions est une application basée sur une architecture microservices permettant d’évaluer le risque de diabète de type 2 à partir des données patients et des notes médicales.
Les médecins auront la possibilité d'ajouter des patients, de mettre à jour leurs informations personnelles.
De plus, ils auront la possibilité d'ajouter et modifier des notes liées aux rendez-vous médicaux.
Une détection de mots-clés permet d'identifier le risque de diabète du patient en fonction de l'âge et du sexe.


### Services

| Service            | Port | Description                            |
|--------------------|------|----------------------------------------|
| gateway-service    | 8080 | Point d’entrée de l’application        |
| patient-service    | 8081 | Gestion des patients (CRUD, recherche) |
| font-service       | 8082 | Page du site et style                  |
| notes-service      | 8083 | Gestion des notes médicales (MongoDB)  |
| assessment-service | 8084 | Calcul du risque de diabète            |

---

## ⚙️ Prérequis

- Docker
- Docker Compose

---

## 🚀 Installation

### 1. Cloner le projet

Dans le bash : 

git clone https://github.com/Xaarty/Projet-9.git
cd medilabo


### 2. Lancer L'application

docker compose up --build

La commande permet :
le build tous les services
de démarrer MySQL + MongoDB
pour lancer l’application complète

### 3. Lien du site 

Rendez-vous sur : http://localhost:8082/login


### 4. Tester le jeu de test 

Exécuter les jeux de test pour tester le fonctionnement : 

-Création de patients : 
docker exec -i patient-db mysql -u medilabo -pmedilabo medilabo < init/patients-init.sql
                                            ou
Get-Content .\untitled\init\patient-init.sql | docker exec -i patient-db mysql -u medilabo -pmedilabo medilabo


-Création de notes : 

Get-Content .\untitled\init\note-init.js | docker exec -i mongo mongosh medilabo-notes



### 5. Suppression données de test

Une fois les données de test vérifiées, réinitialiser :

docker compose down -v
docker compose up --build