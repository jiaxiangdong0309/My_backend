# 多阶段构建 - 后端
FROM maven:3.9.6-eclipse-temurin-18 AS builder
WORKDIR /app
COPY pom.xml .
COPY peanut-common/pom.xml peanut-common/
COPY peanut-auth/pom.xml peanut-auth/
COPY peanut-product/pom.xml peanut-product/
COPY peanut-trace/pom.xml peanut-trace/
COPY peanut-stats/pom.xml peanut-stats/
COPY peanut-gateway/pom.xml peanut-gateway/
RUN mvn dependency:go-offline -B
COPY . .
RUN mvn clean package -pl peanut-gateway -am -DskipTests -B

FROM eclipse-temurin:18-jre-jammy
WORKDIR /app
COPY --from=builder /app/peanut-gateway/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]
