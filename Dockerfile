FROM openjdk:17-alpine

ENV JAVA_ENABLE_DEBUG=${JAVA_ENABLE_DEBUG}

COPY build/libs/medicalservices-*-SNAPSHOT.jar medicalservices.jar
COPY entrypoint.sh .

RUN addgroup --system --gid 1001 appuser && \
    adduser --system --ingroup appuser --uid 1001 appuser

RUN chown appuser:appuser medicalservices.jar
RUN chmod 500 medicalservices.jar
RUN chmod +x entrypoint.sh

EXPOSE 8080

USER 1001

CMD ["./entrypoint.sh"]
