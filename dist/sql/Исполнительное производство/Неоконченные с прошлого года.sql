--!Неоконченные с прошлого года!--
select count(*) from ip where DATE_IP_IN <"$date" and DATE_IP_OUT is null and ip.num_ip not like '%С%'