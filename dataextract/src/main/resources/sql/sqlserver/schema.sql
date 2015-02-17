-- ----------------------------
-- Table structure for ` sys_area_city`
-- ----------------------------
CREATE TABLE [ sys_area_city] (
[id] int NOT NULL IDENTITY(1,1) ,
[city_id] nvarchar(4) NOT NULL ,
[city] nvarchar(50) NOT NULL ,
[province_id] nvarchar(2) NOT NULL 
)
GO
-- ----------------------------
-- Primary Key structure for table [ sys_area_city]
-- ----------------------------
ALTER TABLE [ sys_area_city] ADD PRIMARY KEY ([id])
GO



-- ----------------------------
-- Table structure for [ sys_area_county]
-- ----------------------------
CREATE TABLE [ sys_area_county] (
[id] int NOT NULL IDENTITY(1,1) ,
[county_id] nvarchar(6) NOT NULL ,
[county] nvarchar(50) NOT NULL ,
[city_id] nvarchar(4) NOT NULL 
)
GO
-- ----------------------------
-- Primary Key structure for table [ sys_area_county]
-- ----------------------------
ALTER TABLE [ sys_area_county] ADD PRIMARY KEY ([id])
GO



-- ----------------------------
-- Table structure for [ sys_area_province]
-- ----------------------------
CREATE TABLE [ sys_area_province] (
[id] int NOT NULL IDENTITY(1,1) ,
[province_id] nvarchar(2) NOT NULL ,
[province] nvarchar(50) NOT NULL ,
[province_for_short] nvarchar(20) NOT NULL ,
[province_for_bus_no] nvarchar(20) NULL 
)
GO
-- ----------------------------
-- Primary Key structure for table [ sys_area_province]
-- ----------------------------
ALTER TABLE [ sys_area_province] ADD PRIMARY KEY ([id])
GO



-- ----------------------------
-- Table structure for [ sys_attachment]
-- ----------------------------
CREATE TABLE [ sys_attachment] (
[id] bigint NOT NULL IDENTITY(1,1) ,
[file_name] nvarchar(500) NOT NULL ,
[file_path] nvarchar(500) NOT NULL ,
[rid] nvarchar(100) NOT NULL 
)
-- ----------------------------
-- Indexes structure for table  sys_attachment
-- ----------------------------
CREATE INDEX [ sys_attachment_rid_index] ON [ sys_attachment]
([rid] ASC) 
GO
-- ----------------------------
-- Primary Key structure for table [ sys_attachment]
-- ----------------------------
ALTER TABLE [ sys_attachment] ADD PRIMARY KEY ([id])
GO


-- ----------------------------
-- Table structure for [ sys_parameter]
-- ----------------------------
CREATE TABLE [ sys_parameter] (
[id] bigint NOT NULL IDENTITY(1,1) ,
[uuid] nvarchar(255) NOT NULL DEFAULT '',
[category] nvarchar(255)  NULL,
[subcategory] nvarchar(255)  NULL,
[name] nvarchar(255) NOT NULL,
[value] nvarchar(255) NOT NULL,
[remark] nvarchar(255)  NULL,
[short_name] nvarchar(255)  NULL,
[sort] int  NULL,
[parent_id] bigint NULL
)
-- ----------------------------
-- Indexes structure for table  sys_parameter
-- ----------------------------
CREATE UNIQUE INDEX [ sys_parameter_value_index] ON [ sys_parameter]
([value] ASC) 
GO
-- ----------------------------
-- Primary Key structure for table [ sys_parameter]
-- ----------------------------
ALTER TABLE [ sys_parameter] ADD PRIMARY KEY ([id])
GO


-- ----------------------------
-- Table structure for ` sys_permission`
-- ----------------------------
CREATE TABLE [ sys_permission] (
[id] bigint NOT NULL IDENTITY(1,1) ,
[pid] bigint NOT NULL DEFAULT ((0)) ,
[ckey] nvarchar(255) NOT NULL ,
[pkey] nvarchar(255) NOT NULL ,
[name] nvarchar(255) NOT NULL ,
[value] nvarchar(255) NOT NULL ,
[permission_type] nvarchar(255) NULL,
[sort] int DEFAULT ((0))
)
-- ----------------------------
-- Records of  sys_permission
-- ----------------------------
GO
SET IDENTITY_INSERT [ sys_permission] ON
GO
INSERT INTO [ sys_permission] ([id], [pid], [ckey], [pkey], [name], [value], [permission_type],[sort]) VALUES ('1', '0', 'bc914d00-6837-492c-8f99-471b2176bdb4', '0', '系统设置', 'menu:setting', '2', '0');
GO
INSERT INTO [ sys_permission] ([id], [pid], [ckey], [pkey], [name], [value], [permission_type],[sort]) VALUES ('2', '1', 'd2274a49-74a6-4d0c-b7a8-0f16d125b4c0', 'bc914d00-6837-492c-8f99-471b2176bdb4', '用户管理', 'menu:user', '2', '0');
GO
INSERT INTO [ sys_permission] ([id], [pid], [ckey], [pkey], [name], [value], [permission_type],[sort]) VALUES ('3', '1', 'd271d733-4e57-4181-9f5f-730b53b4edc8', 'bc914d00-6837-492c-8f99-471b2176bdb4', '角色管理', 'menu:role', '2', '0');
GO
INSERT INTO [ sys_permission] ([id], [pid], [ckey], [pkey], [name], [value], [permission_type],[sort]) VALUES ('4', '1', '92c2b77c-6e77-4eaf-aa43-cfba4c3da583', 'bc914d00-6837-492c-8f99-471b2176bdb4', '权限管理', 'menu:permisson', '2', '0');
GO
INSERT INTO [ sys_permission] ([id], [pid], [ckey], [pkey], [name], [value], [permission_type],[sort]) VALUES ('5', '1', 'c66f2469-ce0e-4e52-9d31-c9940df79ebe', 'bc914d00-6837-492c-8f99-471b2176bdb4', '参数管理', 'menu:parameter', '2', '0');
GO
SET IDENTITY_INSERT [ sys_permission] OFF
GO
-- ----------------------------
-- Indexes structure for table  sys_permission
-- ----------------------------
CREATE INDEX [ sys_permission_pid_index] ON [ sys_permission]
([pid] ASC) 
GO
CREATE UNIQUE INDEX [ sys_permission_value_index] ON [ sys_permission]
([value] ASC) 
GO
CREATE INDEX [ sys_permission_type_index] ON [ sys_permission]
([permission_type] ASC) 
GO

-- ----------------------------
-- Primary Key structure for table [ sys_permission]
-- ----------------------------
ALTER TABLE [ sys_permission] ADD PRIMARY KEY ([id])
GO


-- ----------------------------
-- Table structure for [ sys_role]
-- ----------------------------
CREATE TABLE [ sys_role] (
[id] bigint NOT NULL IDENTITY(1,1) ,
[name] nvarchar(255) NOT NULL ,
[role_type] nvarchar(255) NULL ,
[pid] bigint NULL ,
[remark] text NULL 
)
GO
DBCC CHECKIDENT(N'[ sys_role]', RESEED, 1)
GO
-- ----------------------------
-- Records of  sys_role
-- ----------------------------
SET IDENTITY_INSERT [ sys_role] ON
GO
INSERT INTO [ sys_role] ([id], [name], [role_type], [pid], [remark]) VALUES (N'1', N'超级管理员', N'cb6e6022-955e-4978-ae2c-afa392be5ebf', N'0', N'拥有所有权限');
GO
SET IDENTITY_INSERT [ sys_role] OFF
GO
-- ----------------------------
-- Primary Key structure for table [ sys_role]
-- ----------------------------
ALTER TABLE [ sys_role] ADD PRIMARY KEY ([id])
GO

 
-- ----------------------------
-- Table structure for [ sys_role_permission]
-- ----------------------------
CREATE TABLE [ sys_role_permission] (
[role_id] bigint NOT NULL ,
[permission] nvarchar(255) NOT NULL 
)
GO
-- ----------------------------
-- Records of  sys_role_permission
-- ----------------------------
INSERT INTO [ sys_role_permission] ([role_id], [permission]) VALUES (N'1', N'menu:setting');
GO
INSERT INTO [ sys_role_permission] ([role_id], [permission]) VALUES (N'1', N'menu:user');
GO
INSERT INTO [ sys_role_permission] ([role_id], [permission]) VALUES (N'1', N'menu:role');
GO
INSERT INTO [ sys_role_permission] ([role_id], [permission]) VALUES (N'1', N'menu:permisson');
GO
INSERT INTO [ sys_role_permission] ([role_id], [permission]) VALUES (N'1', N'menu:parameter');
GO
-- ----------------------------
-- Indexes structure for table  sys_role_permission
-- ----------------------------
CREATE INDEX [IX_ sys_role_permission] ON [ sys_role_permission]
([role_id] ASC, [permission] ASC) 
GO 

-- ----------------------------
-- Table structure for ` sys_user`
-- ----------------------------
CREATE TABLE [ sys_user] (
[id] bigint NOT NULL IDENTITY(1,1) ,
[login_name] nvarchar(18) NOT NULL ,
[name] nvarchar(200) NULL ,
[password] nvarchar(100) NULL ,
[salt] nvarchar(64) NULL ,
[area] nvarchar(10) NULL ,
[user_type] int NOT NULL ,
[register_date] varchar(100) NULL ,
[user_status] int NULL ,
[theme] varchar(100) NOT NULL DEFAULT ('bootstrap') ,
)
GO
DBCC CHECKIDENT(N'[ sys_user]', RESEED, 2)
GO
-- ----------------------------
-- Records of  sys_user
-- ----------------------------
SET IDENTITY_INSERT [ sys_user] ON
GO
INSERT INTO [ sys_user] ([id], [login_name], [name], [password], [salt], [area], [user_type], [register_date], [user_status], [theme]) VALUES 
(N'1', N'admin', N'超级管理员', N'af27c8816506bf1b6705b50866f9cde0a67401cb', N'eb69619a75232f52', N'371301', N'0',   N'', N'0', N'bootstrap');
GO
INSERT INTO [ sys_user] ([id], [login_name], [name], [password], [salt], [area], [user_type], [register_date], [user_status], [theme]) VALUES 
(N'2', N'user', N'user', N'fc4b2d83355003aa9649ddcb47a38a4f957636e4', N'cba97144a760fd39', N'371301', N'0',  N'',  N'0', N'bootstrap');
GO
SET IDENTITY_INSERT [ sys_user] OFF
GO
-- ----------------------------
-- Indexes structure for table  sys_user
-- ----------------------------
CREATE INDEX [ sys_user_org_code_index] ON [ sys_user]
([org_code] ASC) 
GO
-- ----------------------------
-- Primary Key structure for table [ sys_user]
-- ----------------------------
ALTER TABLE [ sys_user] ADD PRIMARY KEY ([id])
GO



-- ----------------------------
-- Table structure for [ sys_user_role]
-- ----------------------------
CREATE TABLE [ sys_user_role] (
[user_id] bigint NOT NULL ,
[role_id] bigint NOT NULL 
)
GO
-- ----------------------------
-- Records of  sys_user_role
-- ----------------------------
INSERT INTO [ sys_user_role] ([user_id], [role_id]) VALUES (N'1', N'1');
GO
-- ----------------------------
-- Indexes structure for table  sys_user_role
-- ----------------------------
CREATE INDEX [ sys_user_role_index] ON [ sys_user_role]
([user_id] ASC, [role_id] ASC) 
GO