package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.*;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.tenant.TenantIdProcessor;
import oracle.sysman.emaas.platform.dashboards.core.PreferenceManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Preference;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.DashboardAPIUtil;

import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jishshi
 * @since 1/20/2016.
 */
@Test(groups = {"s2"})
public class PreferenceAPITest {

    PreferenceAPI preferenceAPI;

    @Mocked
    DashboardAPIUtil dashboardAPIUtil;

    @Mocked
    JSONObject jsonObject;

    @BeforeMethod
    public void setUp() {
        preferenceAPI = new PreferenceAPI();

        new NonStrictExpectations() {{
            DashboardAPIUtil.getExternalPreferenceAPIBase(anyString);
            result = anyString;

            //noinspection ResultOfMethodCallIgnored
            jsonObject.toString();
            result = anyString;
        }};
    }

    @Test
    public void testDeleteAllPreferenceByKey() {
        //Test 403, with invalid tenantIdParam DashboardException exception;
        Assert.assertEquals(preferenceAPI.deleteAllPreferenceByKey("", "userTenant", "referer").getStatus(), 403);
        //Test 403, with invalid userTanant BasicServiceMalfunctionException exception;
        Assert.assertEquals(preferenceAPI.deleteAllPreferenceByKey("tenantIdParam", "userTenant", "referer").getStatus(), 403);
    }

    @Test
    public void testDeleteAllPreferenceByKey1(@Mocked final PreferenceManager preferenceManager, @Mocked final TenantIdProcessor tenantIdProcessor) throws BasicServiceMalfunctionException {
        new Expectations() {{
            TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
            result = anyLong;

            preferenceManager.removeAllPreferences(anyLong);
            result = null;
        }};
        //test 204 with valid userTenant
        String validUserTenant = "userTanant.userName";
        Assert.assertEquals(preferenceAPI.deleteAllPreferenceByKey("tenantIdParam", validUserTenant, "referer").getStatus(), 204);
    }

    @Test
    public void testDeletePreferenceByKey() {
        //Test 403, with invalid tenantIdParam DashboardException exception;
        Assert.assertEquals(preferenceAPI.deletePreferenceByKey("", "userTenant", "referer", "key").getStatus(), 403);
        //Test 403, with invalid userTanant BasicServiceMalfunctionException exception;
        Assert.assertEquals(preferenceAPI.deletePreferenceByKey("tenantIdParam", "userTenant", "referer", "key").getStatus(), 403);
    }

    @Test
    public void testDeletePreferenceByKey1(@Mocked final PreferenceManager preferenceManager,@SuppressWarnings("unused") @Mocked final TenantIdProcessor tenantIdProcessor)  {
        try {
			new Expectations() {{
			    preferenceManager.removePreference(anyString, anyLong);
			    result = null;
			}};
		}
		catch (DashboardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //test 204 with valid userTenant
        String validUserTenant = "userTanant.userName";
        Assert.assertEquals(preferenceAPI.deletePreferenceByKey("tenantIdParam", validUserTenant, "referer", "key").getStatus(), 204);
    }

    @Test
    public void testQueryPreferenceByKey() {
        //Test 403, with invalid tenantIdParam DashboardException exception;
        Assert.assertEquals(preferenceAPI.queryPreferenceByKey("", "userTenant", "referer", "key").getStatus(), 403);
        //Test 403, with invalid userTanant BasicServiceMalfunctionException exception;
        Assert.assertEquals(preferenceAPI.queryPreferenceByKey("tenantIdParam", "userTenant", "referer", "key").getStatus(), 403);
    }

    @Test
    public void testQueryPreferenceByKey1(@Mocked final PreferenceManager preferenceManager,@SuppressWarnings("unused") @Mocked final TenantIdProcessor tenantIdProcessor) throws DashboardException {
        new Expectations() {{
            preferenceManager.getPreferenceByKey(anyString, anyLong);
            result = null;
        }};
        //test 204 with valid userTenant
        String validUserTenant = "userTanant.userName";
        Assert.assertEquals(preferenceAPI.queryPreferenceByKey("tenantIdParam", validUserTenant, "referer", "key").getStatus(), 200);
    }

    @Test
    public void testQueryPreferences() {
        //Test 403, with invalid tenantIdParam DashboardException exception;
        Assert.assertEquals(preferenceAPI.queryPreferences("", "userTenant", "referer").getStatus(), 403);
        //Test 403, with invalid userTanant BasicServiceMalfunctionException exception;
        Assert.assertEquals(preferenceAPI.queryPreferences("tenantIdParam", "userTenant", "referer").getStatus(), 403);
    }

    @Test
    public void testQueryPreferences1(@Mocked final PreferenceManager preferenceManager,@SuppressWarnings("unused") @Mocked final TenantIdProcessor tenantIdProcessor) {
        new Expectations() {{
            List<Preference> ps = new ArrayList<>();
            ps.add(new Preference());
            preferenceManager.listPreferences(anyLong);
            result = ps;
        }};
        //test 200 with valid userTenant
        String validUserTenant = "userTanant.userName";
        Assert.assertEquals(preferenceAPI.queryPreferences("tenantIdParam", validUserTenant, "referer").getStatus(), 200);
    }

    @Test
    public void testUpdatePreference() {
        //Test 403, with invalid tenantIdParam DashboardException exception;
        Assert.assertEquals(preferenceAPI.updatePreference("", "userTenant", "referer", "key", jsonObject).getStatus(), 403);
        //Test 403, with invalid userTanant BasicServiceMalfunctionException exception;
        Assert.assertEquals(preferenceAPI.updatePreference("tenantIdParam", "userTenant", "key", "referer", jsonObject).getStatus(), 403);
    }

    @Test
    public void testUpdatePreference1(@Mocked final Preference preference, @Mocked final APIBase apiBase,@SuppressWarnings("unused") @Mocked final PreferenceManager preferenceManager, @Mocked final TenantIdProcessor tenantIdProcessor) throws IOException {
        new Expectations() {{
            apiBase.getJsonUtil().fromJson(anyString, Preference.class);
            result = preference;

            preference.getValue();
            result = anyString;

        }};
        //test 200 with valid userTenant
        String validUserTenant = "userTanant.userName";
        Assert.assertEquals(preferenceAPI.updatePreference("tenantIdParam", validUserTenant, "referer", "key", jsonObject).getStatus(), 200);
    }

    @Test
    public void testUpdatePreference1() {
        new MockUp<APIBase>() {
            @Mock
            public JsonUtil getJsonUtil() throws IOException {
                throw new IOException("exception from getJsonUtil");
            }

            @Mock
            public Response buildErrorResponse(ErrorEntity error) {
                return  null;
            }
        };
        String validUserTenant = "userTanant.userName";
        Assert.assertNull(preferenceAPI.updatePreference("tenantIdParam", validUserTenant, "referer", "key", jsonObject));
    }

}