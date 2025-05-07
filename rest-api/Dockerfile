# --- Build Stage ---
FROM maven:3.8.3-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml /app/
COPY rest-api /app/rest-api/
RUN sed -i '/<module>rule-engine<\/module>/d' /app/pom.xml

RUN mvn clean package -pl rest-api -am -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/rest-api/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
