select npp, num_ip from ip where (num_ip not containing "-�" and ((ssd is not null) or (ssv is not null)))
