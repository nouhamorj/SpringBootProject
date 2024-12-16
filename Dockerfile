# Utilisation d'une image Java officielle comme base
FROM openjdk:17-jdk-slim AS build

# Repertoire de travail
WORKDIR /app

# Copier le fichier pom.xml et les sources
COPY pom.xml .
COPY src ./src

# Installation Maven
RUN apt-get update && apt-get install -y maven

# Construire l'application avec Maven
RUN mvn clean package -DskipTests

# Une image avec l'application compilée
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Port utilise par l'application Spring Boot
EXPOSE 8080

# Commande d'execution
ENTRYPOINT ["java", "-jar", "app.jar"]
