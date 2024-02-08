ARG MSVC_NAME=msvc-auth

# First stage: Build the application
FROM openjdk:17-jdk-alpine as builder
ARG MSVC_NAME
WORKDIR /app/$MSVC_NAME
COPY ./pom.xml  /app
COPY ./$MSVC_NAME/pom.xml .
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target
COPY ./$MSVC_NAME/src ./src
RUN ./mvnw clean package -DskipTests

# Second stage: Set up the production image
FROM openjdk:17-jdk-alpine
ARG MSVC_NAME
ARG TARGET_FOLDER=/app/$MSVC_NAME/target
WORKDIR /app
RUN mkdir ./logs

COPY --from=builder $TARGET_FOLDER/msvc-auth-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=9000
ENV PORT $PORT_APP
EXPOSE $PORT

CMD ["java","-jar","msvc-auth-0.0.1-SNAPSHOT.jar"]
