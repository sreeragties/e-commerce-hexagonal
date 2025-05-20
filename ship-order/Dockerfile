# --- Build Stage ---
FROM maven:3.8.3-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml /app/
COPY ship-order /app/ship-order/

RUN sed -i '/<module>rest-api<\/module>/d' /app/pom.xml
RUN sed -i '/<module>rule-engine<\/module>/d' /app/pom.xml
RUN sed -i '/<module>ecommerce-payment<\/module>/d' /app/pom.xml

RUN mvn clean package -pl ship-order -am -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/ship-order/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
