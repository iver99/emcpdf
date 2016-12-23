Rem
Rem
Rem emaas_dashboards_same_desc_update.sql
Rem
Rem
Rem    NAME
Rem      emaas_dashboards_same_desc_update
Rem
Rem    DESCRIPTION
Rem      Update the dashboard desc with the same upper(name)
Rem		 For JIRA : EMCPDF-2009
Rem
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    xiadai   10/27/2016  Created

SET FEEDBACK ON
SET SERVEROUTPUT ON

DECLARE
    V_TENANT_ID NUMBER(38,0);
    V_DESCRIPTION VARCHAR2(1280 BYTE);
    V_NAME VARCHAR2(320 BYTE);
    V_COUNT     INTEGER;

    TYPE dashboard_record IS RECORD(
        DNAME EMS_DASHBOARD.NAME%TYPE,
        DOWNER EMS_DASHBOARD.OWNER%TYPE,
        DDESC EMS_DASHBOARD.DESCRIPTION%TYPE
    );

    TYPE dashboard_cursor_type IS REF CURSOR RETURN dashboard_record;
    DASHBOARD_CURSOR dashboard_cursor_type;
    C_DB dashboard_record;

    CURSOR DASHBOARD_ID_CURSOR(INAME EMS_DASHBOARD.NAME%TYPE, IOWNER EMS_DASHBOARD.OWNER%TYPE,
            IDESC EMS_DASHBOARD.DESCRIPTION%TYPE, ITENANT_ID EMS_DASHBOARD.TENANT_ID%TYPE)
    IS SELECT EMS_DASHBOARD.DASHBOARD_ID FROM EMS_DASHBOARD
    WHERE UPPER(EMS_DASHBOARD.NAME) = UPPER(INAME) AND EMS_DASHBOARD.OWNER = IOWNER AND EMS_DASHBOARD.DESCRIPTION = IDESC AND TENANT_ID = ITENANT_ID;
    C_DB_ID EMS_DASHBOARD.DASHBOARD_ID%TYPE;

    CURSOR DASHBOARD_ID_CURSOR_NULL (INAME EMS_DASHBOARD.NAME%TYPE, IOWNER EMS_DASHBOARD.OWNER%TYPE,
            IDESC EMS_DASHBOARD.DESCRIPTION%TYPE, ITENANT_ID EMS_DASHBOARD.TENANT_ID%TYPE)
    IS SELECT EMS_DASHBOARD.DASHBOARD_ID FROM EMS_DASHBOARD
    WHERE UPPER(EMS_DASHBOARD.NAME) = UPPER(INAME) AND EMS_DASHBOARD.OWNER = IOWNER AND EMS_DASHBOARD.DESCRIPTION IS NULL AND TENANT_ID = ITENANT_ID;
    C_DB_ID_2 EMS_DASHBOARD.DASHBOARD_ID%TYPE;

    CURSOR TENANT_CURSOR IS
        SELECT DISTINCT TENANT_ID FROM EMS_DASHBOARD ORDER BY TENANT_ID;

BEGIN
--------------------DROP THE UNIQUE CONSTRAINT EMS_DASHBOARD_U1-------------------------
  SELECT count(1) into V_COUNT FROM user_constraints WHERE constraint_name = 'EMS_DASHBOARD_U1' AND constraint_type = 'U' AND table_name = 'EMS_DASHBOARD';
  IF V_COUNT=0 THEN
    DBMS_OUTPUT.PUT_LINE('CONSTRAINT EMS_DASHBOARD_U1 does not exist');
  ELSE
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD DROP CONSTRAINT EMS_DASHBOARD_U1';
    DBMS_OUTPUT.PUT_LINE('CONSTRAINT EMS_DASHBOARD_U1 has been modified to EMS_DASHBOARD');
  END IF;
-------------------END OF DROPPING-----------------------------------------------------

  SELECT count(1) into V_COUNT FROM USER_INDEXES WHERE index_name = 'EMS_DASHBOARD_U2' AND UNIQUENESS = 'UNIQUE' AND table_name = 'EMS_DASHBOARD';
   IF V_COUNT=0 THEN
--------------------INDEX EMS_DASHBOARD_U2 DOES NOT EXISTS ADD NOW--------------------
    OPEN TENANT_CURSOR;
    LOOP
      FETCH TENANT_CURSOR INTO V_TENANT_ID;
      EXIT WHEN TENANT_CURSOR%NOTFOUND;
--------------------Get the dashboard group with the same name(case insensitive), desc and owner------
        OPEN DASHBOARD_CURSOR FOR
        SELECT UPPER(NAME), OWNER, case when DESCRIPTION is null then ' ' else DESCRIPTION end FROM(
                SELECT count(1) as c, UPPER(NAME) as NAME, OWNER as OWNER, DESCRIPTION FROM EMS_DASHBOARD WHERE TENANT_ID = V_TENANT_ID GROUP BY UPPER(NAME), OWNER, DESCRIPTION
                ) WHERE c > 1;
        FETCH DASHBOARD_CURSOR INTO C_DB;
        WHILE DASHBOARD_CURSOR%FOUND LOOP
          IF  C_DB.DDESC = ' ' THEN
                OPEN DASHBOARD_ID_CURSOR_NULL(INAME => C_DB.DNAME, IOWNER => C_DB.DOWNER, IDESC => C_DB.DDESC, ITENANT_ID => V_TENANT_ID);
                V_DESCRIPTION := ' ';
                FETCH DASHBOARD_ID_CURSOR_NULL INTO C_DB_ID_2;
                WHILE DASHBOARD_ID_CURSOR_NULL%FOUND LOOP
                    UPDATE EMS_DASHBOARD SET EMS_DASHBOARD.DESCRIPTION = V_DESCRIPTION WHERE EMS_DASHBOARD.DASHBOARD_ID = C_DB_ID_2 AND TENANT_ID = V_TENANT_ID;
                    V_DESCRIPTION := V_DESCRIPTION || ' ';
                    DBMS_OUTPUT.PUT_LINE(C_DB_ID_2||' with null desc');
                FETCH DASHBOARD_ID_CURSOR_NULL INTO C_DB_ID_2;
                END LOOP;
                CLOSE DASHBOARD_ID_CURSOR_NULL;
          ELSE
--------------------Update their descs to make them different-----------------------------------------
            OPEN DASHBOARD_ID_CURSOR(INAME => C_DB.DNAME, IOWNER => C_DB.DOWNER, IDESC => C_DB.DDESC, ITENANT_ID => V_TENANT_ID);
            V_DESCRIPTION := C_DB.DDESC;
            FETCH DASHBOARD_ID_CURSOR INTO C_DB_ID;
            WHILE DASHBOARD_ID_CURSOR%FOUND LOOP
              IF length(C_DB.DDESC) > 1279 THEN
                SELECT NAME INTO V_NAME FROM EMS_DASHBOARD WHERE EMS_DASHBOARD.DASHBOARD_ID = C_DB_ID;
                V_DESCRIPTION := V_NAME || ':' || substr(C_DB.DDESC, 1, length(C_DB.DDESC)-length(V_NAME)-1);
                UPDATE EMS_DASHBOARD SET EMS_DASHBOARD.DESCRIPTION = V_DESCRIPTION WHERE EMS_DASHBOARD.DASHBOARD_ID = C_DB_ID AND TENANT_ID = V_TENANT_ID;
              ELSE
                UPDATE EMS_DASHBOARD SET EMS_DASHBOARD.DESCRIPTION = V_DESCRIPTION WHERE EMS_DASHBOARD.DASHBOARD_ID = C_DB_ID AND TENANT_ID = V_TENANT_ID;
                IF length(V_DESCRIPTION) < 1279 THEN
                  V_DESCRIPTION := V_DESCRIPTION || ' ';
                ELSE
                  C_DB.DDESC := C_DB.DDESC || '.';
                  V_DESCRIPTION := C_DB.DDESC;
                END IF;
              END IF;
            FETCH DASHBOARD_ID_CURSOR INTO C_DB_ID ;
            END LOOP;
            CLOSE DASHBOARD_ID_CURSOR;
        END IF;

        FETCH DASHBOARD_CURSOR INTO C_DB;
        END LOOP;
        CLOSE DASHBOARD_CURSOR;
    END LOOP;
    CLOSE TENANT_CURSOR;
--------------------ADDING--------------------
    EXECUTE IMMEDIATE 'CREATE UNIQUE INDEX EMS_DASHBOARD_U2 ON EMS_DASHBOARD(UPPER(NAME), DESCRIPTION, OWNER, TENANT_ID, DELETED)';
    DBMS_OUTPUT.PUT_LINE('INDEX EMS_DASHBOARD_U2 does not exist and has been added');
--------------------END OF ADDING--------------------
   ELSE
     DBMS_OUTPUT.PUT_LINE('INDEX EMS_DASHBOARD_U2 already exists');
   END IF;
  EXCEPTION
	WHEN OTHERS THEN
	ROLLBACK;
	DBMS_OUTPUT.PUT_LINE('Fail to upadte dashboards due to '||SQLERRM);
 RAISE;
END;
/