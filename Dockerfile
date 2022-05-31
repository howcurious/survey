FROM maven:3.8.4-openjdk-17 as builder1
WORKDIR application
COPY pom.xml ./
COPY src ./src/
RUN mvn clean package -Dmaven.test.skip=true -P prod

FROM openjdk:17 as builder2
WORKDIR application
COPY --from=builder1 application/target/questionnaire.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:17
WORKDIR application
COPY --from=builder2 application/dependencies/ ./
COPY --from=builder2 application/spring-boot-loader/ ./
COPY --from=builder2 application/snapshot-dependencies/ ./
COPY --from=builder2 application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
