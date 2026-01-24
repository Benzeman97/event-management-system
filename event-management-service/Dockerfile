FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY ./build/libs/event-management-service.jar ./event-management-service.jar
EXPOSE 8195
CMD ["java","-jar","event-management-service.jar"]
