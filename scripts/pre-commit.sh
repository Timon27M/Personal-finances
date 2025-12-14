#!/bin/bash

echo "🚀 Starting Local CI..."

# Проверка форматирования
echo "📝 Checking code format..."
./mvnw spotless:check
if [ $? -ne 0 ]; then
    echo "❌ Code format check failed. Run: ./mvnw spotless:apply"
    exit 1
fi

# Линтинг
echo "🔍 Running linter..."
./mvnw checkstyle:check
if [ $? -ne 0 ]; then
    echo "❌ Checkstyle failed"
    exit 1
fi

# Тесты
echo "🧪 Running tests..."
./mvnw test
if [ $? -ne 0 ]; then
    echo "❌ Tests failed"
    exit 1
fi

# Сборка
echo "🔨 Building project..."
./mvnw clean compile -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Build failed"
    exit 1
fi

echo "✅ Local CI completed successfully!"
