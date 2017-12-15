/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.webutils.dependency;

public class DependencyStatus
{
	private volatile Boolean databaseUp;
	private volatile Boolean entityNamingUp;
	
	private static final DependencyStatus INSTANCE = new DependencyStatus();
	
	private DependencyStatus() {
		databaseUp = Boolean.FALSE;
		entityNamingUp = Boolean.FALSE;
	}

	public static DependencyStatus getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @return the databaseUp
	 */
	public Boolean isDatabaseUp()
	{
		return databaseUp;
	}

	/**
	 * @return the entityNamingUp
	 */
	public Boolean isEntityNamingUp()
	{
		return entityNamingUp;
	}

	/**
	 * @param databaseUp the databaseUp to set
	 */
	public void setDatabaseUp(Boolean databaseUp)
	{
		this.databaseUp = databaseUp;
	}

	/**
	 * @param entityNamingUp the entityNamingUp to set
	 */
	public void setEntityNamingUp(Boolean entityNamingUp)
	{
		this.entityNamingUp = entityNamingUp;
	}
}
