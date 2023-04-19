# ppeng-module-gateway Dockerfile文件

FROM openjdk:17

LABEL maintainer="Zaki<cnzakii@gmail.com>"

VOLUME /tmp

WORKDIR /root/

ADD ppeng-module-gateway-1.0-SNAPSHOT.jar /root/ppeng-gateway.jar

RUN bash -c 'touch /root/ppeng-gateway.jar'

ENTRYPOINT ["java","-jar","/root/ppeng-gateway.jar"]

EXPOSE 6378

# docker run -d -p 6378:6378 --network ppeng_net --restart=always --name ppeng-gateway -v /home/ppeng/ppeng-gateway/logs:/root/ppeng-gateway/logs ppeng/gateway:1.0
