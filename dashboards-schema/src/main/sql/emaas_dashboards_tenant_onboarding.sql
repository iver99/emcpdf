Rem
Rem
Rem emaas_dashboards_tenant_onboarding.sql
Rem
Rem Copyright (c) 2013, 2014, Oracle and/or its affiliates.
Rem All rights reserved.
Rem
Rem    NAME
Rem      emaas_dashboards_tenant_onboarding.sql
Rem
Rem    DESCRIPTION
Rem      Tenant onboarding file for Dashboard Frameowrk - tenantid is passed by RM & we call seed data with
Rem      that tenant id & data gets inserted for that tenant
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    miayu   1/26/1015  Created

WHENEVER SQLERROR EXIT ROLLBACK
SET FEEDBACK ON
SET SERVEROUTPUT ON
DEFINE TENANT_ID = '&1'
DEFINE EMSAAS_SQL_ROOT = '&2'

DECLARE
  oob_dsb_count NUMBER;
  Valid_Input   NUMBER;
BEGIN
  -- If the count of OOB dashboards is ZERO then insert the OOB serches , otherwise do nothing
  -- for given tenant.
  BEGIN
    Valid_Input := TO_NUMBER( &TENANT_ID);
  EXCEPTION
  WHEN VALUE_ERROR THEN
    RAISE_APPLICATION_ERROR(-21000, ' Please  specify valid internal tenant id');
  END;
  SELECT COUNT(*)
  INTO oob_dsb_count
  FROM EMS_DASHBOARD
  WHERE DASHBOARD_ID <=1000
  AND TENANT_ID       ='&TENANT_ID';
  IF oob_dsb_count    >0 THEN
    DBMS_OUTPUT.PUT_LINE('OOB dashboards for &TENANT_ID are already present');
    RAISE_APPLICATION_ERROR(-20000, ' OOB dashboards for &TENANT_ID are already present');
  END IF;
END;
/
@&EMSAAS_SQL_ROOT/1.0.0/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.0.25/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.1.0/emaas_dashboards_seed_data.sql &TENANT_ID

@&EMSAAS_SQL_ROOT/1.2.0/emaas_dashboards_seed_data.sql &TENANT_ID

COMMIT;
/
BEGIN
  DBMS_OUTPUT.PUT_LINE('Inserting OOB dashboards for &TENANT_ID is completed');
END;
/

