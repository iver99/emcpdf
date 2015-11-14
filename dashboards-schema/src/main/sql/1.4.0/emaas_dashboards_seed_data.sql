Rem
Rem emaas_dashboards_seed_data.sql
Rem
Rem Copyright (c) 2013, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      emaas_dashboards_seed_data.sql 
Rem
Rem    DESCRIPTION
Rem      Dashboard Framework seed data sql file.
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    miayu    10/14/15- Created
Rem

DEFINE TENANT_ID = '&1'
SET FEEDBACK ON
SET SERVEROUTPUT ON

DECLARE

BEGIN  

--Rem screenshots for OOB ITA Dashboards
UPDATE EMS_DASHBOARD
SET LAST_MODIFICATION_DATE   =NVL(LAST_MODIFICATION_DATE,CREATION_DATE),
  LAST_MODIFIED_BY='Oracle'
WHERE tenant_id   ='&TENANT_ID'
AND dashboard_id <=1000;

commit;
DBMS_OUTPUT.PUT_LINE('Update OOB Dashboard modification data in Schema object: EMS_DASHBOARD for tenant: &TENANT_ID successfully');

EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to update OOB Dashboard modification data in Schema object: EMS_DASHBOARD for tenant: &TENANT_ID due to '||SQLERRM);   
  RAISE;
END;
/

DECLARE
  V_COUNT number;
BEGIN

SELECT COUNT(1) INTO V_COUNT from EMS_DASHBOARD_TILE where TENANT_ID='&TENANT_ID' AND PROVIDER_VERSION='0.1';
IF (V_COUNT>0) THEN
  UPDATE EMS_DASHBOARD_TILE SET PROVIDER_VERSION='1.0' WHERE TENANT_ID='&TENANT_ID' AND PROVIDER_VERSION='0.1';
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('Provider version has been upgrade from 0.1 to 1.0 for tenant: &TENANT_ID successfully! Upgraded records: '||V_COUNT);
ELSE
  DBMS_OUTPUT.PUT_LINE('Provider version has been upgrade from 0.1 to 1.0 for tenant: &TENANT_ID before, no need to upgrade again');
END IF;

EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('Failed to upgrade version from 0.1 to 1.0 for tenant: &TENANT_ID due to '||SQLERRM);
    RAISE;
END;
/

