--!Количество непринятых описей БД Архив!--
select count(pk) as Количество, sum(count_ip) as 'Количество ИП' from list_ip where list_ip.date_archive_in is null