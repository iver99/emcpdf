Rem
Rem emaas_exadata_dashboard_application_type.sql
Rem
Rem Copyright (c) 2013, 2014, 2015, 2016, 2017 Oracle and/or its affiliates.
Rem All rights reserved.
Rem
Rem    NAME
Rem      emaas_exadata_dashboard_application_type.sql
Rem
Rem    DESCRIPTION
Rem      update OOB UDE dashboard Exadata Health dashboard and its sub dashboards application_type to '2'
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    Chehao      06/28/17 
Rem

SET FEEDBACK ON
SET SERVEROUTPUT ON
DEFINE TENANT_ID ='&1'
DECLARE
    V_TENANT_ID         NUMBER(38,0);
    V_TID               NUMBER(38,0)        := '&TENANT_ID';
    CURSOR TENANT_CURSOR IS
        SELECT DISTINCT TENANT_ID FROM EMS_DASHBOARD ORDER BY TENANT_ID;

BEGIN
  
    LOOP
     IF (V_TID<>-1) THEN
        V_TENANT_ID:=V_TID;
     ELSE
       IF NOT TENANT_CURSOR%ISOPEN THEN
        OPEN TENANT_CURSOR;
       END IF;
       FETCH TENANT_CURSOR INTO V_TENANT_ID;
       EXIT WHEN TENANT_CURSOR%NOTFOUND;
     END IF;
	 
		--OOB UDE Exadata dashboard and its sub dashboards application_type to 2
		UPDATE EMS_DASHBOARD T SET T.APPLICATION_TYPE = 2 WHERE T.TENANT_ID = V_TENANT_ID AND T.DASHBOARD_ID = 28;
		UPDATE EMS_DASHBOARD T SET T.APPLICATION_TYPE = 2 WHERE T.TENANT_ID = V_TENANT_ID AND T.DASHBOARD_ID = 29;
		UPDATE EMS_DASHBOARD T SET T.APPLICATION_TYPE = 2 WHERE T.TENANT_ID = V_TENANT_ID AND T.DASHBOARD_ID = 30;

        DBMS_OUTPUT.PUT_LINE('UPDATE OOB EXEDATA HEALTH DASHBOARD APPLICATION_TYPE TO 2 SUCCESSFULLY FOR TENANT: ' ||V_TENANT_ID);

     IF (V_TID<>-1) THEN
        EXIT;
     END IF;
  END LOOP;
  IF TENANT_CURSOR%ISOPEN THEN
    CLOSE TENANT_CURSOR;
  END IF;

  COMMIT;

EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to UPDATE OOB EXEDATA HEALTH DASHBOARD AND ITS SUB DASHBOARD APPLICATION_TYPE TO 2 due to '||SQLERRM);
  RAISE;  
END;
/