FROM adoptopenjdk:11-jdk-hotspot as builder
WORKDIR /app
ADD ./target/technical-test.jar service.jar
RUN jar -xf service.jar

FROM adoptopenjdk:11-jre-hotspot
WORKDIR app
COPY --from=builder /app/BOOT-INF/lib ./lib
COPY --from=builder /app/META-INF ./META-INF
COPY --from=builder /app/BOOT-INF/classes .
USER nobody:nogroup
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-XX:ActiveProcessorCount=1", "-Dserver.port=${SERVER_PORT}", "-cp", "/app:/app/lib/*", "com.eduardomallmann.compasso.technicaltest.TechnicalTestApplication"]
ENV SERVER_PORT 8080