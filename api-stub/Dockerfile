FROM openjdk:8u111-jdk-alpine

RUN apk --no-cache add curl
RUN mkdir -p /opt/app/

WORKDIR /opt/app/
ADD build/libs/api-stub.jar app.jar

EXPOSE 8888

ENTRYPOINT exec java -Xmx512m -Djava.security.egd=file:/dev/./urandom -jar app.jar