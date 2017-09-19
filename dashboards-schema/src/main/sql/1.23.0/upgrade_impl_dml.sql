Rem ----------------------------------------------------------------
Rem 09/19/2017	GUOCHEN		Created file
Rem
Rem ----------------------------------------------------------------

SET FEEDBACK ON
SET SERVEROUTPUT ON
DECLARE
  v_count     INTEGER;
BEGIN
  --add new column 'EMS_DASHBOARD.FEDERATION_SUPPORTED'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='FEDERATION_SUPPORTED';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD ADD "FEDERATION_SUPPORTED" NUMBER(1,0) DEFAULT(0) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.FEDERATION_SUPPORTED exists already, no change is needed');
  END IF;

  --add new column 'EMS_DASHBOARD.GREENFIELD_SUPPORTED'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='GREENFIELD_SUPPORTED';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD ADD "GREENFIELD_SUPPORTED" NUMBER(1,0) DEFAULT(1) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.GREENFIELD_SUPPORTED exists already, no change is needed');
  END IF;

  --add new column 'EMS_DASHBOARD_TILE.FEDERATION_SUPPORTED'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE' AND column_name='FEDERATION_SUPPORTED';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE ADD "FEDERATION_SUPPORTED" NUMBER(1,0) DEFAULT(0) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE.FEDERATION_SUPPORTED exists already, no change is needed');
  END IF;

  --add new column 'EMS_DASHBOARD_TILE.GREENFIELD_SUPPORTED'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE' AND column_name='GREENFIELD_SUPPORTED';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE ADD "GREENFIELD_SUPPORTED" NUMBER(1,0) DEFAULT(1) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE.GREENFIELD_SUPPORTED exists already, no change is needed');
  END IF;


  EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('Failed to run the sql for federated dashboard support due to: '||SQLERRM);
    RAISE;
END;
/

