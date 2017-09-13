/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows;

/**
 * This class contains the compared result data from the 2 OMC instances, including the different compared result for specific
 * type <code>T</code> for each instance
 * 
 * @author guochen
 */
public class InstancesComparedData<T>
{
	private InstanceData<T> instance1;
	private InstanceData<T> instance2;

	/**
	 * @param instance1
	 * @param instance2
	 */
	public InstancesComparedData(InstanceData<T> instance1, InstanceData<T> instance2)
	{
		super();
		this.instance1 = instance1;
		this.instance2 = instance2;
	}

	/**
	 * @return the instance1
	 */
	public InstanceData<T> getInstance1()
	{
		return instance1;
	}

	/**
	 * @return the instance2
	 */
	public InstanceData<T> getInstance2()
	{
		return instance2;
	}

	public void setInstance1(InstanceData<T> instance1) {
		this.instance1 = instance1;
	}

	public void setInstance2(InstanceData<T> instance2) {
		this.instance2 = instance2;
	}

	
}
