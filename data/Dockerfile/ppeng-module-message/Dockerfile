# ppeng-module-message Dockerfile文件

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

ADD ppeng-module-message-1.0-SNAPSHOT.jar /root/ppeng-message.jar

RUN bash -c 'touch /root/ppeng-message.jar'

ENTRYPOINT ["java","-jar","/root/ppeng-message.jar"]

EXPOSE 6378

# 构建命令
# docker build -t ppeng/message:1.0 .

# 运行命令
# docker run -d -p 4534:4534 --network ppeng_net --restart=always --name ppeng-message -v /home/ppeng/logs/ppeng-message:/root/logs/ppeng-message ppeng/message:1.0
