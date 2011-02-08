--!Выгружено постановлений об отказе в возбуждении!--
select count(pk) as 'Количество' from doc_info where name_fld='isExport' and int_fld=1 and kod=36