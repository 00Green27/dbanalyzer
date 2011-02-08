--!Уволившиеся в БД по фимилии!--
select surname, name, patronymic from s_users where s_users.surname in ($surname)