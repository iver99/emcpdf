Rem --DDL change during upgrade
Rem
Rem upgrade_impl_ddl.sql
Rem
Rem Copyright (c) 2013, 2014, 2015, 2016 Oracle and/or its affiliates.
Rem All rights reserved.
Rem
Rem    NAME
Rem      upgrade_impl_ddl.sql
Rem
Rem    DESCRIPTION
Rem      DDL change during upgrade
Rem
Rem    NOTES
Rem      None
Rem

SET FEEDBACK ON
SET SERVEROUTPUT ON

DECLARE
  v_count     INTEGER;
  v_default   varchar2(4):='(2)';
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='ENABLE_ENTITY_FILTER';
  IF v_count>0 THEN
	SELECT data_default into v_default FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='ENABLE_ENTITY_FILTER';
	if v_default!='(2)' THEN
		EXECUTE IMMEDIATE 'alter table ems_dashboard modify (ENABLE_ENTITY_FILTER NUMBER(2,0) DEFAULT(2))';
		DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.ENABLE_ENTITY_FILTER is change default value to 2!');
	ELSE
		DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.ENABLE_ENTITY_FILTER default value is alreay set to 2, no need to update!');
	END IF;
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.ENABLE_ENTITY_FILTER is not existed! Cannot change column default value!');
  END IF;
EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to change default value for  EMS_DASHBOARD.ENABLE_ENTITY_FILTER to 2 due to '||SQLERRM);
  RAISE;
END;
/

@&EMSAAS_SQL_ROOT/1.27.0/emaas_dashboard_tile_title_length.sql


