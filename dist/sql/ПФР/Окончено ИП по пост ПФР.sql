--!�������� �� �� ���� ���!--
select count(p.pk) as '����������' from ip p join id d on p.pk_id = d.pk where 
	d.prim containing '/' and d.org_id containing '��' and d.d_in >= '$date' and p.date_ip_out is not null