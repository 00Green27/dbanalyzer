select p.num_ip from ip p inner join ebook eb on p.pk = eb.pkip inner join registerorders ro on (eb.ebookid=ro.ebookid)
where p.date_ip_out is not null and eb.sumtotalin is not null and eb.sumrest > 0.001 and eb.iserror is null and ro.rest>0.001
group by p.num_ip
