Rem ----------------------------------------------------------------
Rem 11/21/2017	REX	Created file
Rem
Rem ----------------------------------------------------------------

--OMCSQ048: Indexes with a tenant_id key column must list it first
DECLARE
  v_count_index        INTEGER;
  v_count_position     INTEGER;
BEGIN
  
  -- EMS_DASHBOARD_LAST_ACCESS
  SELECT COUNT(1) INTO v_count_index FROM user_constraints WHERE constraint_name = 'EMS_DASHBOARD_LAST_ACCESS_PK' AND table_name = 'EMS_DASHBOARD_LAST_ACCESS';
  SELECT COUNT(1) INTO v_count_position FROM all_ind_columns WHERE INDEX_NAME = 'EMS_DASHBOARD_LAST_ACCESS_PK' AND TABLE_NAME = 'EMS_DASHBOARD_LAST_ACCESS' AND COLUMN_NAME = 'TENANT_ID' AND COLUMN_POSITION = 1;
  IF v_count_index=0 THEN
    DBMS_OUTPUT.PUT_LINE('constraint EMS_DASHBOARD_LAST_ACCESS_PK does not exist');
  ELSIF  v_count_position=1 THEN
    DBMS_OUTPUT.PUT_LINE('TENANT_ID has been already listed first in EMS_DASHBOARD_LAST_ACCESS_PK');
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_LAST_ACCESS DROP CONSTRAINT EMS_DASHBOARD_LAST_ACCESS_PK DROP INDEX';
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_LAST_ACCESS ADD CONSTRAINT EMS_DASHBOARD_LAST_ACCESS_PK PRIMARY KEY(TENANT_ID, DASHBOARD_ID, ACCESSED_BY) USING INDEX (CREATE UNIQUE INDEX EMS_DASHBOARD_LAST_ACCESS_PK ON EMS_DASHBOARD_LAST_ACCESS (TENANT_ID, DASHBOARD_ID, ACCESSED_BY) COMPRESS ADVANCED LOW)';
    DBMS_OUTPUT.PUT_LINE('TENANT_ID is listed first in EMS_DASHBOARD_LAST_ACCESS_PK successfully!');
  END IF;
  
  -- EMS_DASHBOARD
  SELECT COUNT(1) INTO v_count_index FROM user_constraints WHERE constraint_name = 'EMS_DASHBOARD_PK' AND table_name = 'EMS_DASHBOARD';
  SELECT COUNT(1) INTO v_count_position FROM all_ind_columns WHERE INDEX_NAME = 'EMS_DASHBOARD_PK' AND TABLE_NAME = 'EMS_DASHBOARD' AND COLUMN_NAME = 'TENANT_ID' AND COLUMN_POSITION = 1;
  IF v_count_index=0 THEN
    DBMS_OUTPUT.PUT_LINE('constraint EMS_DASHBOARD_PK does not exist');
  ELSIF  v_count_position=1 THEN
    DBMS_OUTPUT.PUT_LINE('TENANT_ID has been already listed first in EMS_DASHBOARD_PK');
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD DROP CONSTRAINT EMS_DASHBOARD_PK DROP INDEX';
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD ADD CONSTRAINT EMS_DASHBOARD_PK PRIMARY KEY(TENANT_ID, DASHBOARD_ID) USING INDEX (CREATE UNIQUE INDEX EMS_DASHBOARD_PK ON EMS_DASHBOARD (TENANT_ID, DASHBOARD_ID) COMPRESS ADVANCED LOW)';
    DBMS_OUTPUT.PUT_LINE('TENANT_ID is listed first in EMS_DASHBOARD_PK successfully!');
  END IF;
  
  -- EMS_DASHBOARD_FAVORITE
  SELECT COUNT(1) INTO v_count_index FROM user_constraints WHERE constraint_name = 'EMS_DASHBOARD_FAVORITE_PK' AND table_name = 'EMS_DASHBOARD_FAVORITE';
  SELECT COUNT(1) INTO v_count_position FROM all_ind_columns WHERE INDEX_NAME = 'EMS_DASHBOARD_FAVORITE_PK' AND TABLE_NAME = 'EMS_DASHBOARD_FAVORITE' AND COLUMN_NAME = 'TENANT_ID' AND COLUMN_POSITION = 1;
  IF v_count_index=0 THEN
    DBMS_OUTPUT.PUT_LINE('constraint EMS_DASHBOARD_FAVORITE_PK does not exist');
  ELSIF  v_count_position=1 THEN
    DBMS_OUTPUT.PUT_LINE('TENANT_ID has been already listed first in EMS_DASHBOARD_FAVORITE_PK');
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_FAVORITE DROP CONSTRAINT EMS_DASHBOARD_FAVORITE_PK DROP INDEX';
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_FAVORITE ADD CONSTRAINT EMS_DASHBOARD_FAVORITE_PK PRIMARY KEY(TENANT_ID,DASHBOARD_ID,USER_NAME) USING INDEX (CREATE UNIQUE INDEX EMS_DASHBOARD_FAVORITE_PK ON EMS_DASHBOARD_FAVORITE (TENANT_ID,DASHBOARD_ID,USER_NAME) COMPRESS ADVANCED LOW)';
    DBMS_OUTPUT.PUT_LINE('TENANT_ID is listed first in EMS_DASHBOARD_FAVORITE_PK successfully!');
  END IF;
  
  -- EMS_DASHBOARD_TILE
  SELECT COUNT(1) INTO v_count_index FROM user_constraints WHERE constraint_name = 'EMS_DASHBOARD_TILE_PK' AND table_name = 'EMS_DASHBOARD_TILE';
  SELECT COUNT(1) INTO v_count_position FROM all_ind_columns WHERE INDEX_NAME = 'EMS_DASHBOARD_TILE_PK' AND TABLE_NAME = 'EMS_DASHBOARD_TILE' AND COLUMN_NAME = 'TENANT_ID' AND COLUMN_POSITION = 1;
  IF v_count_index=0 THEN
    DBMS_OUTPUT.PUT_LINE('constraint EMS_DASHBOARD_TILE_PK does not exist');
  ELSIF  v_count_position=1 THEN
    DBMS_OUTPUT.PUT_LINE('TENANT_ID has been already listed first in EMS_DASHBOARD_TILE_PK');
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE DROP CONSTRAINT EMS_DASHBOARD_TILE_PK DROP INDEX';
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE ADD CONSTRAINT EMS_DASHBOARD_TILE_PK PRIMARY KEY(TENANT_ID,TILE_ID) USING INDEX (CREATE UNIQUE INDEX EMS_DASHBOARD_TILE_PK ON EMS_DASHBOARD_TILE (TENANT_ID,TILE_ID) COMPRESS ADVANCED LOW)';
    DBMS_OUTPUT.PUT_LINE('TENANT_ID is listed first in EMS_DASHBOARD_TILE_PK successfully!');
  END IF;
  
  -- EMS_DASHBOARD_TILE_PARAMS
  SELECT COUNT(1) INTO v_count_index FROM user_constraints WHERE constraint_name = 'EMS_DASHBOARD_TILE_PARAMS_PK' AND table_name = 'EMS_DASHBOARD_TILE_PARAMS';
  SELECT COUNT(1) INTO v_count_position FROM all_ind_columns WHERE INDEX_NAME = 'EMS_DASHBOARD_TILE_PARAMS_PK' AND TABLE_NAME = 'EMS_DASHBOARD_TILE_PARAMS' AND COLUMN_NAME = 'TENANT_ID' AND COLUMN_POSITION = 1;
  IF v_count_index=0 THEN
    DBMS_OUTPUT.PUT_LINE('constraint EMS_DASHBOARD_TILE_PARAMS_PK does not exist');
  ELSIF  v_count_position=1 THEN
    DBMS_OUTPUT.PUT_LINE('TENANT_ID has been already listed first in EMS_DASHBOARD_TILE_PARAMS_PK');
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE_PARAMS DROP CONSTRAINT EMS_DASHBOARD_TILE_PARAMS_PK DROP INDEX';
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE_PARAMS ADD CONSTRAINT EMS_DASHBOARD_TILE_PARAMS_PK PRIMARY KEY(TENANT_ID,TILE_ID,PARAM_NAME) USING INDEX (CREATE UNIQUE INDEX EMS_DASHBOARD_TILE_PARAMS_PK ON EMS_DASHBOARD_TILE_PARAMS (TENANT_ID,TILE_ID,PARAM_NAME) COMPRESS ADVANCED LOW)';
    DBMS_OUTPUT.PUT_LINE('TENANT_ID is listed first in EMS_DASHBOARD_TILE_PARAMS_PK successfully!');
  END IF;
  
  -- EMS_PREFERENCE
  SELECT COUNT(1) INTO v_count_index FROM user_constraints WHERE constraint_name = 'EMS_PREFERENCES_PK' AND table_name = 'EMS_PREFERENCE';
  SELECT COUNT(1) INTO v_count_position FROM all_ind_columns WHERE INDEX_NAME = 'EMS_PREFERENCES_PK' AND TABLE_NAME = 'EMS_PREFERENCE' AND COLUMN_NAME = 'TENANT_ID' AND COLUMN_POSITION = 1;
  IF v_count_index=0 THEN
    DBMS_OUTPUT.PUT_LINE('constraint EMS_PREFERENCES_PK does not exist');
  ELSIF  v_count_position=1 THEN
    DBMS_OUTPUT.PUT_LINE('TENANT_ID has been already listed first in EMS_PREFERENCES_PK');
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_PREFERENCE DROP CONSTRAINT EMS_PREFERENCES_PK DROP INDEX';
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_PREFERENCE ADD CONSTRAINT EMS_PREFERENCES_PK PRIMARY KEY(TENANT_ID, USER_NAME, PREF_KEY) USING INDEX (CREATE UNIQUE INDEX EMS_PREFERENCES_PK ON EMS_PREFERENCE (TENANT_ID, USER_NAME, PREF_KEY) COMPRESS ADVANCED LOW)';
    DBMS_OUTPUT.PUT_LINE('TENANT_ID is listed first in EMS_PREFERENCES_PK successfully!');
  END IF;
  
  EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('>>>DF DDL ERROR<<<');
    DBMS_OUTPUT.PUT_LINE('Failed to rebuild constraint due to error '||SQLERRM);
    RAISE;
END;
/

