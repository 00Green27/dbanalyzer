select num_ip as 'Номер ИП', 
	  name_d as 'Наименование должника', 
	  name_v as 'Наименование взыскателя', 
	  date_ip_in as 'Дата возбуждения', 
	  sum_ as 'Сумма' 
from ip where name_d containing '$name_d'