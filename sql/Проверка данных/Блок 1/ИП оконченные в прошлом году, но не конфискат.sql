select ip.num_ip from ip left outer join document on (ip.pk=document.fk and document.kod in (141,225,226)) where ip.date_ip_out<='31.12.2009' and document.pk is null

