To run your Spring application with the docker-compose.yml file, you can follow these steps:

1. **Build your Spring application:**
If you haven't already, build your Spring application into a JAR file using your IDE or build tool like Maven or Gradle.

With maven cleans and then packages the project as .jar file under ./target skipping the tests:
```sh 
 mvn clean package -DskipTests
```

2. **Create a Dockerfile for your Spring application:**
If you haven't already, create a Dockerfile for your Spring application in the same directory as your docker-compose.yml file. Here's a basic example for a Spring Boot application:

```Dockerfile
FROM openjdk:11-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

3. **Run your application with Docker Compose:**
Open a terminal, navigate to the directory containing your docker-compose.yml file, and run:
```sh
docker-compose up
```
This command will start the PostgreSQL database and your Spring application. Docker Compose will automatically build the Spring application's Docker image using the Dockerfile and run it.

4. **Access your Spring application:**
Once both services are running, you should be able to access your Spring application at http://localhost:8080 (assuming your Spring application exposes its endpoints on port 8080). You can interact with your application just like you would if it were running outside Docker.

If you make changes to your Spring application code, you'll need to rebuild the application's Docker image (step 3) to apply those changes.