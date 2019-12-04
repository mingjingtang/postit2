# postit2

## Description

Postit2 is a back-end program for the forum-like frontend, which has user account management and authentication, post management, and comment management. 

This project is to decompose a monolith spring back-end to work under microservices architecture and aims at decoupling relationship between services to achieve higher scalability.

##### Links:

[Pivotal Tracker](https://www.pivotaltracker.com/n/projects/2416899)

[Swagger API doc](https://postit2.cfapps.io/swagger-ui.html)

[Demo APP](https://qimingchen.github.io/S1-Project-1.io/src/html/index.html)

## Tech Stack

#### Development

* Java EE
* Spring boot
* PostgreSQL
* Docker 
* RabbitMQ
* Flyway
* Eureka
* Zuul
* Swagger API docs

#### Testing (unit tests and integration tests)

* Springboot-test
* Mockito
* H2 database

#### DevOps

###### CICD - Continuous Integration / Delivery

* Jenkins
* Blue Ocean Pipeline
* Jacoco Coverage Reporting

###### Logging and Monitoring

* Logstash
* ElasticSearch
* Kibana

#### Deployment

* Pivotal Cloud Foundry
  * CloudAMQP (RabbitMQ)
  * ElephantSQL (PostgreSQL)
  * Service Registry (Eureka-Server)

## Design

#### System Architecture

![System Architecture](https://github.com/mingjingtang/postit2/blob/master/assets/postit2_system_architecture.png)

#### Database

![Database ERD](https://github.com/mingjingtang/postit2/blob/master/assets/database_ERD.png)

#### Data Intercommunication
![Data intercommunication diagram](https://github.com/mingjingtang/postit2/blob/master/assets/data_intercommunication.png)

#### DevOps
![DevOps diagram](https://github.com/mingjingtang/postit2/blob/master/assets/devops.png)

## Agile Practice and Extreme Programming

* User Stories : [Pivotal Tracker](https://www.pivotaltracker.com/n/projects/2416899)
  * We break down features into stories
  * We use tasks to track the progress

* Continuous Integration and early deliverable

* Pair programming
  * We worked together as extreme programming, planned before taking action, verified our thoughts by drawing architecture diagrams and pipeline diagrams.
  * We reviewed the code along the way, enhancing the quality of code.


## Wins and challenges

* Entity Relationship between microservices is separate
  * We discussed and compared the trade-offs between different designs, and agreed to focus on high scalability. So the database is logically separate, the application layer taks the responsibility to maintain the entity relationship. Also, this requires us to use HTTP request/response or messaging to transfer data between services.
  
* Data intercommunication
  * Since the entities are unlinked, so how to withdraw the correct data and compose them together is a challenge for us. We made lots of graphs to help verify our thoughts.

* RabbitMQ to use messaging mechanism
  * Messaging queue needs extra setup, while docker simplifies the setup and zuul can discover the service with little configuration
  * We use jackson to convert the object to JSON string for communication
  * RabbitMQ use AMQP(Advanced Message Queuing Protocol), AMQP provides us with the capabilities to easily add in load balancing, and high availability, with guaranteed message deliveries.

* Long starting up for docker-compose
  * At the beginning, the starting up time occupied a lot of our development time. we tried the maven go-offline solution shared by our classmate Davis Allen to speed up the starting up time.

* 504
  * Our application has a cold start problem. Although the eureka shows the registeration of the service, the first run always got 504. We use a zuul/hystrix/ribbon setting for the timeout, which solved this problem

* CORS (multiple headers)
  * During the integration, after setting up the CORS on SecurityConfig, we still have a CORS problem, but it is about multiple headers, we used a zuul setting to solved this problem.

* Project file structure clear and easy to understand
  * We Separate each function and file by its own purpose, so that it's easy to let for us and other developer to understand.

* Enable security in Api-gateway
  * Enabling security in the api-gateway increases the security level

* Debugging for microservices: reading errors from the running printouts
  * Tracking the errors from the running result had been an issue for us. Instead of using the iTerm, we use mac terminal so that we can get all the runtime result and information.

* Writing DRY code in Messaginge Queue
  * It's a challenge to write DRY code in the sender and receiver for messaging, because we have so many different messages to communication.

* Use JdbcTemplate to query database and withdraw record
  * Sometimes we need to query a list like WHERE IN clause, we have to search online to get those query structures.

* Compose all elements into a wrapper in order to produce the correct Json back 
  * We create different models to construct json object
  * We refactor the wrapper files into a sub path in model so the file structure is clearer. 
  * The whole process of composing elements is a hard work, we have to draw the structure first, then add them one by one.
  
* Jenkins: setup pipeline for multiple microservices in 
  * We start with separating microservice into different repo, then setup Jenkins one by one.
  * But we finally find out that we can go with one repo, but setup the pipeline configuration to load a specific Jenkinsfiles to test different directories.

* Jenkins: Jacoco sometimes fails to produce the coverage report

* ELK
  * need to set 'read_only_allow_delete' to false to elasticsearch setting, credit to tracy
  * figure out how to create index pattern for customized patterns.

* Swagger
  * figure out how to setup swagger to work under microservices environment

* Deployment
  * figure out the networking problem, solved by using PCF's provided services

* Unit test and Integration test
  * Challenging to mock some objects, like Jackson's ObjectMapper. We analyzed the role of ObjectMapper in the program workflow and found out we could use the real ObjectMapper and didn't need to mock it
  * Better understanding the purpose of unit testing and integration test.
  * Challenging to test messaging queue.

* Exception Handling
  * challenging to find exceptional cases

## Installation Instruction

#### Installing the app locally

1. Clone the project
2. Install Docker
3. Goto the project's directory, launch the project by 'docker-compose up'

#### Jenkins

1. Create Jenkins file in the project's directory
2. Push the project to Github
3. Install Jenkins and launch Jenkins service
4. Login Jenkins, install plugins
  * Blue Ocean (pipeline plugin)
  * Jacoco, Code coverage API (code coverage plugin)
5. For each service, create a pipeline, setup pipeline from SCM to load the repository for pipeline.
6. Open the Blue Ocean and run the pipeline.

#### ELK

1. Install ELK kit:
    * ElasticSearch
    * Logstash
    * Kibana
2. Download the 'logstash.conf' file, modified the 'path' attributes to match with local machine
3. cmd: '/usr/local/bin/logstash -f logstash.conf' to start logstash service
4. start ElasticSearch and Kibana
5. start the app to generate logs
6. Login to Kibana, goto Management -> Create Index Pattern -> Enter 'users-api-*' (search for the pattern)



## Timeline

* 2019 11 08 Fri
  * analyze entity relationship
  * analyze requirements and write user stories
  * research on methods about data intercommunication, OpenFeign as the backup plan, try RabbitMQ
  * setup spring boot projects
* 2019 11 09 Sat - 2019 11 11 Mon
  * continue on writing user stories, breaking down stories into tasks
  * experiment on rabbitmq
  * experiment on deploying microservices project to pivotal cloud foundry
  * implement a helloworld restcontroller to test the setup
* 2019 11 12 Tue
  * users-api: signup, login
  * posts-api: create post, delete post
  * comments-api: create comment, delete comment
* 2019 11 13 Wed
  * api-gateway: security
  * users-api: security
  * posts-api: save author information for creating posts, list post
  * comments-api: save author information for creating comments, list comments
* 2019 11 14 Thr
  * data intercommunication
  * test apis
  * start deploying to cloud
* 2019 11 15 Fri
  * users-api: user profile
  * cloud deployment
  * integrate with frontend
* 2019 11 16 Sat
  * users-api: user role
  * api-gateway: activate hasRole for ROLE_ADMIN 
  * integrate with frontend
* 2019 11 17 Sun
  * cleanup code, refactor code architecture
  * final check for apis and frontend integration
  * finish readme
  * prepare for presentation
* 2019 11 21 Thr
  * Write User stories
  * List tasks
  * Set up Jenkins
* 2019 11 22 Fri
  * Swagger API Docs
  * SonarQube
* 2019 11 23-24 Weekend
  * Exception Handling
* 2019 11 25 Mon
  * Users-API unit testing
  * Exception Handling
  * Validator
* 2019 11 26 Tue
  * api-gateway unit testing
* 2019 11 27 Wed
  * comments-API testing
  * ELK
* 2019 11 28 Thr
  * posts-api testing
* 2019 11 29 Fri
  * deployment
* 2019 11 30 Sat
  * Integration Test
* 2019 12 01 Sun
  * Jenkins
* 2019 12 02 Mon
  * Jenkins
  * ELK
  * Integration Test
* 2019 12 03 Tue
  * ReadMe


## Contributor

* Mingjing Tang

* Qiming Chen
