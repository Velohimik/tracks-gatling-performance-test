#Stage 1. Download Maven dependencies
FROM maven:3.8.6-openjdk-11-slim AS dependencies
WORKDIR /perf-test
COPY pom.xml .
RUN ["mvn", "dependency:go-offline"]

#Stage 2. Build image
FROM maven:3.8.6-openjdk-11-slim
WORKDIR /perf-test
COPY --from=dependencies /root/.m2 /root/.m2
COPY . .
ENTRYPOINT ["mvn", "gatling:test"]