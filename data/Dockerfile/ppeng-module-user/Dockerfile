# ppeng-module-user Dockerfile文件

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

ADD ppeng-module-user-1.0-SNAPSHOT.jar /root/ppeng-user.jar

RUN bash -c 'touch /root/ppeng-user.jar'

ENTRYPOINT ["java","-jar","/root/ppeng-user.jar"]

EXPOSE 3377


# 构建命令
# docker build -t ppeng/user:1.0 .

# 运行命令
# docker run -d -p 3377:3377 --network ppeng_net --restart=always --name ppeng-user -v /home/ppeng/logs/ppeng-user:/root/logs/ppeng-user  ppeng/user:1.0