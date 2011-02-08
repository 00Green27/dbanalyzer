--!Çàãğóæåíî ïîñòàíîâëåíèé ÏÔĞ ñ äàòû!--
select count(pk) as 'Êîëè÷åñòâî' from id where 
	prim containing '/' and org_id containing 'ÏÔ' and d_in >= '$date'