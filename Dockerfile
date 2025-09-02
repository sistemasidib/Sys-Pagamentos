FROM maven:3.9.9-amazoncorretto-21-alpine AS build
WORKDIR /app
ADD src src
ADD pom.xml .

ARG PROFILE
ARG CODEARTIFACT_TOKEN
ARG BIO_REGISTRO_VERSION=1.1.5

ENV CODEARTIFACT_TOKEN=${CODEARTIFACT_TOKEN}
ENV BIO_REGISTRO_VERSION=${BIO_REGISTRO_VERSION}
COPY .github/maven-settings/settings-template.xml /root/.m2/settings.xml

RUN echo "PROFILE:"
RUN echo $PROFILE

RUN mvn package -s /root/.m2/settings.xml -DskipTests=true -Dbio.registro.version=$BIO_REGISTRO_VERSION

FROM registry.access.redhat.com/ubi8/openjdk-21:1.20

ENV LANG='pt_BR.UTF-8' LANGUAGE='pt_BR:pt'

# Configure the JAVA_OPTIONS, you can add -XshowSettings:vm to also display the heap size.
ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -Duser.timezone=America/Fortaleza"

# We make four distinct layers so if there are application changes the library layers can be re-used
COPY --from=build --chown=185 /app/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build --chown=185 /app/target/quarkus-app/*.jar /deployments/
COPY --from=build --chown=185 /app/target/quarkus-app/app/ /deployments/app/
COPY --from=build --chown=185 /app/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185


ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]