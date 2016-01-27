package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.SanitizedInstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.collections.CollectionUtils;

import java.util.ArrayList;


/**
 * @author jishshi
 * @since 1/18/2016.
 */
@Test(groups = {"s2"})
public class RegistrationEntityTest {
    RegistrationEntity registrationEntity;

    @Mocked
    LookupManager lookupManager;

    @Mocked
    TenantSubscriptionUtil tenantSubscriptionUtil;

    @Mocked
    TenantContext tenantContext;

    @Mocked
    ApplicationEditionConverter applicationEditionConverter;

    @Mocked
    RegistryLookupUtil registryLookupUtil;

    @BeforeMethod
    public void setUp() throws Exception {
        registrationEntity = new RegistrationEntity();

        new NonStrictExpectations() {
            {
                TenantContext.getCurrentTenant();
                returns("tenantName", "tenantName");

                final String APM_SERVICENAME = "APM";
                final String LA_SERVICENAME = "LogAnalytics";
                final String ITA_SERVICENAME = "ITAnalytics";
                ArrayList<String> apps = new ArrayList<>();
                apps.add(APM_SERVICENAME);
                apps.add(LA_SERVICENAME);
                apps.add(ITA_SERVICENAME);

                TenantSubscriptionUtil.getTenantSubscribedServices(anyString);
                result = apps;
            }
        };
    }

    @Test(groups = {"s1"})
    public void testGetSessionExpiryTime() throws Exception {
        Assert.assertNull(registrationEntity.getSessionExpiryTime());
        registrationEntity = new RegistrationEntity("201217");

        Assert.assertEquals(registrationEntity.getSessionExpiryTime(), "201217");
    }

    @Test
    public void testGetAdminLinks() throws Exception {
        Assert.assertFalse(CollectionUtils.hasElements(registrationEntity.getAdminLinks()));
    }

    @Test
    public void testGetCloudServices() throws Exception {
        Assert.assertTrue(CollectionUtils.hasElements(registrationEntity.getCloudServices()));
    }

    @Test
    public void testGetHomeLinks(@Mocked final LookupClient lookupClient, @Mocked final InstanceInfo instanceInfo, @Mocked final Link link,@Mocked final LinkEntity linkEntity) throws Exception {
        new Expectations() {
            {
                instanceInfo.getVersion();
                returns(RegistrationEntity.NAME_DASHBOARD_UI_VERSION, RegistrationEntity.NAME_DASHBOARD_UI_VERSION);

                instanceInfo.getServiceName();
                returns(RegistrationEntity.NAME_DASHBOARD_UI_SERVICENAME, RegistrationEntity.APM_SERVICENAME);

                link.getRel();
                returns("relPrefix/sampleHostName", "relPrefix/sampleHostName");

                link.getHref();
                returns("https://sampleHost:port", "https://sampleHost:port");

                linkEntity.getHref();
                result = "http://sampleHost:port";

                ArrayList<Link> linkArrayList = new ArrayList<>();
                linkArrayList.add(link);
                linkArrayList.add(link);
                instanceInfo.getLinksWithRelPrefix(anyString);
                result = linkArrayList;

                RegistryLookupUtil.getLinksWithRelPrefix(anyString, withAny(new SanitizedInstanceInfo()));
                result = linkArrayList;

                ArrayList<InstanceInfo> infoArrayList = new ArrayList<>();
                infoArrayList.add(instanceInfo);
                lookupClient.getInstancesWithLinkRelPrefix(anyString);
                result = infoArrayList;
            }
        };
        Assert.assertTrue(CollectionUtils.hasElements(registrationEntity.getHomeLinks()));

        new Expectations(){
            {
                instanceInfo.getServiceName();
                result = "ApmUI";

                TenantContext.getCurrentTenant();
                returns("tenantName",withNull());

            }
        };
        Assert.assertTrue(CollectionUtils.hasElements(registrationEntity.getHomeLinks()));

    }


    @Test
    public void testGetVisualAnalyzers() throws Exception {
        Assert.assertNotNull(registrationEntity.getVisualAnalyzers());
    }

}