package oracle.sysman.emaas.platform.dashboards.core.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class DashboardApplicationTypeTest
{

	private static final Logger logger = LogManager.getLogger(DashboardApplicationTypeTest.class);
	@Test(groups = { "s2" })
	public void testFromJsonValue()
	{
		Assert.assertEquals(DashboardApplicationType.APM, DashboardApplicationType.fromJsonValue("APM"));
		Assert.assertEquals(DashboardApplicationType.ITAnalytics, DashboardApplicationType.fromJsonValue("ITAnalytics"));
		Assert.assertEquals(DashboardApplicationType.LogAnalytics, DashboardApplicationType.fromJsonValue("LogAnalytics"));
		Assert.assertEquals(DashboardApplicationType.Monitoring, DashboardApplicationType.fromJsonValue("Monitoring"));
		Assert.assertEquals(DashboardApplicationType.SecurityAnalytics,
				DashboardApplicationType.fromJsonValue("SecurityAnalytics"));
		Assert.assertEquals(DashboardApplicationType.Orchestration, DashboardApplicationType.fromJsonValue("Orchestration"));
		Assert.assertEquals(DashboardApplicationType.Compliance, DashboardApplicationType.fromJsonValue("Compliance"));
		try {
			DashboardApplicationType.fromJsonValue("Not Existing");
			Assert.fail("Fail: trying to get application type from invalid value");
		}
		catch (IllegalArgumentException e) {
			// expected exception
			logger.info("context",e);
		}
	}

	@Test(groups = { "s2" })
	public void testFromValue()
	{
		Assert.assertEquals(DashboardApplicationType.APM, DashboardApplicationType.fromValue(1));
		Assert.assertEquals(DashboardApplicationType.ITAnalytics, DashboardApplicationType.fromValue(2));
		Assert.assertEquals(DashboardApplicationType.LogAnalytics, DashboardApplicationType.fromValue(3));
		Assert.assertEquals(DashboardApplicationType.Monitoring, DashboardApplicationType.fromValue(4));
		Assert.assertEquals(DashboardApplicationType.SecurityAnalytics, DashboardApplicationType.fromValue(5));
		Assert.assertEquals(DashboardApplicationType.Orchestration, DashboardApplicationType.fromValue(6));
		Assert.assertEquals(DashboardApplicationType.Compliance, DashboardApplicationType.fromValue(7));
		try {
			DashboardApplicationType.fromValue(Integer.MAX_VALUE);
			Assert.fail("Fail: trying to get application type from invalid value");
		}
		catch (IllegalArgumentException e) {
			// expected exception
			logger.info("context",e);
		}
	}

	@Test(groups = { "s2" })
	public void testGetJsonValue()
	{
		Assert.assertEquals(DashboardApplicationType.APM_STRING, DashboardApplicationType.APM.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.ITA_SRING, DashboardApplicationType.ITAnalytics.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.LA_STRING, DashboardApplicationType.LogAnalytics.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.MONITORING_STRING, DashboardApplicationType.Monitoring.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.SECURITY_ANALYTICS_STRING,
				DashboardApplicationType.SecurityAnalytics.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.ORCHESTRATION_STRING, DashboardApplicationType.Orchestration.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.COMPLIANCE_STRING, DashboardApplicationType.Compliance.getJsonValue());
	}
}
