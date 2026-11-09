# Build stage: compile and package the Spring Boot jar
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B -DskipTests package

# Runtime stage: slim JRE running as a non-root user
FROM eclipse-temurin:17-jre
WORKDIR /app
RUN useradd --system --create-home appuser
COPY --from=build /workspace/target/skillverse-v2-0.0.1-SNAPSHOT.jar app.jar
USER appuser

# Self-contained demo by default: in-memory H2, seeded demo data, no external services.
# Override SPRING_PROFILES_ACTIVE (and the database variables) to run against PostgreSQL.
ENV SPRING_PROFILES_ACTIVE=demo
EXPOSE 8080

# Keep the heap inside small free-tier containers
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70", "-XX:+UseSerialGC", "-jar", "/app/app.jar"]
