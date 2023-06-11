/*
 Navicat Premium Data Transfer

 Source Server         : 10.0.0.88
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : 10.0.0.88:6326
 Source Schema         : db_ppeng

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 11/06/2023 16:21:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin` (
  `id` bigint NOT NULL COMMENT '管理员id',
  `email` char(11) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '邮箱',
  `password` char(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '密码（加密方式：SM3 盐值：id）',
  `nick_name` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL COMMENT '昵称，默认是用户id前20位',
  `icon` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL COMMENT '头像路径',
  `role` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL COMMENT '用户角色：admin，superAdmin\n',
  `processed` int DEFAULT NULL COMMENT '已处理任务数',
  `passed` int DEFAULT NULL COMMENT '审核通过数',
  `rejected` int DEFAULT NULL COMMENT '审核未通过数',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员表';

-- ----------------------------
-- Table structure for t_collect
-- ----------------------------
DROP TABLE IF EXISTS `t_collect`;
CREATE TABLE `t_collect` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `recipe_id` bigint NOT NULL COMMENT '菜谱id\n',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `t_collect_user_id` (`user_id`) USING BTREE,
  KEY `t_collect_recipe_id` (`recipe_id`) USING BTREE,
  CONSTRAINT `t_collect_recipe_id` FOREIGN KEY (`recipe_id`) REFERENCES `t_recipe` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `t_collect_user_id` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收藏菜谱表';

-- ----------------------------
-- Table structure for t_follow
-- ----------------------------
DROP TABLE IF EXISTS `t_follow`;
CREATE TABLE `t_follow` (
  `fid` int NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `follow_id` bigint NOT NULL COMMENT '关注的人的id',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`fid`),
  KEY `t_follow_user_id` (`user_id`),
  KEY `t_follow_follow_id` (`follow_id`),
  CONSTRAINT `t_follow_follow_id` FOREIGN KEY (`follow_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `t_follow_user_id` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户关注表';

-- ----------------------------
-- Table structure for t_like
-- ----------------------------
DROP TABLE IF EXISTS `t_like`;
CREATE TABLE `t_like` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `recipe_id` bigint NOT NULL COMMENT '点赞的菜谱id',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `t_like_user_id` (`user_id`),
  KEY `t_like_recipe_id` (`recipe_id`),
  CONSTRAINT `t_like_recipe_id` FOREIGN KEY (`recipe_id`) REFERENCES `t_recipe` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `t_like_user_id` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='点赞菜谱表';

-- ----------------------------
-- Table structure for t_recipe
-- ----------------------------
DROP TABLE IF EXISTS `t_recipe`;
CREATE TABLE `t_recipe` (
  `id` bigint NOT NULL COMMENT '菜谱id',
  `user_id` bigint NOT NULL COMMENT '发布者id',
  `type_id` int NOT NULL COMMENT '菜谱类型id',
  `title` varchar(250) NOT NULL COMMENT '标题：不超过250字',
  `material` varchar(255) NOT NULL COMMENT '配料表',
  `content` text NOT NULL COMMENT '文字内容 ',
  `media_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片路径 或者视频路径',
  `is_video` int DEFAULT NULL COMMENT '是否为视频： 0:不是  1：是',
  `is_professional` int DEFAULT NULL COMMENT '0代表非专业，1代表是专业\n',
  `likes` varchar(255) NOT NULL COMMENT '点赞数',
  `collections` varchar(255) NOT NULL COMMENT '收藏数',
  `create_time` datetime(3) NOT NULL COMMENT '创建时间',
  `update_time` datetime(3) DEFAULT NULL COMMENT '更新时间',
  `censor_state` int NOT NULL COMMENT '审核状态： 0 未审核，1 机器审核，2 人工审核，3 人工复审',
  `is_baned` int NOT NULL COMMENT '违规状态： 0 未违规， 1 违规',
  `is_deleted` int NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='普通菜谱表';

-- ----------------------------
-- Table structure for t_recipe_censor
-- ----------------------------
DROP TABLE IF EXISTS `t_recipe_censor`;
CREATE TABLE `t_recipe_censor` (
  `recipeId` bigint NOT NULL COMMENT '菜谱id',
  `auto_censor_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '机器审核结果',
  `auto_censor_time` timestamp NULL DEFAULT NULL COMMENT '机器审核时间',
  `manual_censor_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '人工审查结果',
  `manual_censor_id` bigint DEFAULT NULL COMMENT '第一次人工审查人员id\n',
  `manual_censor_time` timestamp NULL DEFAULT NULL COMMENT '人工审查时间',
  `second_manual_censor_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '第二次人工审核结果',
  `second_manual_censor_id` bigint DEFAULT NULL COMMENT '第二次人工审核人员id',
  `second_manual_censor_time` timestamp NULL DEFAULT NULL COMMENT '第二次人工审核时间\n',
  PRIMARY KEY (`recipeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜谱审查表';

-- ----------------------------
-- Table structure for t_recipe_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_recipe_comment`;
CREATE TABLE `t_recipe_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `recipe_id` bigint NOT NULL COMMENT '菜谱id',
  `parent_id` bigint NOT NULL COMMENT '关联一级评论id，如果是一级评论，则值为0',
  `commenter_id` bigint NOT NULL COMMENT '评论者id',
  `content` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '评论内容，不超过150字',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int NOT NULL COMMENT '逻辑删除：0，未删除，1，已删除',
  PRIMARY KEY (`id`),
  KEY `recipe_comments_id` (`recipe_id`),
  KEY `commenter_id` (`commenter_id`),
  CONSTRAINT `commenter_id` FOREIGN KEY (`commenter_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `recipe_comments_id` FOREIGN KEY (`recipe_id`) REFERENCES `t_recipe` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜谱评论表';

-- ----------------------------
-- Table structure for t_recipe_type
-- ----------------------------
DROP TABLE IF EXISTS `t_recipe_type`;
CREATE TABLE `t_recipe_type` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '类型',
  `parent_id` int NOT NULL COMMENT '父类id，顶级分类为0',
  `score` int DEFAULT NULL COMMENT '顺序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜谱类型表';

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint NOT NULL COMMENT '手机号登录用户：雪花算法唯一ID\n微信登录用户：openid',
  `email` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL COMMENT '用户邮箱',
  `open_id` varchar(50) DEFAULT NULL COMMENT '微信用户唯一凭证',
  `password` char(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL COMMENT '密码（加密方式：SM3 盐值：id）',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称，默认是用户id前20位',
  `icon` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL COMMENT '头像路径',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户所在城市',
  `introduce` varchar(128) DEFAULT NULL COMMENT '用户简介，不超过128个字符',
  `fans` int DEFAULT NULL COMMENT '用户粉丝数',
  `follows` int DEFAULT NULL COMMENT '用户关注数',
  `gender` int DEFAULT NULL COMMENT '用户性别，0：未指定，1：女，3：男',
  `birthday` date DEFAULT NULL COMMENT '用户生日',
  `role` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '用户角色：user',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表，包含了用户的基本信息：用户id，手机号，昵称，头像地址等';

-- ----------------------------
-- Table structure for t_user_message
-- ----------------------------
DROP TABLE IF EXISTS `t_user_message`;
CREATE TABLE `t_user_message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '内容',
  `status` int DEFAULT NULL COMMENT '状态：0未读，1已读',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `is_deleted` int DEFAULT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=258 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户消息表';

SET FOREIGN_KEY_CHECKS = 1;
