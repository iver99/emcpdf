package oracle.sysman.emaas.platform.dashboards.test.DVdashboard;

import oracle.sysman.qatool.uifwk.utils.Utils;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
/**
 * Created by shangwan on 2017/12/7.
 */
public class TestZDTSyncComparison {
    static private String TENANTID = Utils.getProperty("TENANT_ID");
    static private String REMOTEUSE = Utils.getProperty("SSO_USERNAME");
    static private String AUTHTOKEN = Utils.getProperty("SAAS_AUTH_TOKEN");
    static private String APIURL = Utils.getProperty("DASHBOARD_API_ENDPOINT");
    static private int INDEX = APIURL.indexOf("/emcpdf/api/v1/");

    @Test
    public void testSyncComparsionDataAfterUpgrade() {
        RestAssured.baseURI = APIURL.substring(0, INDEX);
        RestAssured.basePath = "/emcpdf/api/v1";

        try
        {
            Response res = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .log()
                    .everything()
                    .headers("X-USER-IDENTITY-DOMAIN-NAME", TENANTID, "X-REMOTE-USER", TENANTID + "." + REMOTEUSE,
                            "Authorization", AUTHTOKEN).when().get("/zdt/compare/status");
            Assert.assertTrue(res.getStatusCode() == 200);
            Assert.assertTrue(res.jsonPath().getString("lastComparisonDateTime").contains("2017-05-12 15:20:21"));
        }
        catch (Exception e)
        {
            Assert.fail("Test failed: " + e.getLocalizedMessage());
        }
    }
}
