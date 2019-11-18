# postit2

## Description

Postit2 is a back-end program for the forum-like frontend, which has user account management and authentication, post management, and comment management. 

This project is to decompose a monolith spring back-end to work under microservices architecture and aims at decoupling relationship between services to achieve higher scalability.

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

## Further Improvement

* Exception handling

* Cloud deployment

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
  
## Contributor

* Mingjing Tang

* Qiming Chen
