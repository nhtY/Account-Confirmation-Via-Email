FROM amazoncorretto:17-alpine
WORKDIR /opt/java-app
COPY /target/login-register-0.0.1-SNAPSHOT.jar app.jar
# ENTRYPOINT ["java","-jar","app.jar"] Bu da yazılabilir.
CMD ["java","-jar","app.jar"]