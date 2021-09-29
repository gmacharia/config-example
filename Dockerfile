# get the base image from argument
# This makes it easier to manage cross platform images - AMD64 / PPC64le
ARG BASE_IMAGE
FROM ${BASE_IMAGE}

# Add Maintainer Info
LABEL authors="gituikumacharia2@gmail.com"

# Application Name.
ARG APPLICATION_NAME

# Port to be Exposed
ENV EXPOSE_PORT=8080

## Service Home Directory
ENV APP_HOME_DIRECTORY=/apps/kyosk-config

## Switch to User Root.
USER root

## Create the home directory
RUN mkdir -p ${APP_HOME_DIRECTORY}

# Create app user
RUN groupadd -g 10000 appuser
RUN useradd --home-dir ${APP_HOME_DIRECTORY} -u 10000 -g appuser appuser


# Add jar to application
ADD target/${APPLICATION_NAME}.jar ${APP_HOME_DIRECTORY}/application.jar
RUN echo "${APP_HOME_DIRECTORY}/application.jar"


# Grant app user the necessary rights
RUN chmod -R 0766 ${APP_HOME_DIRECTORY}
RUN chown -R appuser:appuser ${APP_HOME_DIRECTORY}
RUN chmod g+w /etc/passwd

EXPOSE ${EXPOSE_PORT}

# Switch to the application directory
WORKDIR ${APP_HOME_DIRECTORY}

# Switch to app user
USER appuser

# Entry point to run jar file
#ENTRYPOINT java -jar <name>.jar
ENTRYPOINT ["java","-Xss512k","-XX:+UseG1GC","-Xss512k","-Xmx640m","-XX:MaxRAM=795m","-Djava.security.egd=file:/dev/./urandom","-jar","-Dserver.port=${EXPOSE_PORT}","application.jar"]
