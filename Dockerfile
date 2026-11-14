# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY backend /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
RUN sed -i 's/\r$//' /app/docker-entrypoint.sh && chmod +x /app/docker-entrypoint.sh
COPY --from=build /app/target/spedex-backend.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["/app/docker-entrypoint.sh"]
