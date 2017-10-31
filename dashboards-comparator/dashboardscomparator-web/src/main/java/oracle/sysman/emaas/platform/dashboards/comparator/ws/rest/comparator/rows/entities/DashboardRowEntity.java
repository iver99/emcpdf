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
 * DASHBOARD_ID
 * NAME
 * TYPE
 * DESCRIPTION
 * CREATION_DATE
 * LAST_MODIFICATION_DATE
 * LAST_MODIFIED_BY
 * OWNER
 * IS_SYSTEM
 * APPLICATION_TYPE
 * ENABLE_TIME_RANGE
 * SCREEN_SHOT
 * DELETED
 * TENANT_ID
 * ENABLE_REFRESH
 * SHARE_PUBLIC
 * ENABLE_ENTITY_FILTER
 * ENABLE_DESCRIPTION
 * EXTENDED_OPTIONS
 * SHOW_INHOME
 *
 *
 */
public class DashboardRowEntity implements RowEntity
{
	@JsonProperty("DASHBOARD_ID")
	private String dashboardId;

	@JsonProperty("NAME")
	private String name;

	@JsonProperty("TYPE")
	private Long type;

	@JsonProperty("DESCRIPTION")
	private String description;

	@JsonProperty("CREATION_DATE")
	private String creationDate;

	@JsonProperty("LAST_MODIFICATION_DATE")
	private String lastModificationDate;

	@JsonProperty("LAST_MODIFIED_BY")
	private String lastModifiedBy;

	@JsonProperty("OWNER")
	private String owner;

	@JsonProperty("IS_SYSTEM")
	private Integer isSystem;

	@JsonProperty("APPLICATION_TYPE")
	private Integer applicationType;

	@JsonProperty("ENABLE_TIME_RANGE")
	private Integer enableTimeRange;

	@JsonProperty("SCREEN_SHOT")
	private String screenShot;

	@JsonProperty("DELETED")
	private String deleted;

	@JsonProperty("TENANT_ID")
	private Long tenantId;

	@JsonProperty("ENABLE_REFRESH")
	private Integer enableRefresh;

	@JsonProperty("SHARE_PUBLIC")
	private Integer sharePublic;

	@JsonProperty("ENABLE_ENTITY_FILTER")
	private Integer enableEntityFilter;

	@JsonProperty("ENABLE_DESCRIPTION")
	private Integer enableDescription;

	@JsonProperty("EXTENDED_OPTIONS")
	private String extendedOptions;
	
	@JsonProperty("SHOW_INHOME")
	private Integer showInHome;

	public DashboardRowEntity()
	{
	}

	/**
	 * @return the applicationType
	 */
	public Integer getApplicationType()
	{
		return applicationType;
	}

	/**
	 * @return the creationDate
	 */
	public String getCreationDate()
	{
		return creationDate;
	}

	/**
	 * @return the dashboardId
	 */
	public String getDashboardId()
	{
		return dashboardId;
	}

	/**
	 * @return the deleted
	 */
	public String getDeleted()
	{
		return deleted;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @return the enableDescription
	 */
	public Integer getEnableDescription()
	{
		return enableDescription;
	}

	/**
	 * @return the enableEntityFilter
	 */
	public Integer getEnableEntityFilter()
	{
		return enableEntityFilter;
	}

	/**
	 * @return the enableRefresh
	 */
	public Integer getEnableRefresh()
	{
		return enableRefresh;
	}

	/**
	 * @return the enableTimeRange
	 */
	public Integer getEnableTimeRange()
	{
		return enableTimeRange;
	}

	/**
	 * @return the extendedOptions
	 */
	public String getExtendedOptions()
	{
		return extendedOptions;
	}

	/**
	 * @return the isSystem
	 */
	public Integer getIsSystem()
	{
		return isSystem;
	}

	/**
	 * @return the lastModificationDate
	 */
	public String getLastModificationDate()
	{
		return lastModificationDate;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public String getLastModifiedBy()
	{
		return lastModifiedBy;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the owner
	 */
	public String getOwner()
	{
		return owner;
	}

	/**
	 * @return the screenShot
	 */
	public String getScreenShot()
	{
		return screenShot;
	}

	/**
	 * @return the sharePublic
	 */
	public Integer getSharePublic()
	{
		return sharePublic;
	}

	/**
	 * @return the tenantId
	 */
	public Long getTenantId()
	{
		return tenantId;
	}

	/**
	 * @return the type
	 */
	public Long getType()
	{
		return type;
	}

	

	/**
	 * @param applicationType
	 *            the applicationType to set
	 */
	public void setApplicationType(Integer applicationType)
	{
		this.applicationType = applicationType;
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
	 * @param dashboardId
	 *            the dashboardId to set
	 */
	public void setDashboardId(String dashboardId)
	{
		this.dashboardId = dashboardId;
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(String deleted)
	{
		this.deleted = deleted;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @param enableDescription
	 *            the enableDescription to set
	 */
	public void setEnableDescription(Integer enableDescription)
	{
		this.enableDescription = enableDescription;
	}

	/**
	 * @param enableEntityFilter
	 *            the enableEntityFilter to set
	 */
	public void setEnableEntityFilter(Integer enableEntityFilter)
	{
		this.enableEntityFilter = enableEntityFilter;
	}

	/**
	 * @param enableRefresh
	 *            the enableRefresh to set
	 */
	public void setEnableRefresh(Integer enableRefresh)
	{
		this.enableRefresh = enableRefresh;
	}

	/**
	 * @param enableTimeRange
	 *            the enableTimeRange to set
	 */
	public void setEnableTimeRange(Integer enableTimeRange)
	{
		this.enableTimeRange = enableTimeRange;
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
	 * @param isSystem
	 *            the isSystem to set
	 */
	public void setIsSystem(Integer isSystem)
	{
		this.isSystem = isSystem;
	}

	/**
	 * @param lastModificationDate
	 *            the lastModificationDate to set
	 */
	public void setLastModificationDate(String lastModificationDate)
	{
		this.lastModificationDate = lastModificationDate;
	}

	/**
	 * @param lastModifiedBy
	 *            the lastModifiedBy to set
	 */
	public void setLastModifiedBy(String lastModifiedBy)
	{
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	/**
	 * @param screenShot
	 *            the screenShot to set
	 */
	public void setScreenShot(String screenShot)
	{
		this.screenShot = screenShot;
	}

	/**
	 * @param sharePublic
	 *            the sharePublic to set
	 */
	public void setSharePublic(Integer sharePublic)
	{
		this.sharePublic = sharePublic;
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
	 * @param type
	 *            the type to set
	 */
	public void setType(Long type)
	{
		this.type = type;
	}
	
	

	public Integer getShowInHome() {
		return showInHome;
	}

	public void setShowInHome(Integer showInHome) {
		this.showInHome = showInHome;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "DashboardRowEntity [dashboardId=" + dashboardId + ", name=" + name + ", type=" + type + ", description="
				+ description + ", creationDate=" + creationDate + ", lastModificationDate=" + lastModificationDate
				+ ", lastModifiedBy=" + lastModifiedBy + ", owner=" + owner + ", isSystem=" + isSystem + ", applicationType="
				+ applicationType + ", enableTimeRange=" + enableTimeRange + ", screenShot=" + screenShot + ", deleted=" + deleted
				+ ", tenantId=" + tenantId + ", enableRefresh=" + enableRefresh + ", sharePublic=" + sharePublic
				+ ", enableEntityFilter=" + enableEntityFilter + ", enableDescription=" + enableDescription + ", extendedOptions="
				+ extendedOptions + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((applicationType == null) ? 0 : applicationType.hashCode());
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result
				+ ((dashboardId == null) ? 0 : dashboardId.hashCode());
		result = prime * result + ((deleted == null) ? 0 : deleted.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime
				* result
				+ ((enableDescription == null) ? 0 : enableDescription
						.hashCode());
		result = prime
				* result
				+ ((enableEntityFilter == null) ? 0 : enableEntityFilter
						.hashCode());
		result = prime * result
				+ ((enableRefresh == null) ? 0 : enableRefresh.hashCode());
		result = prime * result
				+ ((enableTimeRange == null) ? 0 : enableTimeRange.hashCode());
		result = prime * result
				+ ((extendedOptions == null) ? 0 : extendedOptions.hashCode());
		result = prime * result
				+ ((isSystem == null) ? 0 : isSystem.hashCode());
		result = prime
				* result
				+ ((lastModificationDate == null) ? 0 : lastModificationDate
						.hashCode());
		result = prime * result
				+ ((lastModifiedBy == null) ? 0 : lastModifiedBy.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result
				+ ((screenShot == null) ? 0 : screenShot.hashCode());
		result = prime * result
				+ ((sharePublic == null) ? 0 : sharePublic.hashCode());
		result = prime * result
				+ ((showInHome == null) ? 0 : showInHome.hashCode());
		result = prime * result
				+ ((tenantId == null) ? 0 : tenantId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		/**
		 * NOTE: getClass() means obj must be the EXACT the instance of this class
		 * (instanceof means this obj can be this class's instance or its sub-class instance)
		 *
		 */
		if (getClass() != obj.getClass())
			return false;
		DashboardRowEntity other = (DashboardRowEntity) obj;
		if (applicationType == null) {
			if (other.applicationType != null)
				return false;
		} else if (!applicationType.equals(other.applicationType))
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

		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;

		if (enableDescription == null) {
			if (other.enableDescription != null)
				return false;
		} else if (!enableDescription.equals(other.enableDescription))
			return false;

		if (enableEntityFilter == null) {
			if (other.enableEntityFilter != null)
				return false;
		} else if (!enableEntityFilter.equals(other.enableEntityFilter))
			return false;

		if (enableRefresh == null) {
			if (other.enableRefresh != null)
				return false;
		} else if (!enableRefresh.equals(other.enableRefresh))
			return false
					;
		if (enableTimeRange == null) {
			if (other.enableTimeRange != null)
				return false;
		} else if (!enableTimeRange.equals(other.enableTimeRange))
			return false;

		if (extendedOptions == null) {
			if (other.extendedOptions != null)
				return false;
		} else if (!extendedOptions.equals(other.extendedOptions))
			return false;

		if (isSystem == null) {
			if (other.isSystem != null)
				return false;
		} else if (!isSystem.equals(other.isSystem))
			return false;

		if (lastModificationDate == null) {
			if (other.lastModificationDate != null)
				return false;
		} else if (!lastModificationDate.equals(other.lastModificationDate))
			return false;

		if (lastModifiedBy == null) {
			if (other.lastModifiedBy != null)
				return false;
		} else if (!lastModifiedBy.equals(other.lastModifiedBy))
			return false;

		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;

		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;

		if (screenShot == null) {
			if (other.screenShot != null)
				return false;
		} else if (!screenShot.equals(other.screenShot))
			return false;

		if (sharePublic == null) {
			if (other.sharePublic != null)
				return false;
		} else if (!sharePublic.equals(other.sharePublic))
			return false;
		if (showInHome == null) {
			if (other.showInHome != null)
				return false;
		} else if (!showInHome.equals(other.showInHome))
			return false;

		if (tenantId == null) {
			if (other.tenantId != null)
				return false;
		} else if (!tenantId.equals(other.tenantId))
			return false;

		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;

		return true;
	}
	
	
	
	
}
