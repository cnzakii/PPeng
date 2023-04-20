# ppeng-other-api Dockerfile文件

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

ADD ppeng-other-api-1.0-SNAPSHOT.jar /root/ppeng-other-api.jar

RUN bash -c 'touch /root/ppeng-other-api.jar'

ENTRYPOINT ["java","-jar","/root/ppeng-other-api.jar"]

EXPOSE 3450


# 构建命令
# docker build -t ppeng/other-api:1.0 .

# 运行命令
# docker run -d -p 3450:3450 --network ppeng_net --restart=always --name ppeng-other-api  -v /home/ppeng/logs/ppeng-other-api:/root/logs/ppeng-other-api -v /home/ppeng/resource:/root/resource ppeng/other-api:1.0