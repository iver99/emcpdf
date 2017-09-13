package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import mockit.Mock;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.TenantWithoutSubscriptionException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.UserOptionsNotFoundException;

import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.ParameterModel;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.SearchModel;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.DashboardsFilter;
import oracle.sysman.emaas.platform.dashboards.core.UserOptionsManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.PaginatedDashboards;
import oracle.sysman.emaas.platform.dashboards.core.model.Tile;
import oracle.sysman.emaas.platform.dashboards.core.model.UserOptions;
import oracle.sysman.emaas.platform.dashboards.core.model.combined.CombinedDashboard;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.rest.ssfDatautil.SSFDataUtil;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.DashboardAPIUtil;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.ScreenshotData;



/**
 * @author danfjian
 * @since 2016/1/14.
 */
@Test(groups = { "s2" })
public class DashboardAPITest
{

	@Mocked
	APIBase mockedAPIBase;
	@Mocked
	DashboardManager mockedDashboardManager;
	@Mocked
	Throwable throwable;
	DashboardAPI dashboardAPI = new DashboardAPI();

	@Test
	public void testCreateDashboard(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;

				mockedAPIBase.initializeUserContext(anyString, anyString);
				result = null;

				mockedDashboardManager.saveNewDashboard(withAny(new Dashboard()), anyLong);
				result = any;

				Deencapsulation.invoke(dashboardAPI, "updateDashboardAllHref", withAny(new Dashboard()), anyString);
				result = any;
			}
		};
		assertCreateDashboard();
	}
	
	
	@Test
	public void testCreateDashboardWithBasicServiceMalfunctionException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertCreateDashboard();
	}

	@Test
	public void testCreateDashboardWithDashboardException(@SuppressWarnings("unused") @Mocked final JsonUtil jsonUtil,@Mocked final DependencyStatus anyDependencyStatus)
			throws IOException, DashboardException
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				
				mockedDashboardManager.saveNewDashboard(withAny(new Dashboard()), anyLong);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertCreateDashboard();
	}

	@Test
	public void testCreateDashboardWithIOException(@Mocked final JsonUtil jsonUtil,@Mocked final DependencyStatus anyDependencyStatus) throws IOException
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				
				jsonUtil.fromJson(anyString, Dashboard.class);
				result = new IOException();
			}
		};
		assertCreateDashboard();
	}

	@Test
	public void testDeleteDashboard(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedDashboardManager.deleteDashboard((BigInteger) any, anyLong);
			}
		};
		assertDeleteDashboard();
	}

	@Test
	public void testDeleteDashboardBasicServiceMalfunctionException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertDeleteDashboard();
	}

	@Test
	public void testDeleteDashboardWithDashboardException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedDashboardManager.deleteDashboard((BigInteger) any, anyLong);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertDeleteDashboard();
	}

	@Test
	public void testDeleteDashboardWithDeleteSystemDashboardException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedDashboardManager.getDashboardById((BigInteger) any, anyLong);
				Dashboard mockDashboardResult = new Dashboard();
				mockDashboardResult.setIsSystem(true);
				result = mockDashboardResult;
			}
		};
		assertDeleteDashboard();
	}

	@Test
	public void getDashboardUserOptions(@Mocked final UserOptionsManager userOptionsManager,
										@Mocked final DependencyStatus anyDependencyStatus,
										@Mocked final UserOptions userOptions, @Mocked final UserOptionsNotFoundException userOptionNotFoundException) throws DashboardNotFoundException, UserOptionsNotFoundException {
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				UserOptionsManager.getInstance();
				result = userOptionsManager;
				userOptionsManager.getOptionsById((BigInteger)any, anyLong);
				result = userOptionNotFoundException;
			}
		};
		dashboardAPI.getDashboardUserOptions("","","",new BigInteger("1"));
	}


	@Test
	public void getDashboardUserOptions(@Mocked final UserOptionsManager userOptionsManager,
										@Mocked final DependencyStatus anyDependencyStatus,
										@Mocked final UserOptions userOptions, @Mocked final DashboardNotFoundException dashboardNotFoundException) throws DashboardNotFoundException, UserOptionsNotFoundException {
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				UserOptionsManager.getInstance();
				result = userOptionsManager;
				userOptionsManager.getOptionsById((BigInteger)any, anyLong);
				result = dashboardNotFoundException;
			}
		};
		dashboardAPI.getDashboardUserOptions("","","",new BigInteger("1"));
	}

	@Test
	public void getDashboardUserOptions(@Mocked final UserOptionsManager userOptionsManager,
										@Mocked final DependencyStatus anyDependencyStatus,
										@Mocked final UserOptions userOptions, @Mocked final DashboardException dashboardNotFoundException) throws DashboardNotFoundException, UserOptionsNotFoundException {
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				UserOptionsManager.getInstance();
				result = userOptionsManager;
				userOptionsManager.getOptionsById((BigInteger)any, anyLong);
				result = dashboardNotFoundException;
			}
		};
		dashboardAPI.getDashboardUserOptions("","","",new BigInteger("1"));
	}

	@Test
	public void getDashboardUserOptions(@Mocked final UserOptionsManager userOptionsManager,
										@Mocked final DependencyStatus anyDependencyStatus,
										@Mocked final UserOptions userOptions, @Mocked final BasicServiceMalfunctionException dashboardNotFoundException) throws DashboardNotFoundException, UserOptionsNotFoundException {
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				UserOptionsManager.getInstance();
				result = userOptionsManager;
				userOptionsManager.getOptionsById((BigInteger)any, anyLong);
				result = dashboardNotFoundException;
			}
		};
		dashboardAPI.getDashboardUserOptions("","","",new BigInteger("1"));
	}

	@Test
	public void testGetDashboardBase64ScreenShot(@SuppressWarnings("unused") @Mocked DashboardAPIUtil dashboardAPIUtil)
			
	{
		//		new Expectations() {
		//			{
		//				DashboardAPIUtil.getExternalDashboardAPIBase(anyString);
		//				result = "http://external/";
		//			}
		//		};
		assertGetDashboardBase64ScreenShot();
	}

	@Test
	public void testGetDashboardBase64ScreenShotWithBasicServiceMalfunctionException() throws Exception
	{
		new Expectations() {
			{
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertGetDashboardBase64ScreenShot();
	}

	@Test
	public void testGetDashboardBase64ScreenShotWithDashboardException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
            	result=true;
				//anyDependencyStatus.isEntityNamingUp();
            	//result=true;
				mockedDashboardManager.getDashboardBase64ScreenShotById((BigInteger) any, anyLong);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertGetDashboardBase64ScreenShot();
	}

	@Test
	public void testQueryDashboardById(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;

				mockedDashboardManager.getCombinedDashboardById((BigInteger) any, anyLong, anyString);
				result = new CombinedDashboard();

				Deencapsulation.invoke(dashboardAPI, "updateDashboardAllHref", withAny(new CombinedDashboard()), anyString);
				result = any;
			}
		};
		assertQueryDashboardById();
	}
	
	@Test
	public void testExportDashboard(@Mocked final DependencyStatus anyDependencyStatus, 
			@Mocked final CombinedDashboard combinedDashboard,
			@Mocked final JSONObject obj,
			@Mocked final SSFDataUtil ssfUtil) throws Exception, TenantWithoutSubscriptionException {
		final List<BigInteger> dbdIds = new ArrayList<BigInteger>();
		dbdIds.add(new BigInteger("24"));
		final List<Dashboard> subDbds = new ArrayList<Dashboard>();
		subDbds.add(new Dashboard());
		final List<Tile> allTiles = new ArrayList<Tile>();
		Tile tile = new Tile();
		tile.setWidgetUniqueId("566");
		allTiles.add(tile);
		final BigInteger id = new BigInteger("123");
		    
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				
				mockedDashboardManager.getDashboardIdsByNames((List<String>)any, anyLong);
				result = dbdIds;
				
				mockedDashboardManager.getDashboardById((BigInteger) any, anyLong);
      			result = combinedDashboard;
				
      			combinedDashboard.getSubDashboards();
				result = subDbds;
				
				combinedDashboard.getDashboardId();
				result = id;
				
				combinedDashboard.getTileList();
				result = allTiles;
				 
				mockedDashboardManager.getDashboardBase64ScreenShotById((BigInteger)any, anyLong);
				result = new ScreenshotData(anyString, null, null);		
				
				ssfUtil.getSSFData(anyString, anyString);
				result = "{\"name\":\"search\"}";
				
			}
		};
		assertExportDashboard();
	}
	
	@Test
	public void testImportDashboard(@Mocked final DependencyStatus anyDependencyStatus, 
			@Mocked final SSFDataUtil ssfUtil,
			@Mocked final Dashboard dbd
			) throws Exception, TenantWithoutSubscriptionException {	
		Tile tile = new Tile();
		tile.setWidgetUniqueId("123456");
		final List<Tile> tiles = new ArrayList<Tile>();
		tiles.add(tile);
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				ssfUtil.saveSSFData(anyString, anyString, anyBoolean);
				result = "{\"1\":\"1234\"}";
				
				dbd.getType();
				result = "SET";
				
				dbd.getTileList();
				result = tiles;
				
				mockedDashboardManager.saveForImportedDashboard(dbd, anyLong, anyBoolean);
				result = dbd;
				
			}
		};
		assertImportDashboardOverride();
	}
	

	@Test
	public void testQueryCombinedData(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedDashboardManager.getCombinedDashboardById((BigInteger) any, anyLong, anyString, (List<String>)any);
				result = new CombinedDashboard();
				Deencapsulation.invoke(dashboardAPI, "updateDashboardAllHref", withAny(new CombinedDashboard()), anyString);
				result = any;
			}
		};
		assertQueryCombinedDashboardById();
	}

	@Test
	public void testQueryDashboardByIdWithBasicServiceMalfunctionException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertQueryDashboardById();
	}

	@Test
	public void testQueryDashboardByIdWithDashboardException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertQueryDashboardById();
	}

	@Test
	public void testQueryDashboards(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedDashboardManager.listDashboards(anyString, anyInt, anyInt, anyLong, anyBoolean, anyString,
						withAny(new DashboardsFilter()));
				PaginatedDashboards dashboardsResult = new PaginatedDashboards();
				List<Dashboard> dashboardList = new ArrayList<>();
				Dashboard dashboard1 = new Dashboard();
				dashboardList.add(dashboard1);
				dashboardsResult.setDashboards(dashboardList);
				result = dashboardsResult;

				mockedAPIBase.updateDashboardHref(withAny(new Dashboard()), anyString);
				result = null;
			}
		};
		assertQueryDashboards();
	}

	@Test
	public void testQueryDashboardsWithBasicServiceMalfunctionException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertQueryDashboards();
	}

	@Test
	public void testQueryDashboardsWithDashboardException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertQueryDashboards();
	}

	@Test
	public void testQueryDashboardsWithUnsupportedEncodingException(@SuppressWarnings("unused") @Mocked URLDecoder urlDecoder,@Mocked final DependencyStatus anyDependencyStatus)
			throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				URLDecoder.decode(anyString, anyString);
				result = new UnsupportedEncodingException("Test Encoding");
			}
		};
		assertQueryDashboards();
	}

	@Test
	public void testQuickUpdateDashboard() throws Exception
	{
		assertQuickUpdateDashboard();
	}

	@Test
	public void testQuickUpdateDashboardCommonSecurityException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedDashboardManager.getDashboardById((BigInteger) any, anyLong);
				Dashboard dashboardResult = new Dashboard();
				dashboardResult.setIsSystem(true);
				result = dashboardResult;
			}
		};
		assertQuickUpdateDashboard();
	}

	@Test
	public void testQuickUpdateDashboardWithBasicServiceMalfunctionException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertQuickUpdateDashboard();
	}

	@Test
	public void testQuickUpdateDashboardWithDashboardException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertQuickUpdateDashboard();
	}

	@Test
	public void testQuickUpdateDashboardWithJSONException(@Mocked final JSONObject mockedJsonObject,@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				mockedJsonObject.has("sharePublic");
				result = true;

				mockedJsonObject.getBoolean(anyString);
				result = new JSONException("Mocked JSON Exception");
			}
		};
		assertQuickUpdateDashboard();
	}

	@Test
	public void testUpdateDashboard() throws Exception
	{
		assertUpdateDashboard();
	}

	@Test
	public void testUpdateDashboardWithBasicServiceMalfunctionException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
			}
		};
		assertUpdateDashboard();
	}
	
	

	@Test
	public void testUpdateDashboardWithCommonSecurityException(@Mocked final JsonUtil mockedJsonUtil) throws Exception
	{
		new Expectations() {
			{
				mockedJsonUtil.fromJson(anyString, Dashboard.class);
				Dashboard mockDashboardResult = new Dashboard();
				mockDashboardResult.setIsSystem(true);
				result = mockDashboardResult;
			}
		};
		assertUpdateDashboard();
	}

	@Test
	public void testUpdateDashboardWithDashboardException(@Mocked final DependencyStatus anyDependencyStatus) throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				mockedAPIBase.getTenantId(anyString);
				result = new CommonSecurityException("Test Security Error");
			}
		};
		assertUpdateDashboard();
	}

	@Test
	public void testUpdateDashboardWithExternalBase(@SuppressWarnings("unused") @Mocked DashboardAPIUtil dashboardAPIUtil,@Mocked final DependencyStatus anyDependencyStatus)
			throws Exception
	{
		new Expectations() {
			{
				anyDependencyStatus.isDatabaseUp();
				result = true;
				DashboardAPIUtil.getExternalDashboardAPIBase(anyString);
				result = "http://external/";
			}
		};
		assertUpdateDashboard();
	}

	@Test
	public void testUpdateDashboardWithIOException(@Mocked final JsonUtil mockedJsonUtil) throws Exception
	{
		new Expectations() {
			{
				mockedJsonUtil.fromJson(anyString, Dashboard.class);
				result = new IOException("Mocked IO Exception");
			}
		};
		assertUpdateDashboard();
	}

	@Test
	public void testSaveUserOptions(@Mocked final UserOptionsManager mockedUserOptionsManager,@Mocked final DependencyStatus anyDependencyStatus,
									@Mocked final UserOptions anyUserOptions) throws Exception {
        new Expectations() {
            {
            	anyDependencyStatus.isDatabaseUp();
				result = true;
                mockedAPIBase.initializeUserContext(anyString, anyString);
                result = null;

				anyUserOptions.validateExtendedOptions();
				result = true;

                mockedUserOptionsManager.saveOrUpdateUserOptions(anyUserOptions, anyLong);
                result = any;
            }
        };
        assertSaveUserOptions();

	}

	@Test
	public void testUpdateUserOptions(@Mocked final UserOptionsManager mockedUserOptionsManager,@Mocked final DependencyStatus anyDependencyStatus,
									  @Mocked final UserOptions anyUserOptions) throws Exception {
        new Expectations() {
            {
            	anyDependencyStatus.isDatabaseUp();
				result = true;
                mockedAPIBase.initializeUserContext(anyString, anyString);
                result = null;

				anyUserOptions.validateExtendedOptions();
				result = true;

				mockedUserOptionsManager.saveOrUpdateUserOptions(anyUserOptions, anyLong);
				result = any;
			}
		};

		assertUpdateUserOptions();
	}

    @Test
    public void testGetUserOptions(@Mocked final UserOptionsManager mockedUserOptionsManager,@Mocked final DependencyStatus anyDependencyStatus) throws Exception {
        new Expectations() {
            {
            	anyDependencyStatus.isDatabaseUp();
				result = true;
            	anyDependencyStatus.isDatabaseUp();
				result = true;
                mockedAPIBase.initializeUserContext(anyString, anyString);
                result = null;

                mockedUserOptionsManager.getOptionsById((BigInteger) any, anyLong);
                result = any;
            }
        };
        assertGetUserOptions();
    }

	private void assertCreateDashboard()
	{
		JSONObject dashboard = new JSONObject();
		Assert.assertNotNull(dashboardAPI.createDashboard("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", dashboard));
	}

	private void assertDeleteDashboard()
	{
		Response resp = dashboardAPI.deleteDashboard("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", BigInteger.valueOf(123L));
		Assert.assertNotNull(resp);
	}

	private void assertGetDashboardBase64ScreenShot()
	{
		Assert.assertNotNull(dashboardAPI.getDashboardScreenShot("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", BigInteger.valueOf(123L),
				"1.0", "test.png"));
	}

	private void assertGetUserOptions()
	{
		Assert.assertNotNull(dashboardAPI.getDashboardUserOptions("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101/options",
				BigInteger.valueOf(123L)));
	}

	private void assertQueryDashboardById()
	{
		Assert.assertNotNull(dashboardAPI.queryDashboardById("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", BigInteger.valueOf(123L)));
	}
	
	private void assertExportDashboard() throws JSONException
	{
		Assert.assertNotNull(dashboardAPI.exportDashboards("tenant01", "tenant01.emcsadmin", "https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101",
				new JSONArray("[\"DashboardName\"]")));
	}
	
	private void assertImportDashboardOverride() throws JSONException
	{
		Assert.assertNotNull(dashboardAPI.importDashboards("tenant01", "tenant01.emcsadmin", "https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101",true,
				new JSONArray("[{\"Dashboard\": [{\"name\": \"Import Export Sub Dashboard_2\", \"tiles\": [{ \"type\": \"DEFAULT\",\"row\": 0,\"WIDGET_UNIQUE_ID\": \"3201\"}],\"id\": \"255446032935128268636523999445694642173\"},{\"name\": \"TestPamelaDBD_1\",\"description\": \"testDBD\",\"enableDescription\": \"FALSE\",\"userOptions\": {\"userName\": \"emcsadmin\",\"dashboardId\": \"270540442558749074000543557139546793426\",\"autoRefreshInterval\": 300000},\"type\": \"SET\",\"subDashboards\": [{\"name\": \"Import Export Sub Dashboard_2\",\"tiles\": [{ \"type\": \"DEFAULT\",\"row\": 0,\"WIDGET_UNIQUE_ID\": \"3201\"}],\"id\": \"255446032935128268636523999445694642173\"}]}],\"Savedsearch\": [{\"creationDate\": \"2017-04-14T02:24:14.287Z\",\"lastModificationDate\": \"2017-04-14T02:24:14.287Z\",\"id\": \"271515282512072253341097402942789505632\"}]}]")));
	
	}

	private void assertQueryCombinedDashboardById()
	{
		Assert.assertNotNull(dashboardAPI.queryCombinedData("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", BigInteger.valueOf(123L), null));
	}
 
	private void assertQueryDashboards()
	{
		Assert.assertNotNull(dashboardAPI.queryDashboards("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", "query str", 10, 5,
				"name", null));
	}

	private void assertQuickUpdateDashboard() throws JSONException
	{
		Assert.assertNotNull(dashboardAPI.quickUpdateDashboard(
				"tenant01",
				"tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101",
				BigInteger.valueOf(123L),
				new JSONObject(
						"{\"name\":\"daniel\",\"description\":\"DN\",\"sharePublic\":false, \"enableDescription\": false, \"enableEntityFilter\": true, \"enableTimeRange\": true}")));
	}

	private void assertSaveUserOptions() throws JSONException
	{
		Assert.assertNotNull(dashboardAPI.saveUserOptions("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101/options", BigInteger
						.valueOf(1101L), new JSONObject(
						"{ \"dashboardId\": 1127, \"autoRefreshInterval\": 600000, \"extendedOptions\":\"2000\" }")));
	}

	private void assertUpdateDashboard() throws JSONException
	{
		Assert.assertNotNull(dashboardAPI.updateDashboard("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101", BigInteger.valueOf(123L),
				new JSONObject("{\"name\":\"daniel\",\"description\":\"DN\",\"sharePublic\":false}")));
	}

	private void assertUpdateUserOptions() throws JSONException
	{
		Assert.assertNotNull(dashboardAPI.updateUserOptions("tenant01", "tenant01.emcsadmin",
				"https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101/options", BigInteger
						.valueOf(1101L), new JSONObject(
						"{ \"dashboardId\": 1127, \"autoRefreshInterval\": 600000, \"extendedOptions\":\"2000\" }")));
	}

	@Test
	public void testQueyDashboardSetsBySubId(@Mocked final DependencyStatus anyDependencyStatus){
		new Expectations(){
			{
				DashboardManager.getInstance();
				result = mockedDashboardManager;
				anyDependencyStatus.isDatabaseUp();
				result = true;
			}
		};
		dashboardAPI.queryDashboardSetsBySubId("", "", "", BigInteger.valueOf(1L));
	}

	@Test
	public void testDeleteDashboards(){
		new Expectations(){
			{
				DashboardManager.getInstance();
				result = mockedDashboardManager;
			}
		};
		dashboardAPI.deleteDashboards("tenandIdParam","userTenant","refer");
	}
	@Test
	public void testDeleteDashboards(@Mocked final DashboardManager dashboardManager,
									 @Mocked final DashboardException dashboardException) throws DashboardException {
		new Expectations(){
			{
				DashboardManager.getInstance();
				result = dashboardManager;
				dashboardManager.deleteDashboards(anyLong);
				result = dashboardException;
			}
		};
		dashboardAPI.deleteDashboards("tenandIdParam","userTenant","refer");
	}
	@Test
	public void testDeleteDashboards(@Mocked final DashboardManager dashboardManager,
									 @Mocked final BasicServiceMalfunctionException dashboardException) throws DashboardException {
		new Expectations(){
			{
				DashboardManager.getInstance();
				result = dashboardManager;
				dashboardManager.deleteDashboards(anyLong);
				result = dashboardException;
			}
		};
		dashboardAPI.deleteDashboards("tenandIdParam","userTenant","refer");
	}

	@Test
	public void testAddWidgetToDashboard(@Mocked final Dashboard dashboard, @Mocked final DashboardServiceFacade dashboardServiceFacade, @Mocked final JsonUtil jsonUtil, @Mocked final RestClient restClient, @Mocked final RegistryLookupUtil.VersionedLink versionedLink, @Mocked final RegistryLookupUtil registryLookupUtil, @Mocked final DashboardManager dashboardManager, @Mocked final Link link,
										 @Mocked final BasicServiceMalfunctionException dashboardException) throws IOException {
		final SearchModel searchModel = new SearchModel();
		searchModel.setId(new BigInteger("1"));
		searchModel.setDescription("desc");
		searchModel.setOwner("owner");
		searchModel.setCreationDate("date");
		searchModel.setName("name");
		List<ParameterModel> list = new ArrayList<>();
		ParameterModel parameterModel= new ParameterModel();
		parameterModel.setName("name");
		parameterModel.setValue("value");
		parameterModel.setType("type");
		list.add(parameterModel);
		searchModel.setParameters(list);
		SearchModel.InnerCategory category = searchModel.new InnerCategory();
		category.setId("1L");
		searchModel.setCategory(category);
		new Expectations(){
			{
				registryLookupUtil.getServiceInternalLink("SavedSearch", "1.0+", "search", null);
				result = (Link)versionedLink;
				link.getHref();
				result ="link";
				restClient.getWithException(anyString, anyString, anyString);
				result = "resp";
				jsonUtil.fromJson(anyString, SearchModel.class);
				result = searchModel;
				RegistryLookupUtil.getServiceInternalLink("SavedSearch", "1.0+", "category", null);
				result = (Link)versionedLink;
				DashboardManager.getInstance();
				result = dashboardManager;
				Dashboard.valueOf((EmsDashboard) any,(Dashboard)any, anyBoolean,anyBoolean, anyBoolean);
				result = dashboard;


			}
		};
		BigInteger dashboardId = new BigInteger("1");
		BigInteger widgetId = new BigInteger("2");
		dashboardAPI.addNewWidgetToDashboard("tenandIdParam","userTenant","refer",dashboardId,widgetId);
	}
}
