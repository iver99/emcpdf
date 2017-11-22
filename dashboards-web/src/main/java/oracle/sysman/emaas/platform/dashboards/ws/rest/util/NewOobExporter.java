/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.ws.rest.util;

import java.util.Arrays;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;

import oracle.sysman.emaas.platform.emcpdf.util.JsonUtil;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * @author reliang
 *
 */
public class NewOobExporter {
    private static JsonUtil jsonUtil;
    private static String TILES = "tiles";
    private static String SUB_DASHBOARDS = "subDashboards";
    private static String SCREEN_SHOT = "screenShot";
    private static List<String> HIDDEN_DASHBOARD_FIELD = Arrays.asList("sharePublic", "lastModifiedBy", "owner",
            "applicationType", "systemDashboard");
    private static List<String> HIDDEN_TILES_FIELD = Arrays.asList("widgetDeleted", "WIDGET_GROUP_NAME", "WIDGET_OWNER",
            "WIDGET_SOURCE", "WIDGET_SUPPORT_TIME_CONTROL", "WIDGET_LINKED_DASHBOARD");
    private static List<String> HIDDEN_SUB_DASHBOARD_FIELD = Arrays.asList("name", "enableDescription", "enableEntityFilter",
            "showInHome", "sharePublic", "owner");
    
    static {
        jsonUtil = JsonUtil.buildNonNullMapper();
        jsonUtil.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }
    
    static public String exportDashboard(Dashboard db) throws JSONException {
        JSONObject dbJsonObj = new JSONObject(jsonUtil.toJson(db));
        dbJsonObj.put(SCREEN_SHOT, db.getScreenShot());
        for(String hiddenField : HIDDEN_DASHBOARD_FIELD) {
            dbJsonObj.remove(hiddenField);
        }
        if(dbJsonObj.has(TILES)) {
            JSONArray tilesJsonObj = dbJsonObj.getJSONArray(TILES);
            for(int i  = 0; i < tilesJsonObj.length(); i++) {
                JSONObject tileJsonObj = tilesJsonObj.getJSONObject(i);
                for(String hiddenField : HIDDEN_TILES_FIELD) {
                    tileJsonObj.remove(hiddenField);
                }
            }
        }
        if(dbJsonObj.has(SUB_DASHBOARDS)) {
            JSONArray subsJsonObj = dbJsonObj.getJSONArray(SUB_DASHBOARDS);
            for(int i = 0; i < subsJsonObj.length(); i++) {
                JSONObject subJsonObj = subsJsonObj.getJSONObject(i);
                for(String hiddenField : HIDDEN_SUB_DASHBOARD_FIELD) {
                    subJsonObj.remove(hiddenField);
                }
            }
        }
        
        return dbJsonObj.toString();
    }
}
