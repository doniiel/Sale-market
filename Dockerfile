FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

COPY build.gradle settings.gradle gradle* ./
RUN mkdir -p src && echo "// dummy" > src/dummy.java

RUN ./gradlew build -x test --no-daemon || true

COPY . .
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
