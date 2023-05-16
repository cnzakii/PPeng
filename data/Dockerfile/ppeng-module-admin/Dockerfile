# ppeng-module-admin Dockerfile文件

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

ADD ppeng-module-admin-1.0-SNAPSHOT.jar /root/ppeng-admin.jar

RUN bash -c 'touch /root/ppeng-admin.jar'

ENTRYPOINT ["java","-jar","/root/ppeng-admin.jar"]

EXPOSE 6378

# 构建命令
# docker build -t ppeng/admin:1.0 .

# 运行命令
# docker run -d -p 7679:7679 --network ppeng_net --restart=always --name ppeng-admin -v /home/ppeng/logs/ppeng-admin:/root/logs/ppeng-admin ppeng/admin:1.0
