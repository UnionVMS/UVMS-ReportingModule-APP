insert into reporting.alarm (guid, asset_hist_guid , rule_name , movement_guid , status , open_date , updated_date , updated_by)
select t.ticket_guid, t.ticket_assetguid, t.ticket_rulename , t.ticket_movementguid, t.ticket_status , t.ticket_createddate , t.ticket_updattim , t.ticket_upuser c
from rules.ticket t
order by t.ticket_id