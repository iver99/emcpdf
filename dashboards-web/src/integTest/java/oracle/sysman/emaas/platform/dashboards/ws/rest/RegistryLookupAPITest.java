package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.util.HashMap;
import java.util.Map;

import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil.VersionedLink;

import oracle.sysman.emaas.platform.emcpdf.registry.model.EndpointEntity;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author jishshi
 * @since 1/20/2016.
 */
@Test(groups = { "s2" })
public class RegistryLookupAPITest
{

	RegistryLookupAPI registryLookupAPI;

	@BeforeMethod
	public void setUp()
	{
		registryLookupAPI = new RegistryLookupAPI();

	}
	
	@Test
	public void testGetBaseVanityUrls(@Mocked final DependencyStatus anyDependencyStatus, @Mocked final RegistryLookupUtil lookupUtil)
	{
		final Map<String, String> vanityUrls = new HashMap<String, String>();
		vanityUrls.put("ApmUI", "https://vanityhostname:4443");
		vanityUrls.put("LogAnalyticsUI", "https://vanityhostname:4443");
		vanityUrls.put("TargetAnalytics", "https://vanityhostname:4443");
		
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
				RegistryLookupUtil.getVanityBaseURLs(anyString);
				result = vanityUrls;
			}
		};
		// Test 200 status
		Assert.assertEquals(registryLookupAPI.getBaseVanityUrls("tenantIdParam", "Tenant.user", "refer").getStatus(), 200);
		
		// Test 503 status
		Assert.assertEquals(registryLookupAPI.getBaseVanityUrls("tenantIdParam", "Tenantuser", "refer").getStatus(), 503);
		
		new Expectations() {
			{
				RegistryLookupUtil.getVanityBaseURLs(anyString);
				result = null;
			}
		};
		
		// Test 404 status
		Assert.assertEquals(registryLookupAPI.getBaseVanityUrls("tenantIdParam", "Tenant.user", "refer").getStatus(), 404);
	}

	@Test
	public void testGetRegistryLink(@Mocked final DependencyStatus anyDependencyStatus)
	{
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
			}
		};
		// Test 403 Exception;
		Assert.assertEquals(
				registryLookupAPI.getRegistryLink("tenantIdParam", "userTenant", "refer", "serviceName", "version", false).getStatus(),
				403);

		//Test 404 Exception with validUserTenant;
		String validUserTenant = "userTenant.userName";
		Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", false)
				.getStatus(), 404);

		//Test 404 Exception with validUserTenant;
		Assert.assertEquals(
				registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", null,false).getStatus(),
				404);

		//Test 404 empty ServiceName
		String emptyServiceName = "";
		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", validUserTenant, "refer", emptyServiceName, "version", false).getStatus(), 404);

	}

	@Test
	public void testGetRegistryLink1(@Mocked final RegistryLookupUtil registryLookupUtil,@Mocked final DependencyStatus anyDependencyStatus)
	{
		//Test 200 valid endpoint
		String validUserTenant = "userTenant.userName";
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
				RegistryLookupUtil.getServiceExternalEndPointEntity(anyString, anyString, anyString, false);
				result = withAny(new EndpointEntity(anyString, anyString, anyString));
			}
		};
		Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", false)
				.getStatus(), 200);
	}

	@Test
	public void testGetRegistryLink2(@Mocked final DependencyStatus anyDependencyStatus)
	{
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
			}
		};
		// Test 403 Exception;
		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", "userTenant", "refer", "serviceName", "version", "rel", false).getStatus(), 403);

		//Test 404 Exception with validUserTenant;
		String validUserTenant = "userTenant.userName";
		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel", false).getStatus(), 404);

		//Test 404 Exception with validUserTenant;
		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel", false).getStatus(), 404);

		//Test 404  null ServiceName
		Assert.assertEquals(
				registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", null, "version", "rel", false).getStatus(),
				404);

		//Test 404  null versionName
		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", null, "rel", false).getStatus(), 404);

		//Test 404 null rel
		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", null, false).getStatus(), 404);
	}

	@Test
	public void testGetRegistryLink3(@Mocked final DependencyStatus anyDependencyStatus,@Mocked final RegistryLookupUtil registryLookupUtil)
	{
		//Test 200 valid endpoint
		String validUserTenant = "userTenant.userName";
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
				RegistryLookupUtil.getServiceExternalLink(anyString, anyString, anyString, anyString, false);
				result = withAny(new VersionedLink());
			}
		};

		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel", false).getStatus(), 200);
	}

	@Test
	public void testGetRegistryLinkWithRelPrefix4(@Mocked final DependencyStatus anyDependencyStatus)
	{
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
			}
		};
		String validUserTenant = "userTenant.userName";
		new MockUp<RegistryLookupUtil>() {
			@Mock
			public EndpointEntity getServiceExternalEndPointEntity(String serviceName, String version, String tenantName, boolean useAPIGWLookup)
					throws Exception
			{
				throw new Exception("exception from getDefaultBundleString");
			}

			@Mock
			public VersionedLink getServiceExternalLink(String serviceName, String version, String rel, String tenantName, boolean useAPIGWLookup)
					throws Exception
			{
				throw new Exception("exception from getDefaultBundleString");
			}

		};
		Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", false)
				.getStatus(), 503);
		Assert.assertEquals(registryLookupAPI
				.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel", false).getStatus(), 503);
	}

	@Test
	public void testGetRegistryLinkWithRelPrefix(@Mocked final DependencyStatus anyDependencyStatus)
	{
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
			}
		};
		// Test 403 Exception;
		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", "userTenant", "refer", "serviceName", "version", "rel", false)
				.getStatus(), 403);

		//Test 404 Exception with validUserTenant;
		String validUserTenant = "userTenant.userName";
		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel", false)
				.getStatus(), 404);

		//Test 404 Exception with validUserTenant;
		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel", false)
				.getStatus(), 404);

		//Test 404  null ServiceName
		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", null, "version", "rel", false).getStatus(),
				404);

		//Test 404  null versionName
		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", null, "rel", false).getStatus(),
				404);

		//Test 404 null rel
		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", null, false)
				.getStatus(), 404);
	}

	@Test
	public void testGetRegistryLinkWithRelPrefix1(@Mocked final RegistryLookupUtil registryLookupUtil,@Mocked final DependencyStatus anyDependencyStatus)
	{
		
		//Test 200 valid endpoint
		String validUserTenant = "userTenant.userName";
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
				RegistryLookupUtil.getServiceExternalLinkWithRelPrefix(anyString, anyString, anyString, anyString, false);
				result = withAny(new VersionedLink());
			}
		};

		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel", false)
				.getStatus(), 200);
	}

	@Test
	public void testGetRegistryLinkWithRelPrefix2(@Mocked final DependencyStatus anyDependencyStatus)
	{
		new Expectations() {
			{
				anyDependencyStatus.isEntityNamingUp();
				result = true;
			}
		};
		String validUserTenant = "userTenant.userName";
		new MockUp<RegistryLookupUtil>() {
			@Mock
			public VersionedLink getServiceExternalLinkWithRelPrefix(String serviceName, String version, String rel,
					String tenantName, boolean useApiGWLookup) throws Exception
			{
				throw new Exception("exception from getServiceExternalLinkWithRelPrefix");
			}
		};
		Assert.assertEquals(registryLookupAPI
				.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel", false)
				.getStatus(), 503);
	}

}