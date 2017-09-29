package oracle.sysman.emaas.platform.dashboards.ws.rest.preferences;

import oracle.sysman.emaas.platform.dashboards.core.PreferenceManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.PreferenceNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.model.Preference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by guochen on 9/29/17.
 */
public class FeatureShowPreferences {
    private static final Logger LOGGER = LogManager.getLogger(FeatureShowPreferences.class);

    public static List<Preference> getFeatureShowPreferences(Long internalTenantId) throws PreferenceNotFoundException {
        List<Preference> prefs = PreferenceManager.getInstance().getPreferenceByMultipleKeys(Preference.FEATURE_SHOW_PREF_SUPPORTED_KEYS, internalTenantId);
        // if any key isn't found from database, we use default values then
        Set<String> keySet = new HashSet<String>(Preference.FEATURE_SHOW_PREF_DEFAULT_VALUES.keySet());
        if (prefs != null) {
            for (Preference p : prefs) {
                String key = p.getKey();
                keySet.remove(key);
                LOGGER.info("Found pref key {} from database, put to returned value", key);
            }
        }
        if (!keySet.isEmpty()) {
            if (prefs == null) {
                prefs = new ArrayList<Preference>();
            }
            // some preference keys are not found in database, use default values instead
            for (String notFoundKey : keySet) {
                Preference p = new Preference();
                p.setKey(notFoundKey);
                String value = Preference.FEATURE_SHOW_PREF_DEFAULT_VALUES.get(notFoundKey);
                p.setValue(value);
                prefs.add(p);
                LOGGER.info("Didn't found pref value for key {} from database, use default value {} for it", notFoundKey, value);
            }
        }
        return prefs;
    }
}
