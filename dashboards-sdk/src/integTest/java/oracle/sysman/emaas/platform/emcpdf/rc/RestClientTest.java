package oracle.sysman.emaas.platform.emcpdf.rc;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import oracle.sysman.emaas.platform.emcpdf.cache.util.StringUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chehao on 2017/4/18 11:05.
 */
public class RestClientTest {
    private static final String ENTITY_NAMING_DOMAINS_URL = "http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains";
    private static final String TENANT_LOOKUP_URL = "http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/58bfe535-7134-4cae-be4a-d6d3dcfdb4d8/lookups?opcTenantId=emaastesttenant1";


    @Test(groups = {"s2"})
    public void testRestClientGetNull() {
        String res = new RestClient().get(null, null, null);
        Assert.assertNull(res);
    }

    @Test(groups = {"s2"})
    public void testRestClientGetNullAuthS2(@Mocked final DefaultClientConfig anyClientConfig, @Mocked final Client anyClient,
                                            @Mocked final URI anyUri, @Mocked final UriBuilder anyUriBuilder, @Mocked final MediaType anyMediaType,
                                            @Mocked final com.sun.jersey.api.client.WebResource.Builder anyBuilder, @Mocked final StringUtil anyStringUtil)
            throws Exception {
        new NonStrictExpectations() {
            {
                StringUtil.isEmpty(anyString);
                result = false;
            }
        };
        new RestClient().get("http://test.link.com", "emaastesttenant1", null);
        new Verifications() {
            {
                UriBuilder.fromUri(anyString).build();
                anyClient.resource(anyUri).header(anyString, any);
                anyBuilder.get(String.class);
            }
        };
    }

    @Test(groups = {"s2"})
    public void testRestClientGetS2(@Mocked final DefaultClientConfig anyClientConfig, @Mocked final Client anyClient,
                                    @Mocked final URI anyUri, @Mocked final UriBuilder anyUriBuilder, @Mocked final MediaType anyMediaType,
                                    @Mocked final com.sun.jersey.api.client.WebResource.Builder anyBuilder) {
        RestClient rc = new RestClient();
        rc.setHeader("TestHeader","header1");
        rc.get("http://test.link.com", "emaastesttenant1", null);
        new Verifications() {
            {
                UriBuilder.fromUri(anyString).build();
                anyClient.resource(anyUri).header(anyString, any);
                anyBuilder.get(String.class);
            }
        };
    }

    @Test(groups = {"s2"})
    public void testRestClientGetException1(@Mocked final DefaultClientConfig anyClientConfig, @Mocked final Client anyClient,
                                            @Mocked final URI anyUri, @Mocked final UriBuilder anyUriBuilder, @Mocked final MediaType anyMediaType,
                                            @Mocked final com.sun.jersey.api.client.WebResource.Builder anyBuilder, @Mocked final ClientResponse anyClientResponse) {
        // test UniformInterfaceException
        new NonStrictExpectations() {
            {
                anyBuilder.get(String.class);
                result = new UniformInterfaceException(anyClientResponse);
            }
        };
        RestClient rc = new RestClient("logger.name");
        rc.setHeader("TestHeader","header1");
        rc.get("http://test.link.com", "emaastesttenant1", null);
        new Verifications() {
            {
                UriBuilder.fromUri(anyString).build();
                anyClient.resource(anyUri).header(anyString, any);
                anyBuilder.get(String.class);
            }
        };
        // test ClientHandlerException
        new NonStrictExpectations() {
            {
                anyBuilder.get(String.class);
                result = new ClientHandlerException();
            }
        };
        rc = new RestClient("logger.name");
        rc.setHeader("TestHeader","header1");
        rc.get("http://test.link.com", "emaastesttenant1", null);
        // test Exception
        new NonStrictExpectations() {
            {
                anyBuilder.get(String.class);
                result = new Exception();
            }
        };
        rc = new RestClient("logger.name");
        rc.setHeader("TestHeader","header1");
        rc.get("http://test.link.com", "emaastesttenant1", null);
    }

    @Test(groups = {"s2"})
    public void testRestClientPut(@Mocked final DefaultClientConfig anyClientConfig, @Mocked final Client anyClient,
                                  @Mocked final URI anyUri, @Mocked final UriBuilder anyUriBuilder, @Mocked final MediaType anyMediaType,
                                  @Mocked final com.sun.jersey.api.client.WebResource.Builder anyBuilder) {
        RestClient rc = new RestClient();
        List<String> list = new ArrayList<>();
        list.add("test1");
        rc.setHeader("TestHeader", "header1");
        try{
            rc.put("http://test.link.com",list.toString(), "emaastesttenant1", null);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    @Test(groups = {"s2"})
    public void testRestClientPOST(@Mocked final DefaultClientConfig anyClientConfig, @Mocked final Client anyClient,
                                  @Mocked final URI anyUri, @Mocked final UriBuilder anyUriBuilder, @Mocked final MediaType anyMediaType,
                                  @Mocked final com.sun.jersey.api.client.WebResource.Builder anyBuilder,@Mocked final ClientResponse anyClientResponse) {
       	RestClient rc = new RestClient();
        List<String> list = new ArrayList<>();
        list.add("test1");
        rc.setHeader("TestHeader", "header1");
        rc.post("http://test.link.com",list.toString(), "emaastesttenant1", null);
        // test UniformInterfaceException
        new NonStrictExpectations() {
            {
                anyBuilder.post((Class)any, any);
                result = new UniformInterfaceException(anyClientResponse);
            }
        };
        rc = new RestClient();
        rc.post("http://test.link.com",list.toString(), "emaastesttenant1", null);

        // test ClientHandlerException
        new NonStrictExpectations() {
            {
                anyBuilder.post((Class)any, any);
                result = new ClientHandlerException();
            }
        };
        rc = new RestClient();
        rc.post("http://test.link.com",list.toString(), "emaastesttenant1", null);

    }

    @Test(groups = {"s2"})
    public void testRestClientSetHeaders() {
        RestClient rc = new RestClient();

        String testKey = "testKey";
        String testValue = "testValue";
        rc.setHeader(testKey, testValue);
        Map<String, Object> headers = Deencapsulation.getField(rc, "headers");
        Assert.assertEquals(testValue, headers.get(testKey));
        headers.clear();

        // test null key
        rc.setHeader(null, testValue);
        headers = Deencapsulation.getField(rc, "headers");
        Assert.assertTrue(headers.containsValue(testValue));

        rc.setHeader(testKey, null);
        headers = Deencapsulation.getField(rc, "headers");
        Assert.assertTrue(headers.containsKey(testKey));
    }
    @Test(groups = {"s2"})
    public void testAuthHeader(){
        Object key = new String("Authorization");
        Assert.assertTrue("Authorization".equals(key));
    }
}
