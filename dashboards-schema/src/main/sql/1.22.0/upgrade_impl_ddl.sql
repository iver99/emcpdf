Rem --DDL change during upgrade
Rem
Rem upgrade_impl_ddl.sql
Rem
Rem Copyright (c) 2013, 2014, 2015, 2016 Oracle and/or its affiliates.
Rem All rights reserved.
Rem
Rem    NAME
Rem      upgrade_impl_ddl.sql
Rem
Rem    DESCRIPTION
Rem      DDL change during upgrade
Rem
Rem    NOTES
Rem      None
Rem

SET FEEDBACK ON
SET SERVEROUTPUT ON

@&EMSAAS_SQL_ROOT/1.22.0/emaas_dashboard_create_zdt_compare_table.sql
@&EMSAAS_SQL_ROOT/1.22.0/emaas_dashboard_create_zdt_sync_table.sql


