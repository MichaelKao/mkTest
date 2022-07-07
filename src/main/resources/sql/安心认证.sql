SELECT"id"
FROM"qing_ren"
WHERE"shan_chu"IS NULL
AND"zhang_hao"IS NOT NULL
AND"mi_ma"IS NOT NULL
AND"ni_cheng"IS NOT NULL
AND"zi_jie"IS NOT NULL
AND"ha_luo"IS NOT NULL
AND"ti_xing"IS NOT NULL
AND"shen_gao"IS NOT NULL
AND"ti_zhong"IS NOT NULL
AND"xue_li"IS NOT NULL
AND"hun_yin"IS NOT NULL
AND"zhi_ye"IS NOT NULL
AND"chou_yan"IS NOT NULL
AND"yin_jiu"IS NOT NULL
AND"li_xiang_dui_xiang"IS NOT NULL
AND"xing_bie"=?
AND"id"NOT IN(
	SELECT"bei_dong_fang"AS"id"FROM"feng_suo"WHERE"zhu_dong_fang"=?UNION SELECT"zhu_dong_fang"AS"id"FROM"feng_suo"WHERE"bei_dong_fang"=?
)
AND"an_xin"=TRUE
ORDER BY"huo_yue"DESC