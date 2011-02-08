--!Оконченные ИП по ст. 47 п. 5!--
select count(pk) as 'Количество' from ip where ip.date_ip_out>='$date' and ip.nump26=47 and ip.num_pp=5