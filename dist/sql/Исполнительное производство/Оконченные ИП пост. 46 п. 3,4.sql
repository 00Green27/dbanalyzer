--!Оконченные ИП по ст. 46 п. 3,4!--
select count(pk) as 'Количество' from ip where ip.date_ip_out>='$date' and ip.nump26=46 and (ip.num_pp=3 or ip.num_pp=4)