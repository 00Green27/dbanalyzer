--!Непринято ИП БД Архив!--
select count(ip.pk) from ip right join list_ip on list_ip.pk = ip.fk where list_ip.date_archive_in is null