--!4 4 Список должников-юр лиц с неполными данными!--
select num_ip, fio_spi from ip where DATE_IP_OUT is null and NUM_ID is null and VIDD_KEY containing"/2/" and SISP_KEY containing"/2/" and NUM_IP not CONTAINING '-С'
and (VID_ORG_ID_KEY CONTAINING '/1/'or VID_ORG_ID_KEY CONTAINING '/4/' or VID_ORG_ID_KEY CONTAINING '/5/')
