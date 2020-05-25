FROM java:8-jdk-alpine
RUN apk update && apk add bash
RUN mkdir server
WORKDIR /server
COPY ./out .
COPY ./libs .
EXPOSE 8080
CMD ["java", "Server"]
#RUN wget localhost:8080/api/

#CMD ["/bin/echo", "Server Starting"]
#docker login -u "obamwonyia" -p "vCdNpjuUreT3zhe" harbor.scc.lancs.ac.uk
#docker tag my-server harbor.scc.lancs.ac.uk/obamwonyia/server:latest
#docker push harbor.scc.lancs.ac.uk/obamwonyia/server:latest