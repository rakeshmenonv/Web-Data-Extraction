-- ----------------------------
-- Table structure for ` sys_area_city`
-- ----------------------------
DROP TABLE IF EXISTS ` sys_area_city`;
CREATE TABLE ` sys_area_city` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city_id` varchar(4) NOT NULL,
  `city` varchar(50) NOT NULL,
  `province_id` varchar(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ` sys_area_county`
-- ----------------------------
DROP TABLE IF EXISTS ` sys_area_county`;
CREATE TABLE ` sys_area_county` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `county_id` varchar(6) NOT NULL,
  `county` varchar(50) NOT NULL,
  `city_id` varchar(4) NOT NULL,
  `county_value` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for ` sys_area_province`
-- ----------------------------
DROP TABLE IF EXISTS ` sys_area_province`;
CREATE TABLE ` sys_area_province` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province_id` varchar(2) NOT NULL,
  `province` varchar(50) NOT NULL,
  `province_for_short` varchar(20) NOT NULL,
  `province_for_bus_no` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ` sys_attachment`
-- ----------------------------
DROP TABLE IF EXISTS ` sys_attachment`;
CREATE TABLE ` sys_attachment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(500) NOT NULL,
  `file_path` varchar(500) NOT NULL,
  `rid` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=628 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of  sys_attachment
-- ----------------------------

-- ----------------------------
-- Table structure for ` sys_organization`
-- ----------------------------
DROP TABLE IF EXISTS ` sys_organization`;
CREATE TABLE ` sys_organization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `unit_code` varchar(100) DEFAULT NULL,
  `unit_name` varchar(50) NOT NULL,
  `pid` int(11) DEFAULT NULL,
  `area` char(10) DEFAULT NULL,
  `remark` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of  sys_organization
-- ----------------------------

-- ----------------------------
-- Table structure for ` sys_parameter`
-- ----------------------------
DROP TABLE IF EXISTS ` sys_parameter`;
CREATE TABLE ` sys_parameter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(255) NOT NULL DEFAULT '',
  `category` varchar(255) DEFAULT NULL,
  `subcategory` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `short_name` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `parent_id` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=316 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of  sys_parameter
-- ----------------------------

-- ----------------------------
-- Table structure for ` sys_permission`
-- ----------------------------
DROP TABLE IF EXISTS ` sys_permission`;
CREATE TABLE ` sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pid` bigint(20) NOT NULL,
  `ckey` varchar(255) NOT NULL,
  `pkey` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `permission_type` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=194 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of  sys_permission
-- ----------------------------
INSERT INTO ` sys_permission` VALUES ('1', '0', 'bc914d00-6837-492c-8f99-471b2176bdb4', '0', '系统设置', 'menu:setting', '2', '0');
INSERT INTO ` sys_permission` VALUES ('2', '1', 'd2274a49-74a6-4d0c-b7a8-0f16d125b4c0', 'bc914d00-6837-492c-8f99-471b2176bdb4', '用户管理', 'menu:user', '2', '0');
INSERT INTO ` sys_permission` VALUES ('3', '1', 'd271d733-4e57-4181-9f5f-730b53b4edc8', 'bc914d00-6837-492c-8f99-471b2176bdb4', '角色管理', 'menu:role', '2', '0');
INSERT INTO ` sys_permission` VALUES ('4', '1', '92c2b77c-6e77-4eaf-aa43-cfba4c3da583', 'bc914d00-6837-492c-8f99-471b2176bdb4', '权限管理', 'menu:permisson', '2', '0');
INSERT INTO ` sys_permission` VALUES ('5', '1', 'c66f2469-ce0e-4e52-9d31-c9940df79ebe', 'bc914d00-6837-492c-8f99-471b2176bdb4', '参数管理', 'menu:parameter', '2', '0');

-- ----------------------------
-- Table structure for ` sys_role`
-- ----------------------------
DROP TABLE IF EXISTS ` sys_role`;
CREATE TABLE ` sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `role_type` varchar(255) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `remark` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of  sys_role
-- ----------------------------
INSERT INTO ` sys_role` VALUES ('1', '超级管理员', 'cb6e6022-955e-4978-ae2c-afa392be5ebf', '0', '拥有最高权限');

-- ----------------------------
-- Table structure for ` sys_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS ` sys_role_permission`;
CREATE TABLE ` sys_role_permission` (
  `role_id` bigint(20) NOT NULL,
  `permission` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of  sys_role_permission
-- ----------------------------
INSERT INTO ` sys_role_permission` VALUES ('1', 'menu:setting');
INSERT INTO ` sys_role_permission` VALUES ('1', 'menu:user');
INSERT INTO ` sys_role_permission` VALUES ('1', 'menu:role');
INSERT INTO ` sys_role_permission` VALUES ('1', 'menu:permisson');
INSERT INTO ` sys_role_permission` VALUES ('1', 'menu:parameter');

-- ----------------------------
-- Table structure for ` sys_user`
-- ----------------------------
DROP TABLE IF EXISTS ` sys_user`;
CREATE TABLE ` sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(18) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `salt` varchar(64) DEFAULT NULL,
  `area` varchar(10) DEFAULT NULL,
  `user_type` int(11) NOT NULL,
  `register_date` varchar(100) DEFAULT NULL,
  `user_status` int(11) DEFAULT '0',
  `theme` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of  sys_user
-- ----------------------------
INSERT INTO ` sys_user` VALUES ('1', 'admin', '超级管理员', 'af27c8816506bf1b6705b50866f9cde0a67401cb', 'eb69619a75232f52', '371401', '0',  '0', '0', 'bootstrap');
INSERT INTO ` sys_user` VALUES ('2', 'user', 'user', 'fc4b2d83355003aa9649ddcb47a38a4f957636e4', 'cba97144a760fd39', null, '0',  null, '0', 'bootstrap');

-- ----------------------------
-- Table structure for ` sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS ` sys_user_role`;
CREATE TABLE ` sys_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `user_id_index` (`user_id`),
  KEY `role_id_index` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of  sys_user_role
-- ----------------------------
INSERT INTO ` sys_user_role` VALUES ('1', '1');
