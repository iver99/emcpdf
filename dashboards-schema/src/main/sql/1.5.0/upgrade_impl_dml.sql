DEFINE TENANT_ID = '&1'

REM --update OOB data

@&EMSAAS_SQL_ROOT/1.5.0/emaas_dashboards_seed_data.sql '&TENANT_ID'
@&EMSAAS_SQL_ROOT/1.5.0/emaas_dashboards_seed_data_ita.sql '&TENANT_ID'
@&EMSAAS_SQL_ROOT/1.5.0/emaas_dashboards_seed_data_ta.sql '&TENANT_ID'

COMMIT;

