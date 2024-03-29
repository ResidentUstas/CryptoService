#
FROM  maven:3.8.3-openjdk-17 as build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /target/Block_Algorithms-0.0.1-SNAPSHOT.war CryptoService.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","CryptoService.jar"]