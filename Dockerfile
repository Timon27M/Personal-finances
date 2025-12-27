# Этап сборки
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Копируем POM для кеширования зависимостей
COPY pom.xml .

# Скачиваем зависимости (кешируем отдельно)
RUN mvn dependency:go-offline -B

# Копируем исходный код
COPY src ./src

# Собираем приложение (пропускаем тесты в Docker)
RUN mvn clean package -DskipTests

# Этап выполнения
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Создаем не-root пользователя для безопасности
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Копируем JAR файл
COPY --from=build /app/target/*.jar app.jar

# Открываем порт приложения
EXPOSE 8080

# Команда запуска с поддержкой .env файлов
ENTRYPOINT ["java", "-jar", "app.jar"]