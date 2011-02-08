select distinct(p.num_ip)
from ip p
  inner join register_take_sum rts on rts.pk_ip = p.pk
  inner join register_notice rn on rn.pk = rts.pk_notice
where rts.pr_del is null and rn.pr_del is null and rn.status not containing "�������" and p.date_ip_out is not null
