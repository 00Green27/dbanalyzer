select npp, num_ip from ip where (num_ip containing "-��" and ((ssd is null) or (ssd = 1) or (ssv is null) or (ssv = 0)) )
