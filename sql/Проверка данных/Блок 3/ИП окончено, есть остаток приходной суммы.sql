select p.num_ip, ro.registerid, ro.rest from ip p
inner join registerorders ro on ro.pkip = p.pk
where ro.iserror is null and p.date_ip_out is not null and ro.rest <> 0