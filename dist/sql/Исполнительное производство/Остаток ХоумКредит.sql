--!������� ����������!--
select name_d as '������������ ��������', name_v as '������������ ����������', 
	  date_ip_in as '���� �����������', sum_ as '�����', fio_spi as '���'
from ip where (name_v  containing '����������' or name_v  containing '���� ������') and date_ip_in < '01.11.2009' and date_ip_out is null