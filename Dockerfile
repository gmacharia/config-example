ARG BASE_IMAGE
ARG APPLICATION_NAME
# get the base image from argument
FROM maven:3.6.1-jdk-11-slim as builder

WORKDIR /app

COPY . .


RUN mvn clean package -DskipTests package


FROM ${BASE_IMAGE}

# Add Maintainer Info
LABEL authors="gituikumachari2@gmail.com"

# reference application name build argument
ARG APPLICATION_NAME

# App home directory
ENV APP_HOME_DIR=/apps/development

# App jar file name
ENV EXPOSE_PORT=8080

# Switch to root user
USER root

# Create application folder
RUN mkdir -p ${APP_HOME_DIR}

# Create app user
RUN groupadd -g 10000 appuser
RUN useradd --home-dir ${APP_HOME_DIR} -u 10000 -g appuser appuser

# Add jar to application
COPY --from=builder /app/target/${APPLICATION_NAME}-*.jar ${APP_HOME_DIR}/application.jar
#ADD target/${APPLICATION_NAME}.jar ${APP_HOME_DIR}/application.jar
RUN echo "${APP_HOME_DIR}/application.jar"

# Grant app user the necessary rights
RUN chmod -R 0766 ${APP_HOME_DIR}
RUN chown -R appuser:appuser ${APP_HOME_DIR}
RUN chmod g+w /etc/passwd

EXPOSE ${EXPOSE_PORT}

# Switch to the application directore
WORKDIR ${APP_HOME_DIR}

# Switch to app user
USER appuser


# Entry point to run jar file
#ENTRYPOINT java -jar <name>.jar
ENTRYPOINT ["java","-Xss512k","-XX:+UseG1GC","-Xss512k","-Xmx640m","-XX:MaxRAM=795m","-Djava.security.egd=file:/dev/./urandom","-jar","-Dserver.port=${EXPOSE_PORT}","application.jar"]
