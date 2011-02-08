--!Уволившиеся в БД!--
select full_name from s_users where full_name in ($full_name)