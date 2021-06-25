CREATE SCHEMA IF NOT EXISTS"yuepao";
CREATE EXTENSION IF NOT EXISTS"uuid-ossp";

/**
 * 国家
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
(E'ROLE_ALMIGHTY'),-- 万能的
(E'ROLE_FINANCE'),-- 财务
(E'ROLE_YONGHU');-- 用户

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

/**
 * 地区
 */
CREATE TABLE"yuepao"."di_qu"(
	"id"serial2 PRIMARY KEY,
	"xian_shi_ming"varchar NOT NULL UNIQUE
);
COMMENT ON TABLE"yuepao"."di_qu"IS'地区';
COMMENT ON COLUMN"yuepao"."di_qu"."id"IS'主键';
COMMENT ON COLUMN"yuepao"."di_qu"."xian_shi_ming"IS'县市名(i18n 键)';
--
INSERT INTO"yuepao"."di_qu"("xian_shi_ming")VALUES
(E'TAI_BEI'),(E'JI_LONG'),--北基
(E'TAO_YUAN'),(E'XIN_ZHU'),(E'MIAO_LI'),--桃竹苗
(E'TAI_ZHONG'),(E'ZHANG_HUA'),(E'NAN_TOU'),--中彰投
(E'YUN_LIN'),(E'JIA_YI'),(E'TAI_NAN'),--云嘉南
(E'GAO_XIONG'),(E'PING_DONG'),--高屏
(E'YI_LAN'),(E'HUA_LIAN'),(E'TAI_DONG'),--宜花东
(E'MA_ZU'),(E'JIN_MEN'),(E'PENG_HU');--马金澎

CREATE TYPE"yuepao"."ti_xing"AS ENUM(
	'PING_JUN',
	'MIAO_TIAO',
	'YUN_DONG',
	'QU_XIAN',
	'WEI_PANG',
	'FENG_MAN'
);
COMMENT ON TYPE"yuepao"."ti_xing"IS'体型';

CREATE TYPE"yuepao"."xue_li"AS ENUM(
	'GUO_XIAO',
	'GUO_ZHONG',
	'GAO_ZHONG',
	'GAO_ZHI',
	'ZHUAN_KE',
	'DA_XUE',
	'YAN_JIU_SUO'
);
COMMENT ON TYPE"yuepao"."xue_li"IS'学历';

CREATE TYPE"yuepao"."hun_yin"AS ENUM(
	'DAN_SHEN',
	'SI_HUI',
	'YI_HUN'
);
COMMENT ON TYPE"yuepao"."hun_yin"IS'婚姻';

CREATE TYPE"yuepao"."chou_yan"AS ENUM(
	'BU_CHOU_YAN',
	'OU_ER_CHOU',
	'JING_CHANG_CHOU'
);
COMMENT ON TYPE"yuepao"."chou_yan"IS'抽烟';

CREATE TYPE"yuepao"."yin_jiu"AS ENUM(
	'BU_HE_JIU',
	'OU_ER_HE',
	'JING_CHANG_HE'
);
COMMENT ON TYPE"yuepao"."yin_jiu"IS'饮酒';

ALTER TABLE"yuepao"."qing_ren"
ADD COLUMN"huo_yue"timestamptz,--活跃
ADD COLUMN"dao_qi"timestamptz,--到期
ADD COLUMN"di_qu"int2 REFERENCES"yuepao"."di_qu"("id")ON DELETE RESTRICT ON UPDATE CASCADE,--地区
ADD COLUMN"ni_cheng"varchar,--昵称
ADD COLUMN"sheng_ri"date,--生日
ADD COLUMN"xing_bie"bool,--性别
ADD COLUMN"da_tou"text,--大头
ADD COLUMN"zi_jie"text,--自介
ADD COLUMN"ha_luo"text,--哈啰
ADD COLUMN"ti_xing" "yuepao"."ti_xing",--体型
ADD COLUMN"shen_gao"int2,--身高
ADD COLUMN"ti_zhong"int2,--体重
ADD COLUMN"xue_li" "yuepao"."xue_li",--学历
ADD COLUMN"hun_yin" "yuepao"."hun_yin",--婚姻
ADD COLUMN"zhi_ye"varchar,--职业
ADD COLUMN"chou_yan" "yuepao"."chou_yan",--抽烟
ADD COLUMN"yin_jiu" "yuepao"."yin_jiu",--饮酒
ADD COLUMN"tian_jia_hao_you"text,--添加好友
ADD COLUMN"li_xiang_dui_xiang"text;--理想对象
COMMENT ON COLUMN"yuepao"."qing_ren"."huo_yue"IS'活跃';
COMMENT ON COLUMN"yuepao"."qing_ren"."dao_qi"IS'到期';
COMMENT ON COLUMN"yuepao"."qing_ren"."di_qu"IS'地区';
COMMENT ON COLUMN"yuepao"."qing_ren"."ni_cheng"IS'昵称';
COMMENT ON COLUMN"yuepao"."qing_ren"."sheng_ri"IS'生日';
COMMENT ON COLUMN"yuepao"."qing_ren"."xing_bie"IS'性别';
COMMENT ON COLUMN"yuepao"."qing_ren"."da_tou"IS'大头';
COMMENT ON COLUMN"yuepao"."qing_ren"."zi_jie"IS'自介';
COMMENT ON COLUMN"yuepao"."qing_ren"."ha_luo"IS'哈啰';
COMMENT ON COLUMN"yuepao"."qing_ren"."ti_xing"IS'体型';
COMMENT ON COLUMN"yuepao"."qing_ren"."shen_gao"IS'身高';
COMMENT ON COLUMN"yuepao"."qing_ren"."ti_zhong"IS'体重';
COMMENT ON COLUMN"yuepao"."qing_ren"."xue_li"IS'学历';
COMMENT ON COLUMN"yuepao"."qing_ren"."hun_yin"IS'婚姻';
COMMENT ON COLUMN"yuepao"."qing_ren"."zhi_ye"IS'职业';
COMMENT ON COLUMN"yuepao"."qing_ren"."chou_yan"IS'抽烟';
COMMENT ON COLUMN"yuepao"."qing_ren"."yin_jiu"IS'饮酒';
COMMENT ON COLUMN"yuepao"."qing_ren"."tian_jia_hao_you"IS'添加好友';
COMMENT ON COLUMN"yuepao"."qing_ren"."li_xiang_dui_xiang"IS'理想对象';

/**
 * 绿界
 */
CREATE TABLE"lu_jie"(
	"id"serial8 PRIMARY KEY
);
COMMENT ON TABLE"lu_jie"IS'绿界';
COMMENT ON COLUMN"lu_jie"."id"IS'主键';

<<<<<<< HEAD
/**
 * 生活照
 */
CREATE TABLE"yuepao"."sheng_huo_zhao"(
	"id"serial2 PRIMARY KEY,
        "qing_ren"int NOT NULL REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"shi_bie_ma"uuid NOT NULL UNIQUE,
	"shi_chuo"timestamptz NOT NULL DEFAULT"now"()
);
COMMENT ON TABLE"yuepao"."sheng_huo_zhao"IS'生活照';
COMMENT ON COLUMN"yuepao"."sheng_huo_zhao"."id"IS'主鍵';
COMMENT ON COLUMN"yuepao"."sheng_huo_zhao"."qing_ren"IS'情人';
COMMENT ON COLUMN"yuepao"."sheng_huo_zhao"."shi_bie_ma"IS'識別碼';
COMMENT ON COLUMN"yuepao"."sheng_huo_zhao"."shi_chuo"IS'時戳';

=======
CREATE TYPE"xing_wei"AS ENUM(
	'YUE_FEI',--月费
	'CHU_ZHI',--储值
	'JI_WO_LAI',--给我赖
	'JI_NI_LAI',--给你赖
	'DA_ZHAO_HU',--打招呼
	'KAN_GUO_WO',--看过我
	'CHE_MA_FEI'--车马费
);
COMMENT ON TYPE"xing_wei"IS'行为';

/**
 * 历程
 */
CREATE TABLE"yuepao"."li_cheng"(
	"id"serial8 PRIMARY KEY,
	"zhu_dong_de"int NOT NULL REFERENCES"yuepao"."qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,--主动的
	"bei_dong_de"int REFERENCES"yuepao"."qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,--被动的
	"xing_wei" "yuepao"."xing_wei",--行为
	"shi_chuo"timestamptz,--时戳
	"dian_shu"int2,--点数
	"lu_jie"int8 REFERENCES"yuepao"."lu_jie"("id")ON DELETE RESTRICT ON UPDATE CASCADE,--绿界
	"zhao_hu_yu"text--招呼语
);
COMMENT ON TABLE"yuepao"."li_cheng"IS'历程';
COMMENT ON COLUMN"yuepao"."li_cheng"."id"IS'主键';
COMMENT ON COLUMN"yuepao"."li_cheng"."zhu_dong_de"IS'主动的';
COMMENT ON COLUMN"yuepao"."li_cheng"."bei_dong_de"IS'被动的';
COMMENT ON COLUMN"yuepao"."li_cheng"."xing_wei"IS'行为';
COMMENT ON COLUMN"yuepao"."li_cheng"."shi_chuo"IS'时戳';
COMMENT ON COLUMN"yuepao"."li_cheng"."dian_shu"IS'点数';
COMMENT ON COLUMN"yuepao"."li_cheng"."lu_jie"IS'绿界';
COMMENT ON COLUMN"yuepao"."li_cheng"."zhao_hu_yu"IS'招呼语';
>>>>>>> feature-inpay2
