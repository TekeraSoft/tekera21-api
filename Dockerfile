FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace

# 1) Maven wrapper, .mvn dizini ve pom.xml’i birlikte kopyala
COPY mvnw pom.xml ./
COPY .mvn/ .mvn

# 2) CRLF temizle + yürütme izni ver
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# 3) Bağımlılıkları indir (offline cache)
RUN ./mvnw -q dependency:go-offline

# 4) Kaynak kodu ekle ve JAR üret
COPY src src
RUN ./mvnw -q package -DskipTests


######################## RUNTIME evresi ######################
FROM eclipse-temurin:17-jre-jammy

ENV JAVACPP_PLATFORM=linux-x86_64 \
    JAVACPP_DEPENDENCIES=true \
    JAVA_TOOL_OPTIONS="-Djava.io.tmpdir=/tmp"

WORKDIR /tekera-marketplace
COPY --from=build /workspace/target/*.jar tekera-marketplace.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","tekera-marketplace.jar"]