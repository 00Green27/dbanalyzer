--!Âîçáóæäåíî ÈÏ ïî ïîñò ÏÔĞ!--
select count(p.pk) as 'Êîëè÷åñòâî' from ip p join id d on p.pk_id = d.pk 
where d.prim containing '/' and d.org_id containing 'ÏÔ' and d.d_in >= '$date'