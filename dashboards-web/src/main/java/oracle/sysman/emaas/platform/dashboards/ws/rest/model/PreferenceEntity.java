package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import oracle.sysman.emaas.platform.dashboards.core.model.Preference;

/**
 * Created by guochen on 9/28/17.
 */
public class PreferenceEntity {
    private String key;
    private String value;

    public PreferenceEntity(Preference pref) {
        this.key = pref.getKey();
        this.value = pref.getValue();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
