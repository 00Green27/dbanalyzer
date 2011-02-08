--!Административные штрафы судов (всего)!--
select count(*) from ip where ip.date_ip_out is null and ip.num_ip not like '%С%' 
and SISP_TTL containing 'ИМУЩЕСТВЕННОГО ХАРАКТЕРА.ШТРАФ.ШТРАФ СУДА КАК ВИД АДМИНИСТРАТИВНОГО НАКАЗАНИЯ'