# postit2

## Description

Postit2 is a back-end program for the forum-like frontend, which has user account management and authentication, post management, and comment management. This project is under microservices architecture and aims at decomposing connection between entities to achieve higher scalability.

## Tech Stack

* Java
* Spring boot
* PostgreSQL
* Docker
* RabbitMQ
* Flyway
* Eureka
* Zuul
* Pivotal Cloud Foundry (In progress)

## Design

#### System Architecture

![System Architecture](https://github.com/mingjingtang/postit2/blob/dev/assets/postit2_system_architecture.png)

#### Database

![Database ERD](https://github.com/mingjingtang/postit2/blob/dev/assets/database_ERD.png)

#### Data Intercommunication
![Data intercommunication diagram](https://github.com/mingjingtang/postit2/blob/dev/assets/data_intercommunication.png)

## Agile Practice and Extreme Programming

* User Stories : [Pivotal Tracker](https://www.pivotaltracker.com/n/projects/2416899)

* Continuous Integration and early deliverable

* Pair programming

## Wins

* Entity Relationship is logically separate

* RabbitMQ to use messaging mechanism instead of Http Req/Response mechanism

## Challenges

* Long starting up for docker-compose

* Data intercommunication

* 504

* CORS (multiple headers)


## Contributor

* Mingjing Tang

* Qiming Chen
