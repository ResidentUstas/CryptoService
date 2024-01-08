FROM docker.io/tomcat:9.0.68-jdk17

COPY /target/*.war /usr/local/tomcat/webapps/samplewarapp.war

RUN addgroup  springboot && adduser tomcat
RUN usermod -G springboot tomcat
RUN chown -R tomcat /usr/local/tomcat
USER tomcat

EXPOSE 8080