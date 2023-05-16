# ppeng-module-recipe Dockerfile文件

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

ADD ppeng-module-recipe-1.0-SNAPSHOT.jar /root/ppeng-recipe.jar

RUN bash -c 'touch /root/ppeng-recipe.jar'

ENTRYPOINT ["java","-jar","/root/ppeng-recipe.jar"]

EXPOSE 3377


# 构建命令
# docker build -t ppeng/recipe:1.0 .

# 运行命令
# docker run -d -p 9836:9836 --network ppeng_net --restart=always --name ppeng-recipe -v /home/ppeng/logs/ppeng-recipe:/root/logs/ppeng-recipe -v /home/ppeng/resource:/root/resource ppeng/recipe:1.0