--!Выгружено постановлений об окончании!--
select count(pk) as 'Количество' from doc_info where name_fld='isExport' and int_fld=1 and (kod=99 or kod=347)