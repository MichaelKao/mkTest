select * from qing_ren lover0_
where lover0_.xing_bie='true'
and lover0_.shan_chu is null
and (lover0_.id in(
	select lovers2_.qing_ren
	from di_qu location1_
	join qing_ren_yu_di_qu lovers2_
	on location1_.id=lovers2_.di_qu
	where location1_.id in (%s))
	)
and (lover0_.id not in(
	select lovers3_.zhu_dong_fang
	from qing_ren l1
	join feng_suo lovers3_
	on l1.id=lovers3_.zhu_dong_fang
	where lovers3_.bei_dong_fang = ?
	)
)
and (lover0_.id not in(
	select lovers4_.bei_dong_fang
	from qing_ren l2
	join feng_suo lovers4_
	on l2.id=lovers4_.bei_dong_fang
	where lovers4_.zhu_dong_fang = ?
	)
)
and (lover0_.id not in(
	select lovers5_.nan_sheng
	from qing_ren l3
	join gei_bu_gei_lai lovers5_
	on l3.id=lovers5_.nan_sheng
	where lovers5_.jie_guo = 'true'
	)
)
order by lover0_.huo_yue desc
limit ?


