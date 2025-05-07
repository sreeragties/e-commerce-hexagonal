# --- Build Stage ---
FROM maven:3.8.3-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml /app/
COPY ecommerce-payment /app/ecommerce-payment/
RUN sed -i '/<module>rule-engine<\/module>/d' /app/pom.xml
RUN sed -i '/<module>rest-api<\/module>/d' /app/pom.xml

RUN mvn clean package -pl ecommerce-payment -am -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/ecommerce-payment/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
