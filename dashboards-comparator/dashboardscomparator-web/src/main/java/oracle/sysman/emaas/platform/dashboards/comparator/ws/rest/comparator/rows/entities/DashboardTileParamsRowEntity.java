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
 * PARAM_NAME
 * TENANT_ID
 * IS_SYSTEM
 * PARAM_TYPE
 * PARAM_VALUE_STR
 * PARAM_VALUE_NUM
 * PARAM_VALUE_TIMESTAMP
 * TILE_ID
 * CREATION_DATE
 * LAST_MODIFICATION_DATE
 * DELETED
 */
public class DashboardTileParamsRowEntity implements RowEntity
{
	@JsonProperty("TILE_ID")
	private String tileId;

	@JsonProperty("PARAM_NAME")
	private String paramName;

	@JsonProperty("TENANT_ID")
	private Long tenantId;

	@JsonProperty("IS_SYSTEM")
	private Integer isSystem;

	@JsonProperty("PARAM_TYPE")
	private Long paramType;

	@JsonProperty("PARAM_VALUE_STR")
	private String paramValueStr;

	@JsonProperty("PARAM_VALUE_NUM")
	private Long paramValueNum;

	@JsonProperty("PARAM_VALUE_TIMESTAMP")
	private String paramValueTimestamp;

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
	 * @return the isSystem
	 */
	public Integer getIsSystem()
	{
		return isSystem;
	}

	/**
	 * @return the paramName
	 */
	public String getParamName()
	{
		return paramName;
	}

	/**
	 * @return the paramType
	 */
	public Long getParamType()
	{
		return paramType;
	}

	/**
	 * @return the paramValueNum
	 */
	public Long getParamValueNum()
	{
		return paramValueNum;
	}

	/**
	 * @return the paramValueStr
	 */
	public String getParamValueStr()
	{
		return paramValueStr;
	}

	/**
	 * @return the paramValueTimestamp
	 */
	public String getParamValueTimestamp()
	{
		return paramValueTimestamp;
	}

	/**
	 * @return the tenantId
	 */
	public Long getTenantId()
	{
		return tenantId;
	}

	/**
	 * @return the tileId
	 */
	public String getTileId()
	{
		return tileId;
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
	 * @param isSystem
	 *            the isSystem to set
	 */
	public void setIsSystem(Integer isSystem)
	{
		this.isSystem = isSystem;
	}

	/**
	 * @param paramName
	 *            the paramName to set
	 */
	public void setParamName(String paramName)
	{
		this.paramName = paramName;
	}

	/**
	 * @param paramType
	 *            the paramType to set
	 */
	public void setParamType(Long paramType)
	{
		this.paramType = paramType;
	}

	/**
	 * @param paramValueNum
	 *            the paramValueNum to set
	 */
	public void setParamValueNum(Long paramValueNum)
	{
		this.paramValueNum = paramValueNum;
	}

	/**
	 * @param paramValueStr
	 *            the paramValueStr to set
	 */
	public void setParamValueStr(String paramValueStr)
	{
		this.paramValueStr = paramValueStr;
	}

	/**
	 * @param paramValueTimestamp
	 *            the paramValueTimestamp to set
	 */
	public void setParamValueTimestamp(String paramValueTimestamp)
	{
		this.paramValueTimestamp = paramValueTimestamp;
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
	 * @param tileId
	 *            the tileId to set
	 */
	public void setTileId(String tileId)
	{
		this.tileId = tileId;
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
		return "DashboardTileParamsRowEntity [tileId=" + tileId + ", paramName=" + paramName + ", tenantId=" + tenantId
				+ ", isSystem=" + isSystem + ", paramType=" + paramType + ", paramValueStr=" + paramValueStr + ", paramValueNum="
				+ paramValueNum + ", paramValueTimestamp=" + paramValueTimestamp + ", creationDate=" + creationDate + ", deleted=" + deleted + " lastModificationDate=" + lastModificationDate+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((deleted == null) ? 0 : deleted.hashCode());
		result = prime * result
				+ ((isSystem == null) ? 0 : isSystem.hashCode());
		result = prime
				* result
				+ ((lastModificationDate == null) ? 0 : lastModificationDate
						.hashCode());
		result = prime * result
				+ ((paramName == null) ? 0 : paramName.hashCode());
		result = prime * result
				+ ((paramType == null) ? 0 : paramType.hashCode());
		result = prime * result
				+ ((paramValueNum == null) ? 0 : paramValueNum.hashCode());
		result = prime * result
				+ ((paramValueStr == null) ? 0 : paramValueStr.hashCode());
		result = prime
				* result
				+ ((paramValueTimestamp == null) ? 0 : paramValueTimestamp
						.hashCode());
		result = prime * result
				+ ((tenantId == null) ? 0 : tenantId.hashCode());
		result = prime * result + ((tileId == null) ? 0 : tileId.hashCode());
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
		DashboardTileParamsRowEntity other = (DashboardTileParamsRowEntity) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (deleted == null) {
			if (other.deleted != null)
				return false;
		} else if (!deleted.equals(other.deleted))
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
		if (paramName == null) {
			if (other.paramName != null)
				return false;
		} else if (!paramName.equals(other.paramName))
			return false;
		if (paramType == null) {
			if (other.paramType != null)
				return false;
		} else if (!paramType.equals(other.paramType))
			return false;
		if (paramValueNum == null) {
			if (other.paramValueNum != null)
				return false;
		} else if (!paramValueNum.equals(other.paramValueNum))
			return false;
		if (paramValueStr == null) {
			if (other.paramValueStr != null)
				return false;
		} else if (!paramValueStr.equals(other.paramValueStr))
			return false;
		if (paramValueTimestamp == null) {
			if (other.paramValueTimestamp != null)
				return false;
		} else if (!paramValueTimestamp.equals(other.paramValueTimestamp))
			return false;
		if (tenantId == null) {
			if (other.tenantId != null)
				return false;
		} else if (!tenantId.equals(other.tenantId))
			return false;
		if (tileId == null) {
			if (other.tileId != null)
				return false;
		} else if (!tileId.equals(other.tileId))
			return false;
		return true;
	}
	
	
}
