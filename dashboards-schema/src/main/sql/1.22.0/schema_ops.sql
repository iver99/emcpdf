Rem ----------------------------------------------------------------
Rem 08/15/2017	PINGWU	Created file
Rem ----------------------------------------------------------------

@&EMSAAS_SQL_ROOT/1.22.0/upgrade_impl_ddl.sql

SET HEADING OFF
SET FEEDBACK OFF
SET LINESIZE 2000

@&EMSAAS_SQL_ROOT/1.22.0/upgrade_impl_dml.sql

