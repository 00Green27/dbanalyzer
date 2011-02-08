--!Количество запросов в Крайтехинвентаризацию!--
select count(pk) as Количество from  zapros where zapros.status='Запрос отправлен'
and zapros.adresat='Крайтехинвентаризация'