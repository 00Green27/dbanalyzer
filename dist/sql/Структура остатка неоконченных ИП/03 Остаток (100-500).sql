--!Остаток (100-500)!--
select count(*) from ip where ip.date_ip_out is null and ip.num_ip not like '%С%' and ip.sum_ <= 500 and ip.sum_ > 100