package oracle.sysman.emaas.platform.dashboards.ws.rest.preferences;

import mockit.Expectations;
import mockit.Mocked;

import oracle.sysman.emaas.platform.dashboards.core.PreferenceManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.PreferenceNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.model.Preference;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by guochen on 9/29/17.
 */
@Test(groups={"s1"})
public class FeatureShowPreferencesTest {
    @Test
    public void testGetFeatureShowPreferences(@Mocked final PreferenceManager anyPreferenceManager) throws PreferenceNotFoundException {
        Preference p1 = new Preference();
        p1.setKey(Preference.PREF_KEY_HM_DBMGMT_SHOW);
        p1.setValue("true");

        final List<Preference> queried = new ArrayList<Preference>();
        queried.add(p1);
        new Expectations(){
            {
                anyPreferenceManager.getPreferenceByMultipleKeys((List<String>)any, anyLong);
                result = queried;
            }
        };
        List<Preference> result = FeatureShowPreferences.getFeatureShowPreferences(1L);
        Assert.assertEquals(2, result.size());
        // check value is from database
        Assert.assertEquals(result.get(0).getKey(), Preference.PREF_KEY_HM_DBMGMT_SHOW);
        Assert.assertEquals(result.get(0).getValue(), "true");

        // check default value is used as not found from database
        Assert.assertEquals(result.get(1).getKey(), Preference.PREF_KEY_HM_FEDERATION_SHOW);
        Assert.assertEquals(result.get(1).getValue(), Preference.FEATURE_SHOW_PREF_DEFAULT_VALUES.get(Preference.PREF_KEY_HM_FEDERATION_SHOW));
    }

    @Test
    public void testGetFeatureShowPreferences_foundNull(@Mocked final PreferenceManager anyPreferenceManager) throws PreferenceNotFoundException {
        Preference p1 = new Preference();
        p1.setKey(Preference.PREF_KEY_HM_DBMGMT_SHOW);
        p1.setValue(Preference.FEATURE_SHOW_PREF_DEFAULT_VALUES.get(Preference.PREF_KEY_HM_DBMGMT_SHOW));

        final List<Preference> queried = Arrays.asList(p1);
        new Expectations(){
            {
                anyPreferenceManager.getPreferenceByMultipleKeys((List<String>)any, anyLong);
                result = null;
            }
        };
        List<Preference> result = FeatureShowPreferences.getFeatureShowPreferences(1L);
        Assert.assertEquals(2, result.size());

        // check default value is used as not found from database
        Assert.assertEquals(result.get(0).getKey(), Preference.PREF_KEY_HM_FEDERATION_SHOW);
        Assert.assertEquals(result.get(0).getValue(), Preference.FEATURE_SHOW_PREF_DEFAULT_VALUES.get(Preference.PREF_KEY_HM_FEDERATION_SHOW));
        Assert.assertEquals(result.get(1).getKey(), Preference.PREF_KEY_HM_DBMGMT_SHOW);
        Assert.assertEquals(result.get(1).getValue(), Preference.FEATURE_SHOW_PREF_DEFAULT_VALUES.get(Preference.PREF_KEY_HM_DBMGMT_SHOW));
    }
}
