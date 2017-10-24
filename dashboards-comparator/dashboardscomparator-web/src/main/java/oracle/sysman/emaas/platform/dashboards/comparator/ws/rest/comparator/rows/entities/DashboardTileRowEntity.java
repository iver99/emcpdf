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
 * CREATION_DATE
 * LAST_MODIFICATION_DATE
 * LAST_MODIFIED_BY
 * OWNER
 * TITLE
 * HEIGHT
 * WIDTH
 * IS_MAXIMIZED
 * POSITION
 * TENANT_ID
 * WIDGET_UNIQUE_ID
 * WIDGET_NAME
 * WIDGET_DESCRIPTION
 * WIDGET_GROUP_NAME
 * WIDGET_ICON
 * WIDGET_HISTOGRAM
 * WIDGET_OWNER
 * WIDGET_CREATION_TIME
 * WIDGET_SOURCE
 * WIDGET_KOC_NAME
 * WIDGET_VIEWMODE
 * WIDGET_TEMPLATE
 * PROVIDER_NAME
 * PROVIDER_VERSION
 * PROVIDER_ASSET_ROOT
 * TILE_ROW
 * TILE_COLUMN
 * TYPE
 * WIDGET_SUPPORT_TIME_CONTROL
 * WIDGET_LINKED_DASHBOARD
 * WIDGET_DELETED
 * WIDGET_DELETION_DATE
 * TILE_ID
 * DELETED
 */
public class DashboardTileRowEntity implements RowEntity
{
	@JsonProperty("TILE_ID")
	private String tileId;

	@JsonProperty("DASHBOARD_ID")
	private String dashboardId;

	@JsonProperty("CREATION_DATE")
	private String creationDate;

	@JsonProperty("LAST_MODIFICATION_DATE")
	private String lastModificationDate;

	@JsonProperty("LAST_MODIFIED_BY")
	private String lastModifiedBy;

	@JsonProperty("OWNER")
	private String owner;

	@JsonProperty("TITLE")
	private String title;

	@JsonProperty("HEIGHT")
	private Long height;

	@JsonProperty("WIDTH")
	private Long width;

	@JsonProperty("IS_MAXIMIZED")
	private Integer isMaximized;

	@JsonProperty("POSITION")
	private Long position;

	@JsonProperty("TENANT_ID")
	private Long tenantId;

	@JsonProperty("WIDGET_UNIQUE_ID")
	private String widgetUniqueId;

	@JsonProperty("WIDGET_NAME")
	private String widgetName;

	@JsonProperty("WIDGET_DESCRIPTION")
	private String widgetDescription;

	@JsonProperty("WIDGET_GROUP_NAME")
	private String widgetGroupName;

	@JsonProperty("WIDGET_ICON")
	private String widgetIcon;

	@JsonProperty("WIDGET_HISTOGRAM")
	private String widgetHistogram;

	@JsonProperty("WIDGET_OWNER")
	private String widgetOwner;

	@JsonProperty("WIDGET_CREATION_TIME")
	private String widgetCreationTime;

	@JsonProperty("WIDGET_SOURCE")
	private Long widgetSource;

	@JsonProperty("WIDGET_KOC_NAME")
	private String widgetKocName;

	@JsonProperty("WIDGET_VIEWMODE")
	private String widgetViewmode;

	@JsonProperty("WIDGET_TEMPLATE")
	private String widgetTemplate;

	@JsonProperty("PROVIDER_NAME")
	private String providerName;

	@JsonProperty("PROVIDER_VERSION")
	private String providerVersion;

	@JsonProperty("PROVIDER_ASSET_ROOT")
	private String providerAssetRoot;

	@JsonProperty("TILE_ROW")
	private Long tileRow;

	@JsonProperty("TILE_COLUMN")
	private Long tileColumn;

	@JsonProperty("TYPE")
	private Long type;

	@JsonProperty("WIDGET_SUPPORT_TIME_CONTROL")
	private Integer widgetSupportTimeControl;

	@JsonProperty("WIDGET_LINKED_DASHBOARD")
	private Long widgetLinkedDashboard;
	
	@JsonProperty("WIDGET_DELETED")
	private Integer widgetDeleted;
	
	@JsonProperty("WIDGET_DELETION_DATE")
	private String widgetDeletionDate;
	
	@JsonProperty("DELETED")
	private Integer deleted;
	
	

	public Integer getWidgetDeleted() {
		return widgetDeleted;
	}

	public void setWidgetDeleted(Integer widgetDeleted) {
		this.widgetDeleted = widgetDeleted;
	}

	public String getWidgetDeletionDate() {
		return widgetDeletionDate;
	}

	public void setWidgetDeletionDate(String widgetDeletionDate) {
		this.widgetDeletionDate = widgetDeletionDate;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
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
	 * @return the height
	 */
	public Long getHeight()
	{
		return height;
	}

	/**
	 * @return the isMaximized
	 */
	public Integer getIsMaximized()
	{
		return isMaximized;
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
	 * @return the owner
	 */
	public String getOwner()
	{
		return owner;
	}

	/**
	 * @return the position
	 */
	public Long getPosition()
	{
		return position;
	}

	/**
	 * @return the providerAssetRoot
	 */
	public String getProviderAssetRoot()
	{
		return providerAssetRoot;
	}

	/**
	 * @return the providerName
	 */
	public String getProviderName()
	{
		return providerName;
	}

	/**
	 * @return the providerVersion
	 */
	public String getProviderVersion()
	{
		return providerVersion;
	}

	/**
	 * @return the tenantId
	 */
	public Long getTenantId()
	{
		return tenantId;
	}

	/**
	 * @return the tileColumn
	 */
	public Long getTileColumn()
	{
		return tileColumn;
	}

	/**
	 * @return the tileId
	 */
	public String getTileId()
	{
		return tileId;
	}

	/**
	 * @return the tileRow
	 */
	public Long getTileRow()
	{
		return tileRow;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @return the type
	 */
	public Long getType()
	{
		return type;
	}

	/**
	 * @return the widgetCreationTime
	 */
	public String getWidgetCreationTime()
	{
		return widgetCreationTime;
	}

	/**
	 * @return the widgetDescription
	 */
	public String getWidgetDescription()
	{
		return widgetDescription;
	}

	/**
	 * @return the widgetGroupName
	 */
	public String getWidgetGroupName()
	{
		return widgetGroupName;
	}

	/**
	 * @return the widgetHistogram
	 */
	public String getWidgetHistogram()
	{
		return widgetHistogram;
	}

	/**
	 * @return the widgetIcon
	 */
	public String getWidgetIcon()
	{
		return widgetIcon;
	}

	/**
	 * @return the widgetKocName
	 */
	public String getWidgetKocName()
	{
		return widgetKocName;
	}

	/**
	 * @return the widgetLinkedDashboard
	 */
	public Long getWidgetLinkedDashboard()
	{
		return widgetLinkedDashboard;
	}

	/**
	 * @return the widgetName
	 */
	public String getWidgetName()
	{
		return widgetName;
	}

	/**
	 * @return the widgetOwner
	 */
	public String getWidgetOwner()
	{
		return widgetOwner;
	}

	/**
	 * @return the widgetSource
	 */
	public Long getWidgetSource()
	{
		return widgetSource;
	}

	/**
	 * @return the widgetSupportTimeControl
	 */
	public Integer getWidgetSupportTimeControl()
	{
		return widgetSupportTimeControl;
	}

	/**
	 * @return the widgetTemplate
	 */
	public String getWidgetTemplate()
	{
		return widgetTemplate;
	}

	/**
	 * @return the widgetUniqueId
	 */
	public String getWidgetUniqueId()
	{
		return widgetUniqueId;
	}

	/**
	 * @return the widgetViewmode
	 */
	public String getWidgetViewmode()
	{
		return widgetViewmode;
	}

	/**
	 * @return the width
	 */
	public Long getWidth()
	{
		return width;
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
	 * @param height
	 *            the height to set
	 */
	public void setHeight(Long height)
	{
		this.height = height;
	}

	/**
	 * @param isMaximized
	 *            the isMaximized to set
	 */
	public void setIsMaximized(Integer isMaximized)
	{
		this.isMaximized = isMaximized;
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
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(String owner)
	{
		this.owner = owner;
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
	 * @param providerAssetRoot
	 *            the providerAssetRoot to set
	 */
	public void setProviderAssetRoot(String providerAssetRoot)
	{
		this.providerAssetRoot = providerAssetRoot;
	}

	/**
	 * @param providerName
	 *            the providerName to set
	 */
	public void setProviderName(String providerName)
	{
		this.providerName = providerName;
	}

	/**
	 * @param providerVersion
	 *            the providerVersion to set
	 */
	public void setProviderVersion(String providerVersion)
	{
		this.providerVersion = providerVersion;
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
	 * @param tileColumn
	 *            the tileColumn to set
	 */
	public void setTileColumn(Long tileColumn)
	{
		this.tileColumn = tileColumn;
	}

	/**
	 * @param tileId
	 *            the tileId to set
	 */
	public void setTileId(String tileId)
	{
		this.tileId = tileId;
	}

	/**
	 * @param tileRow
	 *            the tileRow to set
	 */
	public void setTileRow(Long tileRow)
	{
		this.tileRow = tileRow;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Long type)
	{
		this.type = type;
	}

	/**
	 * @param widgetCreationTime
	 *            the widgetCreationTime to set
	 */
	public void setWidgetCreationTime(String widgetCreationTime)
	{
		this.widgetCreationTime = widgetCreationTime;
	}

	/**
	 * @param widgetDescription
	 *            the widgetDescription to set
	 */
	public void setWidgetDescription(String widgetDescription)
	{
		this.widgetDescription = widgetDescription;
	}

	/**
	 * @param widgetGroupName
	 *            the widgetGroupName to set
	 */
	public void setWidgetGroupName(String widgetGroupName)
	{
		this.widgetGroupName = widgetGroupName;
	}

	/**
	 * @param widgetHistogram
	 *            the widgetHistogram to set
	 */
	public void setWidgetHistogram(String widgetHistogram)
	{
		this.widgetHistogram = widgetHistogram;
	}

	/**
	 * @param widgetIcon
	 *            the widgetIcon to set
	 */
	public void setWidgetIcon(String widgetIcon)
	{
		this.widgetIcon = widgetIcon;
	}

	/**
	 * @param widgetKocName
	 *            the widgetKocName to set
	 */
	public void setWidgetKocName(String widgetKocName)
	{
		this.widgetKocName = widgetKocName;
	}

	/**
	 * @param widgetLinkedDashboard
	 *            the widgetLinkedDashboard to set
	 */
	public void setWidgetLinkedDashboard(Long widgetLinkedDashboard)
	{
		this.widgetLinkedDashboard = widgetLinkedDashboard;
	}

	/**
	 * @param widgetName
	 *            the widgetName to set
	 */
	public void setWidgetName(String widgetName)
	{
		this.widgetName = widgetName;
	}

	/**
	 * @param widgetOwner
	 *            the widgetOwner to set
	 */
	public void setWidgetOwner(String widgetOwner)
	{
		this.widgetOwner = widgetOwner;
	}

	/**
	 * @param widgetSource
	 *            the widgetSource to set
	 */
	public void setWidgetSource(Long widgetSource)
	{
		this.widgetSource = widgetSource;
	}

	/**
	 * @param widgetSupportTimeControl
	 *            the widgetSupportTimeControl to set
	 */
	public void setWidgetSupportTimeControl(Integer widgetSupportTimeControl)
	{
		this.widgetSupportTimeControl = widgetSupportTimeControl;
	}

	/**
	 * @param widgetTemplate
	 *            the widgetTemplate to set
	 */
	public void setWidgetTemplate(String widgetTemplate)
	{
		this.widgetTemplate = widgetTemplate;
	}

	/**
	 * @param widgetUniqueId
	 *            the widgetUniqueId to set
	 */
	public void setWidgetUniqueId(String widgetUniqueId)
	{
		this.widgetUniqueId = widgetUniqueId;
	}

	/**
	 * @param widgetViewmode
	 *            the widgetViewmode to set
	 */
	public void setWidgetViewmode(String widgetViewmode)
	{
		this.widgetViewmode = widgetViewmode;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(Long width)
	{
		this.width = width;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "DashboardTileRowEntity [tileId=" + tileId + ", dashboardId=" + dashboardId + ", creationDate=" + creationDate
				+ ", lastModificationDate=" + lastModificationDate + ", lastModifiedBy=" + lastModifiedBy + ", owner=" + owner
				+ ", title=" + title + ", height=" + height + ", width=" + width + ", isMaximized=" + isMaximized + ", position="
				+ position + ", tenantId=" + tenantId + ", widgetUniqueId=" + widgetUniqueId + ", widgetName=" + widgetName
				+ ", widgetDescription=" + widgetDescription + ", widgetGroupName=" + widgetGroupName + ", widgetIcon="
				+ widgetIcon + ", widgetHistogram=" + widgetHistogram + ", widgetOwner=" + widgetOwner + ", widgetCreationTime="
				+ widgetCreationTime + ", widgetSource=" + widgetSource + ", widgetKocName=" + widgetKocName + ", widgetViewmode="
				+ widgetViewmode + ", widgetTemplate=" + widgetTemplate + ", providerName=" + providerName + ", providerVersion="
				+ providerVersion + ", providerAssetRoot=" + providerAssetRoot + ", tileRow=" + tileRow + ", tileColumn="
				+ tileColumn + ", type=" + type + ", widgetSupportTimeControl=" + widgetSupportTimeControl + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result
				+ ((dashboardId == null) ? 0 : dashboardId.hashCode());
		result = prime * result + ((deleted == null) ? 0 : deleted.hashCode());
		result = prime * result + ((height == null) ? 0 : height.hashCode());
		result = prime * result
				+ ((isMaximized == null) ? 0 : isMaximized.hashCode());
		result = prime
				* result
				+ ((lastModificationDate == null) ? 0 : lastModificationDate
						.hashCode());
		result = prime * result
				+ ((lastModifiedBy == null) ? 0 : lastModifiedBy.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		result = prime
				* result
				+ ((providerAssetRoot == null) ? 0 : providerAssetRoot
						.hashCode());
		result = prime * result
				+ ((providerName == null) ? 0 : providerName.hashCode());
		result = prime * result
				+ ((providerVersion == null) ? 0 : providerVersion.hashCode());
		result = prime * result
				+ ((tenantId == null) ? 0 : tenantId.hashCode());
		result = prime * result
				+ ((tileColumn == null) ? 0 : tileColumn.hashCode());
		result = prime * result + ((tileId == null) ? 0 : tileId.hashCode());
		result = prime * result + ((tileRow == null) ? 0 : tileRow.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime
				* result
				+ ((widgetCreationTime == null) ? 0 : widgetCreationTime
						.hashCode());
		result = prime * result
				+ ((widgetDeleted == null) ? 0 : widgetDeleted.hashCode());
		result = prime
				* result
				+ ((widgetDeletionDate == null) ? 0 : widgetDeletionDate
						.hashCode());
		result = prime
				* result
				+ ((widgetDescription == null) ? 0 : widgetDescription
						.hashCode());
		result = prime * result
				+ ((widgetGroupName == null) ? 0 : widgetGroupName.hashCode());
		result = prime * result
				+ ((widgetHistogram == null) ? 0 : widgetHistogram.hashCode());
		result = prime * result
				+ ((widgetIcon == null) ? 0 : widgetIcon.hashCode());
		result = prime * result
				+ ((widgetKocName == null) ? 0 : widgetKocName.hashCode());
		result = prime
				* result
				+ ((widgetLinkedDashboard == null) ? 0 : widgetLinkedDashboard
						.hashCode());
		result = prime * result
				+ ((widgetName == null) ? 0 : widgetName.hashCode());
		result = prime * result
				+ ((widgetOwner == null) ? 0 : widgetOwner.hashCode());
		result = prime * result
				+ ((widgetSource == null) ? 0 : widgetSource.hashCode());
		result = prime
				* result
				+ ((widgetSupportTimeControl == null) ? 0
						: widgetSupportTimeControl.hashCode());
		result = prime * result
				+ ((widgetTemplate == null) ? 0 : widgetTemplate.hashCode());
		result = prime * result
				+ ((widgetUniqueId == null) ? 0 : widgetUniqueId.hashCode());
		result = prime * result
				+ ((widgetViewmode == null) ? 0 : widgetViewmode.hashCode());
		result = prime * result + ((width == null) ? 0 : width.hashCode());
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
		DashboardTileRowEntity other = (DashboardTileRowEntity) obj;
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
		if (height == null) {
			if (other.height != null)
				return false;
		} else if (!height.equals(other.height))
			return false;
		if (isMaximized == null) {
			if (other.isMaximized != null)
				return false;
		} else if (!isMaximized.equals(other.isMaximized))
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
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (providerAssetRoot == null) {
			if (other.providerAssetRoot != null)
				return false;
		} else if (!providerAssetRoot.equals(other.providerAssetRoot))
			return false;
		if (providerName == null) {
			if (other.providerName != null)
				return false;
		} else if (!providerName.equals(other.providerName))
			return false;
		if (providerVersion == null) {
			if (other.providerVersion != null)
				return false;
		} else if (!providerVersion.equals(other.providerVersion))
			return false;
		if (tenantId == null) {
			if (other.tenantId != null)
				return false;
		} else if (!tenantId.equals(other.tenantId))
			return false;
		if (tileColumn == null) {
			if (other.tileColumn != null)
				return false;
		} else if (!tileColumn.equals(other.tileColumn))
			return false;
		if (tileId == null) {
			if (other.tileId != null)
				return false;
		} else if (!tileId.equals(other.tileId))
			return false;
		if (tileRow == null) {
			if (other.tileRow != null)
				return false;
		} else if (!tileRow.equals(other.tileRow))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (widgetCreationTime == null) {
			if (other.widgetCreationTime != null)
				return false;
		} else if (!widgetCreationTime.equals(other.widgetCreationTime))
			return false;
		if (widgetDeleted == null) {
			if (other.widgetDeleted != null)
				return false;
		} else if (!widgetDeleted.equals(other.widgetDeleted))
			return false;
		if (widgetDeletionDate == null) {
			if (other.widgetDeletionDate != null)
				return false;
		} else if (!widgetDeletionDate.equals(other.widgetDeletionDate))
			return false;
		if (widgetDescription == null) {
			if (other.widgetDescription != null)
				return false;
		} else if (!widgetDescription.equals(other.widgetDescription))
			return false;
		if (widgetGroupName == null) {
			if (other.widgetGroupName != null)
				return false;
		} else if (!widgetGroupName.equals(other.widgetGroupName))
			return false;
		if (widgetHistogram == null) {
			if (other.widgetHistogram != null)
				return false;
		} else if (!widgetHistogram.equals(other.widgetHistogram))
			return false;
		if (widgetIcon == null) {
			if (other.widgetIcon != null)
				return false;
		} else if (!widgetIcon.equals(other.widgetIcon))
			return false;
		if (widgetKocName == null) {
			if (other.widgetKocName != null)
				return false;
		} else if (!widgetKocName.equals(other.widgetKocName))
			return false;
		if (widgetLinkedDashboard == null) {
			if (other.widgetLinkedDashboard != null)
				return false;
		} else if (!widgetLinkedDashboard.equals(other.widgetLinkedDashboard))
			return false;
		if (widgetName == null) {
			if (other.widgetName != null)
				return false;
		} else if (!widgetName.equals(other.widgetName))
			return false;
		if (widgetOwner == null) {
			if (other.widgetOwner != null)
				return false;
		} else if (!widgetOwner.equals(other.widgetOwner))
			return false;
		if (widgetSource == null) {
			if (other.widgetSource != null)
				return false;
		} else if (!widgetSource.equals(other.widgetSource))
			return false;
		if (widgetSupportTimeControl == null) {
			if (other.widgetSupportTimeControl != null)
				return false;
		} else if (!widgetSupportTimeControl
				.equals(other.widgetSupportTimeControl))
			return false;
		if (widgetTemplate == null) {
			if (other.widgetTemplate != null)
				return false;
		} else if (!widgetTemplate.equals(other.widgetTemplate))
			return false;
		if (widgetUniqueId == null) {
			if (other.widgetUniqueId != null)
				return false;
		} else if (!widgetUniqueId.equals(other.widgetUniqueId))
			return false;
		if (widgetViewmode == null) {
			if (other.widgetViewmode != null)
				return false;
		} else if (!widgetViewmode.equals(other.widgetViewmode))
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
			return false;
		return true;
	}
	
	
}
