FROM amazoncorretto:24

WORKDIR /app

# COPY --from=builder ./api/build/libs/app.jar ./app.jar

COPY app/build/libs/app.jar ./app.jar
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT [ "java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar" ]
