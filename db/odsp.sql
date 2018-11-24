/*
 Navicat Premium Data Transfer

 Source Server         : mysql8.0
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : odsp

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 22/11/2018 14:51:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `os_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `device` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `browser_type` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ip_address` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` timestamp(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `sys_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 83 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (1, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-10-29 22:09:23');
INSERT INTO `sys_log` VALUES (2, 3, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-10-29 22:10:13');
INSERT INTO `sys_log` VALUES (3, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-10-29 22:13:16');
INSERT INTO `sys_log` VALUES (4, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-10-29 22:20:24');
INSERT INTO `sys_log` VALUES (5, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-10-29 22:24:25');
INSERT INTO `sys_log` VALUES (6, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-10-29 23:18:53');
INSERT INTO `sys_log` VALUES (7, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-10-29 23:31:31');
INSERT INTO `sys_log` VALUES (8, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-10-29 23:36:51');
INSERT INTO `sys_log` VALUES (9, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-10-30 22:40:31');
INSERT INTO `sys_log` VALUES (10, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-04 16:57:06');
INSERT INTO `sys_log` VALUES (11, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-04 17:12:13');
INSERT INTO `sys_log` VALUES (12, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-04 18:15:13');
INSERT INTO `sys_log` VALUES (13, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-11-04 18:16:43');
INSERT INTO `sys_log` VALUES (14, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-04 20:10:40');
INSERT INTO `sys_log` VALUES (15, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-11-04 20:12:50');
INSERT INTO `sys_log` VALUES (16, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-11-04 20:53:31');
INSERT INTO `sys_log` VALUES (17, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-05 13:58:42');
INSERT INTO `sys_log` VALUES (18, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-11-05 14:16:05');
INSERT INTO `sys_log` VALUES (19, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.60.1', '2018-11-05 15:03:07');
INSERT INTO `sys_log` VALUES (20, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.60.1', '2018-11-05 16:26:28');
INSERT INTO `sys_log` VALUES (21, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-11-05 17:29:45');
INSERT INTO `sys_log` VALUES (22, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-11-05 21:03:55');
INSERT INTO `sys_log` VALUES (23, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-05 21:24:20');
INSERT INTO `sys_log` VALUES (24, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-11-05 22:00:55');
INSERT INTO `sys_log` VALUES (25, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.60.1', '2018-11-06 00:22:21');
INSERT INTO `sys_log` VALUES (26, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-11-06 17:28:07');
INSERT INTO `sys_log` VALUES (27, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-11-06 21:54:06');
INSERT INTO `sys_log` VALUES (28, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.60.1', '2018-11-07 00:21:24');
INSERT INTO `sys_log` VALUES (29, 2, 'Windows 10', 'Windows 10', 'Firefox', '127.0.0.1', '2018-11-07 14:31:39');
INSERT INTO `sys_log` VALUES (30, 2, 'Windows 10', 'Windows 10', 'Firefox', '127.0.0.1', '2018-11-07 20:06:01');
INSERT INTO `sys_log` VALUES (31, 2, 'Windows 10', 'Windows 10', 'Firefox', '127.0.0.1', '2018-11-07 21:40:46');
INSERT INTO `sys_log` VALUES (32, 2, 'Windows 10', 'Windows 10', 'Firefox', '127.0.0.1', '2018-11-07 23:23:52');
INSERT INTO `sys_log` VALUES (33, 2, 'Windows 10', 'Windows 10', 'Firefox', '127.0.0.1', '2018-11-08 15:52:05');
INSERT INTO `sys_log` VALUES (34, 2, 'Windows 10', 'Windows 10', 'Firefox', '127.0.0.1', '2018-11-08 16:46:50');
INSERT INTO `sys_log` VALUES (35, 2, 'Windows 10', 'Windows 10', 'Firefox', '127.0.0.1', '2018-11-08 21:44:59');
INSERT INTO `sys_log` VALUES (36, 2, 'Windows 10', 'Windows 10', 'Firefox', '127.0.0.1', '2018-11-08 22:41:05');
INSERT INTO `sys_log` VALUES (37, 2, 'Windows 10', 'Windows 10', 'Firefox', '127.0.0.1', '2018-11-08 23:15:04');
INSERT INTO `sys_log` VALUES (38, 2, 'Windows 10', 'Windows 10', 'Firefox', '127.0.0.1', '2018-11-09 00:04:36');
INSERT INTO `sys_log` VALUES (39, 2, 'Windows 10', 'Windows 10', 'Firefox', '127.0.0.1', '2018-11-09 15:21:22');
INSERT INTO `sys_log` VALUES (40, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-09 15:56:46');
INSERT INTO `sys_log` VALUES (41, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-09 17:56:08');
INSERT INTO `sys_log` VALUES (42, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-09 18:30:09');
INSERT INTO `sys_log` VALUES (43, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-09 21:20:50');
INSERT INTO `sys_log` VALUES (44, 2, 'Windows 10', 'Windows 10', 'Firefox', '127.0.0.1', '2018-11-09 22:42:17');
INSERT INTO `sys_log` VALUES (45, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.60.1', '2018-11-11 13:24:37');
INSERT INTO `sys_log` VALUES (46, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-11 20:32:07');
INSERT INTO `sys_log` VALUES (47, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.60.1', '2018-11-12 13:51:48');
INSERT INTO `sys_log` VALUES (48, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.60.1', '2018-11-12 13:53:47');
INSERT INTO `sys_log` VALUES (49, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.60.1', '2018-11-12 14:02:49');
INSERT INTO `sys_log` VALUES (50, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.60.1', '2018-11-12 14:34:48');
INSERT INTO `sys_log` VALUES (51, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.60.1', '2018-11-12 16:08:57');
INSERT INTO `sys_log` VALUES (52, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-14 14:28:00');
INSERT INTO `sys_log` VALUES (53, 2, 'Windows 10', 'Windows 10', 'Chrome', '127.0.0.1', '2018-11-14 16:09:29');
INSERT INTO `sys_log` VALUES (54, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-14 17:40:01');
INSERT INTO `sys_log` VALUES (55, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-11-14 17:51:23');
INSERT INTO `sys_log` VALUES (56, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-11-14 22:32:54');
INSERT INTO `sys_log` VALUES (57, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-11-14 23:37:42');
INSERT INTO `sys_log` VALUES (58, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-17 15:29:27');
INSERT INTO `sys_log` VALUES (59, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-17 21:50:37');
INSERT INTO `sys_log` VALUES (60, 2, 'Windows 10', 'Windows 10', 'Firefox', '127.0.0.1', '2018-11-17 22:22:30');
INSERT INTO `sys_log` VALUES (61, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-17 23:20:24');
INSERT INTO `sys_log` VALUES (62, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-18 22:12:58');
INSERT INTO `sys_log` VALUES (63, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-19 01:30:11');
INSERT INTO `sys_log` VALUES (64, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-19 02:41:00');
INSERT INTO `sys_log` VALUES (65, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-19 04:18:13');
INSERT INTO `sys_log` VALUES (66, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-19 05:22:38');
INSERT INTO `sys_log` VALUES (67, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.137.1', '2018-11-19 05:51:14');
INSERT INTO `sys_log` VALUES (68, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-19 06:12:25');
INSERT INTO `sys_log` VALUES (69, 2, 'Windows 10', 'Windows 10', 'Firefox', '127.0.0.1', '2018-11-19 07:50:11');
INSERT INTO `sys_log` VALUES (70, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-19 07:51:33');
INSERT INTO `sys_log` VALUES (71, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-19 07:53:06');
INSERT INTO `sys_log` VALUES (72, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-19 09:29:23');
INSERT INTO `sys_log` VALUES (73, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-19 12:17:23');
INSERT INTO `sys_log` VALUES (74, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-19 13:06:41');
INSERT INTO `sys_log` VALUES (75, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-19 14:16:37');
INSERT INTO `sys_log` VALUES (76, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.60.1', '2018-11-19 15:35:38');
INSERT INTO `sys_log` VALUES (77, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.60.1', '2018-11-19 15:49:05');
INSERT INTO `sys_log` VALUES (78, 2, 'Windows 10', 'Windows 10', 'Firefox', '192.168.60.1', '2018-11-19 16:02:10');
INSERT INTO `sys_log` VALUES (79, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-19 20:35:23');
INSERT INTO `sys_log` VALUES (80, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-19 23:15:05');
INSERT INTO `sys_log` VALUES (81, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-21 18:53:57');
INSERT INTO `sys_log` VALUES (82, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-22 13:52:10');
INSERT INTO `sys_log` VALUES (83, 2, 'Windows 10', 'Windows 10', 'Chrome', '192.168.137.1', '2018-11-22 14:29:47');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `permission_id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `mark` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_url` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_id` int(11) NOT NULL DEFAULT -1,
  `is_menu` int(11) NOT NULL DEFAULT 0,
  `order_number` int(11) NOT NULL DEFAULT 0,
  `menu_icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` timestamp(0) NOT NULL,
  `update_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`permission_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, '主页', '', '', -1, 0, 0, 'layui-icon-home', '2018-10-28 22:10:43', '2018-10-28 22:10:42');
INSERT INTO `sys_permission` VALUES (2, '欢迎页', '', 'console', 1, 0, 1, '', '2018-10-28 22:11:42', '2018-10-28 22:11:42');
INSERT INTO `sys_permission` VALUES (3, '优化分析', '', '', -1, 0, 2, 'layui-icon-senior', '2018-10-28 22:14:02', '2018-10-28 22:14:02');
INSERT INTO `sys_permission` VALUES (4, '动态规划', '', 'optimization/dynamic', 3, 0, 4, '', '2018-10-28 22:15:43', '2018-10-28 22:15:43');
INSERT INTO `sys_permission` VALUES (5, '目标规划', '', '', 3, 0, 3, '', '2018-10-28 22:20:07', '2018-10-28 22:20:06');
INSERT INTO `sys_permission` VALUES (6, '图与网络分析', '', '', 3, 0, 5, '', '2018-10-28 22:21:55', '2018-10-28 22:21:54');
INSERT INTO `sys_permission` VALUES (7, '决策模型', '', '', -1, 0, 8, 'layui-icon-chart-screen', '2018-10-28 22:22:05', '2018-10-28 22:22:05');
INSERT INTO `sys_permission` VALUES (8, '风险决策法', '', 'decision/risk', 7, 0, 9, '', '2018-10-28 22:22:23', '2018-10-28 22:22:22');
INSERT INTO `sys_permission` VALUES (9, '不确定型决策法', '', 'decision/uncertain', 7, 0, 10, '', '2018-10-28 22:22:41', '2018-10-28 22:22:40');
INSERT INTO `sys_permission` VALUES (10, '存储论', '', '', -1, 0, 6, 'layui-icon-menu-fill', '2018-10-28 22:22:52', '2018-10-28 22:22:52');
INSERT INTO `sys_permission` VALUES (11, '存储论', '', 'storage/storage', 10, 0, 7, '', '2018-10-28 22:23:09', '2018-10-28 22:23:08');
INSERT INTO `sys_permission` VALUES (12, '系统管理', '', '', -1, 0, 11, 'layui-icon-set', '2018-10-28 22:23:31', '2018-10-28 22:23:31');
INSERT INTO `sys_permission` VALUES (13, '用户管理', '', 'system/user', 12, 0, 14, '', '2018-10-28 22:23:54', '2018-10-28 22:23:53');
INSERT INTO `sys_permission` VALUES (14, '角色管理', '  ', 'system/role', 12, 0, 12, '', '2018-10-28 22:24:18', '2018-10-28 22:24:17');
INSERT INTO `sys_permission` VALUES (15, '权限管理', '', 'system/permission', 12, 0, 13, '', '2018-10-28 22:24:51', '2018-10-28 22:24:51');
INSERT INTO `sys_permission` VALUES (16, '系统监控', '', '', -1, 0, 15, 'layui-icon-engine', '2018-10-28 22:26:13', '2018-10-28 22:26:13');
INSERT INTO `sys_permission` VALUES (17, 'Druid监控', '', '/druid', 16, 0, 0, 'layui-icon-engine', '2018-10-28 22:27:03', '2018-10-28 22:27:02');
INSERT INTO `sys_permission` VALUES (18, '登录日志', '', '/system/log', 16, 0, 0, '', '2018-10-28 22:28:01', '2018-10-28 22:28:00');
INSERT INTO `sys_permission` VALUES (19, '查询用户', 'user:view', '', 13, 1, 0, '', '2018-10-28 22:31:47', '2018-10-28 22:31:46');
INSERT INTO `sys_permission` VALUES (20, '添加用户', 'user:add', '', 13, 1, 0, '', '2018-10-28 22:32:20', '2018-10-28 22:32:20');
INSERT INTO `sys_permission` VALUES (21, '修改用户', 'user:edit', '', 13, 1, 0, '', '2018-10-28 22:32:44', '2018-10-28 22:32:44');
INSERT INTO `sys_permission` VALUES (22, '删除用户', 'user:delete', '', 13, 1, 0, '', '2018-10-28 22:33:03', '2018-10-28 22:33:03');
INSERT INTO `sys_permission` VALUES (23, '查询角色', 'role:view', '', 14, 1, 0, '', '2018-10-28 22:33:32', '2018-10-28 22:33:32');
INSERT INTO `sys_permission` VALUES (24, '添加角色', 'role:add', '', 14, 1, 0, '', '2018-10-28 22:33:52', '2018-10-28 22:33:51');
INSERT INTO `sys_permission` VALUES (25, '修改角色', 'role:edit', '', 14, 1, 0, '', '2018-10-28 22:34:13', '2018-10-28 22:34:12');
INSERT INTO `sys_permission` VALUES (26, '删除角色', 'role:delete', '', 14, 1, 0, '', '2018-10-28 22:34:32', '2018-10-28 22:34:32');
INSERT INTO `sys_permission` VALUES (27, '角色授权', 'role:auth', '', 14, 1, 0, '', '2018-10-28 22:35:12', '2018-10-28 22:35:11');
INSERT INTO `sys_permission` VALUES (28, '查询权限', 'permission:view', '', 15, 1, 0, '', '2018-10-28 22:35:46', '2018-10-28 22:35:45');
INSERT INTO `sys_permission` VALUES (29, '添加权限', 'permission:add', '', 15, 1, 0, '', '2018-10-28 22:36:11', '2018-10-28 22:36:10');
INSERT INTO `sys_permission` VALUES (30, '修改权限', 'permission:edit', '', 15, 1, 0, '', '2018-10-28 22:36:30', '2018-10-28 22:36:29');
INSERT INTO `sys_permission` VALUES (31, '删除权限', 'permission:delete', '', 15, 1, 0, '', '2018-10-28 22:36:50', '2018-10-28 22:36:50');
INSERT INTO `sys_permission` VALUES (32, '日志查询', 'log:view', '', 18, 1, 0, '', '2018-10-28 22:37:20', '2018-10-28 22:37:20');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `describes` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  `create_date` timestamp(0) NOT NULL,
  `update_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', '超级管理员', '0', '2018-10-28 22:42:35', '2018-10-28 22:46:21');
INSERT INTO `sys_role` VALUES (2, '管理员', '管理员', '0', '2018-10-28 22:42:50', '2018-10-28 22:43:41');
INSERT INTO `sys_role` VALUES (3, '普通用户', '普通用户', '0', '2018-10-28 23:32:15', '2018-10-28 23:32:15');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  `create_date` timestamp(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `permission_id`(`permission_id`) USING BTREE,
  CONSTRAINT `sys_role_permission_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `sys_role_permission_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`permission_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 403 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (240, 3, 7, '2018-10-29 13:37:49');
INSERT INTO `sys_role_permission` VALUES (241, 3, 8, '2018-10-29 13:37:49');
INSERT INTO `sys_role_permission` VALUES (242, 3, 9, '2018-10-29 13:37:49');
INSERT INTO `sys_role_permission` VALUES (243, 3, 10, '2018-10-29 13:37:49');
INSERT INTO `sys_role_permission` VALUES (244, 3, 11, '2018-10-29 13:37:49');
INSERT INTO `sys_role_permission` VALUES (245, 3, 3, '2018-10-29 13:37:49');
INSERT INTO `sys_role_permission` VALUES (246, 3, 5, '2018-10-29 13:37:49');
INSERT INTO `sys_role_permission` VALUES (247, 3, 6, '2018-10-29 13:37:49');
INSERT INTO `sys_role_permission` VALUES (248, 3, 4, '2018-10-29 13:37:49');
INSERT INTO `sys_role_permission` VALUES (309, 1, 1, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (310, 1, 2, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (311, 1, 3, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (312, 1, 5, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (313, 1, 4, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (314, 1, 6, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (315, 1, 10, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (316, 1, 11, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (317, 1, 7, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (318, 1, 8, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (319, 1, 9, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (320, 1, 12, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (321, 1, 14, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (322, 1, 23, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (323, 1, 24, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (324, 1, 25, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (325, 1, 26, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (326, 1, 27, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (327, 1, 15, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (328, 1, 28, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (329, 1, 29, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (330, 1, 30, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (331, 1, 31, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (332, 1, 13, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (333, 1, 19, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (334, 1, 20, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (335, 1, 21, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (336, 1, 22, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (337, 1, 16, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (338, 1, 17, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (339, 1, 18, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (340, 1, 32, '2018-10-29 23:41:38');
INSERT INTO `sys_role_permission` VALUES (371, 2, 1, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (372, 2, 2, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (373, 2, 3, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (374, 2, 5, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (375, 2, 4, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (376, 2, 6, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (377, 2, 10, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (378, 2, 11, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (379, 2, 7, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (380, 2, 8, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (381, 2, 9, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (382, 2, 12, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (383, 2, 14, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (384, 2, 23, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (385, 2, 24, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (386, 2, 25, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (387, 2, 26, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (388, 2, 27, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (389, 2, 15, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (390, 2, 28, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (391, 2, 29, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (392, 2, 30, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (393, 2, 31, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (394, 2, 13, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (395, 2, 19, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (396, 2, 20, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (397, 2, 21, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (398, 2, 22, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (399, 2, 16, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (400, 2, 17, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (401, 2, 18, '2018-10-30 22:42:58');
INSERT INTO `sys_role_permission` VALUES (402, 2, 32, '2018-10-30 22:42:58');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `nick_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `avatar` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '男',
  `phone` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `state` int(1) NOT NULL DEFAULT 0,
  `create_date` timestamp(0) NOT NULL,
  `update_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'superAdmin', 'e74a9850cc7bede5841190cfef139356', '超级管理员', NULL, '男', '15732160702', NULL, 0, '2018-10-28 22:55:39', '2018-10-28 22:57:13');
INSERT INTO `sys_user` VALUES (2, 'admin', '3fed7a346e430ea4c2aa10250928f4de', '管理员', NULL, '女', '15732160702', NULL, 0, '2018-10-28 22:53:57', '2018-10-29 20:48:11');
INSERT INTO `sys_user` VALUES (3, 'user', 'dd957e81b004227af3e0aa4bde869b25', '普通用户', NULL, '男', '15732160702', NULL, 0, '2018-10-29 13:48:41', '2018-10-29 13:51:15');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  `create_date` timestamp(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `sys_user_role_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `sys_user_role_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (2, 1, 1, '2018-10-28 23:03:35');
INSERT INTO `sys_user_role` VALUES (16, 3, 3, '2018-10-29 13:52:39');
INSERT INTO `sys_user_role` VALUES (20, 2, 2, '2018-10-29 14:15:22');

SET FOREIGN_KEY_CHECKS = 1;
