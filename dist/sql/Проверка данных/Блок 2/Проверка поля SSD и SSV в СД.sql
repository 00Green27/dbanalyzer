select npp, num_ip from ip where (num_ip containing "-яд" and ((ssd is null) or (ssd = 0) or (ssv is null) or (ssv = 1)) )
