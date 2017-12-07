package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import javax.ws.rs.DefaultValue;

public class DashboardDependencyStatus {

    public static final String UP_STATUS = "UP";

    public static final String DOWN_STATUS = "DOWN";

    private String db_status;
    private String entity_naming_status;

    public DashboardDependencyStatus() {
        //default is UP status
        this.db_status = "UP";
        this.entity_naming_status = "UP";
    }

    public String getDb_status() {
        return db_status;
    }

    public void setDb_status(String db_status) {
        this.db_status = db_status;
    }

    public String getEntity_naming_status() {
        return entity_naming_status;
    }

    public void setEntity_naming_status(String entity_naming_status) {
        this.entity_naming_status = entity_naming_status;
    }
}
