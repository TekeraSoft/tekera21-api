FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw -q dependency:go-offline      # bağımlılıkları indir

COPY src src
RUN ./mvnw -q package -DskipTests        # JAR üret

################## RUNTIME evresi ################
FROM eclipse-temurin:17-jre-jammy

# JavaCPP/JavaCV native’lerini küçült ‑ opsiyonel
ENV JAVACPP_PLATFORM=linux-x86_64 \
    JAVACPP_DEPENDENCIES=true \
    JAVA_TOOL_OPTIONS="-Djava.io.tmpdir=/tmp"

WORKDIR /tekera-marketplace
COPY --from=build /workspace/target/*.jar tekera-marketplace.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","tekera-marketplace.jar"]