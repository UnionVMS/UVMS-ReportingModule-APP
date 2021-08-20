INSERT INTO reporting.movement(id, position_coordinates, position_time, movement_guid, connect_id, "source", movement_type, reported_course, reported_speed, movement_activity_type, closest_port, closest_port_distance, closest_country, closest_country_distance)
SELECT m.move_id,
       m.move_location,
       m.move_timestamp,
       m.move_guid,
       mc.moveconn_value,
       ms.movesour_name,
       mt.movetyp_name,
       m.move_heading,
       m.move_speed,
       mat.acttyp_name,
       mm.movemet_closestport_code,
       mm.movemet_closestport_dist,
       mm.movemet_closecounty_code,
       mm.movemet_closecounty_dist
FROM movement.movement m
JOIN movement.movementtype mt ON m.move_movetyp_id = mt.movetyp_id
JOIN movement.movementsource ms ON m.move_movesour_id = ms.movesour_id
JOIN movement.movementconnect mc ON m.move_moveconn_id = mc.moveconn_id
LEFT JOIN movement.movementmetadata mm ON m.move_id = movemet_id
LEFT JOIN movement.activitytype mat ON m.move_act_id = mat.acttyp_id
WHERE m.move_duplicate = FALSE
  AND m.move_processed = TRUE
ORDER BY m.move_id;

INSERT INTO reporting.area(id, area_code, area_remote_id, area_type)
SELECT DISTINCT ma.movarea_area_id,
                a.area_code,
                a.area_remoteid,
                mat.areatype_name
FROM movement.movementarea ma
LEFT JOIN movement.area a ON ma.movarea_area_id = a.area_id
LEFT JOIN movement.areatype mat ON a.area_id = mat.areatype_id
ORDER BY ma.movarea_area_id;


INSERT INTO reporting.movement_area (movement_id, area_id)
SELECT ma.movarea_move_id,
       ma.movarea_area_id
FROM movement.movementarea ma
LEFT JOIN movement.area a ON ma.movarea_area_id = a.area_id
LEFT JOIN movement.areatype mat ON a.area_id = mat.areatype_id
ORDER BY ma.movarea_move_id;


INSERT INTO reporting.track(id, duration, distance, total_time_at_sea, asset_hist_guid)
SELECT DISTINCT trac_id,
                t.trac_duration,
                t.trac_distance,
                t.track_totalsea,
                mc.moveconn_value
FROM movement.track t
JOIN movement.movement m ON m.move_trac_id = t.trac_id
JOIN movement.movementconnect mc ON m.move_moveconn_id = mc.moveconn_id
ORDER BY trac_id;


INSERT INTO reporting.segment (id, segment_coordinates, duration, distance, track_id, course_over_ground, speed_over_ground, calculated_speed, segment_category, movement_guid, asset_hist_guid)
SELECT DISTINCT s.seg_id,
                s.seg_geom,
                s.seg_duration,
                s.seg_distance,
                s.seg_trac_id,
                s.seg_cog,
                s.seg_sog,
                (s.seg_distance /nullif(s.seg_duration, 0)) AS calc_speed,
                msc.segcat_name,
                m.move_guid,
                mc.moveconn_value AS asset_hist_guid
FROM movement.segment s
JOIN movement.segmentcategory msc ON msc.segcat_id = s.seg_segcat_id
LEFT JOIN movement.movement m ON m.move_id = s.seg_tomove_id
JOIN movement.movementconnect mc ON m.move_moveconn_id = mc.moveconn_id
ORDER BY s.seg_id ASC;

