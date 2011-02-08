select name_v as "Взыскатель по ИП", name_d ad "Должник по ИП", sum_ ad "Сумма взыскания по ИП",
result as "Отметка об исполнении (результат)", date_ip_out as  "Дата окончания ИП" from ip where svod_num containing "-СВ"
and num_ip not containing "-СВ"