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


import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.*;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.DashboardsFilter;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.EntityNamingDependencyUnavailableException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.PaginatedDashboards;
import oracle.sysman.emaas.platform.dashboards.core.model.Preference;
import oracle.sysman.emaas.platform.dashboards.core.util.*;
import oracle.sysman.emaas.platform.dashboards.webutils.ParallelThreadPool;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.PreferenceEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.RegistrationEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.UserInfoEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.TenantSubscriptionsAPI.SubscribedAppsEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.preferences.FeatureShowPreferences;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.PrivilegeChecker;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import oracle.sysman.emaas.platform.emcpdf.tenant.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.TenantSubscriptionInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miao
 * @author guobaochen moving registry APIs from DF UI to DF API project
 */
@Path("/v1/configurations")
public class ConfigurationAPI extends APIBase
{
	private static final Logger _LOGGER = LogManager.getLogger(ConfigurationAPI.class);

	private static class CombinedBrandingBarData {
		public CombinedBrandingBarData(UserInfoEntity userInfo, RegistrationEntity registration,
									   SubscribedAppsEntity subscribedApps,String subscribedApps2,
									   List<PreferenceEntity> preferences,Map<String, String> baseVanityUrls,PaginatedDashboards pd) {
			this.userInfo = userInfo;
			this.registration = registration;
			this.subscribedApps = subscribedApps;
            this.subscribedApps2 = subscribedApps2;
			this.preferences = preferences;
			this.baseVanityUrls = baseVanityUrls;
			this.pd = pd;
		}

		private UserInfoEntity userInfo;
		private RegistrationEntity registration;
		private String subscribedApps2;
        private SubscribedAppsEntity subscribedApps;
		private List<PreferenceEntity> preferences;
		private Map<String, String> baseVanityUrls;
		private PaginatedDashboards pd;


		public PaginatedDashboards getPd() {
			return pd;
		}

		public void setPd(PaginatedDashboards pd) {
			this.pd = pd;
		}

		public Map<String, String> getBaseVanityUrls() {
			return baseVanityUrls;
		}

		public void setBaseVanityUrls(Map<String, String> baseVanityUrls) {
			this.baseVanityUrls = baseVanityUrls;
		}

		public String getSubscribedApps2() {
            return subscribedApps2;
        }

        public void setSubscribedApps2(String subscribedApps2) {
            this.subscribedApps2 = subscribedApps2;
        }

        public UserInfoEntity getUserInfo() {
			return userInfo;
		}

		public void setUserInfo(UserInfoEntity userInfo) {
			this.userInfo = userInfo;
		}

		public SubscribedAppsEntity getSubscribedApps() {
			return subscribedApps;
		}

		public void setSubscribedApps(SubscribedAppsEntity subscribedApps) {
			this.subscribedApps = subscribedApps;
		}

		public RegistrationEntity getRegistration() {
			return registration;
		}

		public void setRegistration(RegistrationEntity registration) {
			this.registration = registration;
		}

		public List<PreferenceEntity> getPreferences() {
			return preferences;
		}

		public void setPreferences(List<PreferenceEntity> preferences) {
			this.preferences = preferences;
		}

		public String getBrandingbarInjectedJS() {
			StringBuilder sb = new StringBuilder();
			if (userInfo != null) {
				sb.append("window._uifwk.cachedData.userInfo=");
				sb.append(JsonUtil.buildNormalMapper().toJson(userInfo));
				sb.append(";");
			}

			if (registration != null) {
				sb.append("window._uifwk.cachedData.registrations=");
				sb.append(JsonUtil.buildNormalMapper().toJson(registration));
				sb.append(";");
			}

			if (subscribedApps != null) {
				sb.append("window._uifwk.cachedData.subscribedapps=");
				sb.append(JsonUtil.buildNormalMapper().toJson(subscribedApps));
				sb.append(";");
			}

            if (subscribedApps2 != null) {
                sb.append("window._uifwk.cachedData.subscribedapps2=");
                sb.append(subscribedApps2);
                sb.append(";");
            }

			if (preferences != null) {
				sb.append("window._uifwk.cachedData.preferences=");
				sb.append(JsonUtil.buildNormalMapper().toJson(preferences));
				sb.append(";");
			}

			if(baseVanityUrls != null){
				sb.append("window._uifwk.cachedData.baseVanityUrls=");
				sb.append(JsonUtil.buildNormalMapper().toJson(baseVanityUrls));
				sb.append(";");
			}

			if(pd != null){
				sb.append("window._uifwk.cachedData.favDashboards=");
				sb.append(JsonUtil.buildNonNullMapper().toJson(pd));
				sb.append(";");
			}


			return sb.toString();
		}
	}

	@Path("/registration")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDiscoveryConfigurations(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@HeaderParam(value = "SESSION_EXP") String sessionExpiryTime)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/configurations/registration");

		try {
			initializeUserContext(tenantIdParam, userTenant);
			String regEntity = JsonUtil.buildNonNullMapper().toJson(new RegistrationEntity(sessionExpiryTime, null));
			_LOGGER.info("Response for [GET] /v1/configurations/registration is \"{}\"", regEntity);
			Response resp = Response.status(Status.OK).entity(regEntity).build();
			return resp;

		}
		catch (DashboardException e) {
			_LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity ee = new ErrorEntity(e);
			return Response.status(ee.getStatusCode()).entity(JsonUtil.buildNormalMapper().toJson(ee)).build();
		}
	}

	@Path("/userInfo")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRolesAndPriviledges(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
											   @HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
											   @HeaderParam(value = "SESSION_EXP") String sessionExpiryTime)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/configurations/userInfo");
		try {
			initializeUserContext(tenantIdParam, userTenant);
			if (!DependencyStatus.getInstance().isEntityNamingUp())  {
				_LOGGER.error("Error to call [GET] /v1/configurations/userInfo: EntityNaming service is down");
				throw new EntityNamingDependencyUnavailableException();
			}
            String userInfoEntity = JsonUtil.buildNormalMapper().toJson(new UserInfoEntity());
			_LOGGER.info("Response for [GET] /v1/configurations/userInfo is \"{}\"", userInfoEntity);
			Response resp = Response.status(Status.OK)
					.entity(userInfoEntity).build();
			return resp;

		}
		catch (DashboardException e) {
			_LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity ee = new ErrorEntity(e);
			return Response.status(ee.getStatusCode()).entity(JsonUtil.buildNormalMapper().toJson(ee)).build();
		}
	}

	/**
	 * This API returned a combined data for branding bar, including user info, registration data, and subscribed application for the tenant.
	 * This API is supposed to be used internally by OMC services for html data injection for PSR consideration.
	 * This API returns the js String directly containing the branding bar data, thus additional object deserialization on the caller service side is avoided.
	 *
	 * @param tenantIdParam tenant for the request, from header "X-USER-IDENTITY-DOMAIN-NAME"
	 * @param userTenant <tenant.user> for the requesst, from header "X-REMOTE-USER"
	 * @param referer referer for the request
	 * @param sessionExpiryTime session expiry time for the request, from header "SESSION_EXP"
     * @return
     */
	@Path("/brandingbardata")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCombinedBrandingBarData(final @HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
												 @HeaderParam(value = "X-REMOTE-USER") final String userTenant, @HeaderParam(value = "Referer") String referer,
												 @HeaderParam(value = "SESSION_EXP") String sessionExpiryTime)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/configurations/brandingbardata");
        long start = System.currentTimeMillis();
		try {
			initializeUserContext(tenantIdParam, userTenant);
            if (!DependencyStatus.getInstance().isEntityNamingUp())  {
                _LOGGER.error("Error to call [GET] /v1/configurations/userInfo: EntityNaming service is down");
                throw new EntityNamingDependencyUnavailableException();
            }

            Future<List<String>> futureUserRoles =null;
            Future<String> futureUserGrants =null;
            Future<List<String>> futureSubscribedApps =null;
			Future<List<Preference>> featurePreferences = null;
			Future<Map<String, String>> futureBaseVanityUrls =null;
			Future<PaginatedDashboards> futureFavDashboard =null;
            ExecutorService pool = ParallelThreadPool.getThreadPool();
			final long TIMEOUT=30000;

            final String curTenant = TenantContext.getCurrentTenant();
            final String curUser = UserContext.getCurrentUser();

			featurePreferences = pool.submit(new Callable<List<Preference>>() {
				@Override
				public List<Preference> call() throws Exception {
					try {
						_LOGGER.info("Parallel request to get preference settings for features...");
						long startPrefs = System.currentTimeMillis();
						Long internalTenantId = ConfigurationAPI.this.getTenantId(tenantIdParam);
						UserContext.setCurrentUser(curUser);
						List<Preference> prefs = FeatureShowPreferences.getFeatureShowPreferences(internalTenantId);
						long endPrefs = System.currentTimeMillis();
						_LOGGER.info("Time to get features preferences: {}ms. Retrieved data is: {}", (endPrefs - startPrefs), prefs);
						return prefs;
					} catch (Exception e) {
						_LOGGER.error("Error occurred when retrieving feature preferences settings using parallel request!", e);
						throw e;
					}
				}
			});

            futureUserRoles = pool.submit(new Callable<List<String>>() {
                @Override
                public List<String> call() throws Exception {
                    try{
                        long startUserRoles = System.currentTimeMillis();
                        _LOGGER.info("Parallel request user roles...");
                        List<String> userRoles = PrivilegeChecker.getUserRoles(curTenant, curUser);
                        long endUserRoles = System.currentTimeMillis();
                        _LOGGER.info("Time to get user roles: {}ms, user roles are: {}", (endUserRoles - startUserRoles), userRoles);
                        return userRoles;
                    }catch(Exception e){
                        _LOGGER.error("Error occurred when retrieving user roles data using parallel request!", e);
                        throw e;
                    }
                }
            });
            
            //retrieve user grants
            futureUserGrants = pool.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    try{
                        long startUserGrants = System.currentTimeMillis();
                        _LOGGER.info("Parallel request to get user grants...");
                        String userGrants = PrivilegeChecker.getUserGrants(curTenant, curUser);
                        long endUserGrants = System.currentTimeMillis();
                        _LOGGER.info("Time to get user grants: {}ms, user grants are: {}", (endUserGrants - startUserGrants), userGrants);
                        return userGrants;
                    }catch(Exception e){
                        _LOGGER.error("Error occurred when retrieving user granted privileges using parallel request!", e);
                        throw e;
                    }
                }
            });

            //retrieve base vanity urls
			futureBaseVanityUrls = pool.submit(new Callable<Map<String, String>>() {
				@Override
				public Map<String, String> call() throws Exception {
					try{
						long start = System.currentTimeMillis();
						_LOGGER.info("Parallel request to get user grants...");
						Map<String, String> baseVanityUrls = RegistryLookupUtil.getVanityBaseURLs(tenantIdParam);
						_LOGGER.info("Retrieved base Vanity Urls are {}", baseVanityUrls);
						Map<String, String> copyBaseVanityUrls = new HashMap<>();
						RegistryLookupAPI.handleBaseVanityUrls(tenantIdParam, baseVanityUrls, copyBaseVanityUrls);
						long end = System.currentTimeMillis();
						_LOGGER.info("Time to get base vanity urls took: {}ms, result is {}", (end - start), copyBaseVanityUrls);
						return copyBaseVanityUrls;
					}catch(Exception e){
						_LOGGER.error("Error occurred when get base vanity urls using parallel request!", e);
						throw e;
					}
				}
			});

            //retrieve subscribapp api data
			final TenantSubscriptionInfo tenantSubscriptionInfo= new TenantSubscriptionInfo();
            futureSubscribedApps = pool.submit(new Callable<List<String>>() {
                @Override
                public List<String> call() throws Exception {
                    try{
                        // this ensures subscribed app data inside cache, and reused by registration data retrieval
                        _LOGGER.info("Parallel request subscribed apps info...");
                        long startSubsApps = System.currentTimeMillis();
                        List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(tenantIdParam, tenantSubscriptionInfo);
                        long endSubsApps = System.currentTimeMillis();
                        _LOGGER.info("Time to get subscribed app: {}ms. Retrieved data is: {}", (endSubsApps - startSubsApps), apps);
                        return apps;
                    }catch(Exception e){
                        _LOGGER.error("Error occurred when retrieving subscribed data using parallel request!", e);
                        throw e;
                    }
                }
            });

			List<PreferenceEntity> prefs = new ArrayList<PreferenceEntity>();
			// by Default FederationFeatureShowInUiPref is false.
			boolean FederationFeatureShowInUiPref = false;
			try {
				if(featurePreferences!=null){
					List<Preference> prefList = featurePreferences.get(TIMEOUT, TimeUnit.MILLISECONDS);

					_LOGGER.debug("Preference settings data is {}", prefList);
					if (prefList != null) {
						for (Preference pref : prefList) {
							prefs.add(new PreferenceEntity(pref));
							//check uifwk.hm.federation.show value
							if("uifwk.hm.federation.show".equals(pref.getKey()) && Boolean.TRUE.equals(pref.getValue())){
								_LOGGER.info("Preference entry 'uifwk.hm.federation.show' is found, value is {}", pref.getValue());
								FederationFeatureShowInUiPref = true;
							}
						}
					}

				}
			} catch (InterruptedException e) {
				_LOGGER.error(e);
			} catch (ExecutionException e) {
				_LOGGER.error(e.getCause() == null ? e : e.getCause());
			}catch(TimeoutException e){
				//if timeout, and the task is still running, attempt to stop the task
				featurePreferences.cancel(true);
				_LOGGER.error(e);
			}

			final boolean boolFederationFeatureShowInUi = FederationFeatureShowInUiPref;
			//retrieve favorite dashboard
			futureFavDashboard = pool.submit(new Callable<PaginatedDashboards>() {
				@Override
				public PaginatedDashboards call() throws Exception {
					try{
						long start = System.currentTimeMillis();
						_LOGGER.info("Parallel request to get favorite dashboards...");
						initializeUserContext(tenantIdParam, userTenant);
						String filterString = "favorites";
						//federation dashboards doesn't support favorite, so set federationMode=false
						boolean federationMode = false;
						DashboardManager manager = DashboardManager.getInstance();
						DashboardsFilter filter = new DashboardsFilter();
						filter.initializeFilters(filterString);
						Long tenantId = getTenantId(tenantIdParam);
						PaginatedDashboards pd = manager.listDashboards(null, 0, 120, tenantId, true, "default", filter, federationMode, boolFederationFeatureShowInUi);
						if (pd != null && pd.getDashboards() != null) {
							for (Dashboard d : pd.getDashboards()) {
								new DashboardAPI().updateDashboardAllHref(d, tenantIdParam);
							}
						}
						_LOGGER.info("Retrieved get favorite dashboard is {}", pd);

						long end = System.currentTimeMillis();
						_LOGGER.info("Time to get favorite dashboard took: {}ms", (end - start));
						return pd;
					}catch(Exception e){
						_LOGGER.error("Error occurred when get favorite dashboard using parallel request!", e);
						throw e;
					}
				}
			});


            //get subscribed apps data
            List<String> subApps = null;
            try {
                if(futureSubscribedApps!=null){
                    subApps = futureSubscribedApps.get(TIMEOUT, TimeUnit.MILLISECONDS);
                    _LOGGER.debug("Subscribed apps data is " + subApps);
                }
            } catch (InterruptedException e) {
                _LOGGER.error(e);
            } catch (ExecutionException e) {
                _LOGGER.error(e.getCause() == null ? e : e.getCause());
            }catch(TimeoutException e){
				//if timeout, and the task is still running, attempt to stop the task
				futureSubscribedApps.cancel(true);
                _LOGGER.error(e);
            }
            //get subscribapps2 API data
            String subApps2 = tenantSubscriptionInfo.toJson(tenantSubscriptionInfo);

            List<String> userRoles = null;
            try {
                if(futureUserRoles!=null){
                    userRoles = futureUserRoles.get(TIMEOUT, TimeUnit.MILLISECONDS);
                    _LOGGER.debug("User roles data is {}", userRoles);
                }
            } catch (InterruptedException e) {
                _LOGGER.error(e);
            } catch (ExecutionException e) {
                _LOGGER.error(e.getCause() == null ? e : e.getCause());
            }catch(TimeoutException e){
				//if timeout, and the task is still running, attempt to stop the task
				futureUserRoles.cancel(true);
                _LOGGER.error(e);
            }
            
            String userGrants = null;
            try {
                if(futureUserGrants!=null){
                	userGrants = futureUserGrants.get(TIMEOUT, TimeUnit.MILLISECONDS);
                    _LOGGER.debug("User grants data is {}", userGrants);
                }
            } catch (InterruptedException e) {
                _LOGGER.error(e);
            } catch (ExecutionException e) {
                _LOGGER.error(e.getCause() == null ? e : e.getCause());
            }catch(TimeoutException e){
				//if timeout, and the task is still running, attempt to stop the task
            	futureUserGrants.cancel(true);
                _LOGGER.error(e);
            }

			Map<String, String> baseVanityUrls = null;
            try{
            	if(futureBaseVanityUrls != null){
					baseVanityUrls = futureBaseVanityUrls.get(TIMEOUT, TimeUnit.MILLISECONDS);
					_LOGGER.debug("baseVanityUrls {}", baseVanityUrls);
				}
			}catch (InterruptedException e) {
				_LOGGER.error(e);
			} catch (ExecutionException e) {
				_LOGGER.error(e.getCause() == null ? e : e.getCause());
			}catch(TimeoutException e){
				//if timeout, and the task is still running, attempt to stop the task
				futureBaseVanityUrls.cancel(true);
				_LOGGER.error(e);
			}

			PaginatedDashboards pd = null;
			try{
				if(futureFavDashboard != null){
					pd = futureFavDashboard.get(TIMEOUT, TimeUnit.MILLISECONDS);
					_LOGGER.debug("Favorite dashboard is  {}", JsonUtil.buildNormalMapper().toJson(pd));
				}
			}catch (InterruptedException e) {
				_LOGGER.error(e);
			} catch (ExecutionException e) {
				_LOGGER.error(e.getCause() == null ? e : e.getCause());
			}catch(TimeoutException e){
				//if timeout, and the task is still running, attempt to stop the task
				futureFavDashboard.cancel(true);
				_LOGGER.error(e);
			}


			SubscribedAppsEntity sae = subApps == null ? null : new SubscribedAppsEntity(subApps);
			RegistrationEntity re = new RegistrationEntity(sessionExpiryTime, userRoles);
			CombinedBrandingBarData cbbd = new CombinedBrandingBarData(new UserInfoEntity(userRoles, userGrants), re, sae,subApps2, prefs, baseVanityUrls, pd);
			String brandingBarData = cbbd.getBrandingbarInjectedJS();
            long end = System.currentTimeMillis();
			_LOGGER.info("Response for [GET] /v1/configurations/brandingbardata is \"{}\". It takes {}ms for this API", brandingBarData, (end - start));
			Response resp = Response.status(Status.OK).entity(brandingBarData).build();
			return resp;

		}
		catch (DashboardException e) {
			_LOGGER.error(e);
			ErrorEntity ee = new ErrorEntity(e);
			return Response.status(ee.getStatusCode()).entity(JsonUtil.buildNormalMapper().toJson(ee)).build();
		}
	}
}
