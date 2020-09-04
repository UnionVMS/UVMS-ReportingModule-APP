In order to populate the new reporting schema based on the existing data, the scripts in this redesign-reporting-migration-scripts folder  must be executed.

the data from assets will come from the asset sql script
the data form movements will come from the movement sql script
the data for alarms(tickets) will come from the rules sql script
the data for activities will come from the activity sql script

the scripts will have to be executed in the order above the queries inside the script in the order they are as well.

The queries in each sql script has the following format:

INSERT INTO _table_name(......)
SELECT(.....)
FROM...
JOIN...
ORDER BY...
OFFSET X LIMIT 1000 <<<< this will have to be added so as the query to be executed multiple times (batches) for tables with many rows

Note that the Liquibase script for creating the new reporting tables will have to be executed first.
Also the script must have access to the 'from schema' existing movement/asset/rules/activities schema as well as the reporting's which will be the 'target schema'
If access is not possible for all of these, then using the SELECT part of the given queries should be executed and put into a (csv) file and loaded from where the reporting schema is.

The app should not be running and the queries should be executed in batches for large tables,
meaning that a query must be executed multiple times with limit so as to do it in batches:
add i.e an OFFSET 0 LIMIT 1000...OFFSET 1000 LIMIT 1000 to the given query
