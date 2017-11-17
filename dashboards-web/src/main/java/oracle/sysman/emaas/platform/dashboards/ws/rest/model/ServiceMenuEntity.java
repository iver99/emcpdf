/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

/**
 * @author aduan
 */
public class ServiceMenuEntity
{
	private String appId = null;
	private String serviceName = null;
	private String version = null;
	private String metaDataHref = null;
	private Boolean hasAdminMenu = null;

	/**
	 * @return the hasAdminMenu
	 */
	public Boolean getHasAdminMenu()
	{
		return hasAdminMenu;
	}

	/**
	 * @param hasAdminMenu the hasAdminMenu to set
	 */
	public void setHasAdminMenu(Boolean hasAdminMenu)
	{
		this.hasAdminMenu = hasAdminMenu;
	}

	/**
	 * @return the appId
	 */
	public String getAppId()
	{
		return appId;
	}

	/**
	 * @return the metaDataHref
	 */
	public String getMetaDataHref()
	{
		return metaDataHref;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName()
	{
		return serviceName;
	}

	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * @param appId
	 *            the appId to set
	 */
	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	/**
	 * @param metaDataHref
	 *            the metaDataHref to set
	 */
	public void setMetaDataHref(String metaDataHref)
	{
		this.metaDataHref = metaDataHref;
	}

	/**
	 * @param serviceName
	 *            the serviceName to set
	 */
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

}
