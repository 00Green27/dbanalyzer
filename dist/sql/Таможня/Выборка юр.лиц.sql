--!������� ��.���!--
select 
    i.name_d as '������������', i.innd as '���', 
    i.num_ip as '����� ��', i.date_ip_in as '���� ����������� ��', 
    i.num_id as '����� ��', i.date_id_send as '���� ��'
from ip i where
    i.sum_ > 1000 and i.sisp_key containing "/1/" and i.vidd_key containing "/2/" and i.date_ip_out is null