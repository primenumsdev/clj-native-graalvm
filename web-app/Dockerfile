FROM clojure:openjdk-17-lein AS deps

RUN mkdir /app

COPY project.clj /app/project.clj

WORKDIR /app

RUN lein deps

FROM deps AS uberjar

COPY src /app/src
COPY resources /app/resources

WORKDIR /app

RUN lein uberjar

FROM eclipse-temurin:17-jre-alpine AS deploy

COPY --from=uberjar /app/target/uberjar/web-app-0.1.0-SNAPSHOT-standalone.jar /web-app.jar

EXPOSE 3000

ENTRYPOINT ["java", "-jar", "/web-app.jar"]
