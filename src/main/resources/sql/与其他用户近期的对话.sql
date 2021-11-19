SELECT
"greatest",
"least",
"shi_chuo"
FROM(
 SELECT DISTINCT ON(greatest("zhu_dong_de","bei_dong_de"),least("zhu_dong_de","bei_dong_de"))greatest("zhu_dong_de","bei_dong_de"),
 least("zhu_dong_de","bei_dong_de"),
 "shi_chuo"
 FROM"li_cheng"
 WHERE"xing_wei"IN(
  'JI_WO_LAI',--给我 LINE
  'JI_NI_LAI',--给你 LINE
  'BU_JI_LAI',--不给 LINE
  'DA_ZHAO_HU',--打招呼(女对男)
  'CHE_MA_FEI',--车马费
  'PING_JIA',--评价
  'QUN_FA',--群发
  'LIAO_LIAO',--聊聊(男对女)
  'YAO_CHE_MA_FEI',--要车马费
  'FANG_XING_SHENG_HUO_ZHAO',--生活照授权
  'TUI_HUI_CHE_MA_FEI',--退回车马费
  'KE_FANG_XING',--生活照允许
  'BU_FANG_XING'--生活照不允许
 )
 AND("zhu_dong_de"=?OR"bei_dong_de"=?)
 ORDER BY
 greatest("zhu_dong_de","bei_dong_de"),
 least("zhu_dong_de","bei_dong_de"),
 "shi_chuo"DESC
)AS"dialogist"
ORDER BY"shi_chuo"DESC
/*
SELECT"history".*
FROM"li_cheng"AS"history",
(
	SELECT DISTINCT
	"zhu_dong_de",
	"bei_dong_de"
	FROM"li_cheng"
	WHERE"xing_wei"IN(
		'JI_WO_LAI',--给我 LINE
		'JI_NI_LAI',--给你 LINE
		'BU_JI_LAI',--不给 LINE
		'DA_ZHAO_HU',--打招呼(女对男)
		'CHE_MA_FEI',--车马费
		'PING_JIA',--评价
		'QUN_FA',--群发
		'LIAO_LIAO',--聊聊(男对女)
		'YAO_CHE_MA_FEI',--要车马费
		'FANG_XING_SHENG_HUO_ZHAO',--生活照授权
		'TUI_HUI_CHE_MA_FEI',--退回车马费
		'KE_FANG_XING',--生活照允许
		'BU_FANG_XING'--生活照不允许
	)
	AND("zhu_dong_de"=?OR "bei_dong_de"=?)
)AS"benefactors"
WHERE"history"."xing_wei"IN(
	'JI_WO_LAI',--给我 LINE
	'JI_NI_LAI',--给你 LINE
	'BU_JI_LAI',--不给 LINE
	'DA_ZHAO_HU',--打招呼(女对男)
	'CHE_MA_FEI',--车马费
	'PING_JIA',--评价
	'QUN_FA',--群发
	'LIAO_LIAO',--聊聊(男对女)
	'YAO_CHE_MA_FEI',--要车马费
	'FANG_XING_SHENG_HUO_ZHAO',--生活照授权
	'TUI_HUI_CHE_MA_FEI',--退回车马费
	'KE_FANG_XING',--生活照允许
	'BU_FANG_XING'--生活照不允许
)
AND"history"."zhu_dong_de"="benefactors"."zhu_dong_de"
AND"history"."bei_dong_de"="benefactors"."bei_dong_de"
ORDER BY"history"."shi_chuo"DESC
*/