# Stage 1: Build the lab applications
FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /lab-server
COPY lab-server /lab-server/
RUN mvn -f /lab-server/pom.xml clean package -Dmaven.test.skip=true

# Stage 2: Create the final image with both lab and Camunda
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the lab application artifacts
COPY --from=builder /lab-server/lab/target/*.war /opt/jboss/wildfly/standalone/deployments/
COPY --from=builder /lab-server/consumer/target/*.war /opt/jboss/wildfly/standalone/deployments/

# Copy Camunda distribution files to the container
COPY camunda /app/camunda

# Expose ports
EXPOSE 8080 8084

# Start both WildFly and Camunda
CMD ["sh", "-c", "(/opt/jboss/wildfly/bin/standalone.sh -b=0.0.0.0 -bmanagement=0.0.0.0 &) && (/app/camunda/start.sh)"]
