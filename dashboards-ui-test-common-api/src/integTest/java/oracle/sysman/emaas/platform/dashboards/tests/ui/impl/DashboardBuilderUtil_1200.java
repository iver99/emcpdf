package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.*;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

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
		Validator.equalOrLargerThan("index", index, 1);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		driver.waitForElementVisible(DashBoardPageId_190.BUILDERTILESEDITAREA);
		WaitUtil.waitForPageFullyLoaded(driver);

		clickTileConfigButton(driver, widgetName, index);

		//click edit button in widget config menu
		driver.waitForElementPresent(DashBoardPageId_1200.BUILDERTILEEDITLOCATOR);
		driver.click(DashBoardPageId_1200.BUILDERTILEEDITLOCATOR);

		driver.waitForElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTAREACSSLOCATOR);
		driver.waitForElementVisible("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTAREACSSLOCATOR);

		//remove link if widget title is linked
		if(driver.isElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTREMOVELINKCSSLOCATOR)){
			driver.click("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTREMOVELINKCSSLOCATOR);
			driver.waitForElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR);
		}

		if(dashboardName != null && !dashboardName.isEmpty()){
			
			driver.moveToElement("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR);
			driver.clear("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR);
			WaitUtil.waitForPageFullyLoaded(driver);
			
			driver.moveToElement("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR);
			driver.click("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR);
			driver.waitForElementVisible("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHGETCSSLOCATOR);
			driver.sendKeys("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR, dashboardName);		

			//verify input box value
			Assert.assertEquals(driver.getAttribute("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR + "@value"), dashboardName);

			driver.waitForElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBTNCSSLOCATOR);
			driver.click("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHBTNCSSLOCATOR);
			//wait for ajax resolved
			WaitUtil.waitForPageFullyLoaded(driver);
						
			driver.getLogger().info("[DashboardHomeUtil] start to add link");
			int matchWidegtCount = driver.getElementCount("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHGETCSSLOCATOR);
			if (matchWidegtCount <= 0) {
				throw new NoSuchElementException("Right drawer content for search string =" + dashboardName + " is not found");
			}
			WaitUtil.waitForPageFullyLoaded(driver);

			try {
				driver.waitForElementPresent("css=" + DashBoardPageId_1200.BUILDERRIGHTPANELEDITCONTENTSEARCHGETCSSLOCATOR);
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
	public void addLinkToWidgetTitle(WebDriver driver, String widgetName, String dashboardName)
	{
		addLinkToWidgetTitle(driver, widgetName, 1, dashboardName);
	}

	@Override
	public boolean verifyLinkOnWidgetTitle(WebDriver driver, String widgetName, int index, String dashboardName)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.verifyLinkOnWidgetTitle started for widgetName=" + widgetName + ", index=" + index
						+ ", linked dashboardName=" + dashboardName);
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan("index", index, 1);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		driver.waitForElementVisible(DashBoardPageId_190.BUILDERTILESEDITAREA);
		WaitUtil.waitForPageFullyLoaded(driver);

		String titleTitlesLocator = String.format(DashBoardPageId_1200.BUILDERLINKEDTILETITLELOCATOR, widgetName);
		int titleCount = driver.getElementCount(titleTitlesLocator);
		if (titleCount < index) {
			driver.getLogger().info(
					"verifyLinkOnWidgetTitle compelted and returns false. Expected linked dashboardName is " + dashboardName
							+ ", actual it is not linked.");
			return false;
		}else {
			driver.click("xpath=(" +titleTitlesLocator+")[" + index + "]");
			WaitUtil.waitForPageFullyLoaded(driver);
			String realName = driver.getAttribute(DashBoardPageId.BUILDERNAMETEXTLOCATOR+"@titile");
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
		return verifyLinkOnWidgetTitle(driver, widgetName, 1, dashboardName);
        }

	@Override
	public void addTextWidgetToDashboard(WebDriver driver)
	{
		driver.getLogger().info("add text widget started");
		driver.waitForElementPresent("css=" + DashBoardPageId.DASHBOARDADDTEXTWIDGETCSS);
		
		driver.click("css=" + DashBoardPageId.DASHBOARDADDTEXTWIDGETCSS);
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.getLogger().info("add text widget compelted");
	}
	
	@Override
	public void editTextWidgetAddContent(WebDriver driver, int index, String content)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.editTextWidgetAddContent started for Content=" + content + ", index=" + index);
		
		Validator.equalOrLargerThan("index", index, 1);
		
		driver.getLogger().info("editTextWidgetAddContent started");
		
		//click content wrapper area to load ckeditor
		driver.waitForElementPresent("css=" + DashBoardPageId.TEXTWIDGETCONTENTCSS);
		
		driver.click("xpath=("+ DashBoardPageId.TEXTWIDGETCONTENTXPATH +")[" + index +"]");

		//input text string to editor area
		driver.waitForElementPresent("css=" + DashBoardPageId.TEXTWIDGETEDITORCSS);
		driver.clear("xpath=("+ DashBoardPageId.TEXTWIDGETEDITORXPATH +")[" + index +"]");
		driver.click("xpath=("+ DashBoardPageId.TEXTWIDGETEDITORXPATH +")[" + index +"]");
		driver.sendKeys("xpath=("+ DashBoardPageId.TEXTWIDGETEDITORXPATH +")[" + index +"]", content);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.getLogger().info("editTextWidgetAddContent completed");
	}
	
	@Override
	public void clickLinkOnWidgetTitle(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.clickLinkOnWidgetTitle started for widgetName=" + widgetName + ", index=" + index);
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan("index", index, 1);
		
		if(hasWidgetLink(driver, widgetName, index))
		{			
			String titleTitlesLocator = String.format(DashBoardPageId_1200.BUILDERLINKEDTILETITLELOCATOR, widgetName);
			driver.click("xpath=(" + titleTitlesLocator + ")[" + index + "]");
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
		Validator.equalOrLargerThan("index", index, 1);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		driver.waitForElementVisible(DashBoardPageId_190.BUILDERTILESEDITAREA);
		WaitUtil.waitForPageFullyLoaded(driver);

		String titleTitlesLocator = String.format(DashBoardPageId_1200.BUILDERLINKEDTILETITLELOCATOR, widgetName);
		int titleCount = driver.getElementCount(titleTitlesLocator);
		if (titleCount < index) {
			driver.getLogger().info(
					"isWidgetHasLink compelted and returns false. There is no link in the widget");
			return false;
		}else {
			return true;
		}		
	}
		
	@Override
	public boolean hasWidgetLink(WebDriver driver, String widgetName){
		return hasWidgetLink(driver, widgetName, 1);
	}
	
	@Override
	public void addImageInTextWidget(WebDriver driver, int index, String url, String alternativeText)
	{
		driver.getLogger().info("add image in Text Widget");
		
		Validator.notEmptyString("URL", url);
		Validator.equalOrLargerThan("index", index, 1);

		//click content wrapper area to load ckeditor
		driver.waitForElementPresent("css=" + DashBoardPageId.TEXTWIDGETCONTENTCSS);
		driver.click("xpath=("+ DashBoardPageId.TEXTWIDGETCONTENTXPATH +")[" + index +"]");

		driver.waitForElementPresent("css=" + DashBoardPageId.IMAGEICONCSS);

		driver.click("css=" + DashBoardPageId.IMAGEICONCSS);

		driver.waitForElementPresent("css=" + DashBoardPageId.IMAGEDIALOGCSS);
		
		driver.clear(DashBoardPageId.IMAGEURLINPUT);
		driver.sendKeys(DashBoardPageId.IMAGEURLINPUT, url);
		
		if(alternativeText != null)
		{
			driver.clear(DashBoardPageId.ALTERNATIVEINPUT);			
			driver.waitForElementVisible(DashBoardPageId.ALTERNATIVEINPUT);
			driver.sendKeys(DashBoardPageId.ALTERNATIVEINPUT, alternativeText);			
		}

		driver.click("css=" + DashBoardPageId.OKBTNCSS);

		driver.getLogger().info("add link in text widget completed");
	}
	
	@Override
	public void addLinkInTextWidget(WebDriver driver, int index, String url, String option)
	{
		driver.getLogger().info("add link in Text Widget");
		Validator.equalOrLargerThan("index", index, 1);		
	
		//click content wrapper area to load ckeditor
		driver.waitForElementPresent("css=" + DashBoardPageId.TEXTWIDGETCONTENTCSS);
		driver.click("xpath=("+ DashBoardPageId.TEXTWIDGETCONTENTXPATH +")[" + index +"]");
		
		driver.waitForElementPresent("css=" + DashBoardPageId.LINKICONCSS);		
	 	driver.click("css=" + DashBoardPageId.LINKICONCSS);
	 	
	 	driver.waitForElementPresent("css=" + DashBoardPageId.LINKDIALOGCSS);
 	 	driver.click(DashBoardPageId.PROTOCOLOPTION);	 		 	
	 	
	 	switch(option)
	 	{
	 		case DashBoardPageId.PROTOCOLOPTION_HTTP:
	 			driver.getLogger().info("Click http protocol");
	 			driver.select("css=select.cke_dialog_ui_input_select", "value=http://");
				
//	 			driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.HTTPCSS)).findElement(By.xpath("..")).click();
//	 			driver.click("css=" + DashBoardPageId.HTTPCSS);
			    
	 			break;
	 		case DashBoardPageId.PROTOCOLOPTION_HTTPS:
	 			driver.getLogger().info("Click https protocol");			
		
			    driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.HTTPSCSS)).findElement(By.xpath("..")).click();
			    driver.click("css=" + DashBoardPageId.HTTPSCSS);
			    
	 			break;	
	 		case DashBoardPageId.PROTOCOLOPTION_FTP:
	 			driver.getLogger().info("Click ftp protocol");
	 			
	 			driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.FTPCSS)).findElement(By.xpath("..")).click();
	 			driver.click("css=" + DashBoardPageId.FTPCSS);
	 			
	 			break;
	 		case DashBoardPageId.PROTOCOLOPTION_NEWS:
	 			driver.getLogger().info("Click news protocol");
	 			
	 			driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.NEWSCSS)).findElement(By.xpath("..")).click();
	 			driver.click("css=" + DashBoardPageId.NEWSCSS);
	 			
	 			break;
	 		case DashBoardPageId.PROTOCOLOPTION_OTHER:
	 			driver.getLogger().info("Click other protocol");
	 			
	 			driver.getWebDriver().findElement(By.xpath(DashBoardPageId.OTHERXPATH)).findElement(By.xpath("..")).click();
	 			driver.click("css=" + DashBoardPageId.OTHERXPATH);

	 			break;
	 		default:
	 				break;
	 	}
	 	
	 	driver.clear(DashBoardPageId.URLINPUT);
	 	driver.sendKeys(DashBoardPageId.URLINPUT, url);
	 		 	
	 	driver.click("css=" + DashBoardPageId.OKBTNCSS);	
		
		driver.getLogger().info("add link in text widget completed");
	}

}
