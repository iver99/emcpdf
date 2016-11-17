  SET FEEDBACK ON
  SET SERVEROUTPUT ON
  DEFINE TENANT_ID ='&1'
DECLARE
v_count     INTEGER;
V_TENANT_ID NUMBER(38,0);
V_TID 		NUMBER(38,0) := '&TENANT_ID';
V_DASHBOARD_ID NUMBER(38,0);

CURSOR TENANT_CURSOR IS
    SELECT DISTINCT TENANT_ID FROM EMS_DASHBOARD ORDER BY TENANT_ID;
	
BEGIN
	V_DASHBOARD_ID:=37;
	OPEN TENANT_CURSOR;
	LOOP
     IF (V_TID<>-1) THEN
        V_TENANT_ID:=V_TID;
     ELSE
       FETCH TENANT_CURSOR INTO V_TENANT_ID;
       EXIT WHEN TENANT_CURSOR%NOTFOUND;     
     END IF;
	 SELECT COUNT(*) INTO v_count FROM EMS_DASHBOARD where DASHBOARD_ID=V_DASHBOARD_ID and TENANT_ID = V_TENANT_ID;
	 IF v_count > 0 THEN
       UPDATE EMS_DASHBOARD SET EXTENDED_OPTIONS = '{"tsel":{}, "timeSel":{"defaultValue":"last1day","start":1479272760000,"end":1479359160000}}' 
       WHERE DASHBOARD_ID = V_DASHBOARD_ID AND TENANT_ID = V_TENANT_ID;

       DBMS_OUTPUT.PUT_LINE('OOB DASHBOARD Orchestration Workflows default time is updated to last1day succssfully for tenant:'||V_TENANT_ID);
     ELSE
       DBMS_OUTPUT.PUT_LINE('OOB DASHBOARD Orchestration Workflows is not exited for tenant:'||V_TENANT_ID);
     END IF;
	 IF (V_TID<>-1) THEN
        EXIT;
     END IF;
    END LOOP;	
	CLOSE TENANT_CURSOR;
	DBMS_OUTPUT.PUT_LINE('Upgrade is done');    
  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      DBMS_OUTPUT.PUT_LINE('Failed to update the sql due to '||SQLERRM);
      RAISE;
  END;
  /	
  /	