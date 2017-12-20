Rem Copyright (c) 2013, 2014, 2015, 2016, 2017 Oracle and/or its affiliates.
Rem All rights reserved.
Rem
Rem    NAME
Rem      emaas_dashboard_entity_filter_default.sql
Rem
Rem    DESCRIPTION
Rem      update default vale of enable_entity_filter from 1 to 2
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    Chehao       12/04/17
Rem
  
DECLARE
  v_count     INTEGER;
  v_default   varchar2(4):='(2)';
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='ENABLE_ENTITY_FILTER';
  IF v_count>0 THEN
	SELECT data_default into v_default FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='ENABLE_ENTITY_FILTER';
	if v_default!='(2)' THEN
		EXECUTE IMMEDIATE 'alter table ems_dashboard modify (ENABLE_ENTITY_FILTER NUMBER(2,0) DEFAULT(2))';
		DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.ENABLE_ENTITY_FILTER default value is changed to 2!');
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




