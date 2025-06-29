FROM maven:3.8.3-openjdk-17
COPY . /Acharya_University
WORKDIR  /Acharya_University
# RUN mvn clean install
RUN mvn clean package

#FROM azul/zulu-openjdk:17
#WORKDIR /AcharyaInstitute
#COPY --from=0 /Acharya_University/target/Acharya_University-0.0.1-SNAPSHOT.jar /usr/app/
#WORKDIR /usr/app
#ENTRYPOINT [ "java" , "-jar" ,"Acharya_University-0.0.1-SNAPSHOT.jar"]

# Use the official Tomcat image as base image
FROM tomcat:9.0-jdk11-openjdk-slim

# Remove the default ROOT application
RUN rm -rf /usr/local/tomcat/webapps/ROOT
WORKDIR  /Acharya_University
# Copy your WAR file into the webapps directory of Tomcat
COPY --from=0 /Acharya_University/target/Acharya_University-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose the port that Tomcat will listen on
EXPOSE 8080
# Set the context path for the ROOT application
ENV CONTEXT_PATH=/au
# Start Tomcat
CMD ["catalina.sh", "run"]

