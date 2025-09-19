# build stage
FROM maven:3.9.11-amazoncorretto-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests


# run stage
FROM amazoncorretto:21-alpine AS run
WORKDIR /app

COPY --from=build /app/target/twitter-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
