select num_ip as '����� ��', 
	  name_d as '������������ ��������', 
	  name_v as '������������ ����������', 
	  date_ip_in as '���� �����������', 
	  sum_ as '�����' 
from ip where name_d containing '$name_d'