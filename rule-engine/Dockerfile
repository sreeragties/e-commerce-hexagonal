# --- Build Stage ---
FROM maven:3.8.3-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml /app/
COPY rule-engine /app/rule-engine/
RUN sed -i '/<module>rest-api<\/module>/d' /app/pom.xml
RUN sed -i '/<module>ecommerce-payment<\/module>/d' /app/pom.xml
RUN sed -i '/<module>ship-order<\/module>/d' /app/pom.xml

RUN mvn clean package -pl rule-engine -am -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/rule-engine/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
