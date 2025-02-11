FROM openjdk:17-jdk-slim

WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

# Build the Spring Boot application
RUN mvn package -DskipTests

# Create a smaller image for the final application
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=0 /app/target/contas-*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]