/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.zdt;

/**
 * @author guochen
 */
public class ZDTEntity
{
	private long countOfDashboards;
	private long countOfUserOptions;
	private long countOfPreference;
	private long countOfDashboardSet;
	private long countOfTile;
	private long countOfTileParam;

	public ZDTEntity()
	{
	}

	public ZDTEntity(long countOfDashboards, long countOfUserOptions,
			long countOfPreference, long countOfDashboardSet, long countOfTile,
			long countOfTileParam) {
		super();
		this.countOfDashboards = countOfDashboards;
		this.countOfUserOptions = countOfUserOptions;
		this.countOfPreference = countOfPreference;
		this.countOfDashboardSet = countOfDashboardSet;
		this.countOfTile = countOfTile;
		this.countOfTileParam = countOfTileParam;
	}

	public long getCountOfDashboards() {
		return countOfDashboards;
	}

	public void setCountOfDashboards(long countOfDashboards) {
		this.countOfDashboards = countOfDashboards;
	}

	public long getCountOfUserOptions() {
		return countOfUserOptions;
	}

	public void setCountOfUserOptions(long countOfUserOptions) {
		this.countOfUserOptions = countOfUserOptions;
	}

	public long getCountOfPreference() {
		return countOfPreference;
	}

	public void setCountOfPreference(long countOfPreference) {
		this.countOfPreference = countOfPreference;
	}

	public long getCountOfDashboardSet() {
		return countOfDashboardSet;
	}

	public void setCountOfDashboardSet(long countOfDashboardSet) {
		this.countOfDashboardSet = countOfDashboardSet;
	}

	public long getCountOfTile() {
		return countOfTile;
	}

	public void setCountOfTile(long countOfTile) {
		this.countOfTile = countOfTile;
	}

	public long getCountOfTileParam() {
		return countOfTileParam;
	}

	public void setCountOfTileParam(long countOfTileParam) {
		this.countOfTileParam = countOfTileParam;
	}

	
}
