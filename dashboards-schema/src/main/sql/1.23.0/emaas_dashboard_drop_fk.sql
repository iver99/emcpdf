Rem ----------------------------------------------------------------
Rem 08/29/2017	REX	Created file
Rem
Rem ----------------------------------------------------------------

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_FAVORITE DROP CONSTRAINT EMS_DASHBOARD_FAVORITE_FK1';
  DBMS_OUTPUT.PUT_LINE('EMS_DASHBOARD_FAVORITE_FK1 has been dropped');
  EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -02443 THEN
         DBMS_OUTPUT.PUT_LINE('>>>DF DDL ERROR<<<');
         RAISE;
      END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE DROP CONSTRAINT EMS_DASHBOARD_TILE_FK1';
  DBMS_OUTPUT.PUT_LINE('EMS_DASHBOARD_TILE_FK1 has been dropped');
  EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -02443 THEN
         DBMS_OUTPUT.PUT_LINE('>>>DF DDL ERROR<<<');
         RAISE;
      END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE_PARAMS DROP CONSTRAINT EMS_DASHBOARD_TILE_PARAMS_FK1';
  DBMS_OUTPUT.PUT_LINE('EMS_DASHBOARD_TILE_PARAMS_FK1 has been dropped');
  EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -02443 THEN
         DBMS_OUTPUT.PUT_LINE('>>>DF DDL ERROR<<<');
         RAISE;
      END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_SET DROP CONSTRAINT EMS_DASHBOARD_SET_FK1';
  DBMS_OUTPUT.PUT_LINE('EMS_DASHBOARD_SET_FK1 has been dropped');
  EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -02443 THEN
         DBMS_OUTPUT.PUT_LINE('>>>DF DDL ERROR<<<');
         RAISE;
      END IF;
END;
/

