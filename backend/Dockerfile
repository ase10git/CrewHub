FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . /home/app
WORKDIR /home/app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
COPY --from=build /home/app/target/*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
