--!������ ��, � �������, ��������, ����������� ������� ���� �������� ��������!--
SELECT NUM_IP, DATE_BORN_D, NAME_D, DATE_IP_IN, FIO_SPI, PRIMARY_SITE FROM IP left join s_users on (ip.uscode=s_users.uscode) where VIDD_KEY containing"/1/" and date_ip_out is null and ssd is null and ssv is null and DATE_BORN_D is not null AND NOT( (TRIM(DATE_BORN_D) LIKE  '__.__.____' AND TRIM(DATE_BORN_D) NOT LIKE '%..%' AND TRIM(DATE_BORN_D) NOT LIKE '%._.%') OR ((TRIM(DATE_BORN_D) LIKE '__.__.'  OR TRIM(DATE_BORN_D) LIKE '.__.__' ) AND TRIM(DATE_BORN_D) NOT LIKE '%..%' AND TRIM(DATE_BORN_D) NOT LIKE '%._.%') OR ( (TRIM(DATE_BORN_D) LIKE '%.____' AND TRIM(DATE_BORN_D) NOT LIKE '%._.__' AND TRIM(DATE_BORN_D) NOT LIKE '%.__._' AND TRIM(DATE_BORN_D) NOT LIKE '%..__'  AND TRIM(DATE_BORN_D) NOT LIKE '%.___.') OR (TRIM(DATE_BORN_D) LIKE '____' AND TRIM(DATE_BORN_D) NOT LIKE '%.%') ) )