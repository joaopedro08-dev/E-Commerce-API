# ===== STAGE 1: Build =====
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy dependency files first to leverage Docker layer caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B

# Copy source code and build the application
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# ===== STAGE 2: Runtime =====
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root user for better container security
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring

# Copy only the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Activate the production profile by default
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]