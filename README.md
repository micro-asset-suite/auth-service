# auth-service

mvn clean package -DskipTests
docker build -t auth-service:latest .
docker run -p 8083:8083 auth-service:latest

