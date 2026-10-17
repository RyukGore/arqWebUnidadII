# 1. Usa una imagen base ligera de Java (Eclipse Temurin es el estándar actual para JDK 17)
FROM eclipse-temurin:21-jre-alpine

# 2. Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# 3. Copia el archivo .jar compilado desde tu máquina al contenedor
COPY target/ArqWeb-0.0.1.jar app.jar

# 4. Expone los puertos asignados
EXPOSE 5400 9090

# 5. Define el comando que inicia la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
