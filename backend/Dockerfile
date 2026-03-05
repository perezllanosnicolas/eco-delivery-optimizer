# 1. Fase de construcción
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
# Compilamos creando el Fat JAR
RUN mvn clean package -DskipTests

# 2. Fase de ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copiamos el JAR que contiene todas las librerías
COPY --from=builder /app/target/*-jar-with-dependencies.jar app.jar

# Exponemos el puerto de tu API Javalin
EXPOSE 7070

# Arrancamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]