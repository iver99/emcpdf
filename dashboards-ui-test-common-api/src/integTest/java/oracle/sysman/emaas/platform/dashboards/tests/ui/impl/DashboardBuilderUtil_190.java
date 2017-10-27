package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

//import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DelayedPressEnterThread;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

//import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.ExpectedConditions;

import org.testng.Assert;

public class DashboardBuilderUtil_190 extends DashboardBuilderUtil_175
{	
	public static final String REFRESH_DASHBOARD_SETTINGS_OFF = "OFF";
	public static final String REFRESH_DASHBOARD_SETTINGS_5MIN = "On (Every 5 Minutes)";

	public static final String TILE_WIDER = "wider";

	public static final String TILE_NARROWER = "narrower";

	public static final String TILE_TALLER = "taller";

	public static final String TILE_SHORTER = "shorter";

	private static final String DASHBOARD_SELECTION_TAB_NAME = "Dashboard";

	public static final String PENCIL = "pencil";
	public static final String WRENCH = "wrench";

	public static final String DUP_DASHBOARD_NODSUBMENU = "duplicate_noSubMenu";
	public static final String DUP_DASHBOARD_TOSET = "duplicate_addToSet";
	public static final String DUP_SHBOARDSET_NOTTOSET = "duplicate_notAddToSet";

	public static final String DASHBOARD_IN_SET_END_ICON_CSS = ".oj-component-icon.oj-conveyorbelt-overflow-icon.oj-end";
	public static final String DASHBOARD_IN_SET_START_ICON_CSS = ".oj-component-icon.oj-conveyorbelt-overflow-icon.oj-start";

	@Override
	public void addNewDashboardToSet(WebDriver driver, String dashboardName)
	{
		driver.getLogger().info("DashboardBuilderUtil.addNewDashboardToSet started for name=\"" + dashboardName + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);
		
		int dashboardSetContainerCount = driver.getElementCount("css=" + DashBoardPageId_190.DASHBOARDSETNAVSCONTAINERCSS);
		if (dashboardSetContainerCount <= 0) {
			throw new NoSuchElementException(
					"DashboardBuilderUtil.addNewDashboardToSet: the dashboard navigator container is not found");
		}
		
		driver.waitForElementVisible("css=" + DashBoardPageId_190.DASHBOARDSETNAVSCONTAINERCSS);

		boolean isSelectionTabExist = false;
		
		int dashboardTabCount = driver.getElementCount("css=" + DashBoardPageId_190.DASHBOARDSETNAVSCSS);
		if(dashboardTabCount <= 0)
		{
			throw new NoSuchElementException("DashboardBuilderUtil.addNewDashboardToSet: the dashboard navigators is not found");
		}
		
		for(int i=1; i<=dashboardTabCount; i++)
		{
			if(DASHBOARD_SELECTION_TAB_NAME.equals(driver.getAttribute("xpath=(" + DashBoardPageId_190.DASHBOARDSETNAVSXPATH +")[" + i +"]@data-tabs-name")))
			{
				isSelectionTabExist = true;
				driver.click("xpath=(" + DashBoardPageId_190.DASHBOARDSETNAVSXPATH +")[" + i +"]");
				WaitUtil.waitForPageFullyLoaded(driver);
				driver.getLogger().info("DashboardBuilderUtil.addNewDashboardToSet has click on the dashboard selection tab");
				break;
			}
		}

		if (isSelectionTabExist == false) {
			driver.waitForElementEnabled("css=" + DashBoardPageId_190.DASHBOARDSETNAVADDBTNCSS);
			driver.click("css=" + DashBoardPageId_190.DASHBOARDSETNAVADDBTNCSS);
			WaitUtil.waitForPageFullyLoaded(driver); // wait for all dashboards loaded
		}

		try {
			DashboardHomeUtil.selectDashboard(driver, dashboardName);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			driver.getLogger().info("Exception: " + e.getLocalizedMessage());
		}
		
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.getLogger().info(
				"DashboardBuilderUtil.addNewDashboardToSet has selected the dashboard named with \"" + dashboardName + "\"");
		driver.getLogger().info("DashboardBuilderUtil.addNewDashboardToSet completed and returns true");
	}

	@Override
	public void addWidgetToDashboard(WebDriver driver, String searchString)
	{
		Validator.notNull("widgetName", searchString);
		Validator.notEmptyString("widgetName", searchString);

		if (searchString == null) {
			return;
		}
		
		driver.waitForElementVisible("css=" + DashBoardPageId_190.RIGHTDRAWERCSS);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.getLogger().info("[DashboardHomeUtil] call addWidgetToDashboard with search string as " + searchString);

		//show right drawer if it is hidden
		showRightDrawer(driver, WRENCH);
		driver.waitForElementEnabled("css=" + DashBoardPageId_190.RIGHTDRAWERSEARCHINPUTCSS);
		
		driver.moveToElement("css=" + DashBoardPageId_190.RIGHTDRAWERSEARCHINPUTCSS);
		driver.clear("css=" + DashBoardPageId_190.RIGHTDRAWERSEARCHINPUTCSS);
		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.moveToElement("css=" + DashBoardPageId_190.RIGHTDRAWERSEARCHINPUTCSS);
		driver.click("css=" + DashBoardPageId_190.RIGHTDRAWERSEARCHINPUTCSS);
		driver.sendKeys("css=" + DashBoardPageId_190.RIGHTDRAWERSEARCHINPUTCSS, searchString);

		//verify input box value
		Assert.assertEquals(driver.getAttribute("css=" + DashBoardPageId_190.RIGHTDRAWERSEARCHINPUTCSS + "@value"), searchString);
		
		driver.waitForElementPresent("css=" + DashBoardPageId_190.RIGHTDRAWERSEARCHBUTTONCSS);
		driver.click("css=" + DashBoardPageId_190.RIGHTDRAWERSEARCHBUTTONCSS);

		driver.getLogger().info("[DashboardHomeUtil] start to add widget from right drawer");
		int matchWidgetsCount = driver.getElementCount("css=" + DashBoardPageId_190.RIGHTDRAWERWIDGETCSS);
		if (matchWidgetsCount <= 0) {
			throw new NoSuchElementException("Right drawer widget for search string =" + searchString + " is not found");
		}		
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("css=" + DashBoardPageId_190.RIGHTDRAWERWIDGETCSS);
		driver.moveToElement("css=" + DashBoardPageId_190.RIGHTDRAWERWIDGETCSS);

		driver.getLogger().info("Focus to the widget");
		driver.click("css=" + DashBoardPageId_190.RIGHDRAWER_WIDGET_ADD_CSS);
		driver.getLogger().info("Add the widget");

		driver.getLogger().info("[DashboardHomeUtil] finish adding widget from right drawer");
		//clear search box
		driver.moveToElement("css=" + DashBoardPageId_190.RIGHTDRAWERSEARCHINPUTCSS);
		driver.clear("css=" + DashBoardPageId_190.RIGHTDRAWERSEARCHINPUTCSS);
		WaitUtil.waitForPageFullyLoaded(driver);
		hideRightDrawer(driver);// hide drawer;
	}

	@Override
	public void createDashboardInsideSet(WebDriver driver, String name, String descriptions)
	{
		driver.waitForElementPresent("id=" + DashBoardPageId_190.DASHBOARDSETOPTIONSMENUID);
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.getLogger().info("DashboardBuilderUtil.createDashboardInsideSet : " + name);
		Validator.notEmptyString("name", name);
		//validate whether should open new dashboard home in set
		boolean isSelectionTabExist = false;
		
		int dashboardCount = driver.getElementCount("css=" + DashBoardPageId_190.DASHBOARDSETNAVSCSS);
		if (dashboardCount <= 0) {
			throw new NoSuchElementException(
					"DashboardBuilderUtil.createDashboardInsideSet: the dashboard navigators is not found");
		}
		
		for(int i=1; i<=dashboardCount; i++)
		{
			if(DASHBOARD_SELECTION_TAB_NAME.equals(driver.getAttribute("xpath=(" + DashBoardPageId_190.DASHBOARDSETNAVSXPATH +")[" + i +"]@data-tabs-name")))
			{
				isSelectionTabExist = true;
				driver.getLogger().info("DashboardBuilderUtil.createDashboardInsideSet already has dashboard home");
				break;
			}
		}

		if (isSelectionTabExist == false) {			
			if (!driver.isDisplayed("css=" + DashBoardPageId_190.DASHBOARDSETNAVADDBTNCSS)) {
				throw new NoSuchElementException(
						"DashboardBuilderUtil.createDashboardInsideSet: the dashboard 'add' button  is not found");
			}
			driver.click("css=" + DashBoardPageId_190.DASHBOARDSETNAVADDBTNCSS);
			WaitUtil.waitForPageFullyLoaded(driver); // wait for all dashboards loaded
		}

		driver.click("css=" + DashBoardPageId_190.DASHBOARD_HOME_CREATINSET_BUTTON);

		if (name != null && !name.isEmpty()) {
			driver.sendKeys("id=" + DashBoardPageId_190.DASHBOARDNAMEBOXID, name);
		}
		if (descriptions != null && !descriptions.isEmpty()) {
			driver.sendKeys("id=" + DashBoardPageId_190.DASHBOARDDESCBOXID, descriptions);
		}
		driver.click("id=" + DashBoardPageId_190.DASHOKBUTTONID);

		String newTabLocator = ".other-nav[data-dashboard-name-in-set='" + name + "']";
		driver.waitForElementVisible("css=" + newTabLocator);
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		driver.getLogger().info("DashboardBuilderUtil.createDashboardInsideSet completed");
	}

	@Override
	public void deleteDashboard(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil.deleteDashboard started");
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		int index = getSelectedDashboardEl(driver);
		driver.click("xpath=(" + DashBoardPageId_190.BUILDEROPTIONSMENUXPATH +")[" + index +"]");

		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITLOCATORCSS);
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITLOCATORCSS);

		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSDELETELOCATOR);
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSDELETELOCATOR);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERDELETEDIALOGLOCATOR);
		driver.click(DashBoardPageId_190.BUILDERDELETEDIALOGDELETEBTNLOCATOR);

		driver.waitForElementPresent(DashBoardPageId_190.SEARCHDASHBOARDINPUTLOCATOR);

		driver.getLogger().info("DashboardBuilderUtil.deleteDashboard completed");
	}

	@Override
	public void deleteDashboardInsideSet(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil.deleteDashboardInsideSet started");
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);
		
		int index = getSelectedDashboardEl(driver);
		driver.click("xpath=(" + DashBoardPageId_190.BUILDEROPTIONSMENUXPATH +")[" + index +"]");
		
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITLOCATORCSS);
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITLOCATORCSS);
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSDELETELOCATOR);
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSDELETELOCATOR);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERDELETEDIALOGLOCATOR);
		driver.click(DashBoardPageId_190.BUILDERDELETEDIALOGDELETEBTNLOCATOR);

		driver.getLogger().info("DashboardBuilderUtil.deleteDashboardInsideSet completed");
	}

	@Override
	public void deleteDashboardSet(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil.deleteDashboardSet started");

		//open settings menu
		driver.click("id=" + DashBoardPageId_190.DASHBOARDSETOPTIONSMENUID);

		// click edit option
		driver.click("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSEDITCSS);
		driver.click(DashBoardPageId_190.DASHBOARDSETOPTIONSDELETELOCATOR);
		driver.click(DashBoardPageId_190.DASHBOARDSETDELETEDIALOGDELETEBTNLOCATOR);

		// wait until page redirect to dashboard home
		driver.waitForElementPresent(DashBoardPageId_190.SEARCHDASHBOARDINPUTLOCATOR);

		driver.getLogger().info("DashboardBuilderUtil.deleteDashboardSet completed");
	}

	@Override
	public void duplicateDashboard(WebDriver driver, String name, String descriptions)
	{
		duplicateDashboardCommonUse(driver, name, descriptions, DUP_DASHBOARD_NODSUBMENU);
	}

	@Override
	public void duplicateDashboardInsideSet(WebDriver driver, String name, String descriptions, boolean addToSet)

	{
		if (addToSet) {
			duplicateDashboardCommonUse(driver, name, descriptions, DUP_DASHBOARD_TOSET);
		}
		else {
			duplicateDashboardCommonUse(driver, name, descriptions, DUP_SHBOARDSET_NOTTOSET);
		}
	}

	@Override
	public void editDashboard(WebDriver driver, String name, String descriptions)
	{
		editDashboard(driver, name,descriptions,true);
	}

	@Override
	public void editDashboard(WebDriver driver, String name, String descriptions, Boolean toShowDscptn)
	{
		Validator.notNull("editname", name);
		Validator.notEmptyString("editname", name);

		driver.getLogger().info("DashboardBuilderUtil.edit started");
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		int index = getSelectedDashboardEl(driver);
		driver.click("xpath=(" + DashBoardPageId_190.BUILDEROPTIONSMENUXPATH +")[" + index +"]");

		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITLOCATORCSS);
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITLOCATORCSS);	

		//add name and description
		driver.getElement("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITNAMECSS).clear();
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITNAMECSS);

		driver.waitForElementVisible("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITDESCRIPTIONCSS);
		driver.sendKeys("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITNAMECSS, name);

		driver.getElement("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITDESCRIPTIONCSS).clear();
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITDESCRIPTIONCSS);
		
		driver.waitForElementVisible("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITDESCRIPTIONCSS);
		driver.sendKeys("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITDESCRIPTIONCSS, descriptions);
		driver.waitForElementVisible("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITDESCRIPTIONCSS);
		driver.takeScreenShot();
		driver.savePageToFile();

		driver.getElement("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITSHOWDESCRIPTIONCSS);
		if (toShowDscptn) {
			driver.check("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITSHOWDESCRIPTIONCSS);
		}
		else {
			driver.uncheck("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITSHOWDESCRIPTIONCSS);
		}

		//add wait to fix EMCPDF-4112
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.getLogger().info("DashboardBuilderUtil.edit complete");
	}

	/**
	 * @deprecated
	 * @param driver
	 * @param name
	 * @param descriptions
	 *            @
	 */
	@Deprecated
	public void EditDashboard_targetselctor(WebDriver driver, String name, String descriptions)
	{
		Validator.notNull("editname", name);
		Validator.notEmptyString("editname", name);

		driver.getLogger().info("DashboardBuilderUtil.edit started");
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITLOCATORCSS);
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITLOCATORCSS);

		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITNAMECSS);

		//wait for 900s
//		By locatorOfEditDesEl = By.cssSelector(DashBoardPageId_190.BUILDEROPTIONSEDITDESCRIPTIONCSS);
		

		//add name and description
		driver.getElement("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITNAMECSS).clear();
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITNAMECSS);
		
		driver.waitForElementVisible("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITDESCRIPTIONCSS);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
		driver.sendKeys("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITNAMECSS, name);

		driver.getElement("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITDESCRIPTIONCSS).clear();
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITDESCRIPTIONCSS);

		driver.waitForElementVisible("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITDESCRIPTIONCSS);
		driver.waitForElementVisible("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITDESCRIPTIONCSS);
		
		driver.sendKeys("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITDESCRIPTIONCSS, descriptions);
		driver.waitForElementVisible("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITDESCRIPTIONCSS);
		
		driver.takeScreenShot();
		driver.savePageToFile();

		//selctor filetr entity
		driver.waitForElementPresent(DashBoardPageId_190.ENTITYFILTERLOCATOR);
		driver.click(DashBoardPageId_190.ENTITYFILTERLOCATOR);

		//press ok button
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITSAVECSS);
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITSAVECSS);
		driver.getLogger().info("DashboardBuilderUtil.edit complete");
	}

	@Override
	public void editDashboardSet(WebDriver driver, String name, String descriptions)
	{		
		driver.getLogger().info("DashboardBuilderUtil.editDashboardSet started");
		Validator.notNull("editname", name);
		Validator.notEmptyString("editname", name);

		driver.waitForElementPresent("id=" + DashBoardPageId_190.DASHBOARDSETOPTIONSMENUID);
		driver.getLogger().info("DashboardBuilderUtil.editDashboardSet started");
		WaitUtil.waitForPageFullyLoaded(driver);

		//open settings menu
		driver.click("id=" + DashBoardPageId_190.DASHBOARDSETOPTIONSMENUID);

		// click edit option
		driver.click("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSEDITCSS);

		driver.waitForElementVisible("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSEDITNAMECSS);

		//edit name
		driver.getLogger().info("DashboardBuilderUtil.editDashboardSet start editing name");
		driver.getElement("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSEDITNAMECSS).clear();
		driver.click("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSEDITNAMECSS);
		driver.sendKeys("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSEDITNAMECSS, name);

		//edit description
		driver.getLogger().info("DashboardBuilderUtil.editDashboardSet start editing description");
		driver.getElement("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSEDITDESCRIPTIONCSS).clear();
		driver.click("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSEDITDESCRIPTIONCSS);
		driver.sendKeys("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSEDITDESCRIPTIONCSS, descriptions);
		driver.takeScreenShot();
		driver.savePageToFile();
		WaitUtil.waitForPageFullyLoaded(driver);

		//hide settings panel
		hideRightDrawer(driver);
		
		driver.getLogger().info("DashboardBuilderUtil.editDashboardSet finished");
	}

	@Override
	public Boolean favoriteOption(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil.favoriteOption started");
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		boolean favoriteElem = driver.isDisplayed("css=" + DashBoardPageId_190.BUILDEROPTIONSFAVORITELOCATORCSS);
		if (favoriteElem) {
			driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSFAVORITELOCATORCSS);
			driver.getLogger().info("DashboardBuilderUtil add favorite completed");
			return true;
		}
		else {
			driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSREMOVEFAVORITELOCATORCSS);
			driver.getLogger().info("DashboardBuilderUtil remove favorite completed");
			return false;
		}
	}

	@Override
	public Boolean favoriteOptionDashboardSet(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil.favoriteOptionDashboardSet started");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click("id=" + DashBoardPageId_190.DASHBOARDSETOPTIONSMENUID);

		boolean dashboardsetFavoriteElem = driver.isDisplayed("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSREMOVEFAVORITECSS);
		driver.waitForElementPresent("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSFAVORITECSS);
		driver.click("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSFAVORITECSS);
		if (dashboardsetFavoriteElem) {
			driver.getLogger().info("DashboardBuilderUtil remove favorite dashboardset completed");
			return false;
		}
		else {
			driver.getLogger().info("DashboardBuilderUtil add favorite dashboardset completed");
			return true;
		}
	}

	@Override
	public void gridView(WebDriver driver)
	{
		try {
			DashboardHomeUtil.gridView(driver);
		}
		catch (Exception e) {
			driver.getLogger().info(e.getLocalizedMessage());
		}
	}

	@Override
	public boolean isRefreshSettingChecked(WebDriver driver, String refreshSettings)
	{
		driver.getLogger().info("DashboardBuilderUtil.isRefreshSettingChecked started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshSettings", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		driver.waitForElementVisible("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);

		driver.click(DashBoardPageId_190.BUILDEROPTIONSAUTOREFRESHLOCATOR);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDEROPTIONSAUTOREFRESHOFFLOCATOR);
		if (REFRESH_DASHBOARD_SETTINGS_OFF.equals(refreshSettings)) {
			boolean checked = driver.isElementPresent(DashBoardPageId_190.BUILDERAUTOREFRESHOFFSELECTEDLOCATOR);
			driver.getLogger().info("DashboardBuilderUtil.isRefreshSettingChecked completed, return result is " + checked);
			return checked;
		}
		else {//REFRESH_DASHBOARD_PARAM_5MIN:
			boolean checked = driver.isElementPresent(DashBoardPageId_190.BUILDERAUTOREFRESHON5MINSELECTEDLOCATOR);
			driver.getLogger().info("DashboardBuilderUtil.isRefreshSettingChecked completed, return result is " + checked);
			return checked;
		}
	}

	@Override
	public boolean isRefreshSettingCheckedForDashbaordSet(WebDriver driver, String refreshSettings)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshDashboardSet", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.waitForElementVisible("xpath=" + DashBoardPageId_190.DASHBOARDSETOPTIONBTN);

		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click(DashBoardPageId_190.DASHBOARDSETOPTIONBTN);

		driver.click(DashBoardPageId_190.DASHBOARDSETOPTIONSAUTOREFRESHLOCATOR);

		driver.waitForElementPresent(DashBoardPageId_190.DASHBOARDSETOPTIONSAUTOREFRESHOFFLOCATOR);
		driver.takeScreenShot();
		driver.savePageToFile();
		if (REFRESH_DASHBOARD_SETTINGS_OFF.equals(refreshSettings)) {
			boolean checked = driver.isElementPresent(DashBoardPageId_190.DASHBOARDSETAUTOREFRESHOFFSELECTEDLOCATOR);
			driver.getLogger().info(
					"DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet completed, return result is " + checked);
			return checked;
		}
		else {// REFRESH_DASHBOARD_SETTINGS_5MIN:
			boolean checked = driver.isElementPresent(DashBoardPageId_190.DASHBOARDSETAUTOREFRESHON5MINSELECTEDLOCATOR);
			driver.getLogger().info(
					"DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet completed, return result is " + checked);
			return checked;
		}
	}

	@Override
	public void listView(WebDriver driver)
	{
		try {
			DashboardHomeUtil.listView(driver);
		}
		catch (Exception e) {
			driver.getLogger().info(e.getLocalizedMessage());
		}
	}

	@Override
	public void openWidget(WebDriver driver, String widgetName)
	{
		openWidget(driver, widgetName, 0);
	}

	@Override
	public void openWidget(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info("DashboardBuilderUtil.openWidget started for widgetName=" + widgetName + ", index=" + index);

		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		clickTileOpenInDataExplorerButton(driver, widgetName, index);

		driver.getLogger().info("DashboardBuilderUtil.openWidget completed");
	}

	@Override
	public void printDashboard(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil print dashboard started");
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSPRINTLOCATORCSS);

		new DelayedPressEnterThread("DelayedPressEnterThread", 5000);
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSPRINTLOCATORCSS);
		driver.getLogger().info("DashboardBuilderUtil print completed");
	}

	@Override
	public void printDashboardSet(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil print dashboard set started");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId_190.DASHBOARDSETOPTIONSMENUID);
		int waitTime = 5000;

		//click all tabs
//		WebElement dashboardSetContainer = driver.getWebDriver().findElement(
//				By.cssSelector(DashBoardPageId_190.DASHBOARDSETNAVSCONTAINERCSS));
//		if (dashboardSetContainer == null) {
//			throw new NoSuchElementException(
//					"DashboardBuilderUtil.printDashboardSet: the dashboard navigator container is not found");
//		}

//		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId_190.DASHBOARDSETNAVSCSS));
//		if (navs == null || navs.isEmpty()) {
//			throw new NoSuchElementException("DashboardBuilderUtil.printDashboardSet: the dashboard navigators is not found");
//		}
//		for (WebElement nav : navs) {
//			nav.click();
//			WaitUtil.waitForPageFullyLoaded(driver);
//			driver.takeScreenShot();
//			driver.savePageToFile();
//			driver.getLogger().info("DashboardBuilderUtil.printDashboardSet has click on the dashboard selection tab named");
//		}
		
		driver.clickAll("css=" + DashBoardPageId_190.DASHBOARDSETNAVSCSS);

		//click print
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.click("id=" + DashBoardPageId_190.DASHBOARDSETOPTIONSMENUID);
		new DelayedPressEnterThread("DelayedPressEnterThread", waitTime);
		driver.click("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSPRINTCSS);
		
		//have to use thread sleep to wait for the print window(windows dialog) to appear
		try {
			Thread.sleep(waitTime);
		}
		catch (InterruptedException e) {
			driver.getLogger().info(e.getLocalizedMessage());
		}
		driver.getLogger().info("DashboardBuilderUtil.printDashboardSet: print set completed");
	}

	@Override
	public void refreshDashboard(WebDriver driver, String refreshSettings)
	{
		driver.getLogger().info("DashboardBuilderUtil.refreshDashboard started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshSettings", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		driver.waitForElementVisible("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);

		driver.click(DashBoardPageId_190.BUILDEROPTIONSAUTOREFRESHLOCATOR);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDEROPTIONSAUTOREFRESHOFFLOCATOR);
		switch (refreshSettings) {
			case REFRESH_DASHBOARD_SETTINGS_OFF:
				driver.check(DashBoardPageId_190.BUILDEROPTIONSAUTOREFRESHOFFLOCATOR);
				driver.waitForElementPresent(DashBoardPageId_190.BUILDERAUTOREFRESHOFFSELECTEDLOCATOR);
				driver.takeScreenShot();
				driver.savePageToFile();
				break;
			case REFRESH_DASHBOARD_SETTINGS_5MIN:
				driver.check(DashBoardPageId_190.BUILDEROPTIONSAUTOREFRESHON5MINLOCATOR);
				driver.waitForElementPresent(DashBoardPageId_190.BUILDERAUTOREFRESHON5MINSELECTEDLOCATOR);
				driver.takeScreenShot();
				driver.savePageToFile();
				break;
			default:
				break;
		}
		driver.getLogger().info("DashboardBuilderUtil.refreshDashboard completed");
	}

	@Override
	public void refreshDashboardSet(WebDriver driver, String refreshSettings)
	{
		driver.getLogger().info("DashboardBuilderUtil.refreshDashboardSet started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshDashboardSet", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.click(DashBoardPageId_190.DASHBOARDSETOPTIONBTN);
		driver.click(DashBoardPageId_190.DASHBOARDSETOPTIONSAUTOREFRESHLOCATOR);
		driver.waitForElementPresent(DashBoardPageId_190.DASHBOARDSETOPTIONSAUTOREFRESHOFFLOCATOR);
		switch (refreshSettings) {
			case REFRESH_DASHBOARD_SETTINGS_OFF:
				driver.check(DashBoardPageId_190.DASHBOARDSETOPTIONSAUTOREFRESHOFFLOCATOR);
				break;
			case REFRESH_DASHBOARD_SETTINGS_5MIN:
				driver.check(DashBoardPageId_190.DASHBOARDSETOPTIONSAUTOREFRESHON5MINLOCATOR);
				break;
			default:
				break;
		}
		driver.getLogger().info("DashboardBuilderUtil.refreshDashboardSet completed");
	}

	@Override
	public void removeDashboardFromSet(WebDriver driver, String dashboardName)
	{
		driver.getLogger().info("DashboardBuilderUtil.removeDashboardFromSet started for name=\"" + dashboardName + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementVisible("css=" + DashBoardPageId_190.DASHBOARDSETNAVSCONTAINERCSS);

		int targetTab = 0;
		
		int navsCount = driver.getElementCount("css=" + DashBoardPageId_190.DASHBOARDSETNAVSCSS);
//		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId_190.DASHBOARDSETNAVSCSS));
		if (navsCount <= 0) {
			throw new NoSuchElementException("DashboardBuilderUtil.removeDashboardFromSet: the dashboard navigators is not found");
		}

		for (int i = 1; i <= navsCount; i++) {
//			WebElement nav = navs.get(i);
//			if (nav.getAttribute("data-tabs-name").trim().equals(dashboardName)) {
//				targetTab = nav;
//			}
			if(dashboardName.equals(driver.getAttribute("xpath=(" + DashBoardPageId_190.DASHBOARDSETNAVSXPATH + ")[" + i + "]@data-tabs-name").trim())) {
				targetTab = i;
			}
		}

		if (0 == targetTab) {
			throw new NoSuchElementException(
					"DashboardBuilderUtil.removeDashboardFromSet can not find the dashboard named with \"" + dashboardName + "\"");
		}

		driver.getLogger().info(
				"DashboardBuilderUtil.removeDashboardFromSet has found and removed the dashboard named with \"" + dashboardName
						+ "\"");

		String closeBtnLocator = DashBoardPageId_190.DASHBOARDSETTABNAMECSS.replace("_name_", dashboardName);
		driver.waitForElementPresent("css=" + closeBtnLocator);
		driver.evalJavascript("$(\"" + closeBtnLocator + "\").click()");

		WaitUtil.waitForPageFullyLoaded(driver);

		driver.getLogger().info("DashboardBuilderUtil.removeDashboardFromSet completed");
	}

	@Override
	public void removeWidget(WebDriver driver, String widgetName)
	{
		removeWidget(driver, widgetName, 0);
	}

	@Override
	public void removeWidget(WebDriver driver, String widgetName, int index)
	{
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);

		int widgetElIndex = 0;
		try {
			widgetElIndex = getWidgetByName(driver, widgetName, index);
		}
		catch (InterruptedException e) {
			driver.getLogger().info(e.getLocalizedMessage());
		}

		focusOnWidgetHeader(driver, widgetElIndex);
		
		driver.click("xpath=(" + DashBoardPageId_190.CONFIGTILEXPATH + ")[" + widgetElIndex + "]");

//		widgetEl.findElement(By.cssSelector(DashBoardPageId_190.CONFIGTILECSS)).click();
		driver.click("css=" + DashBoardPageId_190.REMOVETILECSS);
		driver.getLogger().info("Remove the widget");
	}

	@Override
	public void resizeWidget(WebDriver driver, String widgetName, int index, String resizeOptions)
	{
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		Validator.fromValidValues("resizeOptions", resizeOptions, TILE_NARROWER, TILE_WIDER, TILE_SHORTER, TILE_TALLER);

		int widgetElIndex = 0;
		try {
			widgetElIndex = getWidgetByName(driver, widgetName, index);
		}
		catch (InterruptedException e) {
			driver.getLogger().info(e.getLocalizedMessage());
		}

		focusOnWidgetHeader(driver, widgetElIndex);
//		driver.takeScreenShot();

		String tileResizeCSS = null;
		switch (resizeOptions) {
			case TILE_WIDER:
				tileResizeCSS = DashBoardPageId_190.WIDERTILECSS;
				break;
			case TILE_NARROWER:
				tileResizeCSS = DashBoardPageId_190.NARROWERTILECSS;
				break;
			case TILE_SHORTER:
				tileResizeCSS = DashBoardPageId_190.SHORTERTILECSS;
				break;
			case TILE_TALLER:
				tileResizeCSS = DashBoardPageId_190.TALLERTILECSS;
				break;
			default:
				break;
		}
		if (null == tileResizeCSS) {
			return;
		}
		
		driver.click("xpath=(" + DashBoardPageId_190.CONFIGTILEXPATH + ")[" + widgetElIndex + "]");

//		widgetEl.findElement(By.cssSelector(DashBoardPageId_190.CONFIGTILECSS)).click();
		driver.click("css=" + tileResizeCSS);
		driver.getLogger().info("Resize the widget");
	}

	@Override
	public void resizeWidget(WebDriver driver, String widgetName, String resizeOptions)
	{
		resizeWidget(driver, widgetName, 0, resizeOptions);
	}

	@Override
	public void saveDashboard(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil.save started");
		driver.waitForElementPresent("css=" + DashBoardPageId_190.DASHBOARDSAVECSS);
		WaitUtil.waitForPageFullyLoaded(driver);
//		WebElement selectedDashboardEl = getSelectedDashboardEl(driver);
//		WebElement saveButton = selectedDashboardEl.findElement(By.cssSelector(DashBoardPageId_190.DASHBOARDSAVECSS));
//		saveButton.click();
//		driver.takeScreenShot();
		
		int index = getSelectedDashboardEl(driver);
		driver.click("xpath=(" + DashBoardPageId_190.DASHBOARDSAVEXPATH +")[" + index +"]");
		
		driver.getLogger().info("save compelted");
	}

	@Override
	public void search(WebDriver driver, String searchString)
	{
		try {
			DashboardHomeUtil.search(driver, searchString);
		}
		catch (Exception e) {
			driver.getLogger().info(e.getLocalizedMessage());
		}
	}

	@Override
	public void selectDashboard(WebDriver driver, String dashboardName)
	{
		try {
			DashboardHomeUtil.selectDashboard(driver, dashboardName);
		}
		catch (Exception e) {
			driver.getLogger().info(e.getLocalizedMessage());
		}
	}

	@Override
	public void selectDashboardInsideSet(WebDriver driver, String dashboardName)
	{
		driver.getLogger().info("DashboardBuilderUtil.selectDashboardInsideSet started for name=\"" + dashboardName + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

//		WebElement dashboardSetContainer = driver.getWebDriver().findElement(
//				By.cssSelector(DashBoardPageId_190.DASHBOARDSETNAVSCONTAINERCSS));
//		if (dashboardSetContainer == null) {
//			throw new NoSuchElementException(
//					"DashboardBuilderUtil.selectDashboardInsideSet: the dashboard navigator container is not found");
//		}

		//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wait.until(ExpectedConditions.visibilityOf(dashboardSetContainer));
		driver.waitForElementVisible("css=" + DashBoardPageId_190.DASHBOARDSETNAVSCONTAINERCSS);
		WaitUtil.waitForPageFullyLoaded(driver);
		
		int navCount = driver.getElementCount("xpath=" + DashBoardPageId_190.DASHBOARDSETNAVSXPATH);

//		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId_190.DASHBOARDSETNAVSCSS));
		if (navCount <= 0) {
			throw new NoSuchElementException(
					"DashboardBuilderUtil.selectDashboardInsideSet: the dashboard navigators is not found");
		}
		
		String dsbNameInSet = null;

//		for (WebElement nav : navs) {
		for (int i=1; i<=navCount; i++) {
			dsbNameInSet = driver.getAttribute("xpath=(" + DashBoardPageId_190.DASHBOARDSETNAVSXPATH + ")[" + i + "]@data-dashboard-name-in-set").trim();
			if (dsbNameInSet != null && dsbNameInSet.equals(dashboardName)) {
				driver.getLogger().info("DashboardBuilderUtil.selectDashboardInsideSet has found the corresponding name");
				while (!driver.isDisplayed("xpath=(" + DashBoardPageId_190.DASHBOARDSETNAVSXPATH + ")[" + i + "]@data-dashboard-name-in-set")) {
					if (driver.isDisplayed("css=" + DASHBOARD_IN_SET_END_ICON_CSS)) {
						driver.click("css=" + DASHBOARD_IN_SET_END_ICON_CSS);
					}
					else if (driver.isDisplayed("css=" + DASHBOARD_IN_SET_START_ICON_CSS)) {
						driver.click("css=" + DASHBOARD_IN_SET_START_ICON_CSS);
					}
					else {
						throw new NoSuchElementException(
								"DashboardBuilderUtil.selectDashboardInsideSet: the '<' or '>' icon are not found");
					}
				}
				driver.click("xpath=(" + DashBoardPageId_190.DASHBOARDSETNAVSXPATH + ")[" + i + "]@data-dashboard-name-in-set");
				break;
			}
		}
		driver.getLogger().info("DashboardBuilderUtil.selectDashboardInsideSet completed");
	}

	@Override
	public void setEntitySupport(WebDriver driver, String mode)
	{
		driver.getLogger().info("DashboardBuilderUtil.setEntitySupport started, the param of mode is: " + mode);
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		openFiltersInRightPanel(driver);

		driver.getElement("css=" + DashBoardPageId_190.RIGHTDRAWEREDITDBENTITYSUPPORTCSS);
		if ("MULTIPLE".equals(mode)) {
			driver.check("css=" + DashBoardPageId_190.RIGHTDRAWEREDITDBENTITYSUPPORTCSS);
			driver.getLogger().info("DashboardBuilderUtil.setEntitySupport checked entity support");
		}
		else {
			driver.uncheck("css=" + DashBoardPageId_190.RIGHTDRAWEREDITDBENTITYSUPPORTCSS);
			driver.getLogger().info("DashboardBuilderUtil.setEntitySupport unchecked entity support");
		}
		driver.getLogger().info("DashboardBuilderUtil.setEntitySupport finished!!!!");
	}

	@Override
	public boolean showEntityFilter(WebDriver driver, boolean showEntityFilter)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.showEntityFilter started, the param of showEntityFilter is: " + showEntityFilter);
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		openFiltersInRightPanel(driver);

		if (showEntityFilter) {
			driver.click("css=" + DashBoardPageId_190.RIGHTDRAWEREDITDBENABLEENTITYFILTERCSS);
			driver.getLogger().info("DashboardBuilderUtil show entity filter finished!!!");
			return true;
		}
		else {
			driver.click("css=" + DashBoardPageId_190.RIGHTDRAWEREDITDBDISABLEENTITYFILTERCSS);
			driver.getLogger().info("DashboardBuilderUtil hide entity filter finished!!!");
			return false;
		}
	}

	@Override
	public boolean showTimeRangeFilter(WebDriver driver, boolean showTimeRangeFilter)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.showTimeRangeFilter started, the param of showTimeRangeFilter is: " + showTimeRangeFilter);
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		openFiltersInRightPanel(driver);

		if (showTimeRangeFilter) {
			driver.click("css=" + DashBoardPageId_190.RIGHTDRAWEREDITDBENABLETIMERANGEFILTERCSS);
			driver.getLogger().info("DashboardBuilderUtil show time range filter finished!!!");
			return true;
		}
		else {
			driver.click("css=" + DashBoardPageId_190.RIGHTDRAWEREDITDBDISABLETIMERANGEFILTERCSS);
			driver.getLogger().info("DashboardBuilderUtil hide time range filter finished!!!");
			return false;
		}
	}

	@Override
	public void showWidgetTitle(WebDriver driver, String widgetName, boolean visibility)
	{
		showWidgetTitle(driver, widgetName, 0, visibility);
	}

	@Override
	public void showWidgetTitle(WebDriver driver, String widgetName, int index, boolean visibility)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.showWidgetTitle started for widgetName=" + widgetName + ", index=" + index
						+ ", visibility=" + visibility);
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);		
		driver.waitForElementVisible("xpath=" + DashBoardPageId_190.BUILDERTILESEDITAREA);
		WaitUtil.waitForPageFullyLoaded(driver);

		clickTileConfigButton(driver, widgetName, index);

		if (visibility) {
			if (driver.isDisplayed(DashBoardPageId_190.BUILDERTILEHIDELOCATOR)) {
				driver.takeScreenShot();
				driver.savePageToFile();
				driver.getLogger().info("DashboardBuilderUtil.showWidgetTitle completed as title is shown already");
				return;
			}
			driver.click(DashBoardPageId_190.BUILDERTILESHOWLOCATOR);
		}
		else {
			if (driver.isDisplayed(DashBoardPageId_190.BUILDERTILESHOWLOCATOR)) {
				driver.takeScreenShot();
				driver.savePageToFile();
				driver.getLogger().info("DashboardBuilderUtil.showWidgetTitle completed as title is hidden already");
				return;
			}
			driver.click(DashBoardPageId_190.BUILDERTILEHIDELOCATOR);
		}
		driver.getLogger().info("DashboardBuilderUtil.showWidgetTitle completed");
	}

	/**
	 * sort dashboards
	 *
	 * @param driver
	 * @param option
	 *            sort by - default, access_date_asc, access_date_dsc, name_asc, name_dsc, creation_date_asc, creation_date_dsc,
	 *            last_modification_date_asc, last_modification_date_dsc, owner_asc, owner_dsc @
	 */
	@Override
	public void sortBy(WebDriver driver, String option)
	{
		try {
			DashboardHomeUtil.sortBy(driver, option);
		}
		catch (Exception e) {
			driver.getLogger().info(e.getLocalizedMessage());
		}
	}

	@Override
	public Boolean toggleHome(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil.asHomeOption started");

		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		boolean homeElem = driver.isDisplayed("css=" + DashBoardPageId_190.BUILDEROPTIONSSETHOMELOCATORCSS);

		if (homeElem) {
			driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSSETHOMELOCATORCSS);
			driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSSETHOMELOCATORCSS);

			boolean comfirmDialog = driver.isDisplayed("css=" + DashBoardPageId_190.BUILDEROPTIONSSETHOMESAVECSS);
			if (comfirmDialog) {
				driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSSETHOMESAVECSS);
			}
			driver.getLogger().info("DashboardBuilderUtil set home completed");
			return true;
		}
		else {
			driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSREMOVEHOMELOCATORCSS);
			driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSREMOVEHOMELOCATORCSS);
			driver.getLogger().info("DashboardBuilderUtil remove home completed");
			return false;
		}
	}

	@Override
	public Boolean toggleHomeDashboardSet(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil.toggleHomeOptionDashboardSet started");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.click("id=" + DashBoardPageId_190.DASHBOARDSETOPTIONSMENUID);

		boolean homeElem = driver.isDisplayed("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSADDHOMECSS);

		driver.click("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSHOMECSS);

		if (homeElem) {
			driver.getLogger().info("DashboardBuilderUtil set home in dashboard set completed");
			return true;
		}
		else {
			driver.getLogger().info("DashboardBuilderUtil remove home in dashboard set completed");
			return false;
		}
	}

	@Override
	public Boolean toggleShareDashboard(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil.sharedashboard started");
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);
		
		int index = getSelectedDashboardEl(driver);
		driver.click("xpath=(" + DashBoardPageId_190.BUILDEROPTIONSMENUXPATH +")[" + index +"]");

		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITLOCATORCSS);
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITLOCATORCSS);

		driver.waitForElementPresent("css=" + DashBoardPageId_190.RIGHTDRAWEREDITSINGLEDBSHARECSS);
		driver.click("css=" + DashBoardPageId_190.RIGHTDRAWEREDITSINGLEDBSHARECSS);

		boolean shareFlagElem = driver.isDisplayed("css=" + DashBoardPageId_190.RIGHTDRAWEREDITSINGLEDBTOSHARESELECTEDCSS);
		if (shareFlagElem) {
			driver.click("css=" + DashBoardPageId_190.RIGHTDRAWEREDITSINGLEDBNOTSHARECSS);

			driver.getLogger().info("DashboardBuilderUtil unshare dashboardset");
			return false;
		}
		else {
			driver.click("css=" + DashBoardPageId_190.RIGHTDRAWEREDITSINGLEDBTOSHARECSS);
			driver.getLogger().info("DashboardBuilderUtil share dashboardset");
			return true;
		}
	}

	/**
	 * @param driver
	 * @return true if toggle to share the dashboardset @
	 */
	@Override
	public Boolean toggleShareDashboardset(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil.toggleShareDashboardset started");
		WaitUtil.waitForPageFullyLoaded(driver);

		//open the edit/share dialog
		driver.getLogger().info("DashboardBuilderUtil.toggleShareDashboardset open share/edit dialog");

		driver.click("id=" + DashBoardPageId_190.DASHBOARDSETOPTIONSMENUID);

		driver.click("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSEDITCSS);

		//open share collapsible
		boolean editShareElem = driver.isDisplayed("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSSHARECONTENTCSS);
		if (!editShareElem) {
			driver.click("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSSHARECOLLAPSIBLECSS);
		}
		driver.getLogger().info("DashboardBuilderUtil.toggleShareDashboardset sharing form has opened");

		//toggle share dashboardset
		driver.waitForElementPresent("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSUNSHARECSS);
		boolean isSharedSelected = driver.isDisplayed("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSSHARESTATUSCSS);
		if (isSharedSelected) {
			driver.click("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSUNSHARECSS);

			driver.getLogger().info("DashboardBuilderUtil unshare dashboardset");
			return false;
		}
		else {
			driver.click("css=" + DashBoardPageId_190.DASHBOARDSETOPTIONSSHARECSS);
			driver.getLogger().info("DashboardBuilderUtil share dashboardset");
			return true;
		}
	}

	@Override
	public boolean verifyDashboard(WebDriver driver, String dashboardName, String description, boolean showTimeSelector)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.verifyDashboard started for name=\"" + dashboardName + "\", description=\"" + description
						+ "\", showTimeSelector=\"" + showTimeSelector + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR);
		//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR)));
		driver.waitForElementVisible("xpath=" + DashBoardPageId_190.BUILDERNAMETEXTLOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR);
		driver.click(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR);

		String realName = driver.getAttribute(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR + "@title");
		if (!dashboardName.equals(realName)) {
			driver.getLogger().info(
					"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected dashboard name is "
							+ dashboardName + ", actual dashboard name is " + realName);
			return false;
		}

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERDESCRIPTIONTEXTLOCATOR);
		String realDesc = driver.getAttribute(DashBoardPageId_190.BUILDERDESCRIPTIONTEXTLOCATOR + "@title");
		if (description == null || "".equals(description)) {
			if (realDesc != null && !"".equals(realDesc.trim())) {
				driver.getLogger().info(
						"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected description is "
								+ description + ", actual dashboard description is " + realDesc);
				return false;
			}
		}
		else {
			if (!description.equals(realDesc)) {
				driver.getLogger().info(
						"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected description is "
								+ description + ", actual dashboard description is " + realDesc);
				return false;
			}
		}

		boolean actualTimeSelectorShown = driver.isDisplayed(DashBoardPageId_190.BUILDERDATETIMEPICKERLOCATOR);
		if (actualTimeSelectorShown != showTimeSelector) {
			driver.getLogger().info(
					"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected showTimeSelector is "
							+ showTimeSelector + ", actual dashboard showTimeSelector is " + actualTimeSelectorShown);
			return false;
		}

		driver.getLogger().info("DashboardBuilderUtil.verifyDashboard compelted and returns true");
		return true;
	}

	@Override
	public boolean verifyDashboardInsideSet(WebDriver driver, String dashboardName)
	{
		driver.getLogger().info("DashboardBuilderUtil.verifyDashboardInsideSet started for name=\"" + dashboardName + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);
		
		driver.waitForElementVisible("css=" + DashBoardPageId_190.DASHBOARDSETNAVSCONTAINERCSS);

//		WebElement dashboardSetContainer = driver.getWebDriver().findElement(
//				By.cssSelector(DashBoardPageId_190.DASHBOARDSETNAVSCONTAINERCSS));
//		if (dashboardSetContainer == null) {
//			throw new NoSuchElementException(
//					"DashboardBuilderUtil.verifyDashboardInsideSet: the dashboard navigator container is not found");
//		}
//
//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
//		wait.until(ExpectedConditions.visibilityOf(dashboardSetContainer));

		WaitUtil.waitForPageFullyLoaded(driver);

		boolean hasFound = false;
		int navCount = driver.getElementCount("xpath=" + DashBoardPageId_190.DASHBOARDSETNAVSXPATH);
//		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId_190.DASHBOARDSETNAVSCSS));
		if (navCount <= 0) {
			throw new NoSuchElementException(
					"DashboardBuilderUtil.verifyDashboardInsideSet: the dashboard navigators is not found");
		}
//
//		for (WebElement nav : navs) {
//			if (nav.getAttribute("data-dashboard-name-in-set") != null
//					&& nav.getAttribute("data-dashboard-name-in-set").trim().equals(dashboardName)) {
//				hasFound = true;
//				break;
//			}
//		}
		String navAttr = null;
		for(int i=0; i<navCount; i++) {
			navAttr = driver.getAttribute("xpath=(" + DashBoardPageId_190.DASHBOARDSETNAVSXPATH + ")[" + i + 1 + "]@data-dashboard-name-in-set").trim();
			
			if(navAttr != null && dashboardName.equals(navAttr)) {
				hasFound = true;
				break;
			}
		}

		if (hasFound) {
			driver.getLogger().info("DashboardBuilderUtil.verifyDashboardInsideSet name=\"" + dashboardName + "\" has found");
		}
		else {
			driver.getLogger().info("DashboardBuilderUtil.verifyDashboardInsideSet name=\"" + dashboardName + "\" has not found");
		}
		driver.getLogger().info("DashboardBuilderUtil.verifyDashboardInsideSet completed");
		return hasFound;
	}

	@Override
	public boolean verifyDashboardSet(WebDriver driver, String dashboardSetName)
	{
		driver.getLogger().info("DashboardBuilderUtil.verifyDashboard started for name=\"" + dashboardSetName + "\"");
		Validator.notEmptyString("dashboardSetName", dashboardSetName);

		driver.waitForElementPresent(DashBoardPageId_190.DASHBOARDSETNAMETEXTLOCATOR);

		driver.waitForElementVisible("xpath=" + DashBoardPageId_190.DASHBOARDSETNAMETEXTLOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent(DashBoardPageId_190.DASHBOARDSETNAMETEXTLOCATOR);
		driver.click(DashBoardPageId_190.DASHBOARDSETNAMETEXTLOCATOR);
		String realName = driver.getText(DashBoardPageId_190.DASHBOARDSETNAMETEXTLOCATOR);
		if (!dashboardSetName.equals(realName)) {
			driver.getLogger().info(
					"DashboardBuilderUtil.verifyDashboardSet compelted and returns false. Expected dashboard set name is "
							+ dashboardSetName + ", actual dashboard set name is " + realName);
			return false;
		}

		driver.getLogger().info("DashboardBuilderUtil.verifyDashboardSet compelted and returns true");
		return true;
	}

	@Override
	public boolean verifyWidget(WebDriver driver, String widgetName)
	{
		return verifyWidget(driver, widgetName, 0);
	}

	@Override
	public boolean verifyWidget(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.verifyWidget started for name=\"" + widgetName + "\", index=\"" + index + "\"");
		Validator.notEmptyString("dashboardName", widgetName);
		
		int widgetIndex = getTileTitleElement(driver, widgetName, index);
		
		if(widgetIndex > 0)
		{
			driver.getLogger().info("DashboardBuilderUtil.verifyWidget compelted and returns true");
			return true;
		}
		else
		{
			driver.getLogger().info("DashboardBuilderUtil.verifyWidget compelted and returns false");
			return false;
		}

//		WebElement we = null;
//		try {
//			WaitUtil.waitForPageFullyLoaded(driver);
//			we = getTileTitleElement(driver, widgetName, index);
//		}
//		catch (NoSuchElementException e) {
//			driver.getLogger().info(e.getLocalizedMessage());
//			driver.getLogger().info("DashboardBuilderUtil.verifyWidget compelted and returns false");
//			return false;
//		}
//		if (we == null) {
//			driver.getLogger().info("DashboardBuilderUtil.verifyWidget compelted and returns false");
//			return false;
//		}
//
//		driver.getLogger().info("DashboardBuilderUtil.verifyWidget compelted and returns true");
//		return true;
	}

	/**
	 * @param driver
	 * @param widgetName
	 * @param index
	 */

	private void clickTileOpenInDataExplorerButton(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info("Start to find widget with widgetName=" + widgetName + ", index=" + index);
		int widgetTitleIndex = getTileTitleElement(driver, widgetName, index);
//		driver.getLogger().info("Found widget configure button");
//		Actions builder = new Actions(driver.getWebDriver());
//		driver.getLogger().info("Now moving to the widget title bar");
//		builder.moveToElement(widgetTitle).perform();
		//driver.waitForServer();
//		driver.takeScreenShot();
//		driver.savePageToFile();
		if (widgetTitleIndex == 0) {
			throw new NoSuchElementException("Widget with title=" + widgetName + ", index=" + index + " is not found");
		}
		driver.getLogger().info("Found widget with name=" + widgetName + ", index =" + index + " before opening widget link");
		
		driver.click("xpath=(" + DashBoardPageId_190.BUILDERTILEHEADERLOCATOR + ")[" + widgetTitleIndex + "]" + DashBoardPageId_190.BUILDERTILEDATAEXPLORELOCATOR);
//		WebElement widgetDataExplore = widgetTitle.findElement(By.xpath(DashBoardPageId_190.BUILDERTILEDATAEXPLORELOCATOR));
//		if (widgetDataExplore == null) {
//			throw new NoSuchElementException("Widget data explorer link for title=" + widgetName + ", index=" + index
//					+ " is not found");
//		}
//		driver.getLogger().info("Found widget configure button");
//		driver.takeScreenShot();
//		driver.savePageToFile();
		//driver.getLogger().info("and clicks the widget config button");
//		builder.moveToElement(widgetDataExplore).click().perform();
		//WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		//wait.until(ExpectedConditions.elementToBeClickable(widgetDataExplore));
		//widgetDataExplore.click();
		WaitUtil.waitForPageFullyLoaded(driver);

	}

	private void duplicateDashboardCommonUse(WebDriver driver, String name, String descriptions, String operationName)

	{
//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		Validator.notNull("duplicatename", name);
		Validator.notEmptyString("duplicatename", name);

		driver.getLogger().info("DashboardBuilderUtil.duplicate started");
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		WaitUtil.waitForPageFullyLoaded(driver);
//		WebElement visibleContainer = getSelectedDashboardEl(driver);
//		WebElement visbleOptionMenu = visibleContainer.findElement(By.cssSelector(DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR));
//		visbleOptionMenu.click();
		
		int index = getSelectedDashboardEl(driver);
		driver.click("xpath=(" + DashBoardPageId_190.BUILDEROPTIONSMENUXPATH +")[" + index +"]");
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATELOCATORCSS);

		// add to set or not,or no dropdownmenu just add
		switch (operationName) {
			case DUP_DASHBOARD_NODSUBMENU:
				driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATELOCATORCSS);
				break;
			case DUP_DASHBOARD_TOSET:
				driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATELOCATORCSS);
				driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATETOSETCSS);
				break;
			case DUP_SHBOARDSET_NOTTOSET:
				driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATELOCATORCSS);
				driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATENOTTOSETCSS);
				break;
		}

		driver.waitForElementPresent("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATENAMECSS);
		driver.waitForElementVisible("id=ojDialogWrapper-duplicateDsbDialog");
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ojDialogWrapper-duplicateDsbDialog")));
		//add name and description
		WaitUtil.waitForPageFullyLoaded(driver);
		//driver.focus("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATENAMECSS);
		driver.getElement("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATENAMECSS).clear();
		driver.click("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATENAMECSS);
//		By locatorOfDuplicateNameEl = By.id(DashBoardPageId_190.BUILDEROPTIONSDUPLICATENAMECSS);
		
		driver.waitForElementVisible("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATENAMECSS);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfDuplicateNameEl));
		driver.click("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATENAMECSS);
		driver.sendKeys("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATENAMECSS, name);
		driver.waitForValue("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATENAMECSS, name);

		driver.getElement("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS).clear();
		driver.click("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS);
		driver.waitForElementVisible("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATENAMECSS);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfDuplicateNameEl));
		if (descriptions == null) {
			driver.sendKeys("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS, "");
		}
		else {
			driver.sendKeys("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS, descriptions);
		}
//		driver.takeScreenShot();
//		driver.savePageToFile();
		if (descriptions != null) {
			driver.waitForValue("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS, descriptions);
		}
		WaitUtil.waitForPageFullyLoaded(driver);
		//press ok button
//		By locatorOfDuplicateSaveEl = By.cssSelector(DashBoardPageId_190.BUILDEROPTIONSDUPLICATESAVECSS);
		
		driver.waitForElementVisible("css=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATESAVECSS);
		driver.waitForElementEnabled("css=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATESAVECSS);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfDuplicateSaveEl));
//		wait.until(ExpectedConditions.elementToBeClickable(locatorOfDuplicateSaveEl));

		driver.sendKeys("id=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS, Keys.chord(Keys.TAB));
//		By locatorOfDuplicateDesEl = By.id(DashBoardPageId_190.BUILDEROPTIONSDUPLICATEDESCRIPTIONCSS);
//		driver.getWebDriver().findElement(locatorOfDuplicateDesEl).sendKeys(Keys.TAB);

//		WebElement saveButton = driver.getElement("css=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATESAVECSS);
//		Actions actions = new Actions(driver.getWebDriver());
//		actions.moveToElement(saveButton).build().perform();
//
//		driver.takeScreenShot();
//		driver.savePageToFile();
		
		driver.moveToElement("css=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATESAVECSS);
		driver.getLogger().info("DashboardBuilderUtil.duplicate save button has been focused");

		boolean isDisplayed = driver.isDisplayed("css=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATESAVECSS);
		driver.getLogger().info("isDisplayed:" + isDisplayed);
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSDUPLICATESAVECSS);
		if (DUP_DASHBOARD_NODSUBMENU.equals(operationName)) {
			driver.setPageLoadDetector(BuildPageLoadDetector.class);
			WaitUtil.waitForPageFullyLoaded(driver);
			driver.setPageLoadDetector(null);
		}
		if (operationName.equals(DUP_DASHBOARD_TOSET)) {
			WaitUtil.waitForPageFullyLoaded(driver);
			driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSMENULOCATOR);
		}
		else {
//			String newTitleLocator = ".dbd-display-hover-area h1[title='" + name + "']";
//			driver.getLogger().info("DashboardBuilderUtil.duplicate : " + newTitleLocator);
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(newTitleLocator)));
			driver.waitForElementVisible("css=.dbd-display-hover-area h1[title='" + name + "']");
			WaitUtil.waitForPageFullyLoaded(driver);
		}
//		driver.takeScreenShot();
//		driver.savePageToFile();
		driver.getLogger().info("DashboardBuilderUtil.duplicate completed");
	}

	private void focusOnWidgetHeader(WebDriver driver, int index)
	{
//		if (null == widgetElement) {
//			driver.getLogger().info("Fail to find the widget element");
//			driver.takeScreenShot();
//			driver.savePageToFile();
//			throw new NoSuchElementException("Widget config menu is not found");
//		}
		
		if (index == 0) {
			driver.getLogger().info("Fail to find the widget element");
			throw new NoSuchElementException("Widget config menu is not found");
		}
		
		driver.moveToElement("xpath=(" + DashBoardPageId_190.TILETITLEXPATH + ")[" + index +"]");

//		WebElement widgetHeader = widgetElement.findElement(By.cssSelector(DashBoardPageId_190.TILETITLECSS));
//		Actions actions = new Actions(driver.getWebDriver());
//		actions.moveToElement(widgetHeader).build().perform();
		driver.getLogger().info("Focus to the widget");
	}

	private int getWidgetByName(WebDriver driver, String widgetName, int index) throws InterruptedException
	{
		if (widgetName == null) {
			return 0;
		}
		
		int widgetsCount = driver.getElementCount("css=" + DashBoardPageId_190.WIDGETTITLECSS);
		int counter = 0;
		
		int i=1;
		for(i=1; i<=widgetsCount; i++) {
			String widgetAttribute = driver.getAttribute("xpath=(" + DashBoardPageId_190.TILETITLEXPATH + ")[" + i + "]@data-tile-title");
			Validator.notNull("widgetTitleAttribute", widgetAttribute);

			if (widgetAttribute.trim().equals(widgetName)) {
				if (counter == index) {
//					widget = widgetElement;
					break;
				}
				counter++;
			}
		}

//		List<WebElement> widgets = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId_190.WIDGETTITLECSS));
//		WebElement widget = null;
//		
//		for (WebElement widgetElement : widgets) {
//			WebElement widgetTitle = widgetElement.findElement(By.cssSelector(DashBoardPageId_190.TILETITLECSS));
//			Validator.notNull("widgetTitle", widgetTitle);
//			String widgetAttribute = widgetTitle.getAttribute("data-tile-title");
//			Validator.notNull("widgetTitleAttribute", widgetAttribute);
//
//			if (widgetAttribute.trim().equals(widgetName)) {
//				if (counter == index) {
//					widget = widgetElement;
//					break;
//				}
//				counter++;
//			}
//		}

		return i;
	}

	protected boolean isRightDrawerVisible(WebDriver driver)
	{
		boolean isDisplayed = "none".equals(driver.getCssValue("css=" + DashBoardPageId_190.RIGHTDRAWERPANELCSS, "display")) != true;
        driver.getLogger().info("DashboardBuilderUtil.isRightDrawerVisible,the isDisplayed value is " + isDisplayed);

        boolean isWidthValid = "0px".equals(driver.getCssValue("css=" + DashBoardPageId_190.RIGHTDRAWERPANELCSS, "width")) != true;
        driver.getLogger().info("DashboardBuilderUtil.isRightDrawerVisible,the isWidthValid value is " + isWidthValid);

        return isDisplayed && isWidthValid;
	}

	//to open right drawer and show build dashboard
	private void showRightDrawer(WebDriver driver, String buttonName)
	{
		driver.waitForElementPresent("css=" + DashBoardPageId_190.RIGHTDRAWERCSS);
		if (isRightDrawerVisible(driver) != false) {
			hideRightDrawer(driver);
		}
		switch (buttonName) {
			case PENCIL:
				driver.click("css=" + DashBoardPageId_190.RIGHTDRAWERTOGGLEPENCILBTNCSS);
				break;
			case WRENCH:
				driver.click("css=" + DashBoardPageId_190.RIGHTDRAWERTOGGLEWRENCHBTNCSS);
				break;
			default:
				driver.takeScreenShot();
				driver.savePageToFile();
				return;
		}
		driver.getLogger().info("[DashboardBuilderUtil] triggered showRightDrawer and show build dashboard.");
		WaitUtil.waitForPageFullyLoaded(driver);
	}

	protected void clickTileConfigButton(WebDriver driver, String widgetName, int index)
	{		
		int i = getTileTitleElement(driver, widgetName, index);
		driver.click("xpath=(" + DashBoardPageId_190.BUILDERTILEHEADERLOCATOR + ")[" + i + "]" + DashBoardPageId_190.BUILDERTILECONFIGLOCATOR);
//		WebElement tileConfig = tileTitle.findElement(By.xpath(DashBoardPageId_190.BUILDERTILECONFIGLOCATOR));
//		if (tileConfig == null) {
//			throw new NoSuchElementException("Tile config menu for title=" + widgetName + ", index=" + index + " is not found");
//		}
//		Actions builder = new Actions(driver.getWebDriver());
//		builder.moveToElement(tileTitle).perform();
//		builder.moveToElement(tileConfig).click().perform();
		//return tileConfig;
	}

	/**
	 * @param driver
	 * @return current visible dashboard container
	 */
	protected int getSelectedDashboardEl(WebDriver driver)
	{
		int dashboardContainersCount = driver.getElementCount("xpath=" + DashBoardPageId_190.DASHBOARDSETCONTAINERXPATH);
		for(int i=1; i<=dashboardContainersCount; i++) {			
			if(driver.isDisplayed("xpath=(" + DashBoardPageId_190.DASHBOARDSETCONTAINERXPATH +")[" + i +"]")) {
				driver.getLogger().info(
						"[DashboardBuilderUtil] triggered getSelectedDashboardEl and get the dashboard successfully!");
				return i;
			}
		}

		driver.getLogger().info("[DashboardBuilderUtil] triggered getSelectedDashboardEl and fail to find visible dashboard!");
		return 0;
	}

	protected int getTileTitleElement(WebDriver driver, String widgetName, int index)
	{
		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		WaitUtil.waitForPageFullyLoaded(driver);

		String titleTitlesLocator = String.format(DashBoardPageId_190.BUILDERTILETITLELOCATOR, widgetName);
		
		int tileTitlesCount = driver.getElementCount("xpath=" + titleTitlesLocator);
		if (tileTitlesCount <= index) {
			return 0;
		}
		
		int i = 0;
		driver.moveToElement("xpath=("+ titleTitlesLocator +")[" + (index + 1) + "]");
		WaitUtil.waitForPageFullyLoaded(driver);
		
		int tileCount = driver.getElementCount("xpath=" + DashBoardPageId_190.BUILDERTILELOCATOR);
		int j = 1;
		boolean isExist = false;
		for (j=1; j<=tileCount; j++) {
			if (widgetName.equals(driver.getText("xpath=(" + DashBoardPageId_190.BUILDERTILELOCATOR + ")[" + j + "]").trim())) {
				i++;
				if ( i == (index + 1) ) {
					isExist = true;
					break;
				}
			}
		}
		if (isExist) {
			return j;
		}
		else
			return 0;
	}

	@Override
	protected void hideRightDrawer(WebDriver driver)
	{
		driver.waitForElementPresent("css=" + DashBoardPageId_190.RIGHTDRAWERCSS);
		if (isRightDrawerVisible(driver) == true) {
			if (driver.isElementPresent("css=" + DashBoardPageId_190.RIGHTDRAWERTOGGLEPENCILBTNCSSSelected)) {
				driver.click("css=" + DashBoardPageId_190.RIGHTDRAWERTOGGLEPENCILBTNCSS);
			}
			else {
				driver.click("css=" + DashBoardPageId_190.RIGHTDRAWERTOGGLEPENCILBTNCSS);
				if (driver.isElementPresent("css=" + DashBoardPageId_190.RIGHTDRAWERTOGGLEPENCILBTNCSSSelected)) {
					driver.click("css=" + DashBoardPageId_190.RIGHTDRAWERTOGGLEPENCILBTNCSS);
				}
			}

			driver.waitForElementPresent("css=" + DashBoardPageId_190.RIGHTDRAWERPANELHIDECSS);
			driver.getLogger().info("[DashboardBuilderUtil] triggered hideRightDrawer.");
		}
		driver.takeScreenShot();
		driver.savePageToFile();
	}

	protected void openFiltersInRightPanel(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil.openFiltersInRightPanel start");
		//click Options to open Options menu

		int index = getSelectedDashboardEl(driver);
		driver.click("xpath=(" + DashBoardPageId_190.BUILDEROPTIONSMENUXPATH +")[" + index +"]");

		//click Options->edit to open right panel
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITLOCATORCSS);
		driver.click("css=" + DashBoardPageId_190.BUILDEROPTIONSEDITLOCATORCSS);

		//click Right panel->filters
		if (driver.isElementPresent("css=" + DashBoardPageId_190.RIGHTDRAWEREDITDBFILTERCOLLAPSEDCSS)) {
			driver.waitForElementPresent("css=" + DashBoardPageId_190.RIGHTDRAWEREDITDBFILTERCSS);
			driver.click("css=" + DashBoardPageId_190.RIGHTDRAWEREDITDBFILTERCSS);

		}
		else if (driver.isElementPresent("css=" + DashBoardPageId_190.RIGHTDRAWEREDITDBFILTEREXPANDCSS)) {
			driver.getLogger().info("Dashboar Filter has been expanded");
			driver.takeScreenShot();
			driver.savePageToFile();
		}
	}
}
