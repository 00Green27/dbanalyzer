select num_ip as '����� ��', name_d as '������������ ��������', name_v as '������������ ����������', 
	  date_ip_in as '���� �����������', date_ip_out as '���� ���������', sum_ as '�����',
	  NUMP26 as '� ������', NUM_PP as '� �� ������'	   
from ip where name_v containing '$name_v'