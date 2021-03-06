/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.ArrayList;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import java.util.logging.Logger;

/**
 * @author cawei
 */
public class EntitySelectorUtil
{
        public static final String CATEGORY_COMPOSITE = IEntitySelectorUtil.CATEGORY_COMPOSITE;
	public static final String CATEGORY_ENTITIES = IEntitySelectorUtil.CATEGORY_ENTITIES;

	public static void clearContext(WebDriver driver, Logger logger)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.clearContext(driver, logger);
	}

	/**
	 * @param driver
	 * @param logger
	 * @return
	 */
	public static int getNumberOfPills(WebDriver driver, Logger logger)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		return esu.getNumberOfPills(driver, logger);
	}
        
        /**
         * @param driver
         * @param logger
         * @return 
         */
        public static ArrayList<String> getPillContents(WebDriver driver, Logger logger)
        {
                IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		return esu.getPillContents(driver, logger);
        }

	/**
	 * @param driver
	 * @param logger
	 */
	public static void openEntitySelector(WebDriver driver, Logger logger)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.openEntitySelector(driver, logger);
	}

	/**
	 * @param driver
	 * @param logger
         * @param indexOfPillToRemove
	 */
	public static void removePill(WebDriver driver, Logger logger, int indexOfPillToRemove)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.removePill(driver, logger, indexOfPillToRemove);
	}
        
        /**
         * 
         * @param driver
	 * @param logger
         * @param pillIndex
         * @param entityName
         * @param entityType
         * @param category 
         */
        public static void replacePillSelection(WebDriver driver, Logger logger, int pillIndex, String entityName, String entityType, String category) {
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
                esu.replacePillSelection(driver, logger, pillIndex, entityName, entityType, category);
        }
        
        /**
	 * @param driver
	 * @param logger
	 * @param entityName
	 */
	public static void searchText(WebDriver driver, Logger logger, String entityName)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.searchText(driver, logger, entityName);
	}

	/**
	 * @param driver
	 * @param logger
	 * @param entityName
         * @param entityType
         * @param category
	 */
	public static void searchText(WebDriver driver, Logger logger, String entityName, String entityType, String category)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.searchText(driver, logger, entityName, entityType, category);
	}

	/**
	 * @param driver
	 * @param logger
	 * @param entityName
         * @param entityType
	 */
	public static void selectCompositeEntity(WebDriver driver, Logger logger, String entityName, String entityType)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.selectCompositeEntity(driver, logger, entityName, entityType);
	}

	/**
	 * @param driver
	 * @param logger
	 * @param entityName
         * @param entityType
	 */
	public static void selectEntity(WebDriver driver, Logger logger, String entityName, String entityType)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.selectEntity(driver, logger, entityName, entityType);
	}

	/**
	 * @param driver
	 * @param logger
	 * @return
	 */
	public static boolean validateReadOnlyMode(WebDriver driver, Logger logger)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		return esu.validateReadOnlyMode(driver, logger);
	}
        
        /**
         * @param driver
         * @param logger
         * @param displayName 
         */
        public static void verifyCompositePillContent(WebDriver driver, Logger logger, String displayName)
        {
                IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
                esu.verifyCompositePillContent(driver, logger, displayName);
        }
        
        /**
         * @param driver
         * @param logger
         * @param displayName 
         */
        public static void verifyEntityPillContent(WebDriver driver, Logger logger, String displayName)
        {
                IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
                esu.verifyEntityPillContent(driver, logger, displayName);
        }
        
        /**
         * @param driver
         * @param logger
         * @param displayName 
         */
        public static void verifyPillContains(WebDriver driver, Logger logger, String displayName)
        {
                IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
                esu.verifyPillContains(driver, logger, displayName);
        }
        
        /**
         * @param driver
         * @param logger
         * @param pillIndex
         * @param displayName 
         */
        public static void verifyPillContentByIndex(WebDriver driver, Logger logger, int pillIndex, String displayName)
        {
                IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
                esu.verifyPillContentByIndex(driver, logger, pillIndex, displayName);
        }
        
        /**
         * @param driver
         * @param logger
         * @param displayName
         * @return 
         */
        public static boolean verifyPillExistsByDisplayName(WebDriver driver, Logger logger, String displayName)
        {
                IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
                return esu.verifyPillExistsByDisplayName(driver, logger, displayName);
        }
        
        /**
         * @param driver
         * @param logger
         * @param pillIndex
         * @param displayName
         * @return 
         */
        public static boolean verifyPillExistsByIndex(WebDriver driver, Logger logger, int pillIndex, String displayName)
        {
                IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
                return esu.verifyPillExistsByIndex(driver, logger, pillIndex, displayName);
        }

}
