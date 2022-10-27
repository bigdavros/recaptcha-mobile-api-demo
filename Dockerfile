FROM google/cloud-sdk:alpine
FROM tomcat:9.0.8-jre8-alpine

LABEL maintainer="dlenehan@google.com"

RUN apk update && apk add jq
WORKDIR /
RUN rm -rf /usr/local/tomcat/webapps/ROOT
ADD target/demo-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
ADD config.json /
ADD config.sh /

RUN chmod +x /config.sh && /config.sh && sed 's/#!\/usr\/bin\/env bash//g' /usr/local/tomcat/bin/catalina.sh >> /newcatalina.sh && cp /newcatalina.sh /usr/local/tomcat/bin/catalina.sh && chmod +x /usr/local/tomcat/bin/catalina.sh

EXPOSE 8080
CMD ["catalina.sh", "run"]