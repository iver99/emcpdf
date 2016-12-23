/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.cache.screenshot;

import java.math.BigInteger;
import java.util.Date;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LRUCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.screenshot.Binary;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.DefaultKeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.core.util.Base64;

/**
 * @author guochen
 */
public class ScreenshotCacheManager
{
	private static final Logger LOGGER = LogManager.getLogger(ScreenshotCacheManager.class);

	private static ScreenshotCacheManager instance = new ScreenshotCacheManager();

	public static ScreenshotCacheManager getInstance()
	{
		return instance;
	}

	private final ICacheManager cm;

	private ScreenshotCacheManager()
	{
		cm = LRUCacheManager.getInstance();
	}

	public ScreenshotElement getScreenshotFromCache(Tenant tenant, BigInteger dashboardId, String fileName) throws Exception
	{
		if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
			LOGGER.info("Unexpected dashboard id to get screenshot from cache for tenant={}, dashboard id={}, fileName={}",
					tenant, dashboardId, fileName);
			return null;
		}
		if (StringUtil.isEmpty(fileName)) {
			LOGGER.info("Unexpected empty screenshot file name for tenant={}, dashboard id={}", tenant, dashboardId);
			return null;
		}
		ScreenshotElement se = (ScreenshotElement) cm.getCache(CacheConstants.CACHES_SCREENSHOT_CACHE).get(DefaultKeyGenerator.getInstance().generate(tenant,new Keys(dashboardId)));
		if (se == null) {
			LOGGER.info("Retrieved null screenshot element from cache for tenant={}, dashboard id={}, fileName={}", tenant,
					dashboardId, fileName);
			return null;
		}
		return se;
	}

	public ScreenshotElement storeBase64ScreenshotToCache(Tenant tenant, BigInteger dashboardId, Date creation, Date modification,
			String screenshot)
	{
		if (screenshot == null) {
			return null;
		}
		String fileName = ScreenshotPathGenerator.getInstance().generateFileName(dashboardId, creation, modification);
		byte[] decoded = null;
		if (screenshot.startsWith(DashboardManager.SCREENSHOT_BASE64_PNG_PREFIX)) {
			decoded = Base64.decode(screenshot.substring(DashboardManager.SCREENSHOT_BASE64_PNG_PREFIX.length()));
		}
		else if (screenshot.startsWith(DashboardManager.SCREENSHOT_BASE64_JPG_PREFIX)) {
			decoded = Base64.decode(screenshot.substring(DashboardManager.SCREENSHOT_BASE64_JPG_PREFIX.length()));
		}
		else {
			LOGGER.debug("Failed to retrieve screenshot decoded bytes as the previs isn't supported");
			return null;
		}
		Binary bin = new Binary(decoded);
		ScreenshotElement se = new ScreenshotElement(fileName, bin);
		cm.getCache(CacheConstants.CACHES_SCREENSHOT_CACHE).put(DefaultKeyGenerator.getInstance().generate(tenant,new Keys(dashboardId)),se);
		return se;
	}

	public ScreenshotElement storeBase64ScreenshotToCache(Tenant tenant, BigInteger dashboardId, ScreenshotData ssd)
	{
		if (ssd == null) {
			return null;
		}
		return storeBase64ScreenshotToCache(tenant, dashboardId, ssd.getCreationDate(), ssd.getModificationDate(),
				ssd.getScreenshot());
	}
}