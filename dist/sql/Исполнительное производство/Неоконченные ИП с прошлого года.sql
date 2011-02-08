--!Неоконченные ИП с прошлого года!--
select count(*) as 'Количество' from ip where DATE_IP_IN <"01.01.2010" and DATE_IP_OUT is null