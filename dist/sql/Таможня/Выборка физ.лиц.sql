--!Выборка физ.лиц!--
select 
    i.name_d as 'Ф.И.О.', i.date_born_d as 'Дата рождения', 
    i.num_ip as 'Номер ИП', i.date_ip_in as 'Дата возбуждения ИП', 
    i.num_id as 'Номер ИД', i.date_id_send as 'Дата ИД'
from ip i where
    i.sum_ > 1000 and i.sisp_key containing "/1/" and i.vidd_key containing "/1/" and i.date_ip_out is null