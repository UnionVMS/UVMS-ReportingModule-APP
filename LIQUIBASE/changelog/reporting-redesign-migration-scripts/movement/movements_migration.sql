
insert into reporting.movement(id, position_coordinates , position_time , movement_guid ,
connect_id , "source" , movement_type , reported_course ,
reported_speed , movement_activity_type, closest_port , closest_port_distance , closest_country , closest_country_distance )
select m.move_id, m.move_location, m.move_timestamp, m.move_guid,
mc.moveconn_value, ms.movesour_name, mt.movetyp_name,
m.move_heading, m.move_speed, mat.acttyp_name,
mm.movemet_closestport_code, mm.movemet_closestport_dist, mm.movemet_closecounty_code, mm.movemet_closecounty_dist
from movement.movement m
join movement.movementtype mt on m.move_movetyp_id = mt.movetyp_id
join movement.movementsource ms on m.move_movesour_id = ms.movesour_id
join movement.movementconnect mc on m.move_moveconn_id = mc.moveconn_id
left join movement.movementmetadata mm on m.move_id = movemet_id
left join movement.activitytype mat on m.move_act_id = mat.acttyp_id
where m.move_duplicate = false and m.move_processed = true
order by m.move_id

insert into reporting.area(id, area_code , area_remote_id , area_type )
select distinct ma.movarea_area_id, a.area_code, a.area_remoteid, mat.areatype_name
from movement.movementarea ma
left join movement.area a on ma.movarea_area_id = a.area_id
left join movement.areatype mat on a.area_id = mat.areatype_id
order by ma.movarea_area_id


insert into reporting.movement_area (movement_id , area_id )
select ma.movarea_move_id, ma.movarea_area_id
from movement.movementarea ma
left join movement.area a on ma.movarea_area_id = a.area_id
left join movement.areatype mat on a.area_id = mat.areatype_id
order by ma.movarea_move_id


insert into reporting.track(id, duration, distance, total_time_at_sea , asset_hist_guid )
select distinct trac_id, t.trac_duration, t.trac_distance, t.track_totalsea,  mc.moveconn_value
from movement.track t
join movement.movement m on m.move_trac_id = t.trac_id
join movement.movementconnect mc on m.move_moveconn_id = mc.moveconn_id
order by trac_id


insert into reporting.segment (id, segment_coordinates ,duration, distance , track_id, course_over_ground ,
speed_over_ground , calculated_speed , segment_category , movement_guid ,asset_hist_guid )
select distinct s.seg_id, s.seg_geom , s.seg_duration ,s.seg_distance, s.seg_trac_id ,
s.seg_cog, s.seg_sog, (s.seg_distance /s.seg_duration ) AS calc_speed,
msc.segcat_name , m.move_guid, mc.moveconn_value as asset_hist_guid
from movement.segment s
join movement.segmentcategory msc on msc.segcat_id = s.seg_segcat_id
left join movement.movement m on m.move_id = s.seg_tomove_id
join movement.movementconnect mc on m.move_moveconn_id = mc.moveconn_id
order by s.seg_id asc