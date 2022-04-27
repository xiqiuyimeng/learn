FROM jre8:alpine3.9
WORKDIR /root
COPY target/learn-0.0.1-SNAPSHOT.jar .
CMD ["java", "-Denv=local", "-Dapollo.cacheDir=apollo", "-jar", "learn-0.0.1-SNAPSHOT.jar"]
