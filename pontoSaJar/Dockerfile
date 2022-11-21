FROM maven:3.8.6-openjdk-18 as build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:18 
WORKDIR /app
VOLUME /app
COPY --from=build ./build/target/*.jar ./ponto-sa.jar 
EXPOSE 8080

ENTRYPOINT java -jar ponto-sa.jar