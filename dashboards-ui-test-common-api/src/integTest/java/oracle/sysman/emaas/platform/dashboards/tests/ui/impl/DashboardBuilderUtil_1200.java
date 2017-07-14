package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.*;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class DashboardBuilderUtil_1200 extends DashboardBuilderUtil_1190
{
	@Override
	public void addLinkToWidgetTitle(WebDriver driver, String widgetName, int index, String dashboardName)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.addLinkToWidgetTitle started for widgetName=" + widgetName + ", index=" + index
						+ ", dashboardName=" + dashboardName);
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId_190.BUILDERTILESEDITAREA)));
		WaitUtil.waitForPageFullyLoaded(driver);

		clickTileConfigButton(driver, widgetName, index);

		//click edit button in widget config menu
		driver.waitForElementPresent(DashBoardPageId_1200.BUILDERTILEEDITLOCATOR);
		driver.click(DashBoardPageId_1200.BUILDERTILEEDITLOCATOR);

		driver.waitForElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTAREACSSLOCATOR);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTAREACSSLOCATOR)));

		//remove link if widget title is linked
		if(driver.isElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTREMOVELINKCSSLOCATOR)){
			driver.click("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTREMOVELINKCSSLOCATOR);
			driver.waitForElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR);
		}

		if(dashboardName != null && !dashboardName.isEmpty()){
			WebElement searchInput = driver.getElement("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR);
			// focus on search input box
			wait.until(ExpectedConditions.elementToBeClickable(searchInput));

			Actions actions = new Actions(driver.getWebDriver());
			actions.moveToElement(searchInput).build().perform();
			searchInput.clear();
			WaitUtil.waitForPageFullyLoaded(driver);
			actions.moveToElement(searchInput).build().perform();
			driver.click("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR);
			WaitUtil.waitForPageFullyLoaded(driver);
			searchInput.sendKeys(dashboardName);
			driver.takeScreenShot();
			//verify input box value
			Assert.assertEquals(searchInput.getAttribute("value"), dashboardName);

			WebElement searchButton = driver.getElement("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBTNCSSLOCATOR);
			driver.waitForElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBTNCSSLOCATOR);
			searchButton.click();
			//wait for ajax resolved
			WaitUtil.waitForPageFullyLoaded(driver);
			driver.takeScreenShot();

			driver.getLogger().info("[DashboardHomeUtil] start to add link");
			List<WebElement> matchingWidgets = driver.getWebDriver().findElements(
					By.cssSelector(DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHGETCSSLOCATOR));
			if (matchingWidgets == null || matchingWidgets.isEmpty()) {
				throw new NoSuchElementException("Right drawer content for search string =" + dashboardName + " is not found");
			}
			WaitUtil.waitForPageFullyLoaded(driver);

			Actions builder = new Actions(driver.getWebDriver());
			try {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHGETCSSLOCATOR)));
				driver.click("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHGETCSSLOCATOR);

				driver.waitForElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTADDBTNCSSLOCATOR);
				driver.click("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTADDBTNCSSLOCATOR);

				driver.getLogger().info("Content added");			
			}
			catch (IllegalArgumentException e) {
				throw new NoSuchElementException("Content for " + dashboardName + " is not found");
			}
		}
		driver.getLogger().info("DashboardBuilderUtil.addLinkToWidgetTitle completed");
	}

	@Override
	public void addLinkToWidgetTitle(WebDriver driver, String widgetName, String dashboardName){
		addLinkToWidgetTitle(driver, widgetName, 0, dashboardName);
	}

	@Override
	public boolean verifyLinkOnWidgetTitle(WebDriver driver, String widgetName, int index, String dashboardName){
		driver.getLogger().info(
				"DashboardBuilderUtil.verifyLinkOnWidgetTitle started for widgetName=" + widgetName + ", index=" + index
						+ ", linked dashboardName=" + dashboardName);
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId_190.BUILDERTILESEDITAREA)));
		WaitUtil.waitForPageFullyLoaded(driver);

		String titleTitlesLocator = String.format(DashBoardPageId_1200.BUILDERLINKEDTILETITLELOCATOR, widgetName);
		List<WebElement> tileTitles = driver.getWebDriver().findElements(By.cssSelector(titleTitlesLocator));
		if (tileTitles == null || tileTitles.size() <= index) {
			driver.getLogger().info(
					"verifyLinkOnWidgetTitle compelted and returns false. Expected linked dashboardName is " + dashboardName
							+ ", actual it is not linked.");
			return false;
		}else {
			tileTitles.get(index).click();
			WaitUtil.waitForPageFullyLoaded(driver);
			String realName = driver.getElement(DashBoardPageId.BUILDERNAMETEXTLOCATOR).getAttribute("title");
			if (!dashboardName.equals(realName)) {
				driver.getLogger().info(
						"verifyLinkOnWidgetTitle compelted and returns false. Expected linked dashboardName is " + dashboardName
								+ ", actual linked dashboardName is " + realName);
				return false;
			}
			return true;
		}
	}

	@Override
	public boolean verifyLinkOnWidgetTitle(WebDriver driver, String widgetName, String dashboardName){
		return verifyLinkOnWidgetTitle(driver, widgetName, 0, dashboardName);
        }

	@Override
	public void addTextWidgetToDashboard(WebDriver driver)
	{
		driver.getLogger().info("add text widget started");
		driver.waitForElementPresent("css=" + DashBoardPageId.DASHBOARDADDTEXTWIDGETCSS);
		
		WaitUtil.waitForPageFullyLoaded(driver);
		WebElement selectedDashboardEl = getSelectedDashboardEl(driver);
		WebElement textButton = selectedDashboardEl.findElement(By.cssSelector(DashBoardPageId.DASHBOARDADDTEXTWIDGETCSS));
		textButton.click();
		driver.waitForServer();
		driver.takeScreenShot();
		driver.getLogger().info("add text widget compelted");
	}
	
	@Override
	public void editTextWidgetAddContent(WebDriver driver, int index, String content)
	{
		driver.getLogger().info("editTextWidgetAddContent started");
		//find current dashboard
		WebElement selectedDashboardEl = getSelectedDashboardEl(driver);
		//click content wrapper area to load ckeditor
		driver.waitForElementPresent("css=" + DashBoardPageId.TEXTWIDGETCONTENTCSS);
		WebElement widget = selectedDashboardEl.findElements(By.cssSelector(DashBoardPageId.TEXTWIDGETCONTENTCSS)).get(index-1);
		widget.click();
		//input text string to editor area
		driver.waitForElementPresent("css=" + DashBoardPageId.TEXTWIDGETEDITORCSS);
		widget = selectedDashboardEl.findElements(By.cssSelector(DashBoardPageId.TEXTWIDGETEDITORCSS)).get(index-1);
		widget.clear();
		widget.click();
		widget.sendKeys(content);
		
		driver.waitForServer();
		
		driver.takeScreenShot();
		driver.getLogger().info("editTextWidgetAddContent completed");
	}
	
	@Override
	public void clickLinkOnWidgetTitle(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.clickLinkOnWidgetTitle started for widgetName=" + widgetName + ", index=" + index);
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		
		if(hasWidgetLink(driver, widgetName, index))
		{
			String titleTitlesLocator = String.format(DashBoardPageId_1200.BUILDERLINKEDTILETITLELOCATOR, widgetName);
			List<WebElement> tileTitles = driver.getWebDriver().findElements(By.cssSelector(titleTitlesLocator));
			
			tileTitles.get(index).click();
			driver.takeScreenShot();
			driver.savePageToFile();			
		}
		else
		{
			throw new NoSuchElementException("The Widget '" + widgetName + "' doesn't have link");
		}	
	}
	
	@Override
	public void clickLinkOnWidgetTitle(WebDriver driver, String widgetName)
	{
		clickLinkOnWidgetTitle(driver, widgetName, 0);
	}
	
	@Override
	public boolean hasWidgetLink(WebDriver driver, String widgetName, int index){
		driver.getLogger().info(
				"DashboardBuilderUtil.isWidgetHasLink started for widgetName=" + widgetName + ", index=" + index);
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId_190.BUILDERTILESEDITAREA)));
		WaitUtil.waitForPageFullyLoaded(driver);

		String titleTitlesLocator = String.format(DashBoardPageId_1200.BUILDERLINKEDTILETITLELOCATOR, widgetName);
		List<WebElement> tileTitles = driver.getWebDriver().findElements(By.cssSelector(titleTitlesLocator));
		if (tileTitles == null || tileTitles.size() <= index) {
			driver.getLogger().info(
					"isWidgetHasLink compelted and returns false. There is no link in the widget");
			return false;
		}else {
			return true;
		}		
	}
		
	@Override
	public boolean hasWidgetLink(WebDriver driver, String widgetName){
		return hasWidgetLink(driver, widgetName, 0);
	}
}
