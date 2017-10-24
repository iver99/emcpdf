Rem ----------------------------------------------------------------
Rem 09/21/2017	chendong	Created file
Rem
Rem    DESCRIPTION
Rem     This script will delete all un-synced data in comparator table, in case there is a schema change in upgrade and leading sync failure.
Rem ----------------------------------------------------------------
SET FEEDBACK ON
SET SERVEROUTPUT ON

DECLARE
  v_count       NUMBER;
  v_date 		VARCHAR2(60);
BEGIN
   
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_ZDT_COMPARATOR';
  IF v_count > 0 THEN
	SELECT LAST_COMPARISON_DATE into v_date FROM (SELECT to_char(LAST_COMPARISON_DATE,'yyyy-mm-dd hh24:mi:ss.ff3') as LAST_COMPARISON_DATE FROM EMS_ZDT_SYNC WHERE SYNC_RESULT = 'SUCCESSFUL' ORDER BY SYNC_DATE DESC) WHERE ROWNUM = 1;
	DBMS_OUTPUT.PUT_LINE('Last compare date is ' || v_date);
  
	delete from ems_zdt_comparator where comparison_date > to_timestamp(v_date,'yyyy-mm-dd hh24:mi:ss.ff');
	DBMS_OUTPUT.PUT_LINE('Delete un-synced data in comparator table successfully');   
  ELSE
    DBMS_OUTPUT.PUT_LINE('EMS_ZDT_COMPARATOR table is not existed, no need to delete any data');      
  END IF;
  
  COMMIT;
  EXCEPTION
  WHEN NO_DATA_FOUND THEN DBMS_OUTPUT.PUT_LINE('Did not find un-synced data, so nothing is needed to do...');
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('Failed to delete un-synced data before upgrade due to '||SQLERRM);
    RAISE;
END;
/