Rem ----------------------------------------------------------------
Rem 03/27/2017	MIAYU	Created file
Rem
Rem ----------------------------------------------------------------

@&EMSAAS_SQL_ROOT/1.21.0/cleanup_oob.sql
@&EMSAAS_SQL_ROOT/1.21.0/oob_tenant_onboarding.sql -11
@&EMSAAS_SQL_ROOT/1.21.0/update_oob_time.sql
COMMIT;


