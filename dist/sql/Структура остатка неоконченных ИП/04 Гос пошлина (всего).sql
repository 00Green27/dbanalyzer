--!��������������� ������� (�����)!--
select count(*) from ip where ip.date_ip_out is null and ip.num_ip not like '%�%' 
and SISP_TTL containing '�������������� ���������.������ � �����.����������'