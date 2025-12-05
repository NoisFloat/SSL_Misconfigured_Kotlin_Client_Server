FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# copiamos todo el código fuente (la carpeta src completa)
COPY src ./src

# compilamos todas las clases .java que haya dentro de src
RUN javac $(find src -type f -name "*.java")

# Arranco un shell para poder ejecutar el Main a mano
CMD ["sh"]
