--!яСДЕАМШЕ ОПХЙЮГШ МЮКНЦХ (100-500)!--
select count(*) from ip where ip.date_ip_out is null and ip.num_ip not like '%я%' 
and VID_ORG_ID_TTL containing 'ясдеамше нпцюмш.лхпнбни ясдэъ' and SISP_TTL containing 'хлсыеярбеммнцн уюпюйрепю.мюкнцх х яанпш' and SISP_TTL <> 'хлсыеярбеммнцн уюпюйрепю.мюкнцх х яанпш.цняонькхмю' 
and ip.sum_ <= 500 and ip.sum_ > 100