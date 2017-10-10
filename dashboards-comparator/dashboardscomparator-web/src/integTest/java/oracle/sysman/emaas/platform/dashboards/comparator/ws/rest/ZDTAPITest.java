package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.tenant.TenantIdProcessor;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.RestClientProxy;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.AbstractComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.CountsEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstanceData;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstancesComparedData;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.DashboardRowEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.TableRowsEntity;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by xiadai on 2017/1/10.
 */
@Test(groups = {"s2"})
public class ZDTAPITest {
    private ZDTAPI zdtapi = new ZDTAPI();
    @Mocked
    AbstractComparator abstractComparator;
    @Mocked
    InstancesComparedData<CountsEntity> set;
    @Mocked
    InstanceData<CountsEntity> data;
    @Mocked
    CountsEntity count;
    @Mocked
    Throwable throwable;
    @Mocked
    String tenant;
    @Mocked
    String userTenant;
    @Mocked
	RestClient restClient;
	@Mocked
	RestClientProxy proxy;
	@Mocked
	LookupManager lookupManager;

    @Test
    public void testSyncOnDF(@Mocked final JsonUtil jsonUtil, @Mocked final LookupClient client1, @Mocked final LookupClient client2) throws IOException{
    	final TableRowsEntity tableRow1 = new TableRowsEntity();
    	tableRow1.setEmsDashboard(new ArrayList<DashboardRowEntity>());
    	 
        final HashMap<String, LookupClient> lookupEntry = new HashMap<String, LookupClient>();
    	
    	new Expectations() {
    		{ 	
    			abstractComparator.getOMCInstances();
    			result = lookupEntry;
    			lookupEntry.put("omc1",client1);
    	    	lookupEntry.put("omc2",client2);
    		/*	
    			JsonUtil.buildNormalMapper();
    			result = jsonUtil;
    			jsonUtil.fromJson(anyString,TableRowsEntity.class);
    			result = tableRow1;*/
    		}
    	};
       // zdtapi.syncOnDF(tenant, userTenant, tenant);
    	 zdtapi.syncOnDF(tenant);
    }
    @Test
    public void testInnerClasses(){
        ZDTAPI.InstanceCounts counts = new ZDTAPI.InstanceCounts(data);
        counts.getCounts();
        counts.getInstanceName();
        counts.setCounts(count);
        counts.setInstanceName("name");

        ZDTAPI.InstancesComapredCounts instancesComapredCounts = new ZDTAPI.InstancesComapredCounts(counts, counts);
        instancesComapredCounts.getInstance1();
        instancesComapredCounts.getInstance2();
        instancesComapredCounts.setInstance1(counts);
        instancesComapredCounts.setInstance2(counts);
    }
   


    @Test
    public void testgetSyncStatus() {
    	//zdtapi.getCompareStatus("id", "userName");
    	zdtapi.getCompareStatus("id");
    	//zdtapi.getCompareStatus(null,null);
    	zdtapi.getCompareStatus(null);
    }
    
    @Test
    public void testgetCompareStatus() {
    	//zdtapi.getSyncStatus("id", "userName");
    	zdtapi.getSyncStatus("id");

    	//zdtapi.getCompareStatus(null,null);
    	zdtapi.getCompareStatus(null);
    }
    

}