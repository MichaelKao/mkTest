CREATE EXTENSION IF NOT EXISTS"uuid-ossp";

/**
 * 国家
 */
CREATE TABLE"guo_jia"(
	"id"serial2 PRIMARY KEY,
	"guo_ma"varchar NOT NULL UNIQUE,
	"guo_ming"varchar NOT NULL UNIQUE
);
COMMENT ON TABLE"guo_jia"IS'国家';
COMMENT ON COLUMN"guo_jia"."id"IS'主键';
COMMENT ON COLUMN"guo_jia"."guo_ma"IS'国码';
COMMENT ON COLUMN"guo_jia"."guo_ming"IS'国名(i18n 键)';
--
INSERT INTO"guo_jia"("guo_ma","guo_ming")VALUES
(E'886',E'Taiwan');

/**
 * 地区
 */
CREATE TABLE"di_qu"(
	"id"serial2 PRIMARY KEY,
	"xian_shi_ming"varchar NOT NULL UNIQUE
);
COMMENT ON TABLE"di_qu"IS'地区';
COMMENT ON COLUMN"di_qu"."id"IS'主键';
COMMENT ON COLUMN"di_qu"."xian_shi_ming"IS'县市名(i18n 键)';
--
INSERT INTO"di_qu"("xian_shi_ming")VALUES
(E'TAI_BEI'),(E'JI_LONG'),--北基
(E'TAO_YUAN'),(E'XIN_ZHU'),(E'MIAO_LI'),--桃竹苗
(E'TAI_ZHONG'),(E'ZHANG_HUA'),(E'NAN_TOU'),--中彰投
(E'YUN_LIN'),(E'JIA_YI'),(E'TAI_NAN'),--云嘉南
(E'GAO_XIONG'),(E'PING_DONG'),--高屏
(E'YI_LAN'),(E'HUA_LIAN'),(E'TAI_DONG'),--宜花东
(E'MA_ZU'),(E'JIN_MEN'),(E'PENG_HU');--马金澎

CREATE TYPE"ti_xing"AS ENUM(
	'PING_JUN',
	'MIAO_TIAO',
	'YUN_DONG',
	'QU_XIAN',
	'WEI_PANG',
	'FENG_MAN'
);
COMMENT ON TYPE"ti_xing"IS'体型';

CREATE TYPE"xue_li"AS ENUM(
	'GUO_XIAO',
	'GUO_ZHONG',
	'GAO_ZHONG',
	'GAO_ZHI',
	'ZHUAN_KE',
	'DA_XUE',
	'YAN_JIU_SUO'
);
COMMENT ON TYPE"xue_li"IS'学历';

CREATE TYPE"hun_yin"AS ENUM(
	'DAN_SHEN',
	'SI_HUI',
	'YI_HUN'
);
COMMENT ON TYPE"hun_yin"IS'婚姻';

CREATE TYPE"chou_yan"AS ENUM(
	'BU_CHOU_YAN',
	'OU_ER_CHOU',
	'JING_CHANG_CHOU'
);
COMMENT ON TYPE"chou_yan"IS'抽烟';

CREATE TYPE"yin_jiu"AS ENUM(
	'BU_HE_JIU',
	'OU_ER_HE',
	'JING_CHANG_HE'
);
COMMENT ON TYPE"yin_jiu"IS'饮酒';

/**
 * 情人
 */
CREATE TABLE"qing_ren"(
	"id"serial PRIMARY KEY,
	"shi_bie_ma"uuid NOT NULL UNIQUE,
	"guo_jia"int2 NOT NULL REFERENCES"guo_jia"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"zhang_hao"varchar NOT NULL,
	UNIQUE("guo_jia","zhang_hao"),
	"mi_ma"text,
	"huo_yue"timestamptz,
	"dao_qi"timestamptz,
	"di_qu"int2 REFERENCES"di_qu"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"ni_cheng"varchar,
	"sheng_ri"date,
	"xing_bie"bool,
	"da_tou"text,
	"zi_jie"text,
	"ha_luo"text,
	"ti_xing" "ti_xing",
	"shen_gao"int2,
	"ti_zhong"int2,
	"xue_li" "xue_li",
	"hun_yin" "hun_yin",
	"zhi_ye"varchar,
	"chou_yan" "chou_yan",
	"yin_jiu" "yin_jiu",
	"tian_jia_hao_you"text,
	"li_xiang_dui_xiang"text
);
COMMENT ON TABLE"qing_ren"IS'情人';
COMMENT ON COLUMN"qing_ren"."id"IS'主键';
COMMENT ON COLUMN"qing_ren"."shi_bie_ma"IS'识别码';
COMMENT ON COLUMN"qing_ren"."guo_jia"IS'国家';
COMMENT ON COLUMN"qing_ren"."zhang_hao"IS'帐号(手机号)';
COMMENT ON COLUMN"qing_ren"."mi_ma"IS'密码';
COMMENT ON COLUMN"qing_ren"."huo_yue"IS'活跃';
COMMENT ON COLUMN"qing_ren"."dao_qi"IS'到期';
COMMENT ON COLUMN"qing_ren"."di_qu"IS'地区';
COMMENT ON COLUMN"qing_ren"."ni_cheng"IS'昵称';
COMMENT ON COLUMN"qing_ren"."sheng_ri"IS'生日';
COMMENT ON COLUMN"qing_ren"."xing_bie"IS'性别';
COMMENT ON COLUMN"qing_ren"."da_tou"IS'大头';
COMMENT ON COLUMN"qing_ren"."zi_jie"IS'自介';
COMMENT ON COLUMN"qing_ren"."ha_luo"IS'哈啰';
COMMENT ON COLUMN"qing_ren"."ti_xing"IS'体型';
COMMENT ON COLUMN"qing_ren"."shen_gao"IS'身高';
COMMENT ON COLUMN"qing_ren"."ti_zhong"IS'体重';
COMMENT ON COLUMN"qing_ren"."xue_li"IS'学历';
COMMENT ON COLUMN"qing_ren"."hun_yin"IS'婚姻';
COMMENT ON COLUMN"qing_ren"."zhi_ye"IS'职业';
COMMENT ON COLUMN"qing_ren"."chou_yan"IS'抽烟';
COMMENT ON COLUMN"qing_ren"."yin_jiu"IS'饮酒';
COMMENT ON COLUMN"qing_ren"."tian_jia_hao_you"IS'添加好友';
COMMENT ON COLUMN"qing_ren"."li_xiang_dui_xiang"IS'理想对象';

/**
 * 激活
 */
CREATE TABLE"ji_huo"(
	"id"int PRIMARY KEY REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"zi_fu_chuan"varchar UNIQUE,
	"dao_qi"timestamptz NOT NULL DEFAULT"now"(),
	"shi_chuo"timestamptz
);
COMMENT ON TABLE"ji_huo"IS'激活';
COMMENT ON COLUMN"ji_huo"."id"IS'主键';
COMMENT ON COLUMN"ji_huo"."zi_fu_chuan"IS'字符串';
COMMENT ON COLUMN"ji_huo"."dao_qi"IS'到期';
COMMENT ON COLUMN"ji_huo"."shi_chuo"IS'时戳';

/**
 * LINE User Profile
 */
CREATE TABLE"line_user_profile"(
	"id"int PRIMARY KEY REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"display_name"varchar,
	"user_id"varchar UNIQUE,
	"language"varchar,
	"picture_url"text,
	"status_message"text
);
COMMENT ON TABLE"line_user_profile"IS'LINE User Profile';
COMMENT ON COLUMN"line_user_profile"."id"IS'主键';
COMMENT ON COLUMN"line_user_profile"."display_name"IS'名字';
COMMENT ON COLUMN"line_user_profile"."user_id"IS'用户识别码';
COMMENT ON COLUMN"line_user_profile"."language"IS'语系';
COMMENT ON COLUMN"line_user_profile"."picture_url"IS'头像';
COMMENT ON COLUMN"line_user_profile"."status_message"IS'状态消息';

/**
 * 身份
 */
CREATE TABLE"shen_fen"(
	"id"serial2 PRIMARY KEY,
	"jue_se"varchar NOT NULL UNIQUE
);
COMMENT ON TABLE"shen_fen"IS'身份';
COMMENT ON COLUMN"shen_fen"."id"IS'主键';
COMMENT ON COLUMN"shen_fen"."jue_se"IS'角色';
--
INSERT INTO"shen_fen"("jue_se")VALUES
(E'ROLE_ALMIGHTY'),-- 万能的
(E'ROLE_FINANCE'),-- 财务
(E'ROLE_YONGHU');-- 用户

/**
 * 授权
 */
CREATE TABLE"shou_quan"(
	"qing_ren"int NOT NULL REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"shen_fen"int2 NOT NULL REFERENCES"shen_fen"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	PRIMARY KEY("qing_ren","shen_fen")
);
COMMENT ON TABLE"shou_quan"IS'授权';
COMMENT ON COLUMN"shou_quan"."qing_ren"IS'情人';
COMMENT ON COLUMN"shou_quan"."shen_fen"IS'身份';

/**
 * The default `users` table of Spring Security JDBC authentication
 */
CREATE OR REPLACE VIEW"users"AS
SELECT
"lover"."id",
("country"."guo_ma"||"lover"."zhang_hao")::"varchar"AS"username",
"lover"."mi_ma"::"varchar"AS"password",
("activation"."zi_fu_chuan"IS NULL AND"activation"."shi_chuo"IS NOT NULL)AS"enabled"
FROM"qing_ren"AS"lover"
LEFT JOIN"guo_jia"AS"country"
	ON"country"."id"="lover"."guo_jia"
LEFT JOIN"ji_huo"AS"activation"
	ON"activation"."id"="lover"."id"
ORDER BY
"country"."guo_ma",
"lover"."zhang_hao";

/**
 * The default `authorities` table of Spring Security JDBC authentication
 */
CREATE OR REPLACE VIEW"authorities"AS
SELECT
"LOVER"."username",
"ROLE"."jue_se"AS"authority"
FROM"shou_quan"AS"JT"
LEFT JOIN(
	SELECT
	"lover"."id",
	("country"."guo_ma"||"lover"."zhang_hao")AS"username"
	FROM"qing_ren"AS"lover"
	LEFT JOIN"guo_jia"AS"country"
		ON"country"."id"="lover"."guo_jia"
)AS"LOVER"
	ON"LOVER"."id"="JT"."qing_ren"
LEFT JOIN"shen_fen"AS"ROLE"
	ON"ROLE"."id"="JT"."shen_fen"
ORDER BY
"LOVER"."id",
"ROLE"."id";

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
	"ChargeFee"float4,
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
	"PeriodAmount"int,
	"ProcessDate"varchar(20),
	"TotalSuccessTimes"int,
	--ConsumerInfo 消费者资讯
	"MerchantMemberID"varchar(60),
	--特店自订栏位
	"CustomField"varchar(200)
);
COMMENT ON TABLE"lu_jie"IS'绿界';
COMMENT ON COLUMN"lu_jie"."id"IS'主键';
COMMENT ON COLUMN"lu_jie"."session_id"IS'分配给会话的标识符';
COMMENT ON COLUMN"lu_jie"."MerchantTradeDate"IS'订单资讯：厂商交易时间';
COMMENT ON COLUMN"lu_jie"."MerchantTradeNo"IS'订单资讯：特店交易编号';
COMMENT ON COLUMN"lu_jie"."TotalAmount"IS'订单资讯：交易金额';
COMMENT ON COLUMN"lu_jie"."ItemName"IS'订单资讯：商品名称';
COMMENT ON COLUMN"lu_jie"."TradeNo"IS'订单资讯：绿界交易编号';
COMMENT ON COLUMN"lu_jie"."TradeAmt"IS'订单资讯：交易金额';
COMMENT ON COLUMN"lu_jie"."TradeDate"IS'订单资讯：订单成立时间';
COMMENT ON COLUMN"lu_jie"."PaymentType"IS'订单资讯：付款方式';
COMMENT ON COLUMN"lu_jie"."PaymentDate"IS'订单资讯：付款时间';
COMMENT ON COLUMN"lu_jie"."ChargeFee"IS'订单资讯：手续费';
COMMENT ON COLUMN"lu_jie"."TradeStatus"IS'订单资讯：交易状态';
COMMENT ON COLUMN"lu_jie"."BankCode"IS'ATM 资讯：缴费银行代码';
COMMENT ON COLUMN"lu_jie"."vAccount"IS'ATM 资讯：缴费虚拟帐号';
COMMENT ON COLUMN"lu_jie"."ATMAccBank"IS'ATM 资讯：付款人银行代码';
COMMENT ON COLUMN"lu_jie"."ATMAccNo"IS'ATM 资讯：付款人银行帐号后五码';
COMMENT ON COLUMN"lu_jie"."Barcode1"IS'超商条码资讯：条码第一段号码';
COMMENT ON COLUMN"lu_jie"."Barcode2"IS'超商条码资讯：条码第二段号码';
COMMENT ON COLUMN"lu_jie"."Barcode3"IS'超商条码资讯：条码第三段号码';
COMMENT ON COLUMN"lu_jie"."BarcodeInfoPayFrom"IS'超商条码资讯：缴费超商';
COMMENT ON COLUMN"lu_jie"."PaymentNo"IS'超商代码资讯：缴费代码';
COMMENT ON COLUMN"lu_jie"."CVSInfoPayFrom"IS'超商代码资讯：缴费超商';
COMMENT ON COLUMN"lu_jie"."AuthCode"IS'信用卡资讯：银行授权码';
COMMENT ON COLUMN"lu_jie"."Gwsr"IS'信用卡资讯：授权交易单号';
COMMENT ON COLUMN"lu_jie"."Amount"IS'信用卡资讯：金额';
COMMENT ON COLUMN"lu_jie"."Stage"IS'信用卡资讯：分期期数';
COMMENT ON COLUMN"lu_jie"."Stast"IS'信用卡资讯：首期金额';
COMMENT ON COLUMN"lu_jie"."Staed"IS'信用卡资讯：各期金额';
COMMENT ON COLUMN"lu_jie"."Eci"IS'信用卡资讯：3D(VBV) 回传值';
COMMENT ON COLUMN"lu_jie"."Card6No"IS'信用卡资讯：信用卡卡号前六码';
COMMENT ON COLUMN"lu_jie"."Card4No"IS'信用卡资讯：信用卡卡号末四码';
COMMENT ON COLUMN"lu_jie"."PeriodType"IS'信用卡资讯：定期定额周期种类';
COMMENT ON COLUMN"lu_jie"."Frequency"IS'信用卡资讯：定期定额执行频率';
COMMENT ON COLUMN"lu_jie"."ExecTimes"IS'信用卡资讯：定期定额执行次数';
COMMENT ON COLUMN"lu_jie"."PeriodAmount"IS'信用卡资讯：定期定额每次授权金额';
COMMENT ON COLUMN"lu_jie"."ProcessDate"IS'信用卡资讯：交易时间';
COMMENT ON COLUMN"lu_jie"."TotalSuccessTimes"IS'信用卡资讯：目前已成功授权的次数';
COMMENT ON COLUMN"lu_jie"."MerchantMemberID"IS'消费者资讯：消费者会员编号';
COMMENT ON COLUMN"lu_jie"."CustomField"IS'特店自订栏位：厂商自订栏位';

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
CREATE TABLE"li_cheng"(
	"id"serial8 PRIMARY KEY,
	"zhu_dong_de"int NOT NULL REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,--主动的
	"bei_dong_de"int REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,--被动的
	"xing_wei" "xing_wei",--行为
	"shi_chuo"timestamptz DEFAULT"now"(),--时戳
	"yi_du"timestamptz,--已读
	"dian_shu"int2 NOT NULL DEFAULT'0',--点数
	"lu_jie"int8 REFERENCES"lu_jie"("id")ON DELETE RESTRICT ON UPDATE CASCADE,--绿界
	"zhao_hu_yu"text--招呼语
);
COMMENT ON TABLE"li_cheng"IS'历程';
COMMENT ON COLUMN"li_cheng"."id"IS'主键';
COMMENT ON COLUMN"li_cheng"."zhu_dong_de"IS'主动的';
COMMENT ON COLUMN"li_cheng"."bei_dong_de"IS'被动的';
COMMENT ON COLUMN"li_cheng"."xing_wei"IS'行为';
COMMENT ON COLUMN"li_cheng"."shi_chuo"IS'时戳';
COMMENT ON COLUMN"li_cheng"."yi_du"IS'已读';
COMMENT ON COLUMN"li_cheng"."dian_shu"IS'点数';
COMMENT ON COLUMN"li_cheng"."lu_jie"IS'绿界';
COMMENT ON COLUMN"li_cheng"."zhao_hu_yu"IS'招呼语';

/**
 * 生活照
 */
CREATE TABLE"sheng_huo_zhao"(
	"id"serial2 PRIMARY KEY,
        "qing_ren"int NOT NULL REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"shi_bie_ma"uuid NOT NULL UNIQUE,
	"shi_chuo"timestamptz NOT NULL DEFAULT"now"()
);
COMMENT ON TABLE"sheng_huo_zhao"IS'生活照';
COMMENT ON COLUMN"sheng_huo_zhao"."id"IS'主鍵';
COMMENT ON COLUMN"sheng_huo_zhao"."qing_ren"IS'情人';
COMMENT ON COLUMN"sheng_huo_zhao"."shi_bie_ma"IS'識別碼';
COMMENT ON COLUMN"sheng_huo_zhao"."shi_chuo"IS'時戳';

/**
 * 储值方案
 */
CREATE TABLE"chu_zhi_fang_an"(
	"id"serial2 PRIMARY KEY,
	"ming_cheng"varchar NOT NULL UNIQUE,
	"dian_shu"int2 NOT NULL,
	"shou_xu_fei"int2 NOT NULL,
	"jin_e"int NOT NULL
);
COMMENT ON TABLE"chu_zhi_fang_an"IS'储值方案';
COMMENT ON COLUMN"chu_zhi_fang_an"."id"IS'主鍵';
COMMENT ON COLUMN"chu_zhi_fang_an"."ming_cheng"IS'方案名称';
COMMENT ON COLUMN"chu_zhi_fang_an"."dian_shu"IS'点数';
COMMENT ON COLUMN"chu_zhi_fang_an"."shou_xu_fei"IS'手续费';
COMMENT ON COLUMN"chu_zhi_fang_an"."jin_e"IS'金额';
-- DML
INSERT INTO"chu_zhi_fang_an"("ming_cheng","dian_shu","shou_xu_fei","jin_e")VALUES
(E'plan.3000','3000','375','3375'),
(E'plan.5000','5000','625','5625'),
(E'plan.10000','10000','1250','11250');


/**
 * 給不給賴
 */
CREATE TABLE"gei_bu_gei_lai"(
	"nu_sheng"int8 NOT NULL REFERENCES"qing_ren"("id")ON UPDATE CASCADE ON DELETE RESTRICT,
	"nan_sheng"int8 NOT NULL REFERENCES"qing_ren"("id")ON UPDATE CASCADE ON DELETE RESTRICT,
	PRIMARY KEY("nu_sheng","nan_sheng"),
	"jie_guo"bool
);
COMMENT ON TABLE"gei_bu_gei_lai"IS'女生給不給賴';
COMMENT ON COLUMN"gei_bu_gei_lai"."nu_sheng"IS'女生';
COMMENT ON COLUMN"gei_bu_gei_lai"."nan_sheng"IS'男生';
COMMENT ON COLUMN"gei_bu_gei_lai"."jie_guo"IS'同意或拒絕';
