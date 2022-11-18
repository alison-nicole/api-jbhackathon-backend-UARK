FROM azul/zulu-openjdk-alpine:11
ENV PORT 8080
EXPOSE 8080
ADD target/ws_infrastructure_university_hackathon.jar ws_infrastructure_university_hackathon.jar
ADD target/classes/absolute-point-359019-fd3b732c5cef.json absolute-point-359019-fd3b732c5cef.json
ENV GOOGLE_APPLICATION_CREDENTIALS=absolute-point-359019-fd3b732c5cef.json
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.krb5.conf=/etc/krb5.conf -Duser.timezone=America/Chicago -jar ws_infrastructure_university_hackathon.jar