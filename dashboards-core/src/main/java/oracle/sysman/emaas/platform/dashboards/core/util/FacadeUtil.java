/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.util;

import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;
import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;
import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptions;

/**
 * @author wenjzhu
 */
public class FacadeUtil
{
	public static EmsDashboard cloneEmsDashboard(EmsDashboard dsb)
	{
		if (dsb == null) {
			return null;
		}

		EmsDashboard result = new EmsDashboard();
		result.setDashboardId(dsb.getDashboardId());
		result.setApplicationType(dsb.getApplicationType());
		result.setCreationDate(dsb.getCreationDate());

		if (dsb.getDashboardTileList() != null) {
			for (EmsDashboardTile tile : dsb.getDashboardTileList()) {
				EmsDashboardTile emsDashboardTile = FacadeUtil.cloneEmsDashboardTile(tile);
				result.addEmsDashboardTile(emsDashboardTile);
			}
		}

		result.setDeleted(dsb.getDeleted());
		result.setDescription(dsb.getDescription());
		result.setEnableRefresh(dsb.getEnableRefresh());
		result.setEnableTimeRange(dsb.getEnableTimeRange());
		result.setIsSystem(dsb.getIsSystem());
		result.setLastModificationDate(dsb.getLastModificationDate());
		result.setLastModifiedBy(dsb.getLastModifiedBy());
		result.setName(dsb.getName());
		result.setOwner(dsb.getOwner());
		result.setScreenShot(dsb.getScreenShot());
		result.setSharePublic(dsb.getSharePublic());
		result.setTenantId(dsb.getTenantId());
		result.setType(dsb.getType());
		result.setFederationSupported(dsb.getFederationSupported());

		return result;
	}

	public static EmsDashboardTile cloneEmsDashboardTile(EmsDashboardTile dt)
	{
		if (dt == null) {
			return null;
		}

		EmsDashboardTile result = new EmsDashboardTile();
		result.setColumn(dt.getColumn());
		result.setCreationDate(dt.getCreationDate());

		if (dt.getDashboardTileParamsList() != null) {
			for (EmsDashboardTileParams tp : dt.getDashboardTileParamsList()) {
				EmsDashboardTileParams emsDashboardTileParams = FacadeUtil.cloneEmsDashboardTileParams(tp);
				result.addEmsDashboardTileParams(emsDashboardTileParams);
			}
		}
		//result.setDashboardTileParamsList(dashboardTileParamsList);

		result.setTileId(dt.getTileId());
		result.setHeight(dt.getHeight());
		result.setIsMaximized(dt.getIsMaximized());
		result.setLastModificationDate(dt.getLastModificationDate());
		result.setLastModifiedBy(dt.getLastModifiedBy());
		result.setOwner(dt.getOwner());
		result.setPosition(dt.getPosition());
		result.setProviderAssetRoot(dt.getProviderAssetRoot());
		result.setProviderName(dt.getProviderName());
		result.setProviderVersion(dt.getProviderVersion());
		result.setRow(dt.getRow());
		result.setTitle(dt.getTitle());
		result.setType(dt.getType());
		result.setWidgetCreationTime(dt.getWidgetCreationTime());
		result.setWidgetDescription(dt.getWidgetDescription());
		result.setWidgetGroupName(dt.getWidgetGroupName());
		result.setWidgetHistogram(dt.getWidgetHistogram());
		result.setWidgetIcon(dt.getWidgetIcon());
		result.setWidgetKocName(dt.getWidgetKocName());
		result.setWidgetLinkedDashboard(dt.getWidgetLinkedDashboard());
		result.setWidgetName(dt.getWidgetName());
		result.setWidgetOwner(dt.getWidgetOwner());
		result.setWidgetSource(dt.getWidgetSource());
		result.setWidgetSupportTimeControl(dt.getWidgetSupportTimeControl());
		result.setWidgetTemplate(dt.getWidgetTemplate());
		result.setWidgetUniqueId(dt.getWidgetUniqueId());
		result.setWidgetViewmode(dt.getWidgetViewmode());
		result.setWidth(dt.getWidth());
		result.setFederationSupported(dt.getFederationSupported());

		return result;
	}

	//	public static EmsDashboardFavorite cloneEmsDashboardFavorite(EmsDashboardFavorite df)
	//	{
	//		if (df == null) {
	//			return null;
	//		}
	//
	//		EmsDashboardFavorite result = new EmsDashboardFavorite();
	//		result.setCreationDate(df.getCreationDate());
	//		result.setDashboard(FacadeUtil.cloneEmsDashboard(df.getDashboard()));
	//		result.setUserName(df.getUserName());
	//
	//		return result;
	//	}

	//	public static EmsDashboardLastAccess cloneEmsDashboardLastAccess(EmsDashboardLastAccess dla)
	//	{
	//		if (dla == null) {
	//			return null;
	//		}
	//
	//		EmsDashboardLastAccess result = new EmsDashboardLastAccess();
	//		result.setAccessDate(dla.getAccessDate());
	//		result.setAccessedBy(dla.getAccessedBy());
	//		result.setDashboardId(dla.getDashboardId());
	//
	//		return result;
	//	}

	public static EmsDashboardTileParams cloneEmsDashboardTileParams(EmsDashboardTileParams tp)
	{
		if (tp == null) {
			return null;
		}

		EmsDashboardTileParams result = new EmsDashboardTileParams();
		result.setIsSystem(tp.getIsSystem());
		result.setParamName(tp.getParamName());
		result.setParamType(tp.getParamType());
		result.setParamValueNum(tp.getParamValueNum());
		result.setParamValueStr(tp.getParamValueStr());
		result.setParamValueTimestamp(tp.getParamValueTimestamp());

		return result;
	}

	public static EmsPreference cloneEmsPreference(EmsPreference p)
	{
		if (p == null) {
			return null;
		}
		EmsPreference result = new EmsPreference();
		result.setPrefKey(p.getPrefKey());
		result.setPrefValue(p.getPrefValue());
		result.setUserName(p.getUserName());

		return result;
	}

	public static EmsUserOptions cloneEmsUserOptions(EmsUserOptions df)
	{
		if (df == null) {
			return null;
		}

		EmsUserOptions result = new EmsUserOptions();
		result.setAccessDate(df.getAccessDate());
		result.setDashboardId(df.getDashboardId());
		result.setUserName(df.getUserName());
		result.setAutoRefreshInterval(df.getAutoRefreshInterval());
		result.setIsFavorite(df.getIsFavorite());

		return result;

	}

	private FacadeUtil()
	{
	}
}
