select distinct(p.num_ip)
from ip p
  inner join register_take_sum rts on rts.pk_ip = p.pk
where rts.pr_del is null and rts.pk_notice is null and p.date_ip_out is not null