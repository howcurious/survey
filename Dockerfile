FROM maven:3.6.3-adoptopenjdk-11 as builder1
WORKDIR application
COPY pom.xml ./
COPY src ./src/
RUN mvn clean package -Dmaven.test.skip=true -P prod

FROM adoptopenjdk:11-jre-hotspot as builder2
WORKDIR application
COPY --from=builder1 application/target/questionnaire.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk:11-jre-hotspot
WORKDIR application
RUN fc-cache -f -v
COPY --from=builder2 application/dependencies/ ./
COPY --from=builder2 application/spring-boot-loader/ ./
COPY --from=builder2 application/snapshot-dependencies/ ./
COPY --from=builder2 application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
