select
	name_v as "Взыскатель по ИП", name_d as "Должник по ИП", sum_ as "Сумма взыскания по ИП",
	result as "Отметка об исполнении (результат)", date_ip_out as  "Дата окончания ИП", num_ip
from ip where num_ip containing "-СВ"