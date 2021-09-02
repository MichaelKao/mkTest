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
WHERE("country"."guo_ma"||"lover"."zhang_hao")::"varchar"IS NOT NULL
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
	'LAI_TUI_DIAN',--要求賴的退點
	'SHOU_CANG',--收藏
	'BU_SHOU_CANG'--取消收藏
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

/**
 * 收藏
 */
CREATE TABLE"shou_cang"(
	"shou_cang_de"int8 NOT NULL REFERENCES"qing_ren"("id")ON UPDATE CASCADE ON DELETE RESTRICT,
	"bei_shou_cang"int8 NOT NULL REFERENCES"qing_ren"("id")ON UPDATE CASCADE ON DELETE RESTRICT,
	PRIMARY KEY("shou_cang_de","bei_shou_cang")
);
COMMENT ON TABLE"shou_cang"IS'收藏';
COMMENT ON COLUMN"shou_cang"."shou_cang_de"IS'收藏的';
COMMENT ON COLUMN"shou_cang"."bei_shou_cang"IS'被收藏的';

/**
 * 年收入
 */
CREATE TABLE"nian_shou_ru"(
	"id"serial2 PRIMARY KEY,
	"nian_shou_ru"varchar NOT NULL UNIQUE
);
COMMENT ON TABLE"nian_shou_ru"IS'男生年收入';
COMMENT ON COLUMN"nian_shou_ru"."id"IS'主键';
COMMENT ON COLUMN"nian_shou_ru"."nian_shou_ru"IS'男生年收入';
--
INSERT INTO"nian_shou_ru"("nian_shou_ru")VALUES
(E'lessThan1million'),(E'1to2million'),
(E'2to3million'),(E'3to4million'),
(E'4to5million'),(E'moreThan5million');

/**
 * 零用錢
 */
CREATE TABLE"ling_yong_qian"(
	"id"serial2 PRIMARY KEY,
	"ling_yong_qian"varchar NOT NULL UNIQUE
);
COMMENT ON TABLE"ling_yong_qian"IS'女生期望零用錢';
COMMENT ON COLUMN"ling_yong_qian"."id"IS'主键';
COMMENT ON COLUMN"ling_yong_qian"."ling_yong_qian"IS'女生期望零用錢';
--
INSERT INTO"ling_yong_qian"("ling_yong_qian")VALUES
(E'lessThan10thousand'),(E'10to30thousand'),
(E'30to50thousand'),(E'50to70thousand'),
(E'moreThan70thousand'),(E'negotiable');

ALTER TABLE"qing_ren"
ADD COLUMN"nian_shou_ru"int2 REFERENCES"nian_shou_ru"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
ADD COLUMN"ling_yong_qian"int2 REFERENCES"ling_yong_qian"("id")ON DELETE RESTRICT ON UPDATE CASCADE;
COMMENT ON COLUMN"qing_ren"."nian_shou_ru"IS'男生年收入';
COMMENT ON COLUMN"qing_ren"."ling_yong_qian"IS'女生期望零用錢';

/**
 * 刪除帳號
 */
ALTER TABLE"qing_ren"
ADD COLUMN"shan_chu"varchar;
COMMENT ON COLUMN"qing_ren"."shan_chu"IS'刪除';

/**
 * 帳號 NOT NULL 拿掉，以便刪除時改 NULL
 */
ALTER TABLE"qing_ren" ALTER COLUMN"zhang_hao" DROP NOT NULL;

/**
 * 評價放進行為
 */
ALTER TYPE"xing_wei" ADD VALUE 'PING_JIA';

/**
 * 星級評價
 */
ALTER TABLE"li_cheng"
ADD COLUMN"xing_ji"int2,
ADD COLUMN"ping_jia"varchar;
COMMENT ON COLUMN"li_cheng"."xing_ji"IS'星級';
COMMENT ON COLUMN"li_cheng"."ping_jia"IS'評價留言';

/**
 * 車馬費提領方式
 */
CREATE TYPE"ti_qu_feng_shi"AS ENUM(
	'WIRE_TRANSFER',
	'PAYPAL'
);
COMMENT ON TYPE"ti_qu_feng_shi"IS'車馬費提取方式';

/**
 * 甜心提領車馬費資訊
 */
CREATE TABLE"ti_qu_feng_shi_zi_xun"(
	"id"serial PRIMARY KEY,
	"honey"int NOT NULL REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"wire_transfer_bank_code"varchar,
	"wire_transfer_branch_code"varchar,
	"wire_transfer_account_name"varchar,
	"wire_transfer_account_number"varchar
);
COMMENT ON TABLE"ti_qu_feng_shi_zi_xun"IS'甜心提取車馬費資訊';
COMMENT ON COLUMN"ti_qu_feng_shi_zi_xun"."id"IS'主鍵';
COMMENT ON COLUMN"ti_qu_feng_shi_zi_xun"."honey"IS'甜心';
COMMENT ON COLUMN"ti_qu_feng_shi_zi_xun"."wire_transfer_bank_code"IS'銀行代碼';
COMMENT ON COLUMN"ti_qu_feng_shi_zi_xun"."wire_transfer_branch_code"IS'分行代碼';
COMMENT ON COLUMN"ti_qu_feng_shi_zi_xun"."wire_transfer_account_name"IS'戶名';
COMMENT ON COLUMN"ti_qu_feng_shi_zi_xun"."wire_transfer_account_number"IS'匯款帳號';

/**
 * 甜心提領車馬費紀錄
 */
CREATE TABLE"ti_qu_che_ma_fei"(
	"id"serial PRIMARY KEY,
	"honey"int NOT NULL REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"jin_e"int,
	"zhuang_tai"boolean,
	"ti_qu_feng_shi" "ti_qu_feng_shi",
	"shi_bai_yuan_yin"varchar,
	"shi_chuo"timestamptz
);
COMMENT ON TABLE"ti_qu_che_ma_fei"IS'甜心提取車馬費';
COMMENT ON COLUMN"ti_qu_che_ma_fei"."id"IS'主鍵';
COMMENT ON COLUMN"ti_qu_che_ma_fei"."honey"IS'甜心';
COMMENT ON COLUMN"ti_qu_che_ma_fei"."jin_e"IS'提取金額';
COMMENT ON COLUMN"ti_qu_che_ma_fei"."zhuang_tai"IS'狀態';
COMMENT ON COLUMN"ti_qu_che_ma_fei"."ti_qu_feng_shi"IS'提取方式';
COMMENT ON COLUMN"ti_qu_che_ma_fei"."shi_bai_yuan_yin"IS'失敗原因';
COMMENT ON COLUMN"ti_qu_che_ma_fei"."shi_chuo"IS'時間戳記';

/**
 * 服務
 */
CREATE TABLE"fu_wu"(
	"id"serial2 PRIMARY KEY,
	"fu_wu_biao_qian"varchar NOT NULL UNIQUE
);
COMMENT ON TABLE"fu_wu"IS'甜心提取車馬費';
COMMENT ON COLUMN"fu_wu"."id"IS'主鍵';
COMMENT ON COLUMN"fu_wu"."fu_wu_biao_qian"IS'服務標籤';
--
INSERT INTO"fu_wu"("fu_wu_biao_qian")VALUES
(E'KAN_DIAN_YING'),--看電影
(E'CHANG_GE'),--唱歌
(E'GUANG_JIE'),--逛街
(E'CHI_FAN'),--吃飯
(E'WAN_YOU_XI'),--玩遊戲
(E'QIN_MI_GUAN_XI'),--親密關係
(E'KAN_YE_JING'),--看夜景
(E'JIAN_JIA_ZHANG'),--見家長
(E'XIN_LI_ZI_SHANG');--心理諮商

/**
 * 情人與服務
 */
CREATE TABLE"qing_ren_yu_fu_wu"(
	"qing_ren"int8 NOT NULL REFERENCES"qing_ren"("id")ON UPDATE CASCADE ON DELETE RESTRICT,
	"fu_wu"int2 NOT NULL REFERENCES"fu_wu"("id")ON UPDATE CASCADE ON DELETE RESTRICT,
	PRIMARY KEY("qing_ren","fu_wu")
);
COMMENT ON TABLE"qing_ren_yu_fu_wu"IS'情人與服務';
COMMENT ON COLUMN"qing_ren_yu_fu_wu"."qing_ren"IS'情人';
COMMENT ON COLUMN"qing_ren_yu_fu_wu"."fu_wu"IS'服務';

/**
 * 情人與地區
 */
CREATE TABLE"qing_ren_yu_di_qu"(
	"qing_ren"int8 NOT NULL REFERENCES"qing_ren"("id")ON UPDATE CASCADE ON DELETE RESTRICT,
	"di_qu"int2 NOT NULL REFERENCES"di_qu"("id")ON UPDATE CASCADE ON DELETE RESTRICT,
	PRIMARY KEY("qing_ren","di_qu")
);
COMMENT ON TABLE"qing_ren_yu_di_qu"IS'情人與地區';
COMMENT ON COLUMN"qing_ren_yu_di_qu"."qing_ren"IS'情人';
COMMENT ON COLUMN"qing_ren_yu_di_qu"."di_qu"IS'服務';

/**
 * 安心認證欄位
 */
ALTER TABLE"qing_ren"
ADD COLUMN"an_xin"bool;
COMMENT ON COLUMN"qing_ren"."an_xin"IS'安心';

ALTER TYPE"xing_wei" RENAME VALUE 'LAI_TUI_DIAN' TO 'LAI_KOU_DIAN';

/**
 * 歷程回應欄位
 */
ALTER TABLE"li_cheng"
ADD COLUMN"hui_ying"timestamptz;
COMMENT ON COLUMN"li_cheng"."hui_ying"IS'回應';

/**
 * 提領、安心認證結果
 */
ALTER TYPE"xing_wei" ADD VALUE 'TI_LING_SHI_BAI';
ALTER TYPE"xing_wei" ADD VALUE 'TI_LING_CHENG_GONG';
ALTER TYPE"xing_wei" ADD VALUE 'AN_XIN_SHI_BAI';
ALTER TYPE"xing_wei" ADD VALUE 'AN_XIN_CHENG_GONG';

DROP TABLE"ti_qu_che_ma_fei";

CREATE TABLE"ti_ling"(
	"id"int8 PRIMARY KEY REFERENCES"li_cheng"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"honey"int NOT NULL REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"jin_e"int,
	"zhuang_tai"boolean,
	"ti_qu_feng_shi" "ti_qu_feng_shi",
	"shi_chuo"timestamptz
);
COMMENT ON TABLE"ti_ling"IS'甜心提取車馬費';
COMMENT ON COLUMN"ti_ling"."id"IS'主鍵';
COMMENT ON COLUMN"ti_ling"."honey"IS'甜心';
COMMENT ON COLUMN"ti_ling"."jin_e"IS'提取金額';
COMMENT ON COLUMN"ti_ling"."zhuang_tai"IS'狀態';
COMMENT ON COLUMN"ti_ling"."ti_qu_feng_shi"IS'提取方式';
COMMENT ON COLUMN"ti_ling"."shi_chuo"IS'時間戳記';

/**
 * 情人加註冊時戳
 */
ALTER TABLE"qing_ren"
ADD COLUMN"zhu_ce_shi_chuo"timestamptz NOT NULL DEFAULT"now"();
COMMENT ON COLUMN"qing_ren"."zhu_ce_shi_chuo"IS'註冊時戳';

CREATE OR REPLACE VIEW"首页的贵宾们"AS
SELECT*
FROM"qing_ren"
WHERE"shan_chu"IS NULL--未封号
AND"xing_bie"='true'--男士
AND"dao_qi">"now"()--贵宾有效期须在目前之后
ORDER BY"huo_yue"DESC;--以活跃降幂排序

CREATE OR REPLACE VIEW"首页的安心甜心们"AS
SELECT*
FROM"qing_ren"
WHERE"shan_chu"IS NULL--未封号
AND"xing_bie"='false'--甜心
AND"an_xin"='true'--通过安心认证
ORDER BY"huo_yue"DESC;--以活跃降幂排序

CREATE OR REPLACE VIEW"首页的安心男士们"AS
SELECT*
FROM"qing_ren"
WHERE"shan_chu"IS NULL--未封号
AND"xing_bie"='true'--男士
AND"an_xin"='true'--通过安心认证
ORDER BY"huo_yue"DESC;--以活跃降幂排序

/**
 * 情人的相處關係列舉
 */
CREATE TYPE"xiang_chu_guan_xi"AS ENUM(
	'CHANG_QI',
	'DUAN_QI',
	'CHANG_DUAN_JIE_KE',
	'DAN_CI_YUE_HUI'
);
COMMENT ON TYPE"xiang_chu_guan_xi"IS'相處關係';

/**
 * 情人的相處關係欄位
 */
ALTER TABLE"qing_ren"
ADD COLUMN"xiang_chu_guan_xi" "xiang_chu_guan_xi";
COMMENT ON COLUMN"qing_ren"."xiang_chu_guan_xi"IS'相處關係';

/**
 * 透过 LINE 接收网站服务通知
 */
CREATE TABLE"line_notify_authentication"(
	"id"serial PRIMARY KEY,
	"state"varchar NOT NULL,
	"sucker"int NOT NULL REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE
);
COMMENT ON TABLE"line_notify_authentication"IS'透过 LINE 接收网站服务通知';
COMMENT ON COLUMN"line_notify_authentication"."id"IS'主键';
COMMENT ON COLUMN"line_notify_authentication"."state"IS'响应 CSRF 攻击的令牌';
COMMENT ON COLUMN"line_notify_authentication"."sucker"IS'情人';

/**
 * 群發行為
 */
ALTER TYPE"xing_wei" ADD VALUE 'QUN_FA';

/**
 * 情人添加：LINE 网站服务通知访问令牌
 */
ALTER TABLE"qing_ren"
ADD COLUMN"line_notify_access_token"text;
COMMENT ON COLUMN"qing_ren"."line_notify_access_token"IS'LINE 网站服务通知访问令牌';

/**
 * 情人添加：推荐码、推荐人
 */
ALTER TABLE"qing_ren"
ADD COLUMN"tui_jian_ma"varchar,
ADD COLUMN"tui_jian_ren"int REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE;
COMMENT ON COLUMN"qing_ren"."tui_jian_ma"IS'推荐码';
COMMENT ON COLUMN"qing_ren"."tui_jian_ren"IS'推荐人';

/**
 * 行为
 */
ALTER TYPE"xing_wei"
ADD VALUE'ZAI_LIAO_LIAO',--再聊聊
ADD VALUE'FANG_XING_SHENG_HUO_ZHAO';--放行生活照

/**
 * 男士种类
 */
CREATE TYPE"nan_shi_zhong_lei"AS ENUM(
	'VIP',
	'VVIP'
);
COMMENT ON TYPE"nan_shi_zhong_lei"IS'男士种类';

/**
 * 情人添加：男士种类
 */
ALTER TABLE"qing_ren"
ADD COLUMN"nan_shi_zhong_lei"varchar;
COMMENT ON COLUMN"qing_ren"."nan_shi_zhong_lei"IS'男士种类';

/**
 * 更名再聊聊行為
 */
ALTER TYPE"xing_wei" RENAME VALUE 'ZAI_LIAO_LIAO' TO 'LIAO_LIAO';

/*
 * 用户号之间的封锁
 */
CREATE TABLE"feng_suo"(
	"zhu_dong_fang"int NOT NULL REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"bei_dong_fang"int NOT NULL REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	PRIMARY KEY("zhu_dong_fang","bei_dong_fang")
);
COMMENT ON TABLE"feng_suo"IS'用户号之间的封锁';
COMMENT ON COLUMN"feng_suo"."zhu_dong_fang"IS'主动方(A 封锁 B 的 A)';
COMMENT ON COLUMN"feng_suo"."bei_dong_fang"IS'被动方(A 封锁 B 的 B)';

ALTER TABLE"li_cheng"
ADD COLUMN"fang_xing_sheng_huo_zhao"bool NOT NULL DEFAULT'0';
COMMENT ON COLUMN"li_cheng"."fang_xing_sheng_huo_zhao"IS'放行生活照';

/*
 * 行為
 */
ALTER TYPE"xing_wei"
ADD VALUE'YAO_CHE_MA_FEI';--要求車馬費

ALTER TABLE"li_cheng"
ALTER COLUMN"fang_xing_sheng_huo_zhao"DROP NOT NULL;

/**
 * 申请解除定期定额
 */
CREATE TABLE"shen_qing_jie_chu_ding_qi_ding_e"(
	"id"serial8 PRIMARY KEY,
	"shei_shen_qing"int NOT NULL REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"shen_qing_shi"timestamptz NOT NULL DEFAULT"now"(),
	"shei_chu_li"int REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"chu_li_shi"timestamptz
);
COMMENT ON TABLE"shen_qing_jie_chu_ding_qi_ding_e"IS'申请解除定期定额';
COMMENT ON COLUMN"shen_qing_jie_chu_ding_qi_ding_e"."id"IS'主键';
COMMENT ON COLUMN"shen_qing_jie_chu_ding_qi_ding_e"."shei_shen_qing"IS'谁申请';
COMMENT ON COLUMN"shen_qing_jie_chu_ding_qi_ding_e"."shen_qing_shi"IS'申请时';
COMMENT ON COLUMN"shen_qing_jie_chu_ding_qi_ding_e"."shei_chu_li"IS'谁处理';
COMMENT ON COLUMN"shen_qing_jie_chu_ding_qi_ding_e"."chu_li_shi"IS'处理时';

/**
 * 重设密码
 */
CREATE TABLE"zhong_she_mi_ma"(
	"id"int PRIMARY KEY REFERENCES"qing_ren"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"zi_fu_chuan"varchar UNIQUE,
	"dao_qi"timestamptz NOT NULL DEFAULT"now"(),
	"shi_chuo"timestamptz
);
COMMENT ON TABLE"zhong_she_mi_ma"IS'重设密码';
COMMENT ON COLUMN"zhong_she_mi_ma"."id"IS'主键';
COMMENT ON COLUMN"zhong_she_mi_ma"."zi_fu_chuan"IS'字符串';
COMMENT ON COLUMN"zhong_she_mi_ma"."dao_qi"IS'到期';
COMMENT ON COLUMN"zhong_she_mi_ma"."shi_chuo"IS'时戳';

/**
 * 新增服務標籤
 */
INSERT INTO"fu_wu"("fu_wu_biao_qian")VALUES
(E'LU_YOU'),--旅遊
(E'CHU_CHAI'),--出差
(E'XIAO_ZHUO');--小酌

/**
 * 体验码
 */
CREATE TABLE"ti_yan_ma"(
	"id"serial2 PRIMARY KEY,
	"zi_fu_chuan"varchar UNIQUE,
	"wang_hong"varchar
);
COMMENT ON TABLE"ti_yan_ma"IS'体验码';
COMMENT ON COLUMN"ti_yan_ma"."id"IS'主键';
COMMENT ON COLUMN"ti_yan_ma"."zi_fu_chuan"IS'字符串';
COMMENT ON COLUMN"ti_yan_ma"."wang_hong"IS'网红';

ALTER TABLE"li_cheng"
ADD COLUMN"ti_yan_ma"int2 REFERENCES"ti_yan_ma"("id")ON DELETE RESTRICT ON UPDATE CASCADE,
ADD CHECK(CASE WHEN"xing_wei"='DUAN_QI_GUI_BIN_TI_YAN'THEN"ti_yan_ma"IS NOT NULL END);
COMMENT ON COLUMN"li_cheng"."ti_yan_ma"IS'体验码';

ALTER TYPE"xing_wei"
ADD VALUE'DUAN_QI_GUI_BIN_TI_YAN';--短期贵宾体验
