


FROM maven:3.9.9-eclipse-temurin-24-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN gradle dependency:go-offline -builder

COPY src ./src

# RUN gradle clean build -x test
# RUN gradle assemble -x test
RUN gradle clean build check jar


FROM openjdk:24-jdk AS runner
# FROM eclipse-temurin:23-jre AS runner

WORKDIR /app

# COPY --from=builder ./app/build/libs/airport-service-0.0.1-SNAPSHOT.jar ./app/airport-service.jar
COPY --from=builder ./lib/build/libs/lib.jar ./lib.jar
COPY --from=builder ./api/build/libs/api.jar ./api.jar
COPY --from=builder ./api/build/libs/app.jar ./app.jar

EXPOSE  8100

ENTRYPOINT ["java", "-jar", "app.jar"]
