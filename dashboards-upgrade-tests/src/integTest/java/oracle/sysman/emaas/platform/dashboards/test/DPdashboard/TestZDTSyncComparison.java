package oracle.sysman.emaas.platform.dashboards.test.DPdashboard;

import oracle.sysman.qatool.uifwk.utils.Utils;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * Created by shangwan on 2017/12/7.
 */
public class TestZDTSyncComparison {
    public static String currentDate = null;
    static private String TENANTID = Utils.getProperty("TENANT_ID");
    static private String REMOTEUSE = Utils.getProperty("SSO_USERNAME");
    static private String AUTHTOKEN = Utils.getProperty("SAAS_AUTH_TOKEN");
    static private String APIURL = Utils.getProperty("DASHBOARD_API_ENDPOINT");
    static private int INDEX = APIURL.indexOf("/emcpdf/api/v1/");

    @Test
    public void testSyncComparsionData() {
        RestAssured.baseURI = APIURL.substring(0, INDEX);
        RestAssured.basePath = "/emcpdf/api/v1";

        try
        {
            //call sync API to insert data into ems_zdt_sync table
            Response res = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .log()
                    .everything()
                    .headers("X-USER-IDENTITY-DOMAIN-NAME", TENANTID, "X-REMOTE-USER", TENANTID + "." + REMOTEUSE,
                            "Authorization", AUTHTOKEN).when().get("/zdt/sync");
            //verify sync API successfully
            Assert.assertTrue(res.getStatusCode() == 200);
            Assert.assertTrue("Sync is successful!".equals(res.jsonPath().getString("msg").trim()));

            //call /zdt/compare/result API to insert data into ems_zdt_comparator table
            String jsonString = "{\"lastComparisonDateTime\":\"2017-05-12 15:20:21\", \"comparisonType\":\"full\",\"comparisonResult\":\"{}\",\"divergencePercentage\":0.11,\"nextScheduledComparisonDateTime\":\"2017-05-12 15:20:21\"}";
            Response res1 = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .log()
                    .everything()
                    .headers("X-USER-IDENTITY-DOMAIN-NAME", TENANTID, "X-REMOTE-USER", TENANTID + "." + REMOTEUSE,
                            "Authorization", AUTHTOKEN).body(jsonString).when().put("/zdt/compare/result");
            Assert.assertTrue(res1.getStatusCode() == 200);

            //verify the compare status
            Response res2 = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .log()
                    .everything()
                    .headers("X-USER-IDENTITY-DOMAIN-NAME", TENANTID, "X-REMOTE-USER", TENANTID + "." + REMOTEUSE,
                            "Authorization", AUTHTOKEN).when().get("/zdt/compare/status");
            Assert.assertTrue(res2.getStatusCode() == 200);
            Assert.assertTrue(res2.jsonPath().getString("lastComparisonDateTime").contains("2017-05-12 15:20:21"));

            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            currentDate = dateFormat.format(now);

            String jsonString1 = "{\"lastComparisonDateTime\":\""+ currentDate +"\", \"comparisonType\":\"full\",\"comparisonResult\":\"{}\",\"divergencePercentage\":0.11,\"nextScheduledComparisonDateTime\":\""+ currentDate +"\"}";
            Response res3 = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .log()
                    .everything()
                    .headers("X-USER-IDENTITY-DOMAIN-NAME", TENANTID, "X-REMOTE-USER", TENANTID + "." + REMOTEUSE,
                            "Authorization", AUTHTOKEN).body(jsonString1).when().put("/zdt/compare/result");
            Assert.assertTrue(res3.getStatusCode() == 200);

            Response res4 = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .log()
                    .everything()
                    .headers("X-USER-IDENTITY-DOMAIN-NAME", TENANTID, "X-REMOTE-USER", TENANTID + "." + REMOTEUSE,
                            "Authorization", AUTHTOKEN).when().get("/zdt/compare/status");
            Assert.assertTrue(res4.getStatusCode() == 200);
            Assert.assertTrue(res4.jsonPath().getString("lastComparisonDateTime").contains(currentDate));
        }
        catch(Exception e)
        {
            Assert.fail("Test failed: " + e.getLocalizedMessage());
        }
    }
}
