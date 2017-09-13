Rem emaas_dashboard_create_zdt_comparator_table.sql
Rem   
Rem
Rem    DESCRIPTION
Rem      This file will create the table for zdt comparator and syncer which is used to store status information
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    
Rem    pamela    04/09/17 - Created
Rem

Rem DIVERGENCE_PERCENTAGE - the percentage between different rows and the total rows for comparison;
Rem Sync_date - the date/time when sync happens;
Rem Sync_result - succeed or fail;
Rem last_comparison_date - the last comparing date for data being synced
Rem sync_type - incremental or full;

CREATE TABLE EMS_ZDT_SYNC
(
	SYNC_DATE    TIMESTAMP NOT NULL,
	NEXT_SCHEDULE_SYNC_DATE    TIMESTAMP NOT NULL,
	SYNC_TYPE     VARCHAR(256),
	SYNC_RESULT   VARCHAR(256),
	DIVERGENCE_PERCENTAGE  NUMBER(38,8),
	LAST_COMPARISON_DATE  TIMESTAMP,
	CONSTRAINT EMS_ZDT_SYNC_PK PRIMARY KEY (SYNC_DATE) USING INDEX
);

