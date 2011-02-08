select num_ip, d.numppop, d.title_doc from ip inner join document d on (ip.pk=d.fk)
where ip.date_ip_out is null and d.kod in (4,6,7,11,321,335,373,374,375) and d.pk_parent is null