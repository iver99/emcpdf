Rem
Rem emaas_dashboards_seed_data_cos.sql
Rem
Rem Copyright (c) 2013, 2014, 2015, 2016, 2017 Oracle and/or its affiliates.
Rem All rights reserved.
Rem
Rem    NAME
Rem      emaas_dashboards_seed_data_cos.sql
Rem
Rem    DESCRIPTION
Rem      Seed data for COSUPDATE
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    xiadai      03/27/17 - update data for Orchestration
Rem

SET FEEDBACK ON
SET SERVEROUTPUT ON
DEFINE TENANT_ID ='&1'
DECLARE
    V_TENANT_ID                 NUMBER(38,0);
    V_COUNT                     NUMBER;
    V_WIDGET_OLD_NAME           VARCHAR2(64 BYTE)   := 'Summary';
    V_WIDGET_NEW_NAME           VARCHAR2(64 BYTE)   := 'Overview';
    V_WIDGET_NAME_HISTOGRAM     VARCHAR2(64 BYTE) := 'Histogram of Submissions by Average Failed Steps';
    V_WIDGET_NAME_TYPE          VARCHAR2(64 BYTE) := 'Submissions by Type';
    V_WIDGET_NAME_USER          VARCHAR2(64 BYTE) := 'Submissions by User';
    V_WIDGET_NAME_ALERTS        VARCHAR2(64 BYTE) := 'Workflow Submission Alerts';
    V_TID                       NUMBER(38,0)        := '&TENANT_ID';

    V_TILE_ID                     NUMBER(38,0);
    V_PARAM_NAME                  EMS_DASHBOARD_TILE_PARAMS.PARAM_NAME%TYPE;
    V_PARAM_TYPE                  EMS_DASHBOARD_TILE_PARAMS.PARAM_TYPE%TYPE;
    V_PARAM_VALUE_STR             EMS_DASHBOARD_TILE_PARAMS.PARAM_VALUE_STR%TYPE;
    V_PARAM_VALUE_NUM             EMS_DASHBOARD_TILE_PARAMS.PARAM_VALUE_NUM%TYPE;
    V_PARAM_VALUE_TIMESTAMP       EMS_DASHBOARD_TILE_PARAMS.PARAM_VALUE_TIMESTAMP%TYPE;
    CONST_IS_SYSTEM                  CONSTANT    NUMBER:=1;
    CURSOR TENANT_CURSOR IS
        SELECT DISTINCT TENANT_ID FROM EMS_DASHBOARD ORDER BY TENANT_ID;

BEGIN
    OPEN TENANT_CURSOR;
    LOOP
     IF (V_TID<>-1) THEN
        V_TENANT_ID:=V_TID;
     ELSE
       FETCH TENANT_CURSOR INTO V_TENANT_ID;
       EXIT WHEN TENANT_CURSOR%NOTFOUND;
     END IF;
        ----EMCPDF-3442
        SELECT COUNT(1) INTO V_COUNT FROM EMS_DASHBOARD_TILE
            WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_NAME_HISTOGRAM AND WIDGET_NAME = V_WIDGET_NAME_HISTOGRAM;
        IF V_COUNT < 1 THEN
            DBMS_OUTPUT.PUT_LINE('TILE Histogram of Submissions by Average Failed Steps NOT USED NO NEED TO DELETE FOR TENANT: ' ||V_TENANT_ID);
        ELSE
            DELETE FROM EMS_DASHBOARD_TILE_PARAMS
                WHERE TENANT_ID = V_TENANT_ID AND TILE_ID IN
                    (
                        SELECT TILE_ID FROM EMS_DASHBOARD_TILE
                           WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_NAME_HISTOGRAM AND WIDGET_NAME = V_WIDGET_NAME_HISTOGRAM
                     );
            DELETE FROM EMS_DASHBOARD_TILE WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_NAME_HISTOGRAM AND WIDGET_NAME = V_WIDGET_NAME_HISTOGRAM;
            DBMS_OUTPUT.PUT_LINE('TILE Histogram of Submissions by Average Failed Steps DELETED SUCCESSFULLY FOR TENANT: ' ||V_TENANT_ID);
        END IF;

        ----EMCPDF-3397
        SELECT COUNT(1) INTO V_COUNT FROM EMS_DASHBOARD_TILE
            WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_OLD_NAME AND WIDGET_NAME = V_WIDGET_OLD_NAME;
        IF V_COUNT <1 THEN
            DBMS_OUTPUT.PUT_LINE('TILE Summary NOT USED NO NEED TO UPDATE FOR TENANT: ' ||V_TENANT_ID);
        ELSE
            UPDATE EMS_DASHBOARD_TILE SET TITLE = V_WIDGET_NEW_NAME, WIDGET_NAME = V_WIDGET_NEW_NAME
                WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_OLD_NAME AND WIDGET_NAME = V_WIDGET_OLD_NAME;
            DBMS_OUTPUT.PUT_LINE('TILE Summary UPDATED SUCCESSFULLY FOR TENANT: ' ||V_TENANT_ID);
        END IF;

        V_PARAM_NAME                  := 'DF_HIDE_TITLE';
        V_PARAM_TYPE                  := 1;
        V_PARAM_VALUE_STR             := 'FALSE';
        V_PARAM_VALUE_NUM             := null;
        V_PARAM_VALUE_TIMESTAMP       := null;
        V_TILE_ID                     :=127;
        SELECT COUNT(*) INTO V_COUNT FROM EMS_DASHBOARD_TILE_PARAMS
        WHERE TILE_ID = V_TILE_ID AND PARAM_NAME = V_PARAM_NAME AND UPPER(PARAM_VALUE_STR) = V_PARAM_VALUE_STR AND TENANT_ID = V_TENANT_ID;
        IF v_count > 0 THEN
           V_PARAM_VALUE_STR :='true';
           UPDATE EMS_DASHBOARD_TILE_PARAMS SET PARAM_VALUE_STR = V_PARAM_VALUE_STR
           WHERE TILE_ID = V_TILE_ID AND PARAM_NAME =V_PARAM_NAME  AND TENANT_ID = V_TENANT_ID;
           DBMS_OUTPUT.PUT_LINE('OOB WIDGET WITH TITLE OVREVIEW HAS BEEN UPDATED SUCCESSFULLY! for tenant:'||V_TENANT_ID);
        ELSE
           SELECT COUNT(*) INTO V_COUNT FROM EMS_DASHBOARD_TILE_PARAMS
           WHERE TILE_ID = V_TILE_ID AND PARAM_NAME = V_PARAM_NAME AND TENANT_ID = V_TENANT_ID;
           IF V_COUNT < 1 THEN
                V_PARAM_VALUE_STR := 'true';
                INSERT INTO EMS_DASHBOARD_TILE_PARAMS (TILE_ID,PARAM_NAME,TENANT_ID,IS_SYSTEM,PARAM_TYPE,PARAM_VALUE_STR,PARAM_VALUE_NUM,PARAM_VALUE_TIMESTAMP)
                values (V_TILE_ID,V_PARAM_NAME,V_TENANT_ID,CONST_IS_SYSTEM,V_PARAM_TYPE,V_PARAM_VALUE_STR,V_PARAM_VALUE_NUM,V_PARAM_VALUE_TIMESTAMP);
                DBMS_OUTPUT.PUT_LINE('TILE WITH TITLE OVREVIEW HAS BEEN UPDATED SUCCESSFULLY! for tenant:'||V_TENANT_ID);
           ELSE
              DBMS_OUTPUT.PUT_LINE('TILE WITH TITLE OVREVIEW NEED NOT TO BE UPDATED for tenant:'||V_TENANT_ID);
           END IF;
        END IF;

        ----EMCPDF-3693
        SELECT COUNT(1) INTO V_COUNT FROM EMS_DASHBOARD_TILE
            WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_NAME_TYPE AND WIDGET_NAME = V_WIDGET_NAME_TYPE;
        IF V_COUNT < 1 THEN
            DBMS_OUTPUT.PUT_LINE('TILE Submissions by Type NOT USED NO NEED TO DELETE FOR TENANT: ' ||V_TENANT_ID);
        ELSE
            DELETE FROM EMS_DASHBOARD_TILE_PARAMS
                WHERE TENANT_ID = V_TENANT_ID AND TILE_ID IN
                    (
                        SELECT TILE_ID FROM EMS_DASHBOARD_TILE
                           WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_NAME_TYPE AND WIDGET_NAME = V_WIDGET_NAME_TYPE
                     );
            DELETE FROM EMS_DASHBOARD_TILE WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_NAME_TYPE AND WIDGET_NAME = V_WIDGET_NAME_TYPE;
            DBMS_OUTPUT.PUT_LINE('TILE Submissions by Type DELETED SUCCESSFULLY FOR TENANT: ' ||V_TENANT_ID);
        END IF;

        SELECT COUNT(1) INTO V_COUNT FROM EMS_DASHBOARD_TILE
            WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_NAME_USER AND WIDGET_NAME = V_WIDGET_NAME_USER;
        IF V_COUNT < 1 THEN
            DBMS_OUTPUT.PUT_LINE('TILE Submissions by User NOT USED NO NEED TO DELETE FOR TENANT: ' ||V_TENANT_ID);
        ELSE
            DELETE FROM EMS_DASHBOARD_TILE_PARAMS
                WHERE TENANT_ID = V_TENANT_ID AND TILE_ID IN
                    (
                        SELECT TILE_ID FROM EMS_DASHBOARD_TILE
                           WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_NAME_USER AND WIDGET_NAME = V_WIDGET_NAME_USER
                     );
            DELETE FROM EMS_DASHBOARD_TILE WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_NAME_USER AND WIDGET_NAME = V_WIDGET_NAME_USER;
            DBMS_OUTPUT.PUT_LINE('TILE Submissions by User DELETED SUCCESSFULLY FOR TENANT: ' ||V_TENANT_ID);
        END IF;

        SELECT COUNT(1) INTO V_COUNT FROM EMS_DASHBOARD_TILE
            WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_NAME_ALERTS AND WIDGET_NAME = V_WIDGET_NAME_ALERTS;
        IF V_COUNT < 1 THEN
            DBMS_OUTPUT.PUT_LINE('TILE Workflow Submission Alerts NOT USED NO NEED TO DELETE FOR TENANT: ' ||V_TENANT_ID);
        ELSE
            DELETE FROM EMS_DASHBOARD_TILE_PARAMS
                WHERE TENANT_ID = V_TENANT_ID AND TILE_ID IN
                    (
                        SELECT TILE_ID FROM EMS_DASHBOARD_TILE
                           WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_NAME_ALERTS AND WIDGET_NAME = V_WIDGET_NAME_ALERTS
                     );
            DELETE FROM EMS_DASHBOARD_TILE WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_NAME_ALERTS AND WIDGET_NAME = V_WIDGET_NAME_ALERTS;
            DBMS_OUTPUT.PUT_LINE('TILE Workflow Submission Alerts DELETED SUCCESSFULLY FOR TENANT: ' ||V_TENANT_ID);
        END IF;

        SELECT COUNT(1) INTO V_COUNT FROM EMS_DASHBOARD_TILE
            WHERE TENANT_ID = V_TENANT_ID AND TILE_ID = 127 AND WIDTH <> 12 AND HEIGHT <> 2;
        IF V_COUNT < 1 THEN
            DBMS_OUTPUT.PUT_LINE('TILE Overview NOT USED NO NEED TO DELETE FOR TENANT: ' ||V_TENANT_ID);
        ELSE
            UPDATE EMS_DASHBOARD_TILE SET WIDTH = 12, HEIGHT = 2 WHERE TENANT_ID = V_TENANT_ID AND TILE_ID =127;
            DBMS_OUTPUT.PUT_LINE('TILE Overview UPDATED SUCCESSFULLY FOR TENANT: ' ||V_TENANT_ID);
        END IF;



     IF (V_TID<>-1) THEN
        EXIT;
     END IF;
  END LOOP;
  CLOSE TENANT_CURSOR;
  COMMIT;

EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to UPDATE THE WIDGET FOR COS due to '||SQLERRM);
  RAISE;  
END;
/