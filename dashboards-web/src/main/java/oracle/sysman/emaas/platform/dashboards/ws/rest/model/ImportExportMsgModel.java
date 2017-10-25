package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by chehao on 10/25/2017.
 */
public class ImportExportMsgModel {

    @JsonProperty("true")
    private boolean success;
    private String msg;


    public ImportExportMsgModel() {
    }

    public ImportExportMsgModel(String msg) {

        this.msg = msg;
    }

    public ImportExportMsgModel(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
