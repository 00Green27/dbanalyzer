select name_v as "���������� �� ��", name_d ad "������� �� ��", sum_ ad "����� ��������� �� ��",
result as "������� �� ���������� (���������)", date_ip_out as  "���� ��������� ��" from ip where svod_num containing "-��"
and num_ip not containing "-��"