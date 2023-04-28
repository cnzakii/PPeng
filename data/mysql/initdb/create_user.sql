/*
    该sql文件用于创建并配置canal 和 nacos 用户
 */

/*
  配置canal用户
 */
CREATE DATABASE IF NOT EXISTS db_ppeng;

-- 创建名为 canal 的用户，插件为 mysql_native_password(针对mysql 8+)
CREATE USER 'canal'@'%' IDENTIFIED WITH mysql_native_password BY 'wYyEYbRY4qgv-';

-- 授权 canal 用户访问 db_ppeng 数据库
GRANT ALL PRIVILEGES ON db_ppeng.* TO 'canal'@'%';

-- 授权 canal 用户作为 MySQL slave
GRANT REPLICATION SLAVE ON *.* TO 'canal'@'%';


/*
    配置nacos用户
 */
CREATE DATABASE IF NOT EXISTS db_nacos;

-- 创建名为 nacos的用户，插件为 mysql_native_password(针对mysql 8+)
CREATE USER 'nacos'@'%' IDENTIFIED WITH mysql_native_password BY 'C_jbFhyg93RNB';


-- 授权 nacos 用户访问 db_ppeng 数据库
GRANT ALL PRIVILEGES ON db_nacos.* TO 'nacos'@'%';
