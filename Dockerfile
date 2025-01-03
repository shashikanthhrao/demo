FROM openjdk:17
ARG JAR_FILE=target/demo-0.0.1-SNAPSHOT-exec.jar
COPY ${JAR_FILE} app.jar
ENV JAR_OPTS=""
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar $JAR_OPTS