package oracle.sysman.emaas.platform.dashboards.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import oracle.sysman.emaas.platform.dashboards.core.exception.security.UpdateSystemDashboardException;
import oracle.sysman.emaas.platform.dashboards.core.util.*;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.ExecutionException;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.DefaultKeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.ScreenshotData;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import oracle.sysman.emaas.platform.emcpdf.tenant.SubscriptionAppsUtil;
import oracle.sysman.emaas.platform.emcpdf.tenant.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.AppsInfo;
import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.TenantSubscriptionInfo;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.DashboardSameNameException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.TenantWithoutSubscriptionException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.DashboardApplicationType;
import oracle.sysman.emaas.platform.dashboards.core.model.PaginatedDashboards;
import oracle.sysman.emaas.platform.dashboards.core.model.Tile;
import oracle.sysman.emaas.platform.dashboards.core.model.combined.CombinedDashboard;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil.VersionedLink;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;
import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptions;

public class DashboardManager
{
	private static final Logger LOGGER = LogManager.getLogger(DashboardManager.class);

	private static DashboardManager instance;

	static {
		instance = new DashboardManager();
	}
	
	private static final String HOME_PAGE_PREFERENCE_KEY = "Dashboards.homeDashboardId";
	private static final String DASHBOARD_OPTION_SELECTED_TAB_KEY = "selectedTab";


	public static final String BLANK_SCREENSHOT = DefaultScreenshotConstant.BLANK_SCREENSHOT;

	public static final String SCREENSHOT_BASE64_PNG_PREFIX = "data:image/png;base64,";
	public static final String SCREENSHOT_BASE64_JPG_PREFIX = "data:image/jpeg;base64,";
	public static final Long NON_TENANT_ID = -11L;

	/**
	 * Returns the singleton instance for dashboard manager
	 *
	 * @return
	 */
	public static DashboardManager getInstance()
	{
		return instance;
	}

	private DashboardManager()
	{
	}

	/**
	 * Adds a dashboard as favorite
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @throws DashboardNotFoundException
	 */
	public void addFavoriteDashboard(BigInteger dashboardId, Long tenantId) throws DashboardException
	{
		if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
			throw new DashboardNotFoundException();
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null || ed.getDeleted() != null && ed.getDeleted().compareTo(BigInteger.ZERO) > 0) {
				LOGGER.debug("Dashboard with id {} and tenant id {} is not found, or deleted already", dashboardId, tenantId);
				throw new DashboardNotFoundException();
			}
			if (!isDashboardAccessbyCurrentTenant(ed)) {// system dashboard
				LOGGER.debug(
						"Dashboard with id {} and tenant id {} is a system dashboard and cannot be accessed by current tenant",
						dashboardId, tenantId);
				throw new DashboardNotFoundException();
			}
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			EmsUserOptions edf = dsf.getEmsUserOptions(currentUser, dashboardId);
			if (edf == null) {
				edf = new EmsUserOptions();
				edf.setUserName(currentUser);
				edf.setDashboardId(dashboardId);
				edf.setIsFavorite(1);
				dsf.persistEmsUserOptions(edf);
			}
			else {
				edf.setIsFavorite(1);
				dsf.mergeEmsUserOptions(edf);
			}
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}
	
	public void refreshOobDashboardByAppType(Integer applicationType, Long tenantId, List<EmsDashboard> oobList) {
        DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
        List<BigInteger> ids = dsf.getDashboardIdsByAppType(applicationType);
        dsf.refreshOobDashboards(ids, oobList);
        EntityManager em = dsf.getEntityManager();
        if (em != null) {
            em.close();
        }
	}

	/**
	 * Delete a dashboard specified by dashboard id for given tenant.
	 *
	 * @param dashboardId
	 *            id for the dashboard
	 * @param permanent
	 *            delete permanently or not
	 * @throws DashboardException
	 */
	public void deleteDashboard(BigInteger dashboardId, boolean permanent, Long tenantId) throws DashboardException
	{
		if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
			return;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null) {
				throw new DashboardNotFoundException();
			}
			if (permanent == false && ed.getDeleted() != null && ed.getDeleted().compareTo(BigInteger.ZERO) > 0) {
				throw new DashboardNotFoundException();
			}
			if (!permanent && DataFormatUtils.integer2Boolean(ed.getIsSystem())) {
				throw new CommonSecurityException(
						MessageUtils.getDefaultBundleString(CommonSecurityException.NOT_SUPPORT_DELETE_SYSTEM_DASHBOARD_ERROR));
			}
			String currentUser = UserContext.getCurrentUser();
			// user can access owned or system dashboard
                 	if (!currentUser.equals(ed.getOwner()) && ed.getIsSystem() != 1) {
				throw new DashboardNotFoundException();
			}
			//			if (ed.getDeleted() == null || ed.getDeleted() == 0) {
			//				removeFavoriteDashboard(dashboardId, tenantId);
			//			}

			em.setProperty("soft.deletion.permanent", permanent);
			dsf.updateSubDashboardShowInHome(dashboardId);
			dsf.updateTileLinkedDashboard(dashboardId);

			//emcpdf2801 delete dashboard's user option
			LOGGER.info("Deleting user options for id "+dashboardId);
			dsf.removeAllEmsUserOptions(dashboardId);
			if (!permanent) {
				ed.setDeleted(dashboardId);
				dsf.mergeEmsDashboard(ed);
				dsf.removeEmsSubDashboardBySubId(dashboardId);
				dsf.removeEmsSubDashboardBySetId(dashboardId);
				
			}
			else {
				dsf.removeAllEmsUserOptions(dashboardId);
				dsf.removeEmsSubDashboardBySubId(dashboardId);
				dsf.removeEmsSubDashboardBySetId(dashboardId);
				dsf.removeEmsDashboard(ed);
			}
			dsf.removePreferenceByKey(currentUser, HOME_PAGE_PREFERENCE_KEY, tenantId);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}
	
	/**
	 * Delete dashboards by a given tenant. Soft deletion is supported
	 *
	 * @param tenantId
	 * @throws DashboardNotFoundException
	 */
	public void deleteDashboards(Long tenantId) throws DashboardException
	{
		deleteDashboards(false, tenantId);
	}
	
	public void deleteDashboards(boolean permanent, Long tenantId) 
	{
		if (tenantId == null || tenantId <= 0) {
			return;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			dsf.removeDashboardsByTenant(permanent, tenantId);
			dsf.removeDashboardSetsByTenant(permanent, tenantId);
			dsf.removeDashboardTilesByTenant(permanent, tenantId);
			dsf.removeDashboardTileParamsByTenant(permanent, tenantId);
			dsf.removeDashboardPreferenceByTenant(permanent, tenantId);
			dsf.removeUserOptionsByTenant(permanent, tenantId);			
		}
		finally {
			if (em != null) {
				em.close();
			}
		}		
	}
	
	/**
	 * 
	 * @param name
	 * @param tenantId
	 * @return
	 */
	public List<BigInteger> getDashboardIdsByNames(List<String> names, Long tenantId){
    	if (names == null || names.isEmpty()) {
    		LOGGER.debug("Dashboard not found for no input names");
    		return null;
    	}
    	EntityManager em = null;
    	try {
    		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
    		em = dsf.getEntityManager();
    		return dsf.getDashboardIdsByNames(names, tenantId);   		
    	} catch (NoResultException e) {
    		LOGGER.error(e.getLocalizedMessage(), e);
    	} finally {
    		if (em != null) {
    			em.close();
    		}
    	}
    	return null;
    }
	

	/**
	 * Delete a dashboard specified by dashboard id for given tenant. Soft deletion is supported
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @throws DashboardNotFoundException
	 */
	public void deleteDashboard(BigInteger dashboardId, Long tenantId) throws DashboardException
	{
		deleteDashboard(dashboardId, false, tenantId);
	}

	public ScreenshotData getDashboardBase64ScreenShotById(BigInteger dashboardId, Long tenantId) throws DashboardNotFoundException,TenantWithoutSubscriptionException
	{
		EntityManager em = null;
		try {
			if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
				throw new DashboardNotFoundException();
			}
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null) {
				throw new DashboardNotFoundException();
			}
			Boolean isDeleted = ed.getDeleted() == null ? null : ed.getDeleted().compareTo(BigInteger.ZERO) > 0;
			if (isDeleted != null && isDeleted.booleanValue()) {
				throw new DashboardNotFoundException();
			}
			String currentUser = UserContext.getCurrentUser();
			if (ed.getSharePublic().intValue() == 0) {
				if (!currentUser.equals(ed.getOwner()) && ed.getIsSystem() != 1) {
					throw new DashboardNotFoundException();
				}
			}
			if (!isDashboardAccessbyCurrentTenant(ed)) {
				throw new DashboardNotFoundException();
			}
			if (ed.getScreenShot() == null) {
				LOGGER.info("Retrieved null screenshot base64 data from persistence layer for dashboard id={}, we use a blank screenshot then", dashboardId);
				return new ScreenshotData(BLANK_SCREENSHOT, ed.getCreationDate(), ed.getLastModificationDate());
			}
			else if (!ed.getScreenShot().startsWith(SCREENSHOT_BASE64_PNG_PREFIX)
					&& !ed.getScreenShot().startsWith(SCREENSHOT_BASE64_JPG_PREFIX)) {
				LOGGER.error("Retrieved an invalid screenshot base64 data that is not started with specified prefix, we use a blank screenshot then");
				LOGGER.debug("Th screenshot string with an invalid base64 previs is: {}", ed.getScreenShot());
				return new ScreenshotData(BLANK_SCREENSHOT, ed.getCreationDate(), ed.getLastModificationDate());
			}
			return new ScreenshotData(ed.getScreenShot(), ed.getCreationDate(), ed.getLastModificationDate());
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}
	public EmsDashboard getEmsDashboardById(DashboardServiceFacade dsf, BigInteger dashboardId, Long tenantId) throws DashboardNotFoundException,TenantWithoutSubscriptionException {
		return getEmsDashboardById(dsf, dashboardId, tenantId, null);
	}

	public EmsDashboard getEmsDashboardById(DashboardServiceFacade dsf, BigInteger dashboardId, Long tenantId, List<String> subscribedApps) throws DashboardNotFoundException,TenantWithoutSubscriptionException {
        if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
			LOGGER.debug("Dashboard not found for id {} is invalid", dashboardId);
			throw new DashboardNotFoundException();
		}
		EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
		if (ed == null) {
			LOGGER.debug("Dashboard not found with the specified id {}", dashboardId);
			throw new DashboardNotFoundException();
		}
		Boolean isDeleted = ed.getDeleted() == null ? null : ed.getDeleted().compareTo(BigInteger.ZERO) > 0;
		if (isDeleted != null && isDeleted.booleanValue()) {
			LOGGER.debug("Dashboard with id {} is not found for it's deleted already", dashboardId);
			throw new DashboardNotFoundException();
		}
		String currentUser = UserContext.getCurrentUser();
		// user can access owned or system dashboard
		if (ed.getSharePublic().intValue() == 0) {
			if (!currentUser.equals(ed.getOwner()) && ed.getIsSystem() != 1) {
				LOGGER.debug(
						"Dashboard with id {} is not found for it's a non-OOB dashboard and not owned by current user {}",
						dashboardId, currentUser);
				throw new DashboardNotFoundException();
			}
		}
		if (!isDashboardAccessbyCurrentTenant(ed, subscribedApps)) {
			LOGGER.debug("Dashboard with id {} is not found for it can't be accessed by current tenant", dashboardId);
			throw new DashboardNotFoundException();
		}
		return ed;
	}

	/**
	 * Returns dashboard instance by specifying the id
	 *
	 * @param dashboardId
	 * @return
	 * @throws DashboardException
	 */
	public Dashboard getDashboardById(BigInteger dashboardId, Long tenantId) throws DashboardNotFoundException,TenantWithoutSubscriptionException
	{
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = getEmsDashboardById(dsf, dashboardId, tenantId);
			updateLastAccessDate(dashboardId, tenantId, dsf);
			return Dashboard.valueOf(ed, null, true, true, true);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public CombinedDashboard getCombinedDashboardById(BigInteger dashboardId, Long tenantId, String userName)
			throws DashboardNotFoundException,TenantWithoutSubscriptionException {
		return getCombinedDashboardById(dashboardId, tenantId, userName, null);
	}

	/**
	 * Returns combined dashboard instance by specifying the id
	 *
	 * @param dashboardId
	 * @return
	 * @throws DashboardException
	 * @throws JSONException 
	 */
	public CombinedDashboard getCombinedDashboardById(BigInteger dashboardId,
			Long tenantId, String userName, List<String> subscribedApps) throws DashboardNotFoundException,TenantWithoutSubscriptionException {
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = getEmsDashboardById(dsf, dashboardId, tenantId, subscribedApps);
			EmsPreference ep = dsf.getEmsPreference(userName,"Dashboards.homeDashboardId");
			EmsUserOptions euo = dsf.getEmsUserOptions(userName, dashboardId);
			List<EmsDashboardTile> edbdtList = ed.getDashboardTileList();
			CombinedDashboard cdSet = null;
			BigInteger selectedId = null; // used for selected tab dashbaord ID

			if (Dashboard.DASHBOARD_TYPE_CODE_SET.equals(ed.getType())) {
				// combine dashboard set
				cdSet = CombinedDashboard.valueOf(ed, ep, euo, null);

				// pick selected dashboard
				Object selected = null;
				try {
					JSONObject jsonObj = null;
					if (ed.getExtendedOptions() != null) {
						jsonObj = new JSONObject(ed.getExtendedOptions());
						if (jsonObj.has(DASHBOARD_OPTION_SELECTED_TAB_KEY)) {
							selected = jsonObj.get(DASHBOARD_OPTION_SELECTED_TAB_KEY);
							LOGGER.info("Retrieved selected tab from dashboard table for dashboard {} is {}", dashboardId, selected);
						}
					}
				}catch (JSONException e) {
					// failed to parse dashboard json, so failed to retrieve selected tab.
					// This is unexpected, but if it happens, likes just go ahead w/o selected tab then...
					LOGGER.error(e.getLocalizedMessage(), e);
				}
				try{
					JSONObject jsonObj = null;
					// get selectedTab from user options
					String extOptions = euo == null ? null : euo.getExtendedOptions();
					LOGGER.info(
							"Dashboard ID={} is a dashboard set, its extendedOptions from user option is {}, user is {}",
							dashboardId, extOptions, userName);
					if (extOptions != null) {
						jsonObj = new JSONObject(extOptions);
						if (jsonObj.has(DASHBOARD_OPTION_SELECTED_TAB_KEY)) {
							selected = jsonObj.get(DASHBOARD_OPTION_SELECTED_TAB_KEY);
							LOGGER.info("Retrieved selected tab from user option table for dashboard {} and user {} is {}", dashboardId, userName, selected);
						}
					}
				} catch (JSONException e) {
					// failed to parse extended options json, so failed to
					// retrieve selected tab.
					// This is unexpected, but if it happens, likes just go
					// ahead w/o selected tab then...
					LOGGER.error(e.getLocalizedMessage(), e);
				}

				if (selected != null) {
					try {
						selectedId = new BigInteger(selected.toString());
					} catch (NumberFormatException e) {
						// might be a null 'selectedTab' value or invalid one
						LOGGER.info(
								"Failed to get selected dashboard ID: ID is invalid: {}",
								selected);
						edbdtList = null;
					}
				} else {
					// use the 1st dashboard id
					if (cdSet.getSubDashboards() != null && !cdSet.getSubDashboards().isEmpty()) {
						selectedId = cdSet.getSubDashboards().get(0).getDashboardId();
						LOGGER.info(
								"Retrieved default (1st) tab for dashboard set {}, 1st dashboard id is {}",
								dashboardId, selected);
					}
				}

				if (selectedId != null) {
					try {
						ed = this.getEmsDashboardById(dsf, selectedId, tenantId);
						euo = dsf.getEmsUserOptions(userName, selectedId);
						ep = null;
						edbdtList = ed.getDashboardTileList();
					} catch (DashboardException e) {
						LOGGER.error(e);
						return cdSet;
					}
				}
			}

			// retrieve saved search list
			List<String> ssfIdList = new ArrayList<String>();
			if (edbdtList != null) {
				for (EmsDashboardTile edt : edbdtList) {
					ssfIdList.add(edt.getWidgetUniqueId());
				}
			}
			// we've ensured that the dashoard id and SSF id list are all for the dashboard OR selected tab dashboard and is correctly set
			String savedSearchResponse = retrieveSavedSeasrch(selectedId != null ? selectedId : dashboardId, ed.getIsSystem() == 1, ssfIdList);

			// combine single dashboard or selected dashbaord
			CombinedDashboard cd = CombinedDashboard.valueOf(ed, ep, euo,savedSearchResponse);

			// return combined dashboard Set
			if (cdSet != null) {
				cdSet.setSelected(cd);
				return cdSet;
			}

			// return combined single dashboard
			return cd;
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}
	

    private String retrieveSavedSeasrch(BigInteger dashboardId, boolean isOobDashboard, List<String> ssfIdList) {
		ICacheManager cm= CacheManagers.getInstance().build();
		String cachedData = null;
		Object cacheKey = null;
		long start = System.currentTimeMillis();
		if (dashboardId != null && isOobDashboard) {
			cacheKey = DefaultKeyGenerator.getInstance().generate(new Tenant("COMMON_TENANT_FOR_OOB_DASHBOARD_CACHE"),
					new Keys(CacheConstants.LOOKUP_CACHE_KEY_OOB_DASHBOARD_SAVEDSEARCH, dashboardId));
			try {
				cachedData = (String) cm.getCache(CacheConstants.CACHES_OOB_DASHBOARD_SAVEDSEARCH_CACHE).get(cacheKey);
				if (cachedData != null) {
					LOGGER.info(
							"retrieved OOB widget data for dashboard {} from cache: {}", dashboardId, cachedData);
					return cachedData;
				}
			} catch (ExecutionException e) { // if we see this cache issue, we just log and go ahead
				LOGGER.error(e);
			}
		}

        RestClient rc = new RestClient();
        Link tenantsLink = RegistryLookupUtil.getServiceInternalLink("SavedSearch", "1.0+", "search", null);
        String tenantHref = tenantsLink.getHref() + "/list";
        String tenantName = TenantContext.getCurrentTenant();
        String savedSearchResponse = null;
        try {
			rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME, tenantName);
        	savedSearchResponse = rc.put(tenantHref, ssfIdList.toString(), tenantName, 
        	        ((VersionedLink) tenantsLink).getAuthToken());
        }catch (Exception e) {
        	LOGGER.error(e);
        }
		if (!StringUtil.isEmpty(savedSearchResponse) && dashboardId != null && isOobDashboard) {
			cm.getCache(CacheConstants.CACHES_OOB_DASHBOARD_SAVEDSEARCH_CACHE).put(cacheKey,savedSearchResponse);
		}
		long end = System.currentTimeMillis();
		LOGGER.info("It takes {}ms to retrieve saved search meta data from Dashboard-API", (end - start));
        return savedSearchResponse;
    }

	/**
	 * Returns dashboard instance specified by name for current user Please note that same user under single tenant can't have
	 * more than one dashboards with same name, so this method return single dashboard instance
	 */
	public Dashboard getDashboardByName(String name, Long tenantId)
	{
		if (name == null || "".equals(name)) {
			LOGGER.debug("Dashboard not found for name \"{}\" is invalid", name);
			return null;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = dsf.getEmsDashboardByName(name);
			return Dashboard.valueOf(ed, null, true, true, true, true);
		}
		catch (NoResultException e) {
			LOGGER.debug("Dashboard not found for name \"{}\" because NoResultException is caught", name);
			LOGGER.info("context", e);
			return null;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public Dashboard getDashboardByNameAndDescriptionAndOwner(String name, String description, Long tenantId){
		if(StringUtil.isEmpty(name)){
			LOGGER.debug("Dashboard not found for name \"{}\" is invalid", name);
			return null;
		}
		String currentUser = UserContext.getCurrentUser();
		EntityManager entityManager = null;
		try{
			DashboardServiceFacade dashboardServiceFacade = new DashboardServiceFacade(tenantId);
			entityManager = dashboardServiceFacade.getEntityManager();
			EmsDashboard emsDashboard = dashboardServiceFacade.getEmsDashboardByNameAndDescriptionAndOwner(name, currentUser,description);
			return Dashboard.valueOf(emsDashboard);
		}catch (NoResultException e) {
			LOGGER.debug("Dashboard not found for name \"{}\" because NoResultException is caught", name);
			LOGGER.info("context", e);
			return null;
		}
		finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}

	public Dashboard getDashboardSetsBySubId(BigInteger dashboardId, Long tenantId) throws DashboardNotFoundException,TenantWithoutSubscriptionException
	{
		EntityManager em = null;
		try {
			if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
				LOGGER.debug("Dashboard not found for id {} is invalid", dashboardId);
				throw new DashboardNotFoundException();
			}
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null) {
				LOGGER.debug("Dashboard not found with the specified id {}", dashboardId);
				throw new DashboardNotFoundException();
			}
			Boolean isDeleted = ed.getDeleted() == null ? null : ed.getDeleted().compareTo(BigInteger.ZERO) > 0;
			if (isDeleted != null && isDeleted.booleanValue()) {
				LOGGER.debug("Dashboard with id {} is not found for it's deleted already", dashboardId);
				throw new DashboardNotFoundException();
			}
			String currentUser = UserContext.getCurrentUser();
			// user can access owned or system dashboard
			if (ed.getSharePublic().intValue() == 0) {
				if (!currentUser.equals(ed.getOwner()) && ed.getIsSystem() != 1) {
					LOGGER.debug(
							"Dashboard with id {} is not found for it's a non-OOB dashboard and not owned by current user {}",
							dashboardId, currentUser);
					throw new DashboardNotFoundException();
				}
			}
			if (!isDashboardAccessbyCurrentTenant(ed)) {
				LOGGER.debug("Dashboard with id {} is not found for it can't be accessed by current tenant", dashboardId);
				throw new DashboardNotFoundException();
			}

			updateLastAccessDate(dashboardId, tenantId, dsf);
			Dashboard dashboard = Dashboard.valueOf(ed, null, false, false, false);

			List<EmsDashboard> emsDashboards = dsf.getEmsDashboardsBySubId(dashboardId);
			if (null == emsDashboards) {
				LOGGER.debug("Dashboard not found with the specified sub dashboard id {}", dashboardId);
				throw new DashboardNotFoundException();
			}

			List<Dashboard> dashboardList = new ArrayList<>();
			for (EmsDashboard emsDashboard : emsDashboards) {
				dashboardList.add(Dashboard.valueOf(emsDashboard, null, false, false, false));
			}
			
			List<String> linkedDashboardList = dsf.getlinkedDashboards(dashboardId);		

			dashboard.setDashboardSets(dashboardList);
			dashboard.setLinkedDashboardList(linkedDashboardList);

			return dashboard;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Returns a list of all favorite dashboards for current user
	 *
	 * @param tenantId
	 * @return
	 */
	public List<Dashboard> getFavoriteDashboards(Long tenantId)
	{
		String currentUser = UserContext.getCurrentUser();
		//		String hql = "select d from EmsDashboard d join EmsDashboardFavorite f on d.dashboardId = f.dashboard.dashboardId and f.userName = '"
		//				+ currentUser + "' and d.deleted = ?1";
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			List<EmsDashboard> edList = dsf.getFavoriteEmsDashboards(currentUser);
			List<Dashboard> dbdList = new ArrayList<Dashboard>(edList.size());
			for (EmsDashboard ed : edList) {
				dbdList.add(Dashboard.valueOf(ed));
			}
			return dbdList;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Retrieves last access date for specified dashboard
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @return
	 */
	public Date getLastAccessDate(BigInteger dashboardId, Long tenantId)
	{
		if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
			LOGGER.debug("Last access for dashboard not found for dashboard id {} is invalid", dashboardId);
			return null;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null) {
				LOGGER.debug("Last access is not found for dashboard with id {} is not found", dashboardId);
				return null;
			}
			if (ed.getDeleted() != null && ed.getDeleted().compareTo(BigInteger.ZERO) > 0) {
				LOGGER.debug("Last access is not found for dashboard with id {} is deleted", dashboardId);
				return null;
			}
			String currentUser = UserContext.getCurrentUser();
			EmsUserOptions options = dsf.getEmsUserOptions(currentUser, dashboardId);
			if (options != null) {
				return options.getAccessDate();
			}
			return null;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Check if the dashboard with spacified id is favorite dashboard or not
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @return
	 * @throws DashboardException
	 */
	public boolean isDashboardFavorite(BigInteger dashboardId, Long tenantId) throws DashboardNotFoundException
	{
		if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
			throw new DashboardNotFoundException();
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			EmsUserOptions edf = dsf.getEmsUserOptions(currentUser, dashboardId);
			return edf != null && edf.getIsFavorite() != null && edf.getIsFavorite().intValue() > 0;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Returns all dashboards
	 *
	 * @param tenantId
	 * @return
	 */
	public List<Dashboard> listAllDashboards(Long tenantId)
	{
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		List<EmsDashboard> edList = dsf.getEmsDashboardFindAll();
		List<Dashboard> dbdList = new ArrayList<Dashboard>(edList.size());
		for (EmsDashboard ed : edList) {
			dbdList.add(Dashboard.valueOf(ed, null, false, false, false));
		}
		EntityManager em = null;
		try {
			em = dsf.getEntityManager();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return dbdList;
	}

	/**
	 * Returns dashboards for specified page with given row offset & page size
	 *
	 * @param offset
	 *            number to indicate row offset, started from 0
	 * @param pageSize
	 * @param tenantId
	 * @param ic
	 *            ignore case or not
	 * @return
	 * @throws DashboardException
	 */
	public PaginatedDashboards listDashboards(Integer offset, Integer pageSize, Long tenantId, boolean ic)
			throws DashboardException
	{
		return listDashboards(null, offset, pageSize, tenantId, ic);
	}

	/**
	 * Returns dashboards for specified query string, by providing page number and page size
	 *
	 * @param queryString
	 * @param offset
	 *            number to indicate row index, started from 0
	 * @param pageSize
	 * @param tenantId
	 * @param ic
	 *            ignore case or not
	 * @return
	 */
	public PaginatedDashboards listDashboards(String queryString, final Integer offset, Integer pageSize, Long tenantId,
			boolean ic) throws DashboardException
	{
		return listDashboards(queryString, offset, pageSize, tenantId, ic, null, null);
	}

	/**
	 * Returns dashboards for specified query string, by providing page number and page size
	 *
	 * @param queryString
	 * @param offset
	 *            number to indicate row index, started from 0
	 * @param pageSize
	 * @param tenantId
	 * @param ic
	 *            ignore case or not
	 * @return
	 */
	public PaginatedDashboards listDashboards(String queryString, final Integer offset, Integer pageSize, Long tenantId,
			boolean ic, String orderBy, DashboardsFilter filter) throws DashboardException
	{
		LOGGER.debug(
				"Listing dashboards with parameters: queryString={}, offset={}, pageSize={}, tenantId={}, ic={}, orderBy={}, filter={}",
				queryString, offset, pageSize, tenantId, ic, orderBy, filter);
		if (offset != null && offset < 0) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_QUERY_INVALID_OFFSET));
		}
		int firstResult = 0;
		if (offset != null) {
			firstResult = offset.intValue();
		}

		if (pageSize != null && pageSize <= 0) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_QUERY_INVALID_LIMIT));
		}
		int maxResults = DashboardConstants.DASHBOARD_QUERY_DEFAULT_LIMIT;
		if (pageSize != null) {
			maxResults = pageSize.intValue();
		}
		//v1==>true, v2/v3==>false
        TenantVersionModel tenantVersionModel = new TenantVersionModel(Boolean.FALSE);
		List<DashboardApplicationType> apps = getTenantApplications(tenantVersionModel);
        LOGGER.info("Tenant version info: Is it V1 tenant? {}",tenantVersionModel.getIsV1Tenant());
		// avoid impacts from bundle service, we get basic services only
		apps = DashboardApplicationType.getBasicServiceList(apps);
		if (apps == null || apps.isEmpty()) {
			throw new TenantWithoutSubscriptionException();
		}

		StringBuilder sb = null;
		int index = 1;
		String currentUser = UserContext.getCurrentUser();
		List<Object> paramList = new ArrayList<Object>();
		sb = new StringBuilder(" from Ems_Dashboard p  ");

		boolean joinOptions = false;
		if (getListDashboardsOrderBy(orderBy, false).toLowerCase().contains("access_date")) {
			joinOptions = true;
		}
		if (filter != null && filter.getIncludedFavorites() != null && filter.getIncludedFavorites().booleanValue() == true) {
			joinOptions = true;
		}
		if (joinOptions) {
			sb.append("left join Ems_Dashboard_User_Options le on (p.dashboard_Id =le.dashboard_Id and le.user_name = ?"
					+ index++ + " and le.tenant_Id = ?" + index++ + ") ");
			paramList.add(currentUser);
			paramList.add(tenantId);
		}

		sb.append("where 1=1 ");
		if (filter!= null && filter.getShowInHome()) {
			sb.append(" and p.show_inhome = 1 ");
		}

		StringBuilder sbApps = new StringBuilder();
		//this if branch is useless
		if (apps.isEmpty()) {
			sb.append(" and p.deleted = 0 and (p.tenant_Id = ?" + index++ + " or p.tenant_Id = ?" + index++ + ") and (p.share_public = 1 or p.owner = ?" + index++ + ") ");
			paramList.add(tenantId);
			paramList.add(NON_TENANT_ID);
			paramList.add(currentUser);
		}
		else {
			for (int i = 0; i < apps.size(); i++) {
				DashboardApplicationType app = apps.get(i);
				if (i != 0) {
					sbApps.append(",");
				}
				sbApps.append(String.valueOf(app.getValue()));
			}
			sb.append(" and p.deleted = 0 and (p.tenant_Id = ?" + index++ + " or p.tenant_Id = ?" + index++ + ") and ((p.type<>2 and (p.share_public = 1 or p.owner = ?"+index+++" or p.application_type in (" + sbApps.toString() + "))" );
			paramList.add(tenantId);
			paramList.add(NON_TENANT_ID);
			paramList.add(currentUser);
		}

		StringBuilder sb1 = new StringBuilder();
		if (filter != null) {
			concatIncludedFavoritesSQL(filter, sb);
			index = concatIncludedTypeInteger(filter, sb, index, paramList);
			index = concatIncludedOwners(filter, sb, index, paramList);
		}
		sb.append(" and ((p.is_system=0 ");
		if (filter != null) {
			if (filter.getIncludedWidgetGroupsString(tenantVersionModel) != null && !filter.getIncludedWidgetGroupsString(tenantVersionModel).isEmpty()) {
				sb.append(" and (p.dashboard_id in (select t.dashboard_Id from Ems_Dashboard_Tile t where (t.TENANT_ID = ?"+ index++ +" or t.TENANT_ID = ?" + index++ + ") and t.WIDGET_GROUP_NAME in ("
						+ filter.getIncludedWidgetGroupsString(tenantVersionModel) + " ))) ");
				paramList.add(tenantId);
				paramList.add(NON_TENANT_ID);
			}
		}
		sb.append(") or (p.is_system=1 ");
		if (filter != null) {
			concatIncludedApplicationTypes(filter, sb, tenantVersionModel);
		}
		sb.append("))");

		if (queryString != null && !"".equals(queryString)) {
			Locale locale = AppContext.getInstance().getLocale();
			index=concatQueryString(queryString, ic, sb, index, paramList, locale, tenantId);
		}
		sb.append(")");

		//dashboard Set begin
		sb1.append(" (p.type=2 ");
		if (filter != null) {
			concatIncludedFavoritesSQL(filter, sb1);
			index = concatIncludedTypeInteger(filter, sb1, index, paramList);
			index = concatIncludedOwners(filter, sb1, index, paramList);
		}
		sb1.append(" and ( (p.is_system=0 ");
		if (filter != null) {
			if (filter.getIncludedWidgetGroupsString(tenantVersionModel) != null && !filter.getIncludedWidgetGroupsString(tenantVersionModel).isEmpty()) {
				sb1.append(" and p.DASHBOARD_ID in (SELECT p2.DASHBOARD_SET_ID FROM EMS_DASHBOARD_SET p2 WHERE p2.SUB_DASHBOARD_ID IN (SELECT t.dashboard_Id FROM Ems_Dashboard_Tile t WHERE (t.TENANT_ID = ?"+ index++ +" or t.TENANT_ID = ?" + index++ + ") and t.WIDGET_GROUP_NAME IN ("
						+ filter.getIncludedWidgetGroupsString(tenantVersionModel)+ ")))");
				paramList.add(tenantId);
				paramList.add(NON_TENANT_ID);
			}
		}
		sb1.append(") or (p.is_system=1 ");
		if (filter != null) {
			concatIncludedApplicationTypes(filter, sb1,tenantVersionModel);
		}
		sb1.append("))");
		sb1.append(" and (p.share_public=1 or p.owner =?"+ index++ +"  or p.application_type  IN (" + sbApps.toString() + ")))");
		paramList.add(UserContext.getCurrentUser());
		if (queryString != null && !"".equals(queryString)) {
			Locale locale = AppContext.getInstance().getLocale();

			index=concatQueryString(queryString, ic, sb1, index, paramList, locale, tenantId);
		}
		sb1.append("))");
		if (sb1.length() > 0) {
			sb.append(" OR ( ");
			sb.append(sb1);
		}
		//query
		StringBuilder sbQuery = new StringBuilder(sb);
		//order by
		sbQuery.append(getListDashboardsOrderBy(orderBy, false));
		//			sbQuery.append(sb);
		sbQuery.insert(0,
				"select p.DASHBOARD_ID,p.DELETED,p.DESCRIPTION,p.SHOW_INHOME,p.ENABLE_TIME_RANGE,p.ENABLE_REFRESH,p.IS_SYSTEM,p.SHARE_PUBLIC,"
						+ "p.APPLICATION_TYPE,p.CREATION_DATE,p.LAST_MODIFICATION_DATE,p.NAME,p.OWNER,p.TENANT_ID,p.TYPE,p.APPLICATION_TYPE ");
		String jpqlQuery = sbQuery.toString();

		LOGGER.debug("Executing SQL is: " + jpqlQuery);
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);	
		EntityManager em = dsf.getEntityManager();
		try {
			Query listQuery = em.createNativeQuery(jpqlQuery, EmsDashboard.class);
			initializeQueryParams(listQuery, paramList);
			listQuery.setFirstResult(firstResult);
			listQuery.setMaxResults(maxResults);
			@SuppressWarnings("unchecked")
			List<EmsDashboard> edList = listQuery.getResultList();
			LOGGER.debug("result number is " + edList.size());
			List<Dashboard> dbdList = new ArrayList<Dashboard>(edList.size());

			if (edList != null && !edList.isEmpty()) {
				for (int i = 0; i < edList.size(); i++) {
					dbdList.add(Dashboard.valueOf(edList.get(i), null, false, false, false));
				}
			}

			StringBuilder sbCount = new StringBuilder(sb);
			sbCount.insert(0, "select count(*) ");
			String jpqlCount = sbCount.toString();
			LOGGER.debug(jpqlCount);
			Query countQuery = em.createNativeQuery(jpqlCount);
			initializeQueryParams(countQuery, paramList);
			Long totalResults = 0L;
			try{
				totalResults = ((BigDecimal) countQuery.getSingleResult()).longValue();
			}catch(NoResultException e){
				LOGGER.warn("get all dashboards count did not retrieve any data!");
			}
			LOGGER.debug("Total results is " + totalResults);
			PaginatedDashboards pd = new PaginatedDashboards(totalResults, firstResult, dbdList == null ? 0 : dbdList.size(),
				maxResults, dbdList);
			return pd;
			}
			finally {
				if (em != null) {
					em.close();
			}
		}
	}


	/**
	 * @param filter
	 * @param sb
	 * @param index
	 * @param paramList
	 * @return
	 */
	private int concatIncludedTypeInteger(DashboardsFilter filter, StringBuilder sb, int index, List<Object> paramList)
	{
		if (filter.getIncludedTypeIntegers() != null && !filter.getIncludedTypeIntegers().isEmpty()) {
			sb.append(" and ( ");
			for (int i = 0; i < filter.getIncludedTypeIntegers().size(); i++) {
				if (i != 0) {
					sb.append(" or ");
				}
				sb.append(" p.type = ?" + index++);
				paramList.add(filter.getIncludedTypeIntegers().get(i));
			}
			sb.append(" ) ");

		}
		return index;
	}

	/**
	 * Removes a dashboard from favorite list
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @throws DashboardNotFoundException
	 */
	public void removeFavoriteDashboard(BigInteger dashboardId, Long tenantId) throws DashboardNotFoundException
	{
		if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
			throw new DashboardNotFoundException();
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null || ed.getDeleted() != null && ed.getDeleted().compareTo(BigInteger.ZERO) > 0) {
				LOGGER.debug("Dashboard with id {} is not found for it does not exists or is deleted already", dashboardId);
				throw new DashboardNotFoundException();
			}
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			EmsUserOptions edf = dsf.getEmsUserOptions(currentUser, dashboardId);
			if (edf == null) {
				edf = new EmsUserOptions();
				edf.setUserName(currentUser);
				edf.setDashboardId(dashboardId);
				edf.setIsFavorite(0);
				dsf.persistEmsUserOptions(edf);
			}
			else {
				edf.setIsFavorite(0);
				dsf.mergeEmsUserOptions(edf);
			}
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}
	
	private String generateNewName(DashboardServiceFacade dsf,Long tenantId,String name) {
		String existingName = dsf.getDashboardNameWithMaxSuffixNumber(name, tenantId);
		String finalString  = null;
		if (existingName != null) {
			if (name.equals(existingName)) {
				finalString = name + "_1";
			} else {
				Pattern pattern = Pattern.compile("\\d+$");
				Matcher matcher = pattern.matcher(existingName);
				if (matcher.find()) {
					Integer num = new Integer(matcher.group());
					int increaseNum = num.intValue() + 1;
					if (existingName.endsWith("_"+num)) {
						int flag = existingName.lastIndexOf("_");
						String subName = existingName.substring(0, flag);
						if (subName.equals(name)) {
							finalString = subName + "_" + increaseNum; 
						} else {
							finalString = name + "_1";
						}						
					} else {
						finalString = name + "_1";
					}						
				} else {
					finalString = name + "_1";
				}
			}			
		}
		return finalString;
	}
	
	private Dashboard resetDateAndOwnerForDashboard(Dashboard dbd) {
		dbd.setCreationDate(null);
		dbd.setLastModifiedBy(null);
		dbd.setOwner(null);
		dbd.setIsSystem(false);
		dbd.setLastModificationDate(null);
		if (dbd.getTileList() != null) {
			for (Tile tile : dbd.getTileList()) {
				tile.setCreationDate(null);
				tile.setOwner(null);
				tile.setLastModifiedBy(null);
				tile.setLastModificationDate(null);
				tile.setTileId(null);
			}
		}
		return dbd;
	}
	
	public Dashboard saveForImportedDashboard(Dashboard dbd, Long tenantId, boolean overrided) throws DashboardException {		
		//reset creation date and owner
		resetDateAndOwnerForDashboard(dbd);
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			Dashboard sameName = getDashboardByNameAndDescriptionAndOwner(dbd.getName(), dbd.getDescription(), tenantId);
			if (sameName != null) {
				if (overrided) {
					// update existing row
					dbd.setDashboardId(sameName.getDashboardId());
					return updateDashboard(dbd,tenantId);
				} else {
					// regenerated id and name and then insert new row
					dbd.setDashboardId(null);
					dbd.setName(generateNewName(dsf, tenantId, sameName.getName()));					
					return saveNewDashboard(dbd, tenantId);
				}
			} else {
				// re-generate dashboard ID and then directly insert
				 dbd.setDashboardId(null);
				 return saveNewDashboard(dbd, tenantId);
			}
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}
	
	

	/**
	 * Save a newly created dashboard for given tenant
	 *
	 * @param dbd
	 * @param tenantId
	 * @return the dashboard saved
	 */
	public Dashboard saveNewDashboard(Dashboard dbd, Long tenantId) throws DashboardException
	{
		if (dbd == null) {
			LOGGER.debug("Dashboard is not saved: it's impossible to save null dashboard");
			return null;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			if (dbd.getDashboardId() != null) {
				EmsDashboard sameId = dsf.getEmsDashboardById(dbd.getDashboardId());
				if (sameId != null && sameId.getDeleted().compareTo(BigInteger.ZERO) <= 0) {
					throw new CommonFunctionalException(
							MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_CREATE_SAME_ID_ERROR));
				}
			}
			else {
				// initialize id
				dbd.setDashboardId(IdGenerator.getDashboardId(ZDTContext.getRequestId()));
			}

			//check dashboard name
			if (dbd.getName() == null || "".equals(dbd.getName().trim()) || dbd.getName().length() > 64) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_INVALID_NAME_ERROR));
			}
			LOGGER.debug("Get the dashboard with name: {}, desc: {}", dbd.getName(), dbd.getDescription());
			Dashboard sameName = getDashboardByNameAndDescriptionAndOwner(dbd.getName(), dbd.getDescription(), tenantId);
			if (sameName != null && !sameName.getDashboardId().equals(dbd.getDashboardId())) {
				throw new DashboardSameNameException();
			}
			// init creation date, owner to prevent null insertion
			Date created = DateUtil.getGatewayTime();
			if (dbd.getCreationDate() == null) {
				dbd.setCreationDate(created);
			}
			if (dbd.getOwner() == null) {
				dbd.setOwner(currentUser);
			}
			if (dbd.getType().equals(Dashboard.DASHBOARD_TYPE_SET)) {
				//				dbd.setEnableTimeRange(null);
			}
			else {
				if (dbd.getTileList() != null && !dbd.getTileList().isEmpty()) {
					List<Tile> tiles = dbd.getTileList();
					for (int i = 0; i < tiles.size(); i++) {
						Tile tile = tiles.get(i);
						if(tile.getTileId() == null) {
						    tile.setTileId(IdGenerator.getTileId(ZDTContext.getRequestId(), i));
						}
						LOGGER.info("tile id = " + tile.getTileId());
						LOGGER.info("tenant id = " + tenantId);
						if (tile.getCreationDate() == null) {
							tile.setCreationDate(created);
						}
						if (tile.getOwner() == null) {
							tile.setOwner(currentUser);
						}
						if(tile.getWidgetDeleted()==null) {
							tile.setWidgetDeleted(Boolean.FALSE);
						}
						if(tile.getLastModificationDate() == null) {
						    tile.setLastModificationDate(created);
						}
					}
				}
			}

			EmsDashboard ed = dbd.getPersistenceEntity(null);
			ed.setCreationDate(dbd.getCreationDate());
			ed.setOwner(dbd.getOwner());
			//EMCPDF-2288,if this dashboard is duplicated from other dashboard,copy its screenshot to new dashboard
			if(dbd.getDupDashboardId()!=null){
				LOGGER.debug("Duplicating screenshot from dashoard {} to new Dashboard..",dbd.getDupDashboardId());
				BigInteger dupId=dbd.getDupDashboardId();
				EmsDashboard emsd=dsf.getEmsDashboardById(dupId);
				ed.setScreenShot(emsd.getScreenShot());
				ed.setOwner(currentUser);
			}
			String dbdName = (dbd.getName() !=null? dbd.getName().replace("&amp;", "&"):dbd.getName());
			ed.setName(dbdName);
			String dbdDes = (dbd.getDescription() !=null? dbd.getDescription().replace("&amp;", "&"):dbd.getDescription());
			ed.setDescription(dbdDes);
			dsf.persistEmsDashboard(ed);
			updateLastAccessDate(ed.getDashboardId(), tenantId);
			return Dashboard.valueOf(ed, dbd, true, true, true);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Enables or disables the 'include time control' settings for specified dashboard
	 *
	 * @param dashboardId
	 * @param enable
	 * @param tenantId
	 */
	public void setDashboardIncludeTimeControl(BigInteger dashboardId, boolean enable, Long tenantId)
	{
		EntityManager em = null;
		try {
			if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
				return;
			}
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null) {
				return;
			}
			ed.setEnableTimeRange(DataFormatUtils.boolean2Integer(enable));
			dsf.mergeEmsDashboard(ed);
			em = dsf.getEntityManager();
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Update an existing dashboard for given tenant
	 *
	 * @param dbd
	 * @param tenantId
	 * @return the dashboard saved or updated
	 */
	public Dashboard updateDashboard(Dashboard dbd, Long tenantId) throws DashboardException
	{
		if (dbd == null) {
			LOGGER.debug("Dashboard is not updated: it's impossible to update null dashboard");
			return null;
		}
		EntityManager em = null;
		EmsDashboard ed = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			Dashboard sameName = getDashboardByNameAndDescriptionAndOwner(dbd.getName(), dbd.getDescription(), tenantId);
			if (sameName != null && !sameName.getDashboardId().equals(dbd.getDashboardId())) {
				throw new DashboardSameNameException();
			}
			// init creation date, owner to prevent null insertion
			Date created = DateUtil.getGatewayTime();			
			//			if (dbd.getCreationDate() == null) {
			//				dbd.setCreationDate(created);
			//			}
			if (dbd.getOwner() == null) {
				dbd.setOwner(currentUser);
			}
			if (dbd.getType().equals(Dashboard.DASHBOARD_TYPE_SET)) {
				// do nothing
			}
			else {
				if (dbd.getTileList() != null && !dbd.getTileList().isEmpty()) {
					List<Tile> tiles = dbd.getTileList();
					for (int i = 0; i < tiles.size(); i++) {
						Tile tile = tiles.get(i);
						if (tile.getTileId() == null) {
							tile.setTileId(IdGenerator.getTileId(ZDTContext.getRequestId(), i));
						}
						if (tile.getCreationDate() == null) {
							tile.setCreationDate(created);
						}
						if (tile.getOwner() == null) {
							tile.setOwner(currentUser);
						}
						if (tile.getWidgetDeleted() == null) {
							tile.setWidgetDeleted(Boolean.FALSE);
						}
						tile.setLastModificationDate(created);
					}
				}
			}

			ed = dsf.getEmsDashboardById(dbd.getDashboardId());
			if (ed == null) {
				throw new DashboardNotFoundException();
			}

			Boolean isDeleted = ed.getDeleted() == null ? null : ed.getDeleted().compareTo(BigInteger.ZERO) > 0;
			if (isDeleted != null && isDeleted.booleanValue()) {
				throw new DashboardNotFoundException();
			}

			if (DataFormatUtils.integer2Boolean(ed.getIsSystem())) {
				throw new UpdateSystemDashboardException();
			}
			if (!currentUser.equals(ed.getOwner())) {
				throw new CommonSecurityException(
						MessageUtils.getDefaultBundleString(CommonSecurityException.DASHBOARD_ACTION_REQUIRE_OWNER));
			}
			ed = dbd.getPersistenceEntity(ed);
			ed.setLastModificationDate(DateUtil.getCurrentUTCTime());
			ed.setLastModifiedBy(currentUser);
			if (dbd.getOwner() != null) {
				ed.setOwner(dbd.getOwner());
			}
			dsf.mergeEmsDashboard(ed);
			//EMCPDF-2567,if this dashboard is created in a set, copy its screenshot to its parent dashboard Set
			if(dbd.getDupDashboardId()!=null){
				EmsDashboard parentDashboardSet=dsf.getEmsDashboardById(dbd.getDupDashboardId());
				parentDashboardSet.setScreenShot(ed.getScreenShot());
				dsf.mergeEmsDashboard(parentDashboardSet);
			}
			return Dashboard.valueOf(ed, dbd, true, true, true);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * update name of tiles from specified widget for ALL dashboard of specified tenant<br>
	 * Note: currently we're using eclipse 2.4, so the returned value will always be 1
	 *
	 * @param tenantId
	 * @param widgetName
	 * @param widgetId
	 */
	public int updateDashboardTilesName(Long tenantId, String widgetName, BigInteger widgetId)
	{
		if (StringUtil.isEmpty(widgetName)) {
			LOGGER.debug("Dashboard names are not updated: null or empty widget name isn't expected");
			return 0;
		}
		if (widgetId == null || BigInteger.ZERO.compareTo(widgetId) > 0) {
			LOGGER.debug("Dashboard names are not updated: invalid widget ID is specified");
			return 0;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EntityTransaction et = em.getTransaction();
			Date gtwTime = DateUtil.getGatewayTime();
			String jql = "update EmsDashboardTile t set t.title = :widgetName, t.widgetName = :widgetName, t.lastModificationDate = :lastModificationDate where t.widgetUniqueId = :widgetId";
			Query query = em.createQuery(jql).setParameter("widgetName", widgetName)
					.setParameter("widgetId", String.valueOf(widgetId)).setParameter("lastModificationDate", gtwTime);
			if (!et.isActive()) {
				et.begin();
			}			
			int affacted = query.executeUpdate();
			et.commit();
			LOGGER.info("Update dashboard tiles name: title for {} tiles have been updated to \"{}\" for specified widget ID {}, APIGWTime is {}",
					affacted, widgetName, widgetId, gtwTime);
			return affacted;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Update tiles' 'widgetDeleted' by specifying widget ID for ALL dashboard of specified tenant<br>
	 * Note: currently we're using eclipse 2.4, so the returned value will always be 1
	 *
	 * @param tenantId
	 * @param widgetId
	 */
	public int updateWidgetDeleteForTilesByWidgetId(Long tenantId, BigInteger widgetId)
	{
		if (widgetId == null || BigInteger.ZERO.compareTo(widgetId) > 0) {
			LOGGER.debug("Dashboard tiles 'widgetDeleted' are not updated: invalid widget ID is specified");
			return 0;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EntityTransaction et = em.getTransaction();
			Date gtwTime = DateUtil.getGatewayTime();
			String jql = "update EmsDashboardTile t set t.widgetDeleted = 1, t.lastModificationDate = :lastModificationDate where t.widgetUniqueId = :widgetId";
			Query query = em.createQuery(jql).setParameter("widgetId", String.valueOf(widgetId)).setParameter("lastModificationDate", gtwTime);
			if (!et.isActive()) {
				et.begin();
			}
			int affacted = query.executeUpdate();
			et.commit();
			LOGGER.info(
					"Update dashboard tile 'widgetDeleted': {} tiles have been updated to widgetDeleted=true for specified widget ID {}, APIGWTime is {}",
					affacted, widgetId, gtwTime);
			return affacted;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Updates last access date for specified dashboard
	 *
	 * @param dashboardId
	 * @param tenantId
	 */
	public void updateLastAccessDate(BigInteger dashboardId, Long tenantId)
	{
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			updateLastAccessDate(dashboardId, tenantId, dsf);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	private void updateLastAccessDate(BigInteger dashboardId, Long tenantId, DashboardServiceFacade dsf)
	{
		if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
			LOGGER.debug("Last access date for dashboard is not updated: dashboard id with value {} is invalid", dashboardId);
			return;
		}
		//EntityManager em = null;
		EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
		if (ed == null || ed.getDeleted() != null && ed.getDeleted().compareTo(BigInteger.ZERO) > 0) {
			return;
		}
		//em = dsf.getEntityManager();
		String currentUser = UserContext.getCurrentUser();
		// TODO Shall we still save the last access date if it wan't accessed by a user?
		if(NON_TENANT_ID.equals(tenantId) || currentUser == null) {
		    return;
		}
		EmsUserOptions edla = dsf.getEmsUserOptions(currentUser, dashboardId);
		if (edla == null) {
			edla = new EmsUserOptions();
			edla.setUserName(currentUser);
			edla.setDashboardId(dashboardId);
			edla.setAccessDate(DateUtil.getCurrentUTCTime());
			dsf.persistEmsUserOptions(edla);
		}
		else {
			edla.setAccessDate(DateUtil.getCurrentUTCTime());
			dsf.mergeEmsUserOptions(edla);
		}
	}

	/**
	 * @param filter
	 * @param sb
	 */
	private void concatIncludedApplicationTypes(DashboardsFilter filter, StringBuilder sb, final TenantVersionModel tenantVersionModel)
	{
		if (filter.getIncludedApplicationTypes(tenantVersionModel) != null && !filter.getIncludedApplicationTypes(tenantVersionModel).isEmpty()) {
			sb.append(" and (");
			for (int i = 0; i < filter.getIncludedApplicationTypes(tenantVersionModel).size(); i++) {
				if (i != 0) {
					sb.append(" or ");
				}
				sb.append(" p.application_type = " + filter.getIncludedApplicationTypes(tenantVersionModel).get(i).getValue() + " ");
			}
			sb.append(")");
		}
	}

	/**
	 * @param filter
	 * @param sb
	 */
	private void concatIncludedFavoritesSQL(DashboardsFilter filter, StringBuilder sb)
	{
		if (filter.getIncludedFavorites() != null && filter.getIncludedFavorites().booleanValue() == true) {
			//sb.append(" and df.user_name is not null ");
			sb.append(" and le.is_favorite > 0 ");
		}
	}

	/**
	 * @param filter
	 * @param sb
	 * @param index
	 * @param paramList
	 * @return
	 */
	private int concatIncludedOwners(DashboardsFilter filter, StringBuilder sb, int index, List<Object> paramList)
	{
		if (filter.getIncludedOwners() != null && !filter.getIncludedOwners().isEmpty()) {
			sb.append(" and ( ");
			for (int i = 0; i < filter.getIncludedOwners().size(); i++) {
				if (i != 0) {
					sb.append(" or ");
				}
				if ("Oracle".equals(filter.getIncludedOwners().get(i))) {
					sb.append(" p.owner = ?" + index++);
					paramList.add("Oracle");
				}
				if ("Others".equals(filter.getIncludedOwners().get(i))) {
					sb.append(" p.owner != ?" + index++);
					paramList.add("Oracle");
				}
				if ("Me".equals(filter.getIncludedOwners().get(i))) {
					sb.append(" p.owner = ?" + index++);
					paramList.add(UserContext.getCurrentUser());
				}
				if ("Share".equals(filter.getIncludedOwners().get(i))) {
					sb.append(" p.owner != ?" + index++ + " and p.share_public > 0");
					paramList.add(UserContext.getCurrentUser());
				}
			}
			sb.append(" ) ");

		}
		return index;
	}

	/**
	 * @param queryString
	 * @param ic
	 * @param sb
	 * @param index
	 * @param paramList
	 * @param locale
	 */
	private int concatQueryString(String queryString, boolean ic, StringBuilder sb, int index, List<Object> paramList,
			Locale locale, Long tenantId)
	{
		if (!ic) {
			sb.append(" and (p.name LIKE ?" + index++ +" escape '\\' ");
			paramList.add("%" + StringEscapeUtils.escapeHtml4(queryString) + "%");
		}
		else {
			sb.append(" and (lower(p.name) LIKE ?" + index++ +" escape '\\' ");
			paramList.add("%" + StringEscapeUtils.escapeHtml4(queryString.toLowerCase(locale)) + "%");
		}

		if (!ic) {
			sb.append(" or p.description like ?" + index++ +" escape '\\' ");
			paramList.add("%" + StringEscapeUtils.escapeHtml4(queryString) + "%");
		}
		else {
			sb.append(" or lower(p.description) like ?" + index++ +" escape '\\' ");
			paramList.add("%" + StringEscapeUtils.escapeHtml4(queryString.toLowerCase(locale)) + "%");
		}

		if (!ic) {
			sb.append(" or p.owner like ?" + index++ +" escape '\\' ");
			paramList.add("%" + StringEscapeUtils.escapeHtml4(queryString) + "%");
		}
		else {
			sb.append(" or lower(p.owner) like ?" + index++ +" escape '\\' ");
			paramList.add("%" + StringEscapeUtils.escapeHtml4(queryString.toLowerCase(locale)) + "%");
		}

		if (!ic) {
			sb.append(" or p.dashboard_Id in (select t.dashboard_Id from Ems_Dashboard_Tile t where t.TENANT_ID = ?" + index++ + " and t.type <> 1 and t.title like ?"
					+ index++ + " escape '\\' " + " )) ");
			paramList.add(tenantId);
			paramList.add("%" + queryString + "%");
		}
		else {
			sb.append(" or p.dashboard_Id in (select t.dashboard_Id from Ems_Dashboard_Tile t where t.TENANT_ID = ?" + index++ + " and t.type <> 1 and lower(t.title) like ?"
					+ index++ + " escape '\\' " + " )) ");
			paramList.add(tenantId);
			paramList.add("%" + queryString.toLowerCase(locale) + "%");
		}
		return index;
	}

	private String getListDashboardsOrderBy(String orderBy, boolean isUnion)
	{
		if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_NAME.equals(orderBy)
				|| DashboardConstants.DASHBOARD_QUERY_ORDER_BY_NAME_ASC.equals(orderBy)) {
			return " order by nlssort(name,'NLS_SORT=GENERIC_M'), p.dashboard_Id DESC";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_NAME_DSC.equals(orderBy)) {
			return " order by nlssort(name,'NLS_SORT=GENERIC_M') DESC, p.dashboard_Id DESC";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME.equals(orderBy)
				|| DashboardConstants.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_DSC.equals(orderBy)) {
			return " order by CASE WHEN p.creation_Date IS NULL THEN 0 ELSE 1 END DESC, p.creation_Date DESC, p.dashboard_Id DESC";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_ASC.equals(orderBy)) {
			return " order by CASE WHEN p.creation_Date IS NULL THEN 0 ELSE 1 END, p.creation_Date, p.dashboard_Id";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME.equals(orderBy)
				|| DashboardConstants.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC.equals(orderBy)) {
			if (isUnion) {
				return " order by CASE WHEN p.access_Date IS NULL THEN 0 ELSE 1 END DESC, p.access_Date DESC, p.dashboard_Id DESC";
			}
			else {
				return " order by CASE WHEN le.access_Date IS NULL THEN 0 ELSE 1 END DESC, le.access_Date DESC, p.dashboard_Id DESC";
			}
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC.equals(orderBy)) {
			if (isUnion) {
				return " order by CASE WHEN p.access_Date IS NULL THEN 0 ELSE 1 END, p.access_Date, p.dashboard_Id";
			}
			else {
				return " order by CASE WHEN le.access_Date IS NULL THEN 0 ELSE 1 END, le.access_Date, p.dashboard_Id";
			}
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID.equals(orderBy)
				|| DashboardConstants.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_DSC.equals(orderBy)) {
			return " order by CASE WHEN p.last_modification_Date IS NULL THEN p.creation_Date ELSE p.last_modification_Date END DESC, p.dashboard_Id DESC";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_ASC.equals(orderBy)) {
			return " order by CASE WHEN p.last_modification_Date IS NULL THEN p.creation_Date ELSE p.last_modification_Date END, p.dashboard_Id";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_OWNER.equals(orderBy)
				|| DashboardConstants.DASHBOARD_QUERY_ORDER_BY_OWNER_ASC.equals(orderBy)) {
			return " order by lower(p.owner), p.owner, lower(p.name), p.name, p.dashboard_Id DESC";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_OWNER_DSC.equals(orderBy)) {
			return " order by lower(p.owner) DESC, p.owner DESC, lower(p.name), p.name, p.dashboard_Id DESC";
		}
		else {
			//default order by
			if (isUnion) {
				return " order by p.application_Type, p.type DESC, lower(p.name), p.name, CASE WHEN p.access_Date IS NULL THEN 0 ELSE 1 END DESC, p.access_Date DESC";
			}
			else {
				return " order by p.application_Type, p.type DESC, lower(p.name), p.name, CASE WHEN le.access_Date IS NULL THEN 0 ELSE 1 END DESC, le.access_Date DESC";
			}
		}
	}

	private List<DashboardApplicationType> getTenantApplications(TenantVersionModel tv)
	{
		return getTenantApplications(null,tv);
	}

	private List<DashboardApplicationType> getTenantApplications(List<String> subscribedApps,  TenantVersionModel tv)
	{
		String opcTenantId = TenantContext.getCurrentTenant();
		if (opcTenantId == null || "".equals(opcTenantId)) {
			LOGGER.warn("When trying to retrieve subscribed application, it's found the tenant context is not set (TenantContext.getCurrentTenant() == null)");
			return Collections.emptyList();
		}
		TenantSubscriptionInfo tenantSubscriptionInfo = new TenantSubscriptionInfo();
		List<String> appNames =subscribedApps != null ? subscribedApps : TenantSubscriptionUtil.getTenantSubscribedServices(opcTenantId, tenantSubscriptionInfo);
		tv = checkTenantVersion(subscribedApps, tenantSubscriptionInfo,tv);
		if (appNames == null || appNames.isEmpty()) {
			return Collections.emptyList();
		}
		List<DashboardApplicationType> apps = new ArrayList<DashboardApplicationType>();
		for (String appName : appNames) {
			DashboardApplicationType dat = DashboardApplicationType.fromJsonValue(appName);
			apps.add(dat);
		}
        LOGGER.info("Before handling tenant application is {}", apps);
		//handle v2/v3/v4 tenant
		if(!tv.getIsV1Tenant() && !apps.contains(DashboardApplicationType.UDE)){
			LOGGER.info("#1 Adding UDE application type for v2/v3/v4 tenant");
			apps.add(DashboardApplicationType.UDE);
		}else if(tv.getIsV1Tenant() && apps.contains(DashboardApplicationType.ITAnalytics)){
            apps.add(DashboardApplicationType.UDE);
            LOGGER.info("#1-2 Adding UDE application type for v1 tenant");
        }
        LOGGER.info("Tenant's applications are {}",apps);
		return apps;
	}

	/**
	 * if tenant is v1 , return true, if v2/v3/v4, return false
	 * @return
	 */
	private TenantVersionModel checkTenantVersion(List<String> subscribedApps, TenantSubscriptionInfo tenantSubscriptionInfo, TenantVersionModel tv){
		//check subscribedapps first
		if(subscribedApps !=null && !subscribedApps.isEmpty()){
			LOGGER.info("Checking subscribedapps list...{}",subscribedApps);
			for(String s: subscribedApps){
				if(SubscriptionAppsUtil.OMC_SERVICE_TYPE.equals(s) ||
						SubscriptionAppsUtil.OSMACC_SERVICE_TYPE.equals(s) || SubscriptionAppsUtil.OMCSE_SERVICE_TYPE.equals(s) ||
						SubscriptionAppsUtil.OMCEE_SERVICE_TYPE.equals(s) || SubscriptionAppsUtil.OMCLOG_SERVICE_TYPE.equals(s) ||
						SubscriptionAppsUtil.SECSE_SERVICE_TYPE.equals(s) || SubscriptionAppsUtil.SECSMA_SERVICE_TYPE.equals(s)){
					LOGGER.info("#1 Check tenant version is V2/V3/v4 tenant.");
					tv.setIsV1Tenant(Boolean.FALSE);
					return tv;
				}
			}

		}
		//if subscribedApps is null check tenantSubscriptionInfo
		if(tenantSubscriptionInfo.getAppsInfoList()!=null && !tenantSubscriptionInfo.getAppsInfoList().isEmpty()){
			for(AppsInfo appsInfo : tenantSubscriptionInfo.getAppsInfoList()){
				if(SubscriptionAppsUtil.V2_TENANT.equals(appsInfo.getLicVersion()) ||
						SubscriptionAppsUtil.V3_TENANT.equals(appsInfo.getLicVersion()) ||
						SubscriptionAppsUtil.V4_TENANT.equals(appsInfo.getLicVersion())){
					LOGGER.info("#2 Check tenant version is V2/V3/V4 tenant.");
                    tv.setIsV1Tenant(Boolean.FALSE);
					return tv;
				}
			}
		}
		LOGGER.info("Check tenant version is V1 tenant.");
		//v1
        tv.setIsV1Tenant(Boolean.TRUE);
        return tv;
	}

	private void initializeQueryParams(Query query, List<Object> paramList)
	{
		if (query == null || paramList == null) {
			return;
		}
		for (int i = 0; i < paramList.size(); i++) {
			Object value = paramList.get(i);
			query.setParameter(i + 1, value);
			LOGGER.debug("binding parameter [{}] as [{}]", i + 1, value);
		}
	}

	private boolean isDashboardAccessbyCurrentTenant(EmsDashboard ed) throws TenantWithoutSubscriptionException {
		return isDashboardAccessbyCurrentTenant(ed, null);
	}

	private boolean isDashboardAccessbyCurrentTenant(EmsDashboard ed, List<String> subscribedApps) throws TenantWithoutSubscriptionException
	{
		if (ed == null) {
			LOGGER.debug("null dashboard is not accessed by current tenant");
			return false;
		}
		List<DashboardApplicationType> datList = getTenantApplications(subscribedApps, new TenantVersionModel(Boolean.FALSE));
		// as dashboards only stores basic servcies data, we need to trasfer (possible) bundle services to basic servcies for comparision
		datList = DashboardApplicationType.getBasicServiceList(datList);
		if (datList == null || datList.isEmpty()) { // accessible app list is empty
			throw new TenantWithoutSubscriptionException();
		}
		Boolean isSystem = DataFormatUtils.integer2Boolean(ed.getIsSystem());
		if (!isSystem) { // check system dashboard only
			LOGGER.debug("dashboard with id {} is accessed by current tenant", ed.getDashboardId());
			return true;
		}
		Integer at = ed.getApplicationType();
		if (at == null) { // should be always available for system dashboard
			LOGGER.error("Unexpected: application type for system dashboard with id {} is null", ed.getDashboardId());
			return false;
		}
		DashboardApplicationType app = DashboardApplicationType.fromValue(at.intValue());
		if (app == null) {
			LOGGER.debug("Failed to retrieve a valid DashboardApplicationType from given application type internal value {}", at);
			return false;
		}
		for (DashboardApplicationType dat : datList) {
			if (dat.equals(app)) {
				return true;
			}
		}
		LOGGER.debug("dashboard can't be accessed by current tenant as it's application type isn't in the subscribed application list");
		return false;
	}
}
