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

Rem Comparison_date - the date/time when the comparison is executed;
Rem Comparison_type - full or incremental; 
Rem Comparison_result - the different rows in json format according to comparison results;
Rem DIVERGENCE_PERCENTAGE - the percentage between different rows and the total rows for comparison;


CREATE TABLE EMS_ZDT_COMPARATOR
(
	COMPARISON_DATE    TIMESTAMP NOT NULL,
	NEXT_SCHEDULE_COMPARISON_DATE    TIMESTAMP NOT NULL,
	COMPARISON_TYPE     VARCHAR(256),
	COMPARISON_RESULT   CLOB,
	DIVERGENCE_PERCENTAGE  NUMBER(38,8),
	CONSTRAINT EMS_ZDT_COMPARATOR_PK PRIMARY KEY (COMPARISON_DATE) USING INDEX
);

