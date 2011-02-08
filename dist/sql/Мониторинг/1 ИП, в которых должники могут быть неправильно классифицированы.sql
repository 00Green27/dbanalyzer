--!Список ИП, в которых должники могут быть неправильно классифицированы!--
select NUM_IP, VIDD_KEY, VIDD_TTL, NAME_D, DATE_IP_IN, FIO_SPI, PRIMARY_SITE FROM IP left join s_users on (ip.uscode=s_users.uscode) WHERE date_ip_out is null and ssd is null and ssv is null and VIDD_KEY NOT LIKE '/1/%' AND ((NAME_D LIKE '%ВНА') or (NAME_D LIKE '%ВИЧ')) order by PRIMARY_SITE
