# Estágio de Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
# Baixa as dependências em cache para acelerar os próximos builds
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio de Execução
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S nexusgroup && adduser -S nexususer -G nexusgroup
USER nexususer

COPY --from=build /app/target/nexus.feedback.processor-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
