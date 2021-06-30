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
	"id"serial8 PRIMARY KEY,
	"session_id"varchar,
	--OrderInfo 订单资讯
	"MerchantTradeDate"varchar(20),
	"MerchantTradeNo"varchar(20),
	"TotalAmount"int,
	"ItemName"varchar(400),
	"TradeNo"varchar(20),
	"TradeAmt"int,
	"TradeDate"varchar(20),
	"PaymentType"varchar(20),
	"PaymentDate"varchar(20),
	"ChargeFee"int,
	"TradeStatus"varchar(8),
	--ATMInfo ATM 资讯
	"BankCode"varchar(3),
	"vAccount"varchar(16),
	"ATMAccBank"varchar(3),
	"ATMAccNo"varchar(5),
	--BarcodeInfo 超商条码资讯
	"Barcode1"varchar(20),
	"Barcode2"varchar(20),
	"Barcode3"varchar(20),
	"BarcodeInfoPayFrom"varchar(10),
	--CVSInfo 超商代码资讯
	"PaymentNo"varchar(14),
	"CVSInfoPayFrom"varchar(10),
	--CardInfo 信用卡资讯
	"AuthCode"varchar(6),
	"Gwsr"int,
	"Amount"int,
	"Stage"int,
	"Stast"int,
	"Staed"int,
	"Eci"int,
	"Card6No"varchar(6),
	"Card4No"varchar(4),
	"PeriodType"varchar(1),
	"Frequency"int2,
	"ExecTimes"int2,
	"PeriodAmount"int2,
	"ProcessDate"varchar(20),
	--ConsumerInfo 消费者资讯
	"MerchantMemberID"varchar(60),
	--特店自订栏位
	"CustomField"varchar(200)
);
COMMENT ON TABLE"yuepao"."lu_jie"IS'绿界';
COMMENT ON COLUMN"yuepao"."lu_jie"."id"IS'主键';
COMMENT ON COLUMN"yuepao"."lu_jie"."session_id"IS'分配给会话的标识符';
COMMENT ON COLUMN"yuepao"."lu_jie"."MerchantTradeDate"IS'订单资讯：厂商交易时间';
COMMENT ON COLUMN"yuepao"."lu_jie"."MerchantTradeNo"IS'订单资讯：特店交易编号';
COMMENT ON COLUMN"yuepao"."lu_jie"."TotalAmount"IS'订单资讯：交易金额';
COMMENT ON COLUMN"yuepao"."lu_jie"."ItemName"IS'订单资讯：商品名称';
COMMENT ON COLUMN"yuepao"."lu_jie"."TradeNo"IS'订单资讯：绿界交易编号';
COMMENT ON COLUMN"yuepao"."lu_jie"."TradeAmt"IS'订单资讯：交易金额';
COMMENT ON COLUMN"yuepao"."lu_jie"."TradeDate"IS'订单资讯：订单成立时间';
COMMENT ON COLUMN"yuepao"."lu_jie"."PaymentType"IS'订单资讯：付款方式';
COMMENT ON COLUMN"yuepao"."lu_jie"."PaymentDate"IS'订单资讯：付款时间';
COMMENT ON COLUMN"yuepao"."lu_jie"."ChargeFee"IS'订单资讯：手续费';
COMMENT ON COLUMN"yuepao"."lu_jie"."TradeStatus"IS'订单资讯：交易状态';
COMMENT ON COLUMN"yuepao"."lu_jie"."BankCode"IS'ATM 资讯：缴费银行代码';
COMMENT ON COLUMN"yuepao"."lu_jie"."vAccount"IS'ATM 资讯：缴费虚拟帐号';
COMMENT ON COLUMN"yuepao"."lu_jie"."ATMAccBank"IS'ATM 资讯：付款人银行代码';
COMMENT ON COLUMN"yuepao"."lu_jie"."ATMAccNo"IS'ATM 资讯：付款人银行帐号后五码';
COMMENT ON COLUMN"yuepao"."lu_jie"."Barcode1"IS'超商条码资讯：条码第一段号码';
COMMENT ON COLUMN"yuepao"."lu_jie"."Barcode2"IS'超商条码资讯：条码第二段号码';
COMMENT ON COLUMN"yuepao"."lu_jie"."Barcode3"IS'超商条码资讯：条码第三段号码';
COMMENT ON COLUMN"yuepao"."lu_jie"."BarcodeInfoPayFrom"IS'超商条码资讯：缴费超商';
COMMENT ON COLUMN"yuepao"."lu_jie"."PaymentNo"IS'超商代码资讯：缴费代码';
COMMENT ON COLUMN"yuepao"."lu_jie"."CVSInfoPayFrom"IS'超商代码资讯：缴费超商';
COMMENT ON COLUMN"yuepao"."lu_jie"."AuthCode"IS'信用卡资讯：银行授权码';
COMMENT ON COLUMN"yuepao"."lu_jie"."Gwsr"IS'信用卡资讯：授权交易单号';
COMMENT ON COLUMN"yuepao"."lu_jie"."Amount"IS'信用卡资讯：金额';
COMMENT ON COLUMN"yuepao"."lu_jie"."Stage"IS'信用卡资讯：分期期数';
COMMENT ON COLUMN"yuepao"."lu_jie"."Stast"IS'信用卡资讯：首期金额';
COMMENT ON COLUMN"yuepao"."lu_jie"."Staed"IS'信用卡资讯：各期金额';
COMMENT ON COLUMN"yuepao"."lu_jie"."Eci"IS'信用卡资讯：3D(VBV) 回传值';
COMMENT ON COLUMN"yuepao"."lu_jie"."Card6No"IS'信用卡资讯：信用卡卡号前六码';
COMMENT ON COLUMN"yuepao"."lu_jie"."Card4No"IS'信用卡资讯：信用卡卡号末四码';
COMMENT ON COLUMN"yuepao"."lu_jie"."PeriodType"IS'信用卡资讯：定期定额周期种类';
COMMENT ON COLUMN"yuepao"."lu_jie"."Frequency"IS'信用卡资讯：定期定额执行频率';
COMMENT ON COLUMN"yuepao"."lu_jie"."ExecTimes"IS'信用卡资讯：定期定额执行次数';
COMMENT ON COLUMN"yuepao"."lu_jie"."PeriodAmount"IS'信用卡资讯：定期定额每次授权金额';
COMMENT ON COLUMN"yuepao"."lu_jie"."ProcessDate"IS'信用卡资讯：交易时间';
COMMENT ON COLUMN"yuepao"."lu_jie"."MerchantMemberID"IS'消费者资讯：消费者会员编号';
COMMENT ON COLUMN"yuepao"."lu_jie"."CustomField"IS'特店自订栏位：厂商自订栏位';

CREATE TYPE"xing_wei"AS ENUM(
	'YUE_FEI',--月费
	'CHU_ZHI',--储值
	'JI_WO_LAI',--给我赖
	'JI_NI_LAI',--给你赖
	'BU_JI_LAI',--不给赖
	'DA_ZHAO_HU',--打招呼
	'KAN_GUO_WO',--看过我
	'CHE_MA_FEI',--车马费
	'SHOU_CANG',--收藏
	'BU_SHOU_CANG'--收藏
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
	"shi_chuo"timestamptz DEFAULT"now"(),--时戳
	"yi_du"timestamptz,--已读
	"dian_shu"int2 NOT NULL DEFAULT'0',--点数
	"lu_jie"int8 REFERENCES"yuepao"."lu_jie"("id")ON DELETE RESTRICT ON UPDATE CASCADE,--绿界
	"zhao_hu_yu"text--招呼语
);
COMMENT ON TABLE"yuepao"."li_cheng"IS'历程';
COMMENT ON COLUMN"yuepao"."li_cheng"."id"IS'主键';
COMMENT ON COLUMN"yuepao"."li_cheng"."zhu_dong_de"IS'主动的';
COMMENT ON COLUMN"yuepao"."li_cheng"."bei_dong_de"IS'被动的';
COMMENT ON COLUMN"yuepao"."li_cheng"."xing_wei"IS'行为';
COMMENT ON COLUMN"yuepao"."li_cheng"."shi_chuo"IS'时戳';
COMMENT ON COLUMN"yuepao"."li_cheng"."yi_du"IS'已读';
COMMENT ON COLUMN"yuepao"."li_cheng"."dian_shu"IS'点数';
COMMENT ON COLUMN"yuepao"."li_cheng"."lu_jie"IS'绿界';
COMMENT ON COLUMN"yuepao"."li_cheng"."zhao_hu_yu"IS'招呼语';

/**
 * 生活照
 */
CREATE TABLE"sheng_huo_zhao"(
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

/**
 * 储值方案
 */
CREATE TABLE"yuepao"."chu_zhi_fang_an"(
	"id"serial2 PRIMARY KEY,
	"ming_cheng"varchar NOT NULL UNIQUE,
	"dian_shu"int2 NOT NULL,
	"shou_xu_fei"int2 NOT NULL,
	"jin_e"int NOT NULL
);
COMMENT ON TABLE"yuepao"."chu_zhi_fang_an"IS'储值方案';
COMMENT ON COLUMN"yuepao"."chu_zhi_fang_an"."id"IS'主鍵';
COMMENT ON COLUMN"yuepao"."chu_zhi_fang_an"."ming_cheng"IS'方案名称';
COMMENT ON COLUMN"yuepao"."chu_zhi_fang_an"."dian_shu"IS'点数';
COMMENT ON COLUMN"yuepao"."chu_zhi_fang_an"."shou_xu_fei"IS'手续费';
COMMENT ON COLUMN"yuepao"."chu_zhi_fang_an"."jin_e"IS'金额';
-- DML
INSERT INTO"yuepao"."chu_zhi_fang_an"("ming_cheng","dian_shu","shou_xu_fei","jin_e")VALUES
(E'plan.3000','3000','375','3375'),
(E'plan.5000','5000','625','5625'),
(E'plan.10000','10000','1250','11250');


/**
 * 給不給賴
 */
CREATE TABLE"yuepao"."gei_bu_gei_lai"(
	"nu_sheng"int8 NOT NULL REFERENCES"qing_ren"("id")ON UPDATE CASCADE ON DELETE RESTRICT,
	"nan_sheng"int8 NOT NULL REFERENCES"qing_ren"("id")ON UPDATE CASCADE ON DELETE RESTRICT,
	PRIMARY KEY("nu_sheng","nan_sheng"),
	"jie_guo"bool
);
COMMENT ON TABLE"yuepao"."gei_bu_gei_lai"IS'女生給不給賴';
COMMENT ON COLUMN"yuepao"."gei_bu_gei_lai"."nu_sheng"IS'女生';
COMMENT ON COLUMN"yuepao"."gei_bu_gei_lai"."nan_sheng"IS'男生';
COMMENT ON COLUMN"yuepao"."gei_bu_gei_lai"."jie_guo"IS'同意或拒絕';