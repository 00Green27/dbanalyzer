select distinct(p.num_ip), rn.num_notice
from ip p inner join register_take_sum rts on rts.pk_ip = p.pk
          inner join register_notice rn on rn.pk = rts.pk_notice
where rts.pr_del is null and rn.pr_del is null and p.date_ip_out is not null
  and rn.status containing "œ–»Õﬂ“¿" and rn.isused is null