/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest;

import com.sun.jersey.core.util.Base64;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.*;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.UpdateSystemDashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.DashboardSameNameException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.*;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CreateSystemDashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.DeleteSystemDashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard.EnableDescriptionState;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard.EnableEntityFilterState;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard.EnableTimeRangeState;
import oracle.sysman.emaas.platform.dashboards.core.model.PaginatedDashboards;
import oracle.sysman.emaas.platform.dashboards.core.model.Tile;
import oracle.sysman.emaas.platform.dashboards.core.model.UserOptions;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.util.*;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.webutils.ParallelThreadPool;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.*;
import oracle.sysman.emaas.platform.dashboards.ws.rest.ssfDatautil.SSFDataUtil;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.DashboardAPIUtil;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.PrivilegeChecker;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.*;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.cache.util.ScreenshotPathGenerator;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import oracle.sysman.emaas.platform.emcpdf.tenant.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.TenantSubscriptionInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author wenjzhu
 * @author guobaochen introduce API to query single dashboard by id, and update specified dashboard
 */
@Path("/v1/dashboards")
public class DashboardAPI extends APIBase
{
	private static final Logger LOGGER = LogManager.getLogger(DashboardAPI.class);

	public DashboardAPI()
	{
		super();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			JSONObject dashboard)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [POST] /v1/dashboards");
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [POST] /v1/dashboards: database is down");
				throw new DatabaseDependencyUnavailableException();
			}
			logkeyHeaders("createDashboard()", userTenant, tenantIdParam);
			Dashboard d = getJsonUtil().fromJson(dashboard.toString(), Dashboard.class);
			DashboardManager manager = DashboardManager.getInstance();
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			if (d.getIsSystem() != null && d.getIsSystem()) {
				throw new CreateSystemDashboardException();
			}
			d = manager.saveNewDashboard(d, tenantId);
			updateDashboardAllHref(d, tenantIdParam);
			return Response.status(Status.CREATED).entity(getJsonUtil().toJson(d)).build();
		}
		catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}
		catch (DashboardException e) {
			if(e instanceof DashboardSameNameException){
				LOGGER.warn("Dashboard with the same name exists already!");
			}else{
				LOGGER.error(e.getLocalizedMessage(), e);
			}
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			//e.printStackTrace();
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@PUT
	@Path("{id: [1-9][0-9]*}/addWidget/{widgetId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNewWidgetToDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") final String tenantIdParam,
											@HeaderParam(value = "X-REMOTE-USER") final String userTenant, @HeaderParam(value = "Referer") String referer,
											@PathParam("id") final BigInteger dashboardId, @PathParam("widgetId") final BigInteger widgetId){
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer,
				"Service call to [GET] /v1/dashboards/{}/addWidget/{}",dashboardId, widgetId);
		logkeyHeaders("addNewWidgetToDashboard()", userTenant, tenantIdParam);
		//check if widget is existed.
		long start = System.currentTimeMillis();
		RestClient rc = new RestClient();
		Link searchLink = RegistryLookupUtil.getServiceInternalLink("SavedSearch", "1.0+", "search", null);
		String searchResponse = null;
		String categoryResponse = null;
		String tenantName = null;
		String userName = null;
		SearchModel searchModel = null;
		CategoryModel categoryModel = null;
		try {
			initializeUserContext(tenantIdParam, userTenant);
			String searchHref = searchLink.getHref() + "/" + widgetId;
			tenantName = TenantContext.getCurrentTenant();
			userName = UserContext.getCurrentUser();
			//retrieve search data
			rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME, tenantName);
			rc.setHeader(RestClient.X_REMOTE_USER, tenantName+ "." +userName);
			searchResponse = rc.getWithException(searchHref, tenantName,((RegistryLookupUtil.VersionedLink) searchLink).getAuthToken());
			LOGGER.info("Retrieved from SSF API widget data is {}", searchResponse);
			LOGGER.info("It takes {}ms to retrieve saved search meta data from SavedSearch API", (System.currentTimeMillis()- start));
			JsonUtil ju = JsonUtil.buildNormalMapper();
			searchModel = ju.fromJson(searchResponse, SearchModel.class);
			if(searchResponse == null || searchModel == null){
				LOGGER.error("searchResponse or searchModel is empty or null!!");
				throw new WidgetNotExistedException();
			}
			//retrieve category data
			long start2 =System.currentTimeMillis();
			Link categoryLink = RegistryLookupUtil.getServiceInternalLink("SavedSearch", "1.0+", "category", null);
			LOGGER.info("Retrieving category information with id {}", searchModel.getCategory().getId());
			String categoryHref = categoryLink.getHref() + "/" + searchModel.getCategory().getId();
			categoryResponse = rc.getWithException(categoryHref, tenantName,((RegistryLookupUtil.VersionedLink) searchLink).getAuthToken());
			categoryModel = ju.fromJson(categoryResponse, CategoryModel.class);
			LOGGER.info("Retrieved from SSF API category data is {}", categoryResponse);
			LOGGER.info("It takes {}ms to retrieve category data from SavedSearch API", (System.currentTimeMillis()- start2));
			if(categoryResponse == null || categoryModel == null){
				LOGGER.error("categoryResponse or categoryModel is empty or null!!");
				throw new WidgetNotExistedException();
			}
		}catch(WidgetNotExistedException e){
			LOGGER.error(e);
			return buildErrorResponse(new ErrorEntity(e));
		}catch (Exception e) {
			LOGGER.error(e);
			return buildErrorResponse(new ErrorEntity(DashboardErrorConstants.WIDGET_NOT_EXISTED_EXCEPTION_CODE,"Specified Widget is not found!"));
		}
		//check dashboard is existed, if existed, put new widget into last position
		Long tenantId = null;
		Dashboard dbd = null;
		EmsDashboard ed = null;
		try {
			tenantId = getTenantId(tenantIdParam);
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			DashboardManager manager = DashboardManager.getInstance();
			ed = manager.getEmsDashboardById(dsf, dashboardId, tenantId, null);
//			dbd = Dashboard.valueOf(ed, dbd, true, true, true);
			LOGGER.info("Dashboard with id {} is existed!", ed);
		} catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e);
			return buildErrorResponse(new ErrorEntity(e));
		}catch (DashboardNotFoundException | CommonSecurityException | TenantWithoutSubscriptionException e) {
			LOGGER.error(e);
			return buildErrorResponse(new ErrorEntity(e));
		}catch (DashboardException e) {
			LOGGER.error(e);
			return buildErrorResponse(new ErrorEntity(e));
		}finally {
			clearUserContext();
		}
		//put the widget into the bottom of the dashboard.
		try {
			if(ed == null){
				throw new DashboardNotFoundException();
			}
			addNewTile(searchModel, categoryModel, ed);
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			dsf.mergeEmsDashboard(ed);
			LOGGER.info("new tile list size is {}",ed.getDashboardTileList().size());
			dbd = Dashboard.valueOf(ed, dbd, true, true, true);

		} catch (DashboardNotFoundException e) {
			LOGGER.error(e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		LOGGER.info("Add new Widget into dashboard api tooks {}ms", (System.currentTimeMillis()-start));
		return Response.ok(getJsonUtil().toJson(dbd)).build();

	}

	@DELETE
	@Path("{id: [1-9][0-9]*}")
	public Response deleteDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [DELETE] /v1/dashboards/{}", dashboardId);
		DashboardManager manager = DashboardManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [DELETE] /v1/dashboards/{}: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			logkeyHeaders("deleteDashboard()", userTenant, tenantIdParam);
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			Dashboard dsb = manager.getDashboardById(dashboardId, tenantId);
			if (dsb != null && dsb.getIsSystem() != null && dsb.getIsSystem()) {
				throw new DeleteSystemDashboardException();
			}
			manager.deleteDashboard(dashboardId, tenantId);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			//e.printStackTrace();
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@DELETE
	@Path("all")
	//@Path("tenant/{id: [1-9][0-9]*}")
	public Response deleteDashboards(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer)
			//@PathParam("id") Long tenantId)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [DELETE] /v1/dashboards/all");
		DashboardManager manager = DashboardManager.getInstance();
		try {
			logkeyHeaders("deleteDashboardsByTenant()", userTenant, tenantIdParam);
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			manager.deleteDashboards(tenantId);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		} catch (BasicServiceMalfunctionException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	//	@GET
	//	@Path("{id: [1-9][0-9]*}/screenshot")
	//	@Produces(MediaType.APPLICATION_JSON)
	//	public Response getDashboardBase64ScreenShot(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
	//			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
	//			@PathParam("id") Long dashboardId)
	//	{
	//		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/dashboards/{}/screenshot",
	//				dashboardId);
	//		try {
	//			logkeyHeaders("getDashboardBase64ScreenShot()", userTenant, tenantIdParam);
	//			DashboardManager manager = DashboardManager.getInstance();
	//			Long tenantId = getTenantId(tenantIdParam);
	//			initializeUserContext(tenantIdParam, userTenant);
	//			ScreenshotData ss = manager.getDashboardBase64ScreenShotById(dashboardId, tenantId);
	//			//String screenShotUrl = uriInfo.getBaseUri() + "v1/dashboards/" + dashboardId + "/screenshot";
	//			String externalBase = DashboardAPIUtil.getExternalDashboardAPIBase(tenantIdParam);
	//			String screenShotUrl = externalBase + (externalBase.endsWith("/") ? "" : "/") + dashboardId + "/screenshot";
	//			return Response.ok(getJsonUtil().toJson(new ScreenShotEntity(screenShotUrl, ss.getScreenshot()))).build();
	//		}
	//		catch (DashboardException e) {
	//			return buildErrorResponse(new ErrorEntity(e));
	//		}
	//		catch (BasicServiceMalfunctionException e) {
	//			//e.printStackTrace();
	//			LOGGER.error(e.getLocalizedMessage(), e);
	//			return buildErrorResponse(new ErrorEntity(e));
	//		}
	//		finally {
	//			clearUserContext();
	//		}
	//
	//	}

	@GET
	@Path("{id: [1-9][0-9]*}/screenshot/{serviceVersion}/images/{fileName}")
	@Produces("image/png")
	public Object getDashboardScreenShot(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId, @PathParam("serviceVersion") String serviceVersion,
			@PathParam("fileName") String fileName)
	{
		ICacheManager scm= CacheManagers.getInstance().build();
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer,
				"Service call to [GET] /v1/dashboards/{}/screenshot/{}/images/{}", dashboardId, serviceVersion, fileName);

		logkeyHeaders("getDashboardScreenShot()", userTenant, tenantIdParam);
		DashboardManager manager = DashboardManager.getInstance();
		Long tenantId = null;
		try {
			tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		Tenant cacheTenant = new Tenant(tenantId, TenantContext.getCurrentTenant());
		CacheControl cc = new CacheControl();
		cc.setMaxAge(2592000); //browser side keeps screenshot image in cache for 30 days
		//try to get from cache
		try {
			if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
				LOGGER.warn("Unexpected dashboard id to get screenshot from cache for tenant={}, dashboard id={}, fileName={}",
						cacheTenant, dashboardId, fileName);
			}
			if (StringUtil.isEmpty(fileName)) {
				LOGGER.warn("Unexpected empty screenshot file name for tenant={}, dashboard id={}", cacheTenant, dashboardId);
			}
			final ScreenshotElement se = (ScreenshotElement) scm.getCache(CacheConstants.CACHES_SCREENSHOT_CACHE).get(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(dashboardId)));
			if (se != null) {
				if (fileName.equals(se.getFileName())) {
					return Response.ok(new StreamingOutput() {
						/* (non-Javadoc)
						 * @see javax.ws.rs.core.StreamingOutput#write(java.io.OutputStream)
						 */
						@Override
						public void write(OutputStream os) throws IOException, WebApplicationException
						{
							os.write(se.getBuffer().getData());
//							se.getBuffer().writeTo(os);
							os.flush();
							os.close();
						}

					}).cacheControl(cc).build();
				}
				else { // invalid screenshot file name
					if (!ScreenshotPathGenerator.getInstance().validFileName(dashboardId, fileName, se.getFileName())) {
						LOGGER.error("The requested screenshot file name {} for tenant={}, dashboard id={} is not a valid name",
								fileName, TenantContext.getCurrentTenant(), dashboardId, se.getFileName());
						return Response.status(Status.NOT_FOUND).build();
					}
					LOGGER.debug("The request screenshot file name is not equal to the file name in cache, but it is valid. "
							+ "Try to query from database to see if screenshot is actually updated already");
				}
			}
		}
		catch (Exception e) {
			LOGGER.error("Exception when getting screenshot from cache. Continue to get from database", e);
		}
		//try to get from persist layer
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [GET] /v1/dashboards/{}/screenshot/{}/images/{}: database is down", dashboardId, serviceVersion, fileName);
				throw new DatabaseDependencyUnavailableException();
			}
			final ScreenshotData ss = manager.getDashboardBase64ScreenShotById(dashboardId, tenantId);
			if (ss == null || ss.getScreenshot() == null) {
				LOGGER.error("Does not retrieved base64 screenshot data");
				return Response.status(Status.NOT_FOUND).build();
			}
//			final ScreenshotElement se = scm.storeBase64ScreenshotToCache(cacheTenant, dashboardId, ss);
			String newFileName = ScreenshotPathGenerator.getInstance().generateFileName(dashboardId, ss.getCreationDate(), ss.getModificationDate());
			byte[] decoded = null;
			if (ss.getScreenshot().startsWith(DashboardManager.SCREENSHOT_BASE64_PNG_PREFIX)) {
				decoded = Base64.decode(ss.getScreenshot().substring(DashboardManager.SCREENSHOT_BASE64_PNG_PREFIX.length()));
			}
			else if (ss.getScreenshot().startsWith(DashboardManager.SCREENSHOT_BASE64_JPG_PREFIX)) {
				decoded = Base64.decode(ss.getScreenshot().substring(DashboardManager.SCREENSHOT_BASE64_JPG_PREFIX.length()));
			}
			else {
				LOGGER.debug("Failed to retrieve screenshot decoded bytes as the previs isn't supported");
				decoded=null;
			}
			Binary bin = new Binary(decoded);
			ScreenshotElement nse = new ScreenshotElement(newFileName, bin);
			scm.getCache(CacheConstants.CACHES_SCREENSHOT_CACHE).put(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(dashboardId)),nse);
			if (nse == null || nse.getBuffer() == null) {
				LOGGER.debug("Does not retrieved base64 screenshot data after store to cache. return 404 then");
				return Response.status(Status.NOT_FOUND).build();
			}
			if (!fileName.equals(nse.getFileName())) {
				LOGGER.error("The requested screenshot file name {} for tenant={}, dashboard id={} does not exist", fileName,
						TenantContext.getCurrentTenant(), dashboardId, nse.getFileName());
				return Response.status(Status.NOT_FOUND).build();
			}
			LOGGER.debug(
					"Retrieved screenshot data from persistence layer, and build response now. Data is {}" + ss.getScreenshot());
			return Response.ok(new StreamingOutput() {
				/* (non-Javadoc)
				 * @see javax.ws.rs.core.StreamingOutput#write(java.io.OutputStream)
				 */
				@Override
				public void write(OutputStream os) throws IOException, WebApplicationException
				{
					byte[] decoded = null;
					if (ss.getScreenshot().startsWith(DashboardManager.SCREENSHOT_BASE64_PNG_PREFIX)) {
						decoded = Base64.decode(ss.getScreenshot().substring(
								DashboardManager.SCREENSHOT_BASE64_PNG_PREFIX.length()));
					}
					else if (ss.getScreenshot().startsWith(DashboardManager.SCREENSHOT_BASE64_JPG_PREFIX)) {
						decoded = Base64.decode(ss.getScreenshot().substring(
								DashboardManager.SCREENSHOT_BASE64_JPG_PREFIX.length()));
					}
					else {
						LOGGER.debug("Failed to retrieve screenshot decoded bytes as the previs isn't supported");
						decoded = Base64.decode(DashboardManager.BLANK_SCREENSHOT
								.substring(DashboardManager.SCREENSHOT_BASE64_PNG_PREFIX.length()));
					}
					os.write(decoded);
					os.flush();
					os.close();
				}

			}).cacheControl(cc).build();
		}
		catch(DashboardNotFoundException e){
			LOGGER.warn("Specific dashboard not found for id {}",dashboardId);
			return Response.status(Status.NOT_FOUND).build();
		}
		catch(DatabaseDependencyUnavailableException e){
			LOGGER.error(e.getLocalizedMessage(), e);
			return Response.status(Status.NOT_FOUND).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@GET
	@Path("{id: [1-9][0-9]*}/options")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDashboardUserOptions(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/dashboards/{}/options", dashboardId);
		UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [GET] /v1/dashboards/{}/options: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			UserOptions options = userOptionsManager.getOptionsById(dashboardId, tenantId);
			if (options != null) {
				boolean validated = options.validateExtendedOptions();
				if (!validated) { // if extended options is invalid, we simply return an empty extended option so that UI display won't be break
					LOGGER.error("Extended option for dashboardID={} is {}, it's an invalid json string, so use empty extended option instead",
							dashboardId, options.getExtendedOptions());
					options.setExtendedOptions(null);
				}
			}
			return Response.ok(getJsonUtil().toJson(options)).build();
		}
		catch (UserOptionsNotFoundException e){
			LOGGER.warn("Specific User Option is not found for dashboard id {}", dashboardId);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (DashboardNotFoundException e){
			LOGGER.warn("Specific dashboard is not found for dashboard id {}", dashboardId);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@GET
	@Path("{id: [1-9][0-9]*}")
	//@Produces(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response queryDashboardById(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/dashboards/{}", dashboardId);
		DashboardManager dm = DashboardManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [GET] /v1/dashboards/{}: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			logkeyHeaders("queryDashboardById()", userTenant, tenantIdParam);
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			String userName = UserContext.getCurrentUser();
			Dashboard dbd = dm.getCombinedDashboardById(dashboardId, tenantId, userName);
			updateDashboardAllHref(dbd, tenantIdParam);
			return Response.ok(getJsonUtil().toJson(dbd)).build();
		}
		catch(DashboardNotFoundException e){
			//suppress error information in log file
			LOGGER.warn("Specific dashboard not found for id {}", dashboardId);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@GET
	@Path("{id: [1-9][0-9]*}/combinedData")
	@Produces(MediaType.TEXT_PLAIN)
	public Response queryCombinedData(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") final String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") final String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") final BigInteger dashboardId,@HeaderParam(value = "SESSION_EXP") final String sessionExpiryTime)
	{
		/*final long TIMEOUT=30000;
		Long begin=System.currentTimeMillis();
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/dashboards/{}", dashboardId);
		final DashboardManager dm = DashboardManager.getInstance();
		StringBuilder sb=new StringBuilder();
		ExecutorService pool = ParallelThreadPool.getThreadPool();

		Dashboard dbd =null;
		Future<Dashboard> futureDashboard=null;
		Future<String> futureUserInfo =null;
		Future<String> futureReg =null;
		Future<String> futureSubscried =null;
		Future<String> futureSubscried2 =null;*/
		try {
			final long TIMEOUT=30000;
			Long begin=System.currentTimeMillis();
			initializeUserContext(tenantIdParam, userTenant);
			final String curTenant = TenantContext.getCurrentTenant();
			final String curUser = UserContext.getCurrentUser();
			final Locale curUserLocale = UserContext.getLocale();
			infoInteractionLogAPIIncomingCall(curTenant, referer, "Service call to [GET] /v1/dashboards/{}/combinedData", dashboardId);
			final DashboardManager dm = DashboardManager.getInstance();
			StringBuilder sb=new StringBuilder();
			ExecutorService pool = ParallelThreadPool.getThreadPool();

			//retrieve user info
			List<String> userInfo = null;
			final Future<List<String>> futureUserInfo = pool.submit(new Callable<List<String>>() {
				@Override
				public List<String> call() throws Exception {
					try {
						long startUserRoles = System.currentTimeMillis();
						LOGGER.info("Parallel request user info...");
						List<String> userRoles = PrivilegeChecker.getUserRoles(curTenant, curUser);
						long endUserRoles = System.currentTimeMillis();
						LOGGER.info("Time to get user roles: {}ms, user roles are: {}", (endUserRoles - startUserRoles), userRoles);
						return userRoles;
					} catch (Exception e) {
						LOGGER.error("Error occurred when retrieving userInfo data using parallel request!");
						LOGGER.error(e);
						throw e;
					} finally {
						clearUserContext();
					}
				}
			});
			
			//retrieve user granted privileges
			String userGrants = null;
			final Future<String> futureUserGrants = pool.submit(new Callable<String>() {
				@Override
				public String call() throws Exception {
					try {
						long startUserGrants = System.currentTimeMillis();
						LOGGER.info("Parallel request to get user grants...");
						String userGrants = PrivilegeChecker.getUserGrants(curTenant, curUser);
						long endUserGrants = System.currentTimeMillis();
						LOGGER.info("Time to get user grants: {}ms, user grants are: {}", (endUserGrants - startUserGrants), userGrants);
						return userGrants;
					} catch (Exception e) {
						LOGGER.error("Error occurred when retrieving user granted privileges using parallel request!");
						LOGGER.error(e);
						throw e;
					} finally {
						clearUserContext();
					}
				}
			});

			//retrieve subscribedapps API and subscribedapps2 API info
			List<String> subscribedApps = null;
			String subscribedApps2=null;
			final TenantSubscriptionInfo tenantSubscriptionInfo = new TenantSubscriptionInfo();
			final Future<List<String>> futureSubscried = pool.submit(new Callable<List<String>>() {
				@Override
				public List<String> call() throws Exception {
					try {
						long startApps = System.currentTimeMillis();
						LOGGER.info("Parallel request subscribed apps info...");
						List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(curTenant, tenantSubscriptionInfo);
						long endApps = System.currentTimeMillis();
						LOGGER.info("Time to get subscribed apps: {}ms, subscribed apps are: {}", (endApps - startApps), apps);
						return apps;
					} catch (Exception e) {
						LOGGER.error("Error occurred when retrieving subscribed data using parallel request!");
						LOGGER.error(e);
						throw e;

					} finally {
						clearUserContext();
					}
				}
			});

			Future<Dashboard> futureDashboard = null;
			try {
				if (!DependencyStatus.getInstance().isDatabaseUp()) {
					LOGGER.error("Error to call [GET] /v1/dashboards/{}/combinedData: database is down", dashboardId);
					//throw new DatabaseDependencyUnavailableException(); // incase db is down, just log error and don't query dashboard data then
				} else {
					if (futureSubscried != null) {
						subscribedApps = futureSubscried.get(TIMEOUT, TimeUnit.MILLISECONDS); // retrieve dashboard data after subscribed app thread is done
						if (subscribedApps != null) {
							final List<String> fSubscribedApps = subscribedApps;
							logkeyHeaders("combinedData()", userTenant, curTenant);

							futureDashboard = pool.submit(new Callable<Dashboard>() {
								@Override
								public Dashboard call() throws Exception {
									try {
										long startDash = System.currentTimeMillis();
										LOGGER.info("2nd round parallel thread to request dashboard data info after thread for subscribed apps thread is completed...");
										Long tenantId = getTenantId(curTenant);
										initializeUserContext(curTenant, userTenant, curUserLocale);
										String userName = UserContext.getCurrentUser();
										// put through subscribed apps to avoid to lookup this data again
										Dashboard dashboard = dm.getCombinedDashboardById(dashboardId, tenantId, userName, fSubscribedApps);
										long endDash = System.currentTimeMillis();
										LOGGER.info("Time to retrieve dashboard meta data: {}ms, dashboard meta data are: {}", (endDash - startDash), dashboard);
										return dashboard;
									} catch (Exception e) {
										LOGGER.error("Error occurred when retrieving dashboard meta data using parallel request!");
										LOGGER.error(e);
										throw e;
									} finally {
										clearUserContext();
									}
								}
							});
						} else {
							LOGGER.warn("Failed to get subscribed app data, won't continue to start dashboard data thread");
						}
					}
				}
		} catch (InterruptedException e) {
			LOGGER.error(e);
		} catch (ExecutionException e) {
				LOGGER.error(e);
		} catch (TimeoutException e) {
			//if timeout, and the task is still running, attempt to stop the task
			futureSubscried.cancel(true);
			LOGGER.error(e);
		}

			Future<String> futureReg = null;
			//retrieve registration info
			String regEntity = null;
			try {
				if (futureUserInfo != null) {
					final List<String> fUserInfo = userInfo = futureUserInfo.get(TIMEOUT, TimeUnit.MILLISECONDS); // retrieve reg info after user info thread is done
					if (userInfo != null) {
						futureReg = pool.submit(new Callable<String>() {
							@Override
							public String call() throws Exception {
								try {
									long startRegInfo = System.currentTimeMillis();
									LOGGER.info("2 round parallel to request registry info after thread for user info thread is completed...");
									initializeUserContext(curTenant, userTenant);
									String reg = JsonUtil.buildNonNullMapper().toJson(new RegistrationEntity(sessionExpiryTime, fUserInfo));
									long endRegInfo = System.currentTimeMillis();
									LOGGER.info("Time to get registration info: {}ms, registration info are: {}", (endRegInfo - startRegInfo), reg);
									return reg;
								} catch (Exception e) {
									LOGGER.error("Error occurred when retrieving registration data using parallel request!");
									LOGGER.error(e);
									throw e;
								} finally {
									clearUserContext();
								}
							}
						});
					} else {
						LOGGER.warn("Failed to get futureUserInfo, won't continue to start registry info thread");
					}
				}
			}catch (InterruptedException e) {
			LOGGER.error(e);
		} catch (ExecutionException e) {
			LOGGER.error(e.getCause() == null? e : e.getCause());
		}catch(TimeoutException e){
			//if timeout, and the task is still running, attempt to stop the task
			futureUserInfo.cancel(true);
			LOGGER.error(e);
		}

			//get reg data
			try {
				if (futureReg != null) {
					regEntity = futureReg.get(TIMEOUT, TimeUnit.MILLISECONDS);
					if (regEntity != null && !StringUtils.isEmpty(regEntity)) {
						sb.append("window._registrationServerCache=");
						sb.append(regEntity).append(";");
					}
					LOGGER.debug("Registration data is " + regEntity);
				}
			}catch (InterruptedException e) {
			LOGGER.error(e);
		} catch (ExecutionException e) {
			LOGGER.error(e.getCause());
		}catch(TimeoutException e){
			//if timeout, and the task is still running, attempt to stop the task
			futureReg.cancel(true);
			LOGGER.error(e);
		}
			
			//get user grants
			try {
				if (futureUserGrants != null) {
					userGrants = futureUserGrants.get(TIMEOUT, TimeUnit.MILLISECONDS);
					LOGGER.debug("Retrieved user grants are: " + userGrants);
				}
			}catch (InterruptedException e) {
				LOGGER.error(e);
			} catch (ExecutionException e) {
				LOGGER.error(e.getCause());
			}catch(TimeoutException e){
				//if timeout, and the task is still running, attempt to stop the task
				futureUserGrants.cancel(true);
				LOGGER.error(e);
			}

			sb.append("if(!window._uifwk){window._uifwk={};}if(!window._uifwk.cachedData){window._uifwk.cachedData={};}");
			if (userInfo != null || userGrants != null) {
				String userInfoRes = JsonUtil.buildNormalMapper().toJson(new UserInfoEntity(userInfo, userGrants));
				sb.append("window._uifwk.cachedData.userInfo=");
				sb.append(userInfoRes).append(";");
			}
			LOGGER.debug("User roles are: " + userInfo + ". User grants are: " + userGrants);

			String apps = TenantSubscriptionUtil.getTenantSubscribedServicesString(curTenant);
			if (!StringUtils.isEmpty(apps)) {
				sb.append("window._uifwk.cachedData.subscribedapps=");
				sb.append(apps).append(";");
			}
			LOGGER.debug("Subscribed applications data is " + apps);

			//get subscribedapps2 data
			subscribedApps2 = tenantSubscriptionInfo.toJson(tenantSubscriptionInfo);
			if(!StringUtils.isEmpty(subscribedApps2)){
				sb.append("window._uifwk.cachedData.subscribedapps2=");
				sb.append(subscribedApps2).append(";");
			}
			LOGGER.info("Subscribed applications data2 is " + subscribedApps2);

			//get meta dashboard data
			Dashboard dbd = null;
			try {
				if (futureDashboard != null) {
					dbd = futureDashboard.get(TIMEOUT, TimeUnit.MILLISECONDS);
					if (dbd != null) {
						sb.append("window._dashboardServerCache=");
						sb.append(getJsonUtil().toJson(dbd)).append(";");
					}
					LOGGER.debug("Dashboard data is " + getJsonUtil().toJson(dbd));
					updateDashboardAllHref(dbd, curTenant);
				}
			}catch (ExecutionException e) {
			LOGGER.error(e.getCause() == null? e : e.getCause());
		}catch (InterruptedException e) {
			LOGGER.error(e);
		}catch(TimeoutException e){
			//if timeout, and the task is still running, attempt to stop the task
			futureDashboard.cancel(true);
			LOGGER.error(e);
		}

			LOGGER.info("Retrieving combined data cost {}ms", (System.currentTimeMillis() - begin));
			return Response.ok(sb.toString()).build();

		} catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		} finally {
			clearUserContext();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryDashboards(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@QueryParam("queryString") String queryString, @DefaultValue("") @QueryParam("limit") Integer limit,
			@DefaultValue("0") @QueryParam("offset") Integer offset,
			@DefaultValue(DashboardConstants.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME) @QueryParam("orderBy") String orderBy,
			@QueryParam("filter") String filterString
	/*@QueryParam("types") String types, @QueryParam("appTypes") String appTypes, @QueryParam("owners") String owners,
	@QueryParam("onlyFavorites") Boolean onlyFavorites*/)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer,
				"Service call to [GET] /v1/dashboards?queryString={}&limit={}&offset={}&orderBy={}&filter={}", queryString,
				limit, offset, orderBy, filterString);
		logkeyHeaders("queryDashboards()", userTenant, tenantIdParam);
		String qs = null;
		try {
			//emcpdf-3012
			qs = queryString == null ? null : java.net.URLDecoder.decode(queryString.replaceAll("%", "\\%25"), "UTF-8").replace("%", "\\%");
		}
		catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}

		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [GET] /v1/dashboards?queryString={}&limit={}&offset={}&orderBy={}&filter={}: database is down", queryString, limit,
						offset, orderBy, filterString);
				throw new DatabaseDependencyUnavailableException();
			}
			DashboardManager manager = DashboardManager.getInstance();
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			DashboardsFilter filter = new DashboardsFilter();
			//filter.setIncludedAppsFromString(appTypes);
			//filter.setIncludedOwnersFromString(owners);
			//filter.setIncludedTypesFromString(types);
			//filter.setIncludedFavorites(onlyFavorites);
			filter.initializeFilters(filterString);
			PaginatedDashboards pd = manager.listDashboards(qs, offset, limit, tenantId, true, orderBy, filter);
			if (pd != null && pd.getDashboards() != null) {
				for (Dashboard d : pd.getDashboards()) {
					updateDashboardAllHref(d, tenantIdParam);
				}
			}
			return Response.ok(getJsonUtil().toJson(pd)).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@PUT
	@Path("{id: [1-9][0-9]*}/quickUpdate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response quickUpdateDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId, JSONObject inputJson)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [PUT] /v1/dashboards/{}/quickUpdate",
				dashboardId);
		logkeyHeaders("quickUpdateDashboard()", userTenant, tenantIdParam);
		String name = null;
		String description = null;
		String enableDescription = null;
		String enableEntityFilter = null;
		String enableTimeRange = null;
		Boolean share = null;
		try {
			if (inputJson.has("name")) {
				name = inputJson.getString("name");
			}
			if (inputJson.has("description")) {
				description = inputJson.getString("description");
			}
			if (inputJson.has("enableDescription")) {
				enableDescription = inputJson.getString("enableDescription");
			}
			if (inputJson.has("enableEntityFilter")) {
				enableEntityFilter = inputJson.getString("enableEntityFilter");
			}
			if (inputJson.has("enableTimeRange")) {
				enableTimeRange = inputJson.getString("enableTimeRange");
			}

			if (inputJson.has("sharePublic")) {
				share = inputJson.getBoolean("sharePublic");
			}
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(new IOException("Can't parse input parameters.", e));
			return buildErrorResponse(error);
		}

		DashboardManager dm = DashboardManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [PUT] /v1/dashboards/{}/quickUpdate: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			Dashboard input = dm.getDashboardById(dashboardId, tenantId);
			if (input.getIsSystem() != null && input.getIsSystem()) {
				throw new UpdateSystemDashboardException();
			}
			if (name != null) {
				input.setName(name);
			}
			if (description != null) {
				input.setDescription(description);
			}

			if (enableDescription != null) {
				input.setEnableDescription(EnableDescriptionState.fromName(enableDescription));
			}

			if (enableEntityFilter != null) {
				input.setEnableEntityFilter(EnableEntityFilterState.fromName(enableEntityFilter));
			}

			if (enableTimeRange != null) {
				input.setEnableTimeRange(EnableTimeRangeState.fromName(enableTimeRange));
			}

			if (share != null) {
				input.setSharePublic(share);
			}
			ScreenshotData screenShot = dm.getDashboardBase64ScreenShotById(dashboardId, tenantId);
			input.setScreenShot(screenShot.getScreenshot()); //set screen shot back otherwise it will be cleared
			Dashboard dbd = dm.updateDashboard(input, tenantId);
			updateDashboardAllHref(dbd, tenantIdParam);
			return Response.ok(getJsonUtil().toJson(dbd)).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@POST
	@Path("{id: [1-9][0-9]*}/options")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveUserOptions(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId, JSONObject inputJson)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [POST] /v1/dashboards/{}/options/",
				dashboardId);
		UserOptions userOption;
		try {
			userOption = getJsonUtil().fromJson(inputJson.toString(), UserOptions.class);
			boolean validated = userOption.validateExtendedOptions();
			if (!validated) {
				ErrorEntity error = new ErrorEntity(
						new CommonFunctionalException(
								MessageUtils.getDefaultBundleString(CommonFunctionalException.USER_OPTIONS_INVALID_EXTENDED_OPTIONS)));
				return buildErrorResponse(error);
			}
		}
		catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}

		UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [POST] /v1/dashboards/{}/options/: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			userOption.setDashboardId(dashboardId.toString());//override id in consumed json if exist;
			userOptionsManager.saveOrUpdateUserOptions(userOption, tenantId);
			return Response.ok(getJsonUtil().toJson(userOption)).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@PUT
	@Path("{id: [1-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId, JSONObject inputJson)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [PUT] /v1/dashboards/{}", dashboardId);
		logkeyHeaders("updateDashboard()", userTenant, tenantIdParam);
		Dashboard input = null;
		try {
			input = getJsonUtil().fromJson(inputJson.toString(), Dashboard.class);
		}
		catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}

		DashboardManager dm = DashboardManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [PUT] /v1/dashboards/{}: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			input.setDashboardId(dashboardId);
			if (input.getIsSystem() != null && input.getIsSystem()) {
				throw new UpdateSystemDashboardException();
			}
			Dashboard dbd = dm.updateDashboard(input, tenantId);
			updateDashboardAllHref(dbd, tenantIdParam);
			return Response.ok(getJsonUtil().toJson(dbd)).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@PUT
	@Path("{id: [1-9][0-9]*}/options")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUserOptions(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId, JSONObject inputJson)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [PUT] /v1/dashboards/{}/options/", dashboardId);
		UserOptions userOption;
		try {
			userOption = getJsonUtil().fromJson(inputJson.toString(), UserOptions.class);
			userOption.setDashboardId(dashboardId.toString());
			boolean validated = userOption.validateExtendedOptions();
			if (!validated) {
				ErrorEntity error = new ErrorEntity(
						new CommonFunctionalException(
								MessageUtils.getDefaultBundleString(CommonFunctionalException.USER_OPTIONS_INVALID_EXTENDED_OPTIONS)));
				return buildErrorResponse(error);
			}
		}
		catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}

		UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [PUT] /v1/dashboards/{}/options/: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			userOptionsManager.saveOrUpdateUserOptions(userOption, tenantId);
			return Response.ok(getJsonUtil().toJson(userOption)).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@GET
	@Path("{id: [1-9][0-9]*}/dashboardsets")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryDashboardSetsBySubId(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
									   @HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
									   @PathParam("id") BigInteger dashboardId)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/dashboards/{}/dashboardsets", dashboardId);
		DashboardManager dm = DashboardManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [GET] /v1/dashboards/{}/dashboardsets: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			logkeyHeaders("queryDashboardSetsBySubId()", userTenant, tenantIdParam);
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			Dashboard dbd = dm.getDashboardSetsBySubId(dashboardId, tenantId);
			return Response.ok(getJsonUtil().toJson(dbd)).build();
		}
		catch(DashboardNotFoundException e){
			LOGGER.warn("Specific dashboard not found for id {}",dashboardId);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		} catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		} finally {
			clearUserContext();
		}
	}

	
	@PUT
	@Path("/export")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response exportDashboards(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, 
			@HeaderParam(value = "Referer") String referer,
			JSONArray array){
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [PUT] /v1/dashboards/export");
		List<String> dbdNames = new ArrayList<String>();
		if (array != null && array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				try {
					dbdNames.add(array.getString(i));
				} catch (JSONException e) {
					LOGGER.error(e);
					return Response.status(Status.BAD_REQUEST).build();
				}
			}
		} else {
			LOGGER.error("Error to export dashboard as no input dashboard names");
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		DashboardManager dm = DashboardManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [PUT] /v1/dashboards/export: database is down");
				throw new DatabaseDependencyUnavailableException();
			}
			logkeyHeaders("export()", userTenant, tenantIdParam);
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			List<BigInteger> dbdIds = dm.getDashboardIdsByNames(dbdNames, tenantId);
			List<String> widgetIds = new ArrayList<String>();
			JSONArray finalArray = new JSONArray();
			for (int i = 0; i < dbdIds.size(); i++) {
				JSONArray dbdArray = new JSONArray();
				BigInteger id = dbdIds.get(i);
				Dashboard dbd = dm.getDashboardById(id, tenantId);
				List<Dashboard> subDbds = null;
				List<Tile> allTiles = new ArrayList<Tile>();
				if (dbd != null && dbd.getSubDashboards() != null && !dbd.getSubDashboards().isEmpty()) {
					subDbds = new ArrayList<Dashboard>();
					for (Dashboard subDbd : dbd.getSubDashboards()) {
						BigInteger subId = subDbd.getDashboardId();
						Dashboard completeSubDashboard = dm.getDashboardById(subId, tenantId);
						if (completeSubDashboard.getTileList() != null && !completeSubDashboard.getTileList().isEmpty()) {
							
							allTiles.addAll(completeSubDashboard.getTileList());
						}
						subDbds.add(completeSubDashboard);
					}
				}
				
				if (subDbds != null) {
					for (Dashboard d : subDbds) {
						updateDashboardAllHref(d, tenantIdParam);
						BigInteger dbdId = d.getDashboardId();
						ScreenshotData screenshotData = dm.getDashboardBase64ScreenShotById(dbdId, tenantId);
						String dbdJson = getJsonUtil().toJson(d);						
						JSONObject dbdJsonObjSub = new JSONObject(dbdJson);
						dbdJsonObjSub.put("screenShot", screenshotData.getScreenshot());
						dbdArray.put(dbdJsonObjSub);
					}
				}
				// for original dbd
				updateDashboardAllHref(dbd, tenantIdParam);
				if (dbd.getTileList() != null && !dbd.getTileList().isEmpty()) {
					allTiles.addAll(dbd.getTileList());
				}
				BigInteger originalDb = dbd.getDashboardId();
				ScreenshotData screenshotDataForOriginalDbd = dm.getDashboardBase64ScreenShotById(originalDb, tenantId);
				String dbdJson = getJsonUtil().toJson(dbd);
				JSONObject dbdJsonObj = new JSONObject(dbdJson);
				dbdJsonObj.put("screenShot", screenshotDataForOriginalDbd.getScreenshot());
				dbdArray.put(dbdJsonObj);
				
				JSONObject insideOjb = new JSONObject();
				insideOjb.put("Dashboard", dbdArray);
				
				//Savedsearch data
				if (allTiles != null && !allTiles.isEmpty()) {
					for (Tile tile : allTiles) {
						String widgetUniqueId = tile.getWidgetUniqueId();
						widgetIds.add(widgetUniqueId);
					}
				}
				
				JSONArray requestEntity = new JSONArray();
				if (widgetIds != null) {				
					for (String widgetId : widgetIds) {
						requestEntity.put(widgetId);
					}
				}
				JSONArray ssfObject = null;
				if (requestEntity.length() > 0) {
					String ssfDataResponse = SSFDataUtil.getSSFData(userTenant, requestEntity.toString());
					if (ssfDataResponse != null && ssfDataResponse.startsWith("[")) {
						ssfObject = new JSONArray(ssfDataResponse);
					} else {
						return Response.status(Status.BAD_REQUEST).entity("Could not get ssf data by widget unique ids").build();
					}
					
				}
				
				//Combine dbd json and savedsearch json
				if (ssfObject == null) {
					ssfObject = new JSONArray();
					
				} 
				insideOjb.put("Savedsearch", ssfObject);
				finalArray.put(insideOjb);
			}
		
			return Response.ok(finalArray.toString()).build();
		}
		catch(DashboardNotFoundException e){
			//suppress error information in log file
			LOGGER.warn("Specific dashboard not found");
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		} catch (JSONException e) {
			LOGGER.error(e.getLocalizedMessage(), e);		
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
		finally {
			clearUserContext();
		}
		
		return Response.status(Status.BAD_REQUEST).build();
	}


	@PUT
	@Path("/import")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response importDashboards(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant,@HeaderParam(value = "Referer") String referer,
			@QueryParam("override") boolean override,
			JSONArray jsonArray){
		
		infoInteractionLogAPIIncomingCall(tenantIdParam, null, "Service call to [PUT] /v1/dashboards/import");
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [PUT] /v1/dashboards: database is down");
				throw new DatabaseDependencyUnavailableException();
			}
			if (jsonArray == null) {
				return Response.status(Status.BAD_REQUEST).entity("Could not import any dashboard as the input data is null").build();
			}
			int length = jsonArray.length();
			JSONArray outputJson = new JSONArray();
			logkeyHeaders("importDashboard()", userTenant, tenantIdParam);
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			
			if (length > 0) {
				for (int i = 0; i < length; i ++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					if (!jsonObject.has("Dashboard")) {
						return Response.status(Status.BAD_REQUEST).entity("JsonObject[\"Dashboard\"] not found!").build();
					}
					JSONObject ssfIdMapObj = null;
					if (jsonObject.has("Savedsearch")) {
						JSONArray ssfArray = jsonObject.getJSONArray("Savedsearch");						
						if (ssfArray != null && ssfArray.length() > 0) {
							String ssfResponse = SSFDataUtil.saveSSFData(userTenant, ssfArray.toString(),override);
							if (ssfResponse != null && ssfResponse.startsWith("{")) {
								ssfIdMapObj = new JSONObject(ssfResponse);
								
							}
						}
					}
					
					JSONArray dbdArray = jsonObject.getJSONArray("Dashboard");
					if (dbdArray != null) {
						Map<BigInteger, BigInteger> idMap = new HashMap<BigInteger, BigInteger>();
						Map<String, String> nameMap = new HashMap<String, String>();
						 for (int j = 0; j < dbdArray.length(); j++) {
							// in dbd array, dbd is saved in order; Dashboard set will be saved at last
						    JSONObject dbdObj = dbdArray.getJSONObject(j);
						    
						    Dashboard d = getJsonUtil().fromJson(dbdObj.toString(), Dashboard.class);
						    if (d.getType().equals(Dashboard.DASHBOARD_TYPE_SET)) {
						    	if (d.getSubDashboards() != null) {
						    		for (Dashboard dashboard : d.getSubDashboards()) {
						    			if (dashboard.getTileList() != null) {
						    				for (Tile tile : dashboard.getTileList()) {
						    					String widgetId = tile.getWidgetUniqueId();
						    					if (ssfIdMapObj != null && ssfIdMapObj.has(widgetId)) {
						    						tile.setWidgetUniqueId(ssfIdMapObj.getString(widgetId));
						    					}
						    				}
						    			}
						    			if (idMap.containsKey(dashboard.getDashboardId())) {
						    				dashboard.setDashboardId(idMap.get(dashboard.getDashboardId()));
						    			}
						    			if (nameMap.containsKey(dashboard.getName())) {
						    				dashboard.setName(nameMap.get(dashboard.getName()));
						    			}
						    		}
						    	}
						    }
						    BigInteger originalId = d.getDashboardId();
						    String originalName = d.getName();
							DashboardManager manager = DashboardManager.getInstance();
							if (d.getTileList() != null) {
			    				for (Tile tile : d.getTileList()) {
			    					String widgetId = tile.getWidgetUniqueId();
			    					if (ssfIdMapObj != null && ssfIdMapObj.has(widgetId)) {
			    						tile.setWidgetUniqueId(ssfIdMapObj.getString(widgetId));
			    					}
			    				}
			    			}
							d = manager.saveForImportedDashboard(d, tenantId,override);
							BigInteger changedId = d.getDashboardId();
							String changedName = d.getName();
							updateDashboardAllHref(d, tenantIdParam);
							outputJson.put(new JSONObject(getJsonUtil().toJson(d)));
							idMap.put(originalId, changedId);
							nameMap.put(originalName, changedName);
						  }
					}
					
					
				}
			}
			
			return Response.status(Status.CREATED).entity(outputJson.toString()).build();
		}
		catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			//e.printStackTrace();
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		} catch (JSONException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
		finally {
			clearUserContext();
		}	
		return Response.status(Status.BAD_REQUEST).build();
	}

	
	private void logkeyHeaders(String api, String x_remote_user, String domain_name)
	{
		LOGGER.info("Headers of " + api + ": X-REMOTE-USER=" + x_remote_user + ", X-USER-IDENTITY-DOMAIN-NAME=" + domain_name);
	}

	/*
	 * Updates the specified dashboard by generating all href fields
	 */
	private Dashboard updateDashboardAllHref(Dashboard dbd, String tenantName)
	{
		updateDashboardHref(dbd, tenantName);
		updateDashboardScreenshotHref(dbd, tenantName);
		updateDashboardOptionsHref(dbd, tenantName);
		if (dbd != null && dbd.getType() != null && dbd.getType().equals(Dashboard.DASHBOARD_TYPE_SET)
				&& dbd.getSubDashboards() != null) {
			for (Dashboard subDbd : dbd.getSubDashboards()) {
				updateDashboardHref(subDbd, tenantName);
			}
		}
		return dbd;
	}

	private Dashboard updateDashboardOptionsHref(Dashboard dbd, String tenantName)
	{
		if (dbd == null) {
			return null;
		}
		String externalBase = DashboardAPIUtil.getExternalDashboardAPIBase(tenantName);
		if (StringUtil.isEmpty(externalBase)) {
			return null;
		}
		String optionsUrl = externalBase + (externalBase.endsWith("/") ? "" : "/") + dbd.getDashboardId() + "/options";
		dbd.setOptionsHref(optionsUrl);
		return dbd;
	}

	private Dashboard updateDashboardScreenshotHref(Dashboard dbd, String tenantName)
	{
		if (dbd == null) {
			return null;
		}
		//		String screenShotUrl = uriInfo.getBaseUri() + "v1/dashboards/" + dbd.getDashboardId() + "/screenshot";
		String externalBase = DashboardAPIUtil.getExternalDashboardAPIBase(tenantName);
		if (StringUtil.isEmpty(externalBase)) {
			return null;
		}
		String screenShotUrl = ScreenshotPathGenerator.getInstance().generateScreenshotUrl(externalBase, dbd.getDashboardId(),
				dbd.getCreationDate(), dbd.getLastModificationDate());
		LOGGER.debug("Generate screenshot URL is {} for dashboard id={}", screenShotUrl, dbd.getDashboardId());
		dbd.setScreenShotHref(screenShotUrl);
		return dbd;
	}

	private void addNewTile(SearchModel searchModel, CategoryModel categoryModel, EmsDashboard ed) {
		List<EmsDashboardTile> tileList = ed.getDashboardTileList();
		LOGGER.info("#1 origin tile list size is {}",tileList.size());
		LOGGER.info("#2 origin tile list size is {}",ed.getDashboardTileList().size());
		EmsDashboardTile newTile = new EmsDashboardTile();
		newTile.setTileId(IdGenerator.getTileId(ZDTContext.getRequestId(), 1));//confirm
		newTile.setTitle(searchModel.getName());
		newTile.setWidgetUniqueId(searchModel.getId().toString());
		newTile.setWidgetName(searchModel.getName());
		newTile.setWidgetDescription(searchModel.getDescription());
		newTile.setWidgetOwner(searchModel.getOwner());
		LOGGER.info("creation date is {}",searchModel.getCreationDate());
		newTile.setWidgetCreationTime(String.valueOf(searchModel.getCreationDate()));//format
		newTile.setOwner(searchModel.getOwner());
		newTile.setCreationDate(DateUtil.getGatewayTime());//format
		newTile.setDashboard(ed);
		newTile.setIsMaximized(0);// default is not maximized
		newTile.setPosition(0);//if dashboard contains no widget, set it to 0
		newTile.setWidgetDeleted(0);
		newTile.setDeleted(false);

		newTile.setWidgetSupportTimeControl(1);//TODO confirm
		if(searchModel.getParameters()!=null && !searchModel.getParameters().isEmpty()){
			for(ParameterModel p : searchModel.getParameters()){
				if("WIDGET_KOC_NAME".equals(p.getName())){
					newTile.setWidgetKocName(p.getValue());
				}
				if("WIDGET_VIEWMODEL".equals(p.getName())){
					newTile.setWidgetViewmode(p.getValue());
				}
				if("WIDGET_TEMPLATE".equals(p.getName())){
					newTile.setWidgetTemplate(p.getValue());
				}
				if("WIDGET_SOURCE".equals(p.getName())){
					newTile.setWidgetSource(Integer.valueOf(p.getValue()));
				}
				if("WIDGET_ICON".equals(p.getName())){
					newTile.setWidgetIcon(p.getValue());
				}
			}
		}
		//TODO WIDGET_SCREENSHOT_HREF

		newTile.setWidgetGroupName(categoryModel.getName());
		newTile.setProviderAssetRoot(categoryModel.getProviderAssetRoot());
		newTile.setProviderName(categoryModel.getProviderName());
		newTile.setProviderVersion(categoryModel.getProviderVersion());
		newTile.setColumn(0);//if dashboard contains no widget, set it to 0
		newTile.setRow(0);//if dashboard contains no widget, set it to 0
		newTile.setWidth(12);
		newTile.setHeight(2);
		newTile.setWidgetSource(1);
		newTile.setType(0); //DEFAULT
		//dashboard is empty
		if(tileList == null | tileList.isEmpty()){
			tileList = new ArrayList<>();
			tileList.add(newTile);
			ed.setDashboardTileList(tileList);
			LOGGER.info("Adding the only one tile into tile list..");
		}else{
			//calculate the widget position
			int row = calculateWidgetRowColumn(tileList).getRow();
			int position = calculateWidgetPosition(tileList);
			LOGGER.info("Calculated row number is {}", row);
			LOGGER.info("Calculated position number is {}", position);
			newTile.setRow(row);
			newTile.setPosition(position);
			tileList.add(newTile);
		}
		LOGGER.info("#1 After handling tile list size is {}", tileList.size());
		LOGGER.info("#2 After handling tile list size is {}", ed.getDashboardTileList().size());
	}

	/**
	 * calculate the column and row value(put new widget at the bottom)
	 * @param tileList
	 * @return
	 */
	private Tile calculateWidgetRowColumn(List<EmsDashboardTile> tileList){
		Tile t = new Tile();
		t.setRow(0);
//		t.setColumn(0);
		if(tileList == null || tileList.isEmpty()){
			LOGGER.warn("Tile list is null empty!");
			return t;
		}
		int maxRow = 0;
		int maxHeight = 0;
		for(EmsDashboardTile tile : tileList){
			if(tile.getRow()>maxRow){
				maxRow = tile.getRow();
			}
			if(tile.getHeight()>maxHeight){
				maxHeight = tile.getHeight();
			}
		}
		t.setRow(maxRow + maxHeight);// put new widget at the bottom
//		t.setColumn(col);
		return t;
	}

	/**
	 * calculate widget position
	 * @param tileList
	 * @return
	 */
	private int calculateWidgetPosition(List<EmsDashboardTile> tileList){
		int maxPosition = 0;
		if(tileList == null || tileList.isEmpty()){
			LOGGER.warn("Tile list is null empty!");
			return maxPosition;
		}
		for(EmsDashboardTile tile : tileList){
			if(tile.getPosition() > maxPosition){
				maxPosition = tile.getPosition();
			}
		}
		return maxPosition;
	}
	

}
