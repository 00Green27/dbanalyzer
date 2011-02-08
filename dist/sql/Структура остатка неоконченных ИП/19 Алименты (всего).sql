--!Àëèìåíòû (âñåãî)!--
select count(*) from ip where ip.date_ip_out is null and ip.num_ip not like '%Ñ%' 
and SISP_TTL containing 'ÈÌÓÙÅÑÒÂÅÍÍÎÃÎ ÕÀĞÀÊÒÅĞÀ.ÀËÈÌÅÍÒÛ'