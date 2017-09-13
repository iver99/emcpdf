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

@&EMSAAS_SQL_ROOT/1.23.0/emaas_dashboard_drop_fk.sql
@&EMSAAS_SQL_ROOT/1.23.0/emaas_dashboard_compress_index.sql


