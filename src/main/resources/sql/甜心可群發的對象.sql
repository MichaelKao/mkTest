SELECT lover0_.id FROM qing_ren lover0_
WHERE lover0_.xing_bie = 'true'
AND lover0_.shan_chu IS NULL
AND (lover0_.id IN(
	SELECT lovers2_.qing_ren
	FROM di_qu location1_
	JOIN qing_ren_yu_di_qu lovers2_
	ON location1_.id=lovers2_.di_qu
	WHERE location1_.id in (%s))
	)
AND (lover0_.id NOT IN(
	SELECT lovers3_.zhu_dong_fang
	FROM qing_ren l1
	JOIN feng_suo lovers3_
	ON l1.id=lovers3_.zhu_dong_fang
	WHERE lovers3_.bei_dong_fang = ?
	)
)
and (lover0_.id NOT IN(
	SELECT lovers4_.bei_dong_fang
	FROM qing_ren l2
	JOIN feng_suo lovers4_
	ON l2.id=lovers4_.bei_dong_fang
	WHERE lovers4_.zhu_dong_fang = ?
	)
)
AND (lover0_.id NOT IN(
	SELECT lovers5_.nan_sheng
	FROM qing_ren l3
	JOIN gei_bu_gei_lai lovers5_
	ON l3.id=lovers5_.nan_sheng
	WHERE lovers5_.jie_guo = 'true'
	)
)
ORDER BY lover0_.huo_yue DESC
LIMIT ?


