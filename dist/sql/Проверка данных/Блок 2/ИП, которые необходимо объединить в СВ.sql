select distinct ip1.* from ip ip1, ip ip2 where ip1.pk <> ip2.pk
       and ip1.num_id = ip2.num_id and ip1.date_id_send  = ip2.date_id_send and ip1.sum_ = ip2.sum_
       and ip1.pk_svod is null and ip1.date_ip_out is null
       and ip2.date_ip_out is null  order by ip1.num_id, ip1.date_id_send, ip1.sum_
