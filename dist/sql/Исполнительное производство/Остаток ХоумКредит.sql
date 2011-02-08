--!Остаток ХоумКредит!--
select name_d as 'Наименование должника', name_v as 'Наименование взыскателя', 
	  date_ip_in as 'Дата возбуждения', sum_ as 'Сумма', fio_spi as 'СПИ'
from ip where (name_v  containing 'хоумкредит' or name_v  containing 'хоум кредит') and date_ip_in < '01.11.2009' and date_ip_out is null