select ips.num_IP as num_IP,
 (select RO.numberip from registerorders RO where RO.PKIP=IPS.PK and RO.iserror is null and RO.REST>0.001 group by RO.numberip) as ostatok_summ,
 (select ip.num_ip from ip inner join document on (ip.pk=document.fk) inner join AR_IM on (ar_im.pk_doc=document.pk) where ips.pk=ip.pk group by ip.num_ip) as arest
from ip IPS where
 (Select count(pk) from ip where ip.pk_svod=ips.pk and ((ip.ssd is null) or (ip.ssv is null)))=0 and ((ips.ssd is not null) or (ips.ssv is not null))
