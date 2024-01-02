FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app

ARG JAR_NAME=largo_plazo-parameterization
ARG JAR_FILE=build/libs/${JAR_NAME}.jar

COPY ${JAR_FILE} /app/${JAR_NAME}.jar
RUN mv /app/${JAR_NAME}.jar /app/APP.jar

EXPOSE 8080
CMD ["java", "-jar", "APP.jar"]
