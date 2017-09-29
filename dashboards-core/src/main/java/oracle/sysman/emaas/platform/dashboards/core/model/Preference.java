/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.model;

import java.util.*;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.util.DateUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;

/**
 * @author wenjzhu
 */
public class Preference
{
	public static final String PREF_KEY_HM_DBMGMT_SHOW 					= "uifwk.hm.dbmgmt.show";
	public static final String PREF_KEY_HM_FEDERATION_SHOW 				= "uifwk.hm.federation.show";
	public static final Map<String, String> FEATURE_SHOW_PREF_DEFAULT_VALUES = new HashMap<String, String>();
	public static final List<String> FEATURE_SHOW_PREF_SUPPORTED_KEYS 	= new ArrayList<String>();

	public static final String KEY_REGEX = "[\\w\\-\\.]{1,256}"; // a to Z _ - .

	static {
		FEATURE_SHOW_PREF_DEFAULT_VALUES.put(PREF_KEY_HM_DBMGMT_SHOW, "true");
		FEATURE_SHOW_PREF_DEFAULT_VALUES.put(PREF_KEY_HM_FEDERATION_SHOW, "true");
		FEATURE_SHOW_PREF_SUPPORTED_KEYS.addAll(FEATURE_SHOW_PREF_DEFAULT_VALUES.keySet());
	}

	public static Preference valueOf(EmsPreference ep)
	{
		if (ep == null) {
			return null;
		}
		Preference p = new Preference();
		p.setKey(ep.getPrefKey());
		p.setValue(ep.getPrefValue());
		return p;
	}

	private String href;

	private String key;
	private String value;

	public Preference()
	{
		super();
	}

	/**
	 * @return the href
	 */
	public String getHref()
	{
		return href;
	}

	/**
	 * @return the key
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @param href
	 *            the href to set
	 */
	public void setHref(String href)
	{
		this.href = href;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key)
	{
		this.key = key;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	public EmsPreference toEntity(EmsPreference setEntity, String userName) throws DashboardException
	{
		EmsPreference te;
		Date date = DateUtil.getGatewayTime();
		if (setEntity == null) {
			te = new EmsPreference();
			te.setCreationDate(date);
		} else {
			te = setEntity;
		}
		te.setLastModificationDate(date);

		if (key == null || !key.matches(KEY_REGEX)) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.PREFERENCE_INVALID_KEY));
		}

		if (value == null || value.length() > 256) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.PREFERENCE_INVALID_VALUE));
		}

		te.setPrefKey(key);
		te.setPrefValue(value);
		te.setUserName(userName);

		return te;

	}

}
