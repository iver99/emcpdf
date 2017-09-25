/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * NOTE: if schema changes, corresponding Entity class need to be updated and equals and hashcode methods should be  rewrited too
 * USER_NAME
 * TENANT_ID
 * DASHBOARD_ID
 * AUTO_REFRESH_INTERVAL
 * ACCESS_DATE
 * IS_FAVORITE
 * EXTENDED_OPTIONS
 * CREATION_DATE
 * LAST_MODIFICATION_DATE
 * DELETED
 */
public class DashboardUserOptionsRowEntity implements RowEntity
{
	@JsonProperty("USER_NAME")
	private String userName;

	@JsonProperty("TENANT_ID")
	private Long tenantId;

	@JsonProperty("DASHBOARD_ID")
	private String dashboardId;

	@JsonProperty("AUTO_REFRESH_INTERVAL")
	private Long autoRefreshInterval;

	@JsonProperty("ACCESS_DATE")
	private String accessDate;

	@JsonProperty("IS_FAVORITE")
	private Integer isFavorite;

	@JsonProperty("EXTENDED_OPTIONS")
	private String extendedOptions;

	@JsonProperty("CREATION_DATE")
	private String creationDate;

	@JsonProperty("LAST_MODIFICATION_DATE")
	private String lastModificationDate;
	
	@JsonProperty("DELETED")
	private Integer deleted;
	
	
	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the accessDate
	 */
	public String getAccessDate()
	{
		return accessDate;
	}

	/**
	 * @return the autoRefreshInterval
	 */
	public Long getAutoRefreshInterval()
	{
		return autoRefreshInterval;
	}

	/**
	 * @return the dashboardId
	 */
	public String getDashboardId()
	{
		return dashboardId;
	}

	/**
	 * @return the extendedOptions
	 */
	public String getExtendedOptions()
	{
		return extendedOptions;
	}

	/**
	 * @return the isFavorite
	 */
	public Integer getIsFavorite()
	{
		return isFavorite;
	}

	/**
	 * @return the tenantId
	 */
	public Long getTenantId()
	{
		return tenantId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * @return the creationDate
	 */
	public String getCreationDate()
	{
		return creationDate;
	}

	/**
	 * @return the lastModificationDate
	 */
	public String getLastModificationDate()
	{
		return lastModificationDate;
	}

	/**
	 * @param accessDate
	 *            the accessDate to set
	 */
	public void setAccessDate(String accessDate)
	{
		this.accessDate = accessDate;
	}

	/**
	 * @param autoRefreshInterval
	 *            the autoRefreshInterval to set
	 */
	public void setAutoRefreshInterval(Long autoRefreshInterval)
	{
		this.autoRefreshInterval = autoRefreshInterval;
	}

	/**
	 * @param dashboardId
	 *            the dashboardId to set
	 */
	public void setDashboardId(String dashboardId)
	{
		this.dashboardId = dashboardId;
	}

	/**
	 * @param extendedOptions
	 *            the extendedOptions to set
	 */
	public void setExtendedOptions(String extendedOptions)
	{
		this.extendedOptions = extendedOptions;
	}

	/**
	 * @param isFavorite
	 *            the isFavorite to set
	 */
	public void setIsFavorite(Integer isFavorite)
	{
		this.isFavorite = isFavorite;
	}

	/**
	 * @param tenantId
	 *            the tenantId to set
	 */
	public void setTenantId(Long tenantId)
	{
		this.tenantId = tenantId;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(String creationDate)
	{
		this.creationDate = creationDate;
	}

	/**
	 * @param lastModificationDate
	 *            the lastModificationDate to set
	 */
	public void setLastModificationDate(String lastModificationDate)
	{
		this.lastModificationDate = lastModificationDate;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "DashboardUserOptionsRowEntity [userName=" + userName + ", tenantId=" + tenantId + ", dashboardId=" + dashboardId
				+ ", autoRefreshInterval=" + autoRefreshInterval + ", accessDate=" + accessDate + ", isFavorite=" + isFavorite
				+ ", extendedOptions=" + extendedOptions + ", creationDate=" + creationDate + ", lastModificationDate=" + lastModificationDate+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accessDate == null) ? 0 : accessDate.hashCode());
		result = prime
				* result
				+ ((autoRefreshInterval == null) ? 0 : autoRefreshInterval
						.hashCode());
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result
				+ ((dashboardId == null) ? 0 : dashboardId.hashCode());
		result = prime * result + ((deleted == null) ? 0 : deleted.hashCode());
		result = prime * result
				+ ((extendedOptions == null) ? 0 : extendedOptions.hashCode());
		result = prime * result
				+ ((isFavorite == null) ? 0 : isFavorite.hashCode());
		result = prime
				* result
				+ ((lastModificationDate == null) ? 0 : lastModificationDate
						.hashCode());
		result = prime * result
				+ ((tenantId == null) ? 0 : tenantId.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DashboardUserOptionsRowEntity other = (DashboardUserOptionsRowEntity) obj;
		if (accessDate == null) {
			if (other.accessDate != null)
				return false;
		} else if (!accessDate.equals(other.accessDate))
			return false;
		if (autoRefreshInterval == null) {
			if (other.autoRefreshInterval != null)
				return false;
		} else if (!autoRefreshInterval.equals(other.autoRefreshInterval))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (dashboardId == null) {
			if (other.dashboardId != null)
				return false;
		} else if (!dashboardId.equals(other.dashboardId))
			return false;
		if (deleted == null) {
			if (other.deleted != null)
				return false;
		} else if (!deleted.equals(other.deleted))
			return false;
		if (extendedOptions == null) {
			if (other.extendedOptions != null)
				return false;
		} else if (!extendedOptions.equals(other.extendedOptions))
			return false;
		if (isFavorite == null) {
			if (other.isFavorite != null)
				return false;
		} else if (!isFavorite.equals(other.isFavorite))
			return false;
		if (lastModificationDate == null) {
			if (other.lastModificationDate != null)
				return false;
		} else if (!lastModificationDate.equals(other.lastModificationDate))
			return false;
		if (tenantId == null) {
			if (other.tenantId != null)
				return false;
		} else if (!tenantId.equals(other.tenantId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	
}
