--!������� (�� 100)!--
select count(*) from ip where ip.date_ip_out is null and ip.num_ip not like '%�%' and ip.sum_ <= 100 and ip.sum_ <> 0