# ---- Base image (lightweight + stable)
FROM eclipse-temurin:17-jdk-jammy

# ---- App directory
WORKDIR /app

# ---- Copy jar (make sure jar already built)
COPY target/*.jar app.jar

# ---- Expose application port
EXPOSE 8081

# ---- Run app
ENTRYPOINT ["java","-jar","/app/app.jar"]
