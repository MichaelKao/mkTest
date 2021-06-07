CREATE SCHEMA IF NOT EXISTS"yuepao";
CREATE EXTENSION IF NOT EXISTS"uuid-ossp";

/**
 * 國家
 */
CREATE TABLE"yuepao"."guo_jia"(
	"id"serial2 PRIMARY KEY,
	"guo_ma"varchar NOT NULL UNIQUE,
	"guo_ming"varchar NOT NULL UNIQUE
);
COMMENT ON TABLE"yuepao"."guo_jia"IS'国家';
COMMENT ON COLUMN"yuepao"."guo_jia"."id"IS'主键';
COMMENT ON COLUMN"yuepao"."guo_jia"."guo_ma"IS'国码';
COMMENT ON COLUMN"yuepao"."guo_jia"."guo_ming"IS'国名(i18n 键)';
--
INSERT INTO"yuepao"."guo_jia"("guo_ma","guo_ming")VALUES
(E'886',E'Taiwan');

/**
 * 情人
 */
CREATE TABLE"yuepao"."qing_ren"(
	"id"serial PRIMARY KEY,
	"shi_bie_ma"uuid NOT NULL UNIQUE,
	"guo_jia"int2 NOT NULL REFERENCES"yuepao"."guo_jia"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"zhang_hao"varchar NOT NULL,
	UNIQUE("guo_jia","zhang_hao"),
	"mi_ma"text
	-- "ni_cheng"varchar,
	-- "sheng_ri"date,
	-- "xing_bie"bool,
	-- "da_tou"text,
	-- "zi_jie"text,
	-- "ha_luo"text,
	-- "ti_xing"enum,
	-- "shen_gao"int2,
	-- "ti_zhong"int2,
	-- "xue_li"enum,
	-- "zhi_ye"enum,
	-- "chou_yan"enum,
	-- "yin_jiu"enum,
	-- "tian_jia_hao_you"text
);
COMMENT ON TABLE"yuepao"."qing_ren"IS'情人';
COMMENT ON COLUMN"yuepao"."qing_ren"."id"IS'主键';
COMMENT ON COLUMN"yuepao"."qing_ren"."shi_bie_ma"IS'识别码';
COMMENT ON COLUMN"yuepao"."qing_ren"."guo_jia"IS'国家';
COMMENT ON COLUMN"yuepao"."qing_ren"."zhang_hao"IS'帐号(手机号)';
COMMENT ON COLUMN"yuepao"."qing_ren"."mi_ma"IS'密码';
-- COMMENT ON COLUMN"yuepao"."qing_ren"."ni_cheng"IS'昵称';
-- COMMENT ON COLUMN"yuepao"."qing_ren"."sheng_ri"IS'生日';
-- COMMENT ON COLUMN"yuepao"."qing_ren"."xing_bie"IS'性别';
-- COMMENT ON COLUMN"yuepao"."qing_ren"."da_tou"IS'大头';
-- COMMENT ON COLUMN"yuepao"."qing_ren"."zi_jie"IS'自介';
-- COMMENT ON COLUMN"yuepao"."qing_ren"."ha_luo"IS'哈啰';
-- COMMENT ON COLUMN"yuepao"."qing_ren"."ti_xing"IS'体型';
-- COMMENT ON COLUMN"yuepao"."qing_ren"."shen_gao"IS'身高';
-- COMMENT ON COLUMN"yuepao"."qing_ren"."ti_zhong"IS'体重';
-- COMMENT ON COLUMN"yuepao"."qing_ren"."xue_li"IS'学历';
-- COMMENT ON COLUMN"yuepao"."qing_ren"."zhi_ye"IS'职业';
-- COMMENT ON COLUMN"yuepao"."qing_ren"."chou_yan"IS'抽烟';
-- COMMENT ON COLUMN"yuepao"."qing_ren"."yin_jiu"IS'饮酒';
-- COMMENT ON COLUMN"yuepao"."qing_ren"."tian_jia_hao_you"IS'添加好友';
--
INSERT INTO"yuepao"."qing_ren"("shi_bie_ma","guo_jia","zhang_hao")VALUES
('87115257-1d9e-45ae-a803-cd6d1083d3e2','1','908286449'),
('b8b82059-7b50-4c80-8a5c-d9112b18fc96','1','975312468');

/**
 * 激活
 */
CREATE TABLE"yuepao"."ji_huo"(
	"id"int PRIMARY KEY REFERENCES"yuepao"."qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"zi_fu_chuan"varchar UNIQUE,
	"dao_qi"timestamptz NOT NULL DEFAULT"now"(),
	"shi_chuo"timestamptz
);
COMMENT ON TABLE"yuepao"."ji_huo"IS'激活';
COMMENT ON COLUMN"yuepao"."ji_huo"."id"IS'主键';
COMMENT ON COLUMN"yuepao"."ji_huo"."zi_fu_chuan"IS'字符串';
COMMENT ON COLUMN"yuepao"."ji_huo"."dao_qi"IS'到期';
COMMENT ON COLUMN"yuepao"."ji_huo"."shi_chuo"IS'时戳';

/**
 * LINE User Profile
 */
CREATE TABLE"yuepao"."line_user_profile"(
	"id"int PRIMARY KEY REFERENCES"yuepao"."qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"display_name"varchar,
	"user_id"varchar UNIQUE,
	"language"varchar,
	"picture_url"text,
	"status_message"text
);
COMMENT ON TABLE"yuepao"."line_user_profile"IS'LINE User Profile';
COMMENT ON COLUMN"yuepao"."line_user_profile"."id"IS'主键';
COMMENT ON COLUMN"yuepao"."line_user_profile"."display_name"IS'名字';
COMMENT ON COLUMN"yuepao"."line_user_profile"."user_id"IS'用户识别码';
COMMENT ON COLUMN"yuepao"."line_user_profile"."language"IS'语系';
COMMENT ON COLUMN"yuepao"."line_user_profile"."picture_url"IS'头像';
COMMENT ON COLUMN"yuepao"."line_user_profile"."status_message"IS'状态消息';
--
INSERT INTO"yuepao"."line_user_profile"("id","display_name")VALUES
('2','第二位');

/**
 * 身份
 */
CREATE TABLE"yuepao"."shen_fen"(
	"id"serial2 PRIMARY KEY,
	"jue_se"varchar NOT NULL UNIQUE
);
COMMENT ON TABLE"yuepao"."shen_fen"IS'身份';
COMMENT ON COLUMN"yuepao"."shen_fen"."id"IS'主键';
COMMENT ON COLUMN"yuepao"."shen_fen"."jue_se"IS'角色';
--
INSERT INTO"yuepao"."shen_fen"("jue_se")VALUES
(E'ROLE_ALMIGHTY'),
(E'ROLE_FINANCE'),
(E'ROLE_YONGHU');

/**
 * 授权
 */
CREATE TABLE"yuepao"."shou_quan"(
	"qing_ren"int NOT NULL REFERENCES"yuepao"."qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"shen_fen"int2 NOT NULL REFERENCES"yuepao"."shen_fen"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	PRIMARY KEY("qing_ren","shen_fen")
);
COMMENT ON TABLE"yuepao"."shou_quan"IS'授权';
COMMENT ON COLUMN"yuepao"."shou_quan"."qing_ren"IS'情人';
COMMENT ON COLUMN"yuepao"."shou_quan"."shen_fen"IS'身份';
--
INSERT INTO"yuepao"."shou_quan"("qing_ren","shen_fen")VALUES
('1','1'),('1','2');

/**
 * The default `users` table of Spring Security JDBC authentication
 */
CREATE OR REPLACE VIEW"yuepao"."users"AS
SELECT
"lover"."id",
("country"."guo_ma"||"lover"."zhang_hao")::"varchar"AS"username",
"lover"."mi_ma"::"varchar"AS"password",
("activation"."zi_fu_chuan"IS NULL AND"activation"."shi_chuo"IS NOT NULL)AS"enabled"
FROM"yuepao"."qing_ren"AS"lover"
LEFT JOIN"yuepao"."guo_jia"AS"country"
	ON"country"."id"="lover"."guo_jia"
LEFT JOIN"yuepao"."ji_huo"AS"activation"
	ON"activation"."id"="lover"."id"
ORDER BY
"country"."guo_ma",
"lover"."zhang_hao";

/**
 * The default `authorities` table of Spring Security JDBC authentication
 */
CREATE OR REPLACE VIEW"yuepao"."authorities"AS
SELECT
"LOVER"."username",
"ROLE"."jue_se"AS"authority"
FROM"yuepao"."shou_quan"AS"JT"
LEFT JOIN(
	SELECT
	"lover"."id",
	("country"."guo_ma"||"lover"."zhang_hao")AS"username"
	FROM"yuepao"."qing_ren"AS"lover"
	LEFT JOIN"yuepao"."guo_jia"AS"country"
		ON"country"."id"="lover"."guo_jia"
)AS"LOVER"
	ON"LOVER"."id"="JT"."qing_ren"
LEFT JOIN"yuepao"."shen_fen"AS"ROLE"
	ON"ROLE"."id"="JT"."shen_fen"
ORDER BY
"LOVER"."id",
"ROLE"."id";

/*
CREATE TYPE"zhang_hao_qu_bie"AS ENUM(
	'XIAO_FEI_ZHE',
	'ZHI_BO_ZHU',
	'MOU_SI_REN',
	'JIA_ZHANG_HAO'
);
COMMENT ON TYPE"zhang_hao_qu_bie"IS'帐号区别';
*/
