package oracle.sysman.emaas.platform.dashboards.core.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by guochen on 9/27/17.
 */
public class FederationSupportedTypeTest {
	private static final Logger LOGGER = LogManager.getLogger(FederationSupportedTypeTest.class);

	@Test(groups = { "s1" })
	public void testFromJsonValue()
	{
		Assert.assertEquals(FederationSupportedType.FEDERATION_ONLY, FederationSupportedType.fromJsonValue("FEDERATION_ONLY"));
		Assert.assertEquals(FederationSupportedType.FEDERATION_AND_NON_FEDERATION, FederationSupportedType.fromJsonValue("FEDERATION_AND_NON_FEDERATION"));
		Assert.assertEquals(FederationSupportedType.NON_FEDERATION_ONLY, FederationSupportedType.fromJsonValue("NON_FEDERATION_ONLY"));
		try {
			FederationSupportedType.fromJsonValue("Not Existing");
			Assert.fail("Fail: trying to get FederationSupportedType from invalid value");
		}
		catch (IllegalArgumentException e) {
			// expected exception
			LOGGER.info("context",e);
		}
	}

	@Test(groups = { "s1" })
	public void testFromValue()
	{
		Assert.assertEquals(FederationSupportedType.NON_FEDERATION_ONLY, FederationSupportedType.fromValue(0));
		Assert.assertEquals(FederationSupportedType.FEDERATION_AND_NON_FEDERATION, FederationSupportedType.fromValue(1));
		Assert.assertEquals(FederationSupportedType.FEDERATION_ONLY, FederationSupportedType.fromValue(2));
		try {
			FederationSupportedType.fromValue(Integer.MAX_VALUE);
			Assert.fail("Fail: trying to get FederationSupportedType from invalid value");
		}
		catch (IllegalArgumentException e) {
			// expected exception
			LOGGER.info("context",e);
		}
	}

	@Test(groups = { "s1" })
	public void testGetJsonValue()
	{
		Assert.assertEquals(FederationSupportedType.FEDERATION_ONLY_STRING, FederationSupportedType.FEDERATION_ONLY.getJsonValue());
		Assert.assertEquals(FederationSupportedType.FEDERATION_AND_NON_FEDERATION_SRING, FederationSupportedType.FEDERATION_AND_NON_FEDERATION.getJsonValue());
		Assert.assertEquals(FederationSupportedType.NON_FEDERATION_ONLY_STRING, FederationSupportedType.NON_FEDERATION_ONLY.getJsonValue());
	}
}