# ppeng-module-user Dockerfile文件

FROM openjdk:17

LABEL maintainer="Zaki<cnzakii@gmail.com>"

VOLUME /tmp

WORKDIR /root/

ADD ppeng-module-user-1.0-SNAPSHOT.jar /root/ppeng-user.jar

RUN bash -c 'touch /root/ppeng-user.jar'

ENTRYPOINT ["java","-jar","/root/ppeng-user.jar"]

EXPOSE 3377


