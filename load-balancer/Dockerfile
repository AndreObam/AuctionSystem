#FROM java:8-jdk-alpine
FROM java:8-jdk-alpine
RUN apk update && apk add bash
RUN mkdir load-balancer
WORKDIR /load-balancer
COPY ./out .
COPY ./libs .
EXPOSE 9090