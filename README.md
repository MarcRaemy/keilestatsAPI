# keilestats-api

This project contains a REAST-API for saving and reading Statistics of a just-for-fun ice hockey team to a database.
The project is written with Java Spring Boot using Maven. So you need to have Java SDK v1.6 or higher as well as Spring Boot and Maven installed. For instructions: see https://docs.spring.io/spring-boot/docs/1.0.0.RC5/reference/html/getting-started-installing-spring-boot.html .
If the tools are installed succesfully, you can run the Webservice as follows:
    1. Import the project into your IDE as a Maven project. All the dependecies and an embeded Tomcat-Webserver are imported automatically, if not, compile and build the pom.xml-file with Maven again. 
    2. Launch the application by running the class KeileStatsApplication in folder src/main/java/ch.keilestats.app 
    3. To see the documentation and use the endpoints, go to http://localhost8080/swagger-ui.html. A Swagger representation of the project should be accessible there. 
A H2 in Memory Database is used for the persistence layer in this version. It can be accessed via: "http://localhost:8080/h2-console"
The persistence layer can be changed by adding the dependecy to an alternative relational database in the pom.xml file. Go to Maven Repository https://mvnrepository.com/ to copy the dependency to the pom.xml file. Comment out or delete the H2-dependency. Rebuild and compile the project. To configure the connection to the database, open the "application.properties" file in the source/main/resources folder. There, you must define the url, username, password according to your database credentials. See for example here, https://dzone.com/articles/bounty-spring-boot-and-postgresql-database , for instructions how to do so with PostgreSQL.

