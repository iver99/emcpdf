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
  v_count INTEGER;
  v_sql LONG;
BEGIN
  --create new table 'EMS_DASHBOARD_RESOURCE_BUNDLE' if not exists
  SELECT count(*) into v_count FROM user_tables where table_name = 'EMS_DASHBOARD_RESOURCE_BUNDLE';
  IF (v_count <= 0)  THEN
    v_sql := 'CREATE TABLE EMS_DASHBOARD_RESOURCE_BUNDLE
  (
    LANGUAGE_CODE                VARCHAR(2) NOT NULL,
    COUNTRY_CODE                 VARCHAR(4) NOT NULL,
    SERVICE_NAME                 VARCHAR(255) NOT NULL,
    SERVICE_VERSION              VARCHAR(255),
    PROPERTIES_FILE              NCLOB,
    LAST_MODIFICATION_DATE       TIMESTAMP NOT NULL,
    CONSTRAINT EMS_DASHBOARD_RESOURCE_B_PK PRIMARY KEY (LANGUAGE_CODE, COUNTRY_CODE, SERVICE_NAME) USING INDEX
  )';
    EXECUTE IMMEDIATE v_sql;
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_RESOURCE_BUNDLE table created successfully');
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_RESOURCE_BUNDLE table exists already');
  END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_SET DROP CONSTRAINT EMS_DASHBOARD_SET_FK2';
  EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -2443 THEN
         RAISE;
      END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_USER_OPTIONS DROP CONSTRAINT EMS_DASHBOARD_USER_OPTIONS_FK1';
  EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -2443 THEN
         RAISE;
      END IF;
END;
/


