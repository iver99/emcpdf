/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.core.nls;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import oracle.sysman.emaas.platform.dashboards.core.DashboardsFilter;
import oracle.sysman.emaas.platform.dashboards.core.model.DashboardApplicationType;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author reliang
 *
 */
public class DatabaseResourceBundleUtil {
    //private static final String[] supportedLanguages = new String[]{"en", "fr", "ko", "zh-Hans", "zh-Hant", "zh"};
    private static final Logger LOGGER = LogManager.getLogger(DatabaseResourceBundleUtil.class);
    
    public static String getTranslatedString(String widgetProvider, String key) {
        DashboardApplicationType appType = null;
        switch(widgetProvider) {
            case DashboardsFilter.APM_PROVIDER_APMUI:
                appType = DashboardApplicationType.APM;
                break;
            case DashboardsFilter.LA_PROVIDER_LS:
                appType = DashboardApplicationType.LogAnalytics;
                break;
            case DashboardsFilter.OCS_PROVIDER_OCS:
                appType = DashboardApplicationType.Orchestration;
                break;
            case DashboardsFilter.ITA_PROVIDER_EMCI:
                appType = DashboardApplicationType.ITAnalytics;
                break;
            case DashboardsFilter.ITA_PROVIDER_TA:
                appType = DashboardApplicationType.UDE;
                break;
            default:
                break;
        }
        if(appType == null) {
            LOGGER.warn("Widget provider called {} can not be matched to any of existing application type.", widgetProvider);
            return key;
        }
            
        return getTranslatedString(appType, key);
    }
    
    public static String getTranslatedString(DashboardApplicationType appType, String key) {
        DatabaseResourceBundle rb = null;
        try {
            rb = (DatabaseResourceBundle) ResourceBundle.getBundle(appType.getJsonValue(), UserContext.getLocale(),
                    DatabaseResourceBundleUtil.class.getClassLoader(), new DatabaseResourceBundleControl());
        } catch (Exception ex) {
            LOGGER.warn("Fail to translating '{}' for service {} because: {}", key, appType.getJsonValue(),
                    ex.getLocalizedMessage());
        }
        if (rb != null) {
            try {
                return rb.getString(key);
            }
            catch (MissingResourceException ex) {
                LOGGER.warn("No translation for '{}' for service {} because: {}", key, appType.getJsonValue(),
                        ex.getLocalizedMessage());
            }
        }
        return key;
    }
    
    public static Locale generateLocale(String languageCode) {
        Locale locale = Locale.US;
        String[] array = languageCode.split("-");
        if(array.length > 1) {
            if("zh".equals(array[0]) && "CN".equals(array[1])) {
                locale = new Locale("zh", "Hans");
            } else {
                locale = new Locale(array[0], array[1]);
            }
        } else {
            switch(languageCode) {
                case "en":
                    locale = Locale.US;
                    break;
                case "fr":
                    locale = Locale.FRANCE;
                    break;
                case "ko":
                    locale = Locale.KOREA;
                    break;
                case "zh":
                    locale = new Locale("zh", "Hans");
                    break;
                default:
                    break;
            }
        }
        return locale;
    }
}
