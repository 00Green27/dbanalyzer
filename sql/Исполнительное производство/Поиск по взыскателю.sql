select num_ip as 'Номер ИП', name_d as 'Наименование должника', name_v as 'Наименование взыскателя', 
	  date_ip_in as 'Дата возбуждения', date_ip_out as 'Дата окончания', sum_ as 'Сумма',
	  NUMP26 as '№ статьи', NUM_PP as '№ пп статьи'	   
from ip where name_v containing '$name_v'