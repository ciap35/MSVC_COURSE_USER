ARG MSVC_NAME=msvc-course

# First stage: Build the application
FROM openjdk:17-jdk-alpine as builder
ARG MSVC_NAME
WORKDIR /app/$MSVC_NAME
COPY ./pom.xml  /app
COPY ./$MSVC_NAME/pom.xml .
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .

RUN ./mvnw dependency:go-offline
COPY ./$MSVC_NAME/src ./src

RUN ./mvnw clean package -DskipTests

# Second stage: Set up the production image
FROM openjdk:17-jdk-alpine
ARG MSVC_NAME
ARG TARGET_FOLDER=/app/$MSVC_NAME/target
WORKDIR /app
RUN mkdir ./logs

COPY --from=builder $TARGET_FOLDER/msvc-course-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=8002
ENV PORT $PORT_APP
EXPOSE $PORT

#Delay the application by 20 seconds to allow time for PostgreSQL to start
#CMD sleep 20 && java -jar msvc-course-0.0.1-SNAPSHOT.jar
# Application start command
CMD ["java","-jar","msvc-course-0.0.1-SNAPSHOT.jar"]
