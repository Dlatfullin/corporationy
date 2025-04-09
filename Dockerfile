FROM eclipse-temurin:21-jdk
WORKDIR /app
EXPOSE 8080
COPY build/libs/corporationy-0.0.1-SNAPSHOT.jar /app/coprorationy.jar
ENTRYPOINT ["java", "-jar", "coprorationy.jar"]