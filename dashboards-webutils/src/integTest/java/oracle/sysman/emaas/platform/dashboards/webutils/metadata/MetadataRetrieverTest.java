/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.webutils.metadata;

import java.util.List;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsResourceBundle;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil.VersionedLink;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author reliang
 *
 */
public class MetadataRetrieverTest
{
    private static final String MOCK_OOB_NLS = "[{"
            + "\"languageCode\": \"en\","
            + "\"propertiesFile\": \"UI\\\\ Gallery=Hello My Gallery\\nLine\\\\ Chart=Hello My Chart\\n\","
            + "\"lastModificationDate\": 1497251485787"
            + "}]";
    private static final String MOCK_OOB_DB = "[{" + 
            "    \"id\": \"1000\"," + 
            "    \"name\": \"PERFORMANCE_ANALYTICS_DATABASE_NAME\"," + 
            "    \"description\": \"PERFORMANCE_ANALYTICS_DATABASE_DESC\"," + 
            "    \"enableDescription\": \"FALSE\"," + 
            "    \"enableTimeRange\": \"FALSE\"," + 
            "    \"enableEntityFilter\": \"FALSE\"," + 
            "    \"enableRefresh\": true," + 
            "    \"showInHome\": true," + 
            "    \"screenShotHref\": \"https://den02dtf.us.oracle.com:4443/sso.static/dashboards.service/2/screenshot/1.14.0-161212.175227/images/1479485359489_2.png\"," + 
            "    \"type\": \"NORMAL\"," + 
            "    \"createdOn\": \"2016-11-18T08:09:19.489-0800\"," + 
            "    \"lastModifiedOn\": \"2016-11-18T08:09:19.489-0800\"," + 
            "    \"extendedOptions\": \"{\\\"tsel\\\":{\\\"entitySupport\\\":\\\"byCriteria\\\",\\\"entityContext\\\":\\\"\\\"},\\\"timeSel\\\":{\\\"defaultValue\\\":\\\"last14days\\\",\\\"start\\\":0,\\\"end\\\":0}}\"," + 
            "    \"selectedSsData\": \"\"," + 
            "    \"tiles\": [" + 
            "        {" + 
            "            \"type\": \"DEFAULT\"," + 
            "            \"row\": 0," + 
            "            \"column\": 0," + 
            "            \"height\": 2," + 
            "            \"isMaximized\": false," + 
            "            \"tileId\": \"1\"," + 
            "            \"title\": \"HOME\"," + 
            "            \"width\": 6," + 
            "            \"PROVIDER_ASSET_ROOT\": \"verticalApplication.db-perf\"," + 
            "            \"PROVIDER_NAME\": \"emcitas-ui-apps\"," + 
            "            \"PROVIDER_VERSION\": \"1.0\"," + 
            "            \"WIDGET_CREATION_TIME\": \"2016-11-18T08:09:19.565Z\"," + 
            "            \"WIDGET_DESCRIPTION\": \"WIDGET_XXXX_DESC\"," + 
            "            \"WIDGET_HISTOGRAM\": \"Invisible historgram\"," + 
            "            \"WIDGET_ICON\": \"Invisible icon\"," + 
            "            \"WIDGET_KOC_NAME\": \"DF_V1_WIDGET_ONEPAGE\"," + 
            "            \"WIDGET_NAME\": \"WIDGET_XXXX_NAME\"," + 
            "            \"WIDGET_TEMPLATE\": \"../emcsDependencies/widgets/onepage/onepageTemplate.html\"," + 
            "            \"WIDGET_UNIQUE_ID\": \"0\"," + 
            "            \"WIDGET_VIEWMODEL\": \"../emcsDependencies/widgets/onepage/js/onepageModel\"," + 
            "            \"WIDGET_SUPPORT_TIME_CONTROL\": true," + 
            "            \"WIDGET_LINKED_DASHBOARD\": null," + 
            "            \"WIDGET_GROUP_NAME\": \"Log Analytics\"," + 
            "            \"tileParameters\": [" + 
            "                {" + 
            "                    \"name\": \"DF_HIDE_TITLE\"," + 
            "                    \"type\": \"STRING\"," + 
            "                    \"value\": \"string only\"" + 
            "                }" + 
            "            ]" + 
            "        }" + 
            "    ]," + 
            "    \"subDashboards\" : [{" + 
            "        \"id\": 10001" + 
                "}]" + 
            "}]";
    
    @Test(groups = { "s1" })
    public void testGetAllOobDashboardByService(@Mocked final RegistryLookupUtil rlu, 
            @Mocked final VersionedLink link, @Mocked final RestClient rc) throws CommonFunctionalException {
        new Expectations() {
            {
                RegistryLookupUtil.getServiceInternalLink(anyString, anyString);
                result = link;
                link.getHref();
                result = "http";
                rc.get(anyString, null, anyString);
                result = MOCK_OOB_DB;
                
            }
        };
        
        MetadataRetriever retriever = new MetadataRetriever();
        List<Dashboard> oobList = retriever.getOobDashboardsByService("TargetAnalytics");
        Dashboard oob = oobList.get(0);
        Assert.assertEquals(oob.getDashboardId().toString(), "1000");
        Assert.assertEquals(oob.getOwner(), "Oracle");
        Assert.assertEquals(oob.getLastModifiedBy(), "Oracle");
        Assert.assertEquals(oob.getIsSystem(), Boolean.TRUE);
        Assert.assertEquals(oob.getSharePublic(), Boolean.FALSE);
        Assert.assertEquals(oob.getAppicationType().getValue(), 8);
        Assert.assertEquals(oob.getTileList().size(), 1);
        Assert.assertEquals(oob.getTileList().get(0).getParameters().size(), 1);
        Assert.assertEquals(oob.getTileList().get(0).getWidgetGroupName(), "Log Analytics");
        Assert.assertEquals(oob.getSubDashboards().size(), 1);
        Assert.assertEquals(oob.getSubDashboards().get(0).getDashboardId().toString(), "10001");
    }

    @Test(groups = { "s1" })
    public void testGetResourceBundleByService(@Mocked final RegistryLookupUtil rlu, 
            @Mocked final VersionedLink link, @Mocked final RestClient rc) throws CommonFunctionalException {
        new Expectations() {
            {
                RegistryLookupUtil.getServiceInternalLink(anyString, anyString);
                result = link;
                link.getHref();
                result = "http";
                rc.get(anyString, null, anyString);
                result = MOCK_OOB_NLS;
                
            }
        };
        
        MetadataRetriever retriever = new MetadataRetriever();
        List<EmsResourceBundle> nlsList = retriever.getResourceBundleByService("TargetAnalytics");
        Assert.assertNotNull(nlsList);
        Assert.assertNotNull(nlsList.get(0));
        Assert.assertEquals(nlsList.get(0).getLanguageCode(), "en");
    }

}
