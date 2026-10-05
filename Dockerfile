# # Gunakan image Maven untuk build
# FROM maven:3.9.6-eclipse-temurin-17 AS build
# WORKDIR /app
# COPY . .
# RUN mvn clean package -DskipTests

# # Gunakan image JDK ringan untuk menjalankan aplikasi
# FROM eclipse-temurin:17-jre
# WORKDIR /app
# COPY --from=build /app/target/library-0.0.1-SNAPSHOT.jar app.jar

# EXPOSE 8080

# ENTRYPOINT ["java", "-jar", "app.jar"]

# Gunakan image Maven untuk build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Salin seluruh source code ke dalam container
COPY . .

# Pastikan mvnw executable (penting untuk Linux container)
RUN chmod +x mvnw

# Jalankan mvnw clean install
RUN ./mvnw clean install -DskipTests

# Lalu jalankan package (bila masih perlu menghasilkan jar final)
RUN ./mvnw clean package -DskipTests

# Gunakan image JDK ringan untuk menjalankan aplikasi
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/library-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
