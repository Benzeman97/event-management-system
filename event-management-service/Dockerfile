FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY ./build/libs/patient-management-service-1.0.jar ./patient-management-service-1.0.jar
EXPOSE 8195
CMD ["java","-jar","event-management-service.jar"]
