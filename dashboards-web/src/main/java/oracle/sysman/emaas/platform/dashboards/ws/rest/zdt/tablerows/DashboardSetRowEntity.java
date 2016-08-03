/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author guochen
 */
public class DashboardSetRowEntity implements RowEntity
{
	@JsonProperty("DASHBOARD_SET_ID")
	private Long dashboardSetId;

	@JsonProperty("TENANT_ID")
	private Long tenantId;

	@JsonProperty("SUB_DASHBOARD_ID")
	private Long subDashboardId;

	@JsonProperty("POSITION")
	private Long position;

	@JsonProperty("CREATION_DATE")
	private String creationDate;

	@JsonProperty("LAST_MODIFICATION_DATE")
	private String lastModificationDate;

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DashboardSetRowEntity other = (DashboardSetRowEntity) obj;
		if (creationDate == null) {
			if (other.creationDate != null) {
				return false;
			}
		}
		else if (!creationDate.equals(other.creationDate)) {
			return false;
		}
		if (lastModificationDate == null) {
			if (other.lastModificationDate != null) {
				return false;
			}
		}
		else if (!lastModificationDate.equals(other.lastModificationDate)) {
			return false;
		}
		if (dashboardSetId == null) {
			if (other.dashboardSetId != null) {
				return false;
			}
		}
		else if (!dashboardSetId.equals(other.dashboardSetId)) {
			return false;
		}
		if (position == null) {
			if (other.position != null) {
				return false;
			}
		}
		else if (!position.equals(other.position)) {
			return false;
		}
		if (subDashboardId == null) {
			if (other.subDashboardId != null) {
				return false;
			}
		}
		else if (!subDashboardId.equals(other.subDashboardId)) {
			return false;
		}
		if (tenantId == null) {
			if (other.tenantId != null) {
				return false;
			}
		}
		else if (!tenantId.equals(other.tenantId)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the dashboardSetId
	 */
	public Long getDashboardSetId()
	{
		return dashboardSetId;
	}

	/**
	 * @return the position
	 */
	public Long getPosition()
	{
		return position;
	}

	/**
	 * @return the subDashboardId
	 */
	public Long getSubDashboardId()
	{
		return subDashboardId;
	}

	/**
	 * @return the tenantId
	 */
	public Long getTenantId()
	{
		return tenantId;
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


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (dashboardSetId == null ? 0 : dashboardSetId.hashCode());
		result = prime * result + (position == null ? 0 : position.hashCode());
		result = prime * result + (subDashboardId == null ? 0 : subDashboardId.hashCode());
		result = prime * result + (tenantId == null ? 0 : tenantId.hashCode());
		result = prime * result + (creationDate == null ? 0 : creationDate.hashCode());
		result = prime * result + (lastModificationDate == null ? 0 : lastModificationDate.hashCode());
		return result;
	}

	/**
	 * @param dashboardSetId
	 *            the dashboardSetId to set
	 */
	public void setDashboardSetId(Long dashboardSetId)
	{
		this.dashboardSetId = dashboardSetId;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Long position)
	{
		this.position = position;
	}

	/**
	 * @param subDashboardId
	 *            the subDashboardId to set
	 */
	public void setSubDashboardId(Long subDashboardId)
	{
		this.subDashboardId = subDashboardId;
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
		return "DashboardSetRowEntity [dashboardSetId=" + dashboardSetId + ", tenantId=" + tenantId + ", subDashboardId="
				+ subDashboardId + ", position=" + position + ", creationDate=" + creationDate + ", lastModificationDate=" + lastModificationDate+"]";
	}
}
