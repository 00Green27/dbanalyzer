--!Без постановления об отмене ограничения!--
select ip.num_ip as 'Номер ИП', ip.name_d as 'Должник', d.date_doc as 'Дата документа' from ip join document d on ip.pk = d.fk where
((((0 < (select count(PK) from STATUS_IP where (FK_IP = IP.PK) and (STATUS_CODE = 9) and (DATE_START <= "07.12.2010") 
and (DATE_FINISH is null or DATE_FINISH >= "07.12.2010"))) and(DATE_IP_OUT >="$date")))) 
and  ip.pk not in (select document.fk from document where document.kod=380) and d.kod=600