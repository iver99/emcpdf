package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import oracle.sysman.emInternalSDK.rproxy.lookup.CloudLookupException;
import oracle.sysman.emInternalSDK.rproxy.lookup.CloudLookups;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.AbstractComparator;

import org.testng.annotations.Test;


@Test (groups = {"s2"})
public class AbstractComparatorTest {


    @Test
    public void testGetOMCInstances(final @Mocked CloudLookups anyCloudLookups) throws CloudLookupException {
        AbstractComparator ac = new AbstractComparator(){
            @Override
            public HashMap<String, LookupClient> getOMCInstances() {
                return super.getOMCInstances();
            }

            @Override
            protected Link getSingleInstanceUrl(LookupClient lc, String rel, String protocol) throws Exception {
                return super.getSingleInstanceUrl(lc, rel, protocol);
            }
        };
        final HashMap<String, LookupClient> entrySet = new HashMap<>();
//        entrySet.add()
        new Expectations(){
            {
                anyCloudLookups.getCloudLookupClients();
                result = entrySet;


            }
        };
        ac.getOMCInstances();
    }

    @Test
    public void testGetSingleInstanceUrl(final @Mocked InstanceQuery anyInstanceQuery, final @Mocked InstanceInfo anyInstanceInfo, final @Mocked LookupClient anyLookupClient) throws Exception {
        AbstractComparator ac = new AbstractComparator(){
            @Override
            public HashMap<String, LookupClient> getOMCInstances() {
                return super.getOMCInstances();
            }

            @Override
            protected Link getSingleInstanceUrl(LookupClient lc, String rel, String protocol) throws Exception {
                return super.getSingleInstanceUrl(lc, rel, protocol);
            }
        };
        final List<InstanceInfo> instanceList = new ArrayList<>();

        /*new Expectations(){
            {
                anyLookupClient.lookup(anyInstanceQuery);
                result = instanceList;
            }
        };*/
        Deencapsulation.invoke(ac, "getSingleInstanceUrl", anyLookupClient,"rel","protocal");
        Deencapsulation.invoke(ac, "getSingleInstanceUrl", anyLookupClient,String.class,"protocal");
        Deencapsulation.invoke(ac, "getSingleInstanceUrl", LookupClient.class,"rel","protocal");
        Deencapsulation.invoke(ac, "getSingleInstanceUrl", anyLookupClient,"rel",String.class);
    }

@Mocked
CloudLookups cloudLookups;
    @Injectable
    HashMap<String, LookupClient> hashMap;
    @Test
    public void testGetIns() throws CloudLookupException {
        AbstractComparator abstractComparator = new AbstractComparator(){
            @Override
            public HashMap<String, LookupClient> getOMCInstances() {
                return super.getOMCInstances();
            }

            @Override
            protected Link getSingleInstanceUrl(LookupClient lc, String rel, String protocol) throws Exception {
                return super.getSingleInstanceUrl(lc, rel, protocol);
            }
        };
        new Expectations(){
            {
                cloudLookups.getCloudLookupClients();
                result = hashMap;
            }
        };
        abstractComparator.getOMCInstances();
    }
}
