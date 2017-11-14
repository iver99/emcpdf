/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 */
public interface IMessageUtil extends IUiTestCommonAPI
{

	public static final String CONFIRMMESSAGECSS = ".emaas-appheader-message-box-confirm";
	public static final String WARNINGMESSAGECSS = ".emaas-appheader-message-box-warn";
	public static final String ERRORMESSAGECSS = ".emaas-appheader-message-box-error";
	public static final String CLOSECSS = ".emaas-appheader-message-icon-clear";
	public static final String MESSAGECONTENT = ".emaas-appheader-message-detail";

	/**
	 * Close message banner
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public void closeMessage(WebDriver driver);

	/**
	 * Verify the content in confirm message
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean verifyConfirmMessage(WebDriver driver);

	/**
	 * Verify the content in confirm message
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean verifyConfirmMessage(WebDriver driver, String message);

	/**
	 * Verify the content in Error message
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean verifyErrorMessage(WebDriver driver);

	/**
	 * Verify the content in Error message
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean verifyErrorMessage(WebDriver driver, String message);

	/**
	 * Verify the content in warning message
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean verifyWarningMessage(WebDriver driver);

	/**
	 * Verify the content in warning message
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean verifyWarningMessage(WebDriver driver, String message);

}
