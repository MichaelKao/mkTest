SELECT"lun_tan"."id" FROM"lun_tan" LEFT JOIN"qing_ren"ON"qing_ren"."id"="lun_tan"."zuo_zhe" LEFT JOIN"lun_tan_liu_yan"ON"lun_tan_liu_yan"."lun_tan"="lun_tan"."id" WHERE"qing_ren"."xing_bie"=? GROUP BY"lun_tan"."id";