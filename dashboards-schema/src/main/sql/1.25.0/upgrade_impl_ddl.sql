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
BEGIN
  --add new column 'EMS_DASHBOARD.FEDERATION_SUPPORTED'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='FEDERATION_SUPPORTED';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD ADD "FEDERATION_SUPPORTED" NUMBER(1,0) DEFAULT(0) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.FEDERATION_SUPPORTED exists already, no change is needed');
  END IF;

  --add new column 'EMS_DASHBOARD_TILE.FEDERATION_SUPPORTED'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE' AND column_name='FEDERATION_SUPPORTED';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE ADD "FEDERATION_SUPPORTED" NUMBER(1,0) DEFAULT(0) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE.FEDERATION_SUPPORTED exists already, no change is needed');
  END IF;

  --add new column 'EMS_DASHBOARD.STATE'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='STATE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD ADD "STATE" NUMBER(1,0) DEFAULT(0) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.STATE exists already, no change is needed');
  END IF;

  EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('>>>DF DDL ERROR<<<');
    DBMS_OUTPUT.PUT_LINE('Failed to run the sql for federated dashboard support due to: '||SQLERRM);
    RAISE;
END;
/

DECLARE
  v_count_index        INTEGER;
  v_count_position     INTEGER;
BEGIN
  --OMCSQ048: Indexes with a tenant_id key column must list it first
  SELECT COUNT(1) INTO v_count_index FROM user_constraints WHERE constraint_name = 'EMS_PREFERENCES_PK' AND table_name = 'EMS_PREFERENCE';
  SELECT COUNT(1) INTO v_count_position FROM all_ind_columns WHERE INDEX_NAME = 'EMS_PREFERENCES_PK' AND TABLE_NAME = 'EMS_PREFERENCE' AND COLUMN_NAME = 'TENANT_ID' AND COLUMN_POSITION = 1;
  IF v_count_index=0 THEN
    DBMS_OUTPUT.PUT_LINE('constraint EMS_PREFERENCES_PK does not exist');
  ELSIF  v_count_position=1 THEN
    DBMS_OUTPUT.PUT_LINE('TENANT_ID has been already listed first in EMS_PREFERENCES_PK');
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_PREFERENCE DROP CONSTRAINT EMS_PREFERENCES_PK';
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_PREFERENCE ADD CONSTRAINT EMS_PREFERENCES_PK PRIMARY KEY(TENANT_ID, USER_NAME, PREF_KEY)';
    DBMS_OUTPUT.PUT_LINE('TENANT_ID is listed first in EMS_PREFERENCES_PK successfully!');
  END IF;
  EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('>>>DF DDL ERROR<<<');
    DBMS_OUTPUT.PUT_LINE('Failed to rebuild EMS_PREFERENCES_PK due to error '||SQLERRM);
    RAISE;
END;
/

DECLARE
  v_count_index        INTEGER;
  v_count_position     INTEGER;
BEGIN
  --OMCSQ048: Indexes with a tenant_id key column must list it first
  SELECT COUNT(1) INTO v_count_index FROM user_constraints WHERE constraint_name = 'EMS_DASHBOARD_LAST_ACCESS_PK' AND table_name = 'EMS_DASHBOARD_LAST_ACCESS';
  SELECT COUNT(1) INTO v_count_position FROM all_ind_columns WHERE INDEX_NAME = 'EMS_DASHBOARD_LAST_ACCESS_PK' AND TABLE_NAME = 'EMS_DASHBOARD_LAST_ACCESS' AND COLUMN_NAME = 'TENANT_ID' AND COLUMN_POSITION = 1;
  IF v_count_index=0 THEN
    DBMS_OUTPUT.PUT_LINE('constraint EMS_DASHBOARD_LAST_ACCESS_PK does not exist');
  ELSIF  v_count_position=1 THEN
    DBMS_OUTPUT.PUT_LINE('TENANT_ID has been already listed first in EMS_DASHBOARD_LAST_ACCESS_PK');
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_LAST_ACCESS DROP CONSTRAINT EMS_DASHBOARD_LAST_ACCESS_PK';
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_LAST_ACCESS ADD CONSTRAINT EMS_DASHBOARD_LAST_ACCESS_PK PRIMARY KEY(TENANT_ID, DASHBOARD_ID, ACCESSED_BY)';
    DBMS_OUTPUT.PUT_LINE('TENANT_ID is listed first in EMS_DASHBOARD_LAST_ACCESS_PK successfully!');
  END IF;
  EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('>>>DF DDL ERROR<<<');
    DBMS_OUTPUT.PUT_LINE('Failed to rebuild EMS_DASHBOARD_LAST_ACCESS_PK due to error '||SQLERRM);
    RAISE;
END;
/

