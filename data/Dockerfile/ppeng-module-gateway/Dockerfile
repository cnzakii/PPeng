# ppeng-module-gateway Dockerfile文件

FROM openjdk:17

LABEL maintainer="Zaki<cnzakii@gmail.com>"

#定义时区参数
ENV TZ=Asia/Shanghai

#设置时区
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo '$TZ' > /etc/timezone

#设置编码
ENV LANG C.UTF-8

VOLUME /tmp

WORKDIR /root/

ADD ppeng-module-gateway-1.0-SNAPSHOT.jar /root/ppeng-gateway.jar

RUN bash -c 'touch /root/ppeng-gateway.jar'

ENTRYPOINT ["java","-jar","/root/ppeng-gateway.jar"]

EXPOSE 6378

# 构建命令
# docker build -t ppeng/gateway:1.0 .

# 运行命令
# docker run -d -p 6378:6378 --network ppeng_net --restart=always --name ppeng-gateway -v /home/ppeng/logs/ppeng-gateway:/root/ppeng-gateway/logs ppeng/gateway:1.0
