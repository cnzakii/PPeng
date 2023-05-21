# ppeng-module-search Dockerfile文件

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

ADD ppeng-module-search-1.0-SNAPSHOT.jar /root/ppeng-search.jar

RUN bash -c 'touch /root/ppeng-search.jar'

ENTRYPOINT ["java","-jar","/root/ppeng-search.jar"]

EXPOSE 3377


# 构建命令
# docker build -t ppeng/search:1.0 .

# 运行命令
# docker run -d -p 8264:8264 --network ppeng_net --restart=always --name ppeng-search -v /home/ppeng/logs/ppeng-search:/root/logs/ppeng-search ppeng/search:1.0