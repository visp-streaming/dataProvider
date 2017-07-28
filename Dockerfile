FROM jeanblanchard/java:8
MAINTAINER Christoph Hochreiner <ch.hochreiner@gmail.com>
ADD target/dataProvider.jar app.jar
RUN apk --update add imagemagick && \
    rm -rf /var/cache/apk/*
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
