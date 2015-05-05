------------------------------------------------------------------------------------------------------------------------
select ro.registerid, ro.numberip, ro.registersumm, ro.rest, L_PRD.sum_prd, ROUND((ro.registersumm-L_PRD.sum_prd),2) as newrest
from
(select sum(SUM_OSN_DOLG+SUM_SH+SUM_RAS+SUM_IS+SUM_PR_RAS) as sum_prd, registerid from post_rasp_ds where (TRANSFER_OD is null) and (TRANSFER_PR is null) and (ISERROR is null) group by registerid) L_PRD
inner join registerorders ro ON (L_PRD.registerid=ro.registerid and ROUND((ro.registersumm-L_PRD.sum_prd),2)<>ROUND(ro.rest,2))
where ro.ISERROR is null
------------------------------------------------------------------------------------------------------------------------