package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1200;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class DashboardBuilderUtil_1230 extends DashboardBuilderUtil_1220{
	@Override
	public void editTextWidgetAddContent(WebDriver driver, int index, String content)
	{
		driver.getLogger().info("editTextWidgetAddContent started");
		//find current dashboard
		switchTextWidgetToEditMode(driver, index);
		
		driver.waitForElementPresent("xpath=" + DashBoardPageId.TEXTWIDGETEDITORXPATH);
		driver.clear("xpath=(" + DashBoardPageId.TEXTWIDGETEDITORXPATH + ")[" + index + "]");
		driver.click("xpath=(" + DashBoardPageId.TEXTWIDGETEDITORXPATH + ")[" + index + "]");
		driver.sendKeys("xpath=(" + DashBoardPageId.TEXTWIDGETEDITORXPATH + ")[" + index + "]", content);
		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.getLogger().info("editTextWidgetAddContent completed");
	}
	
	@Override
	public void addImageInTextWidget(WebDriver driver, int index, String url, String alternativeText)
	{
		driver.getLogger().info("add image in Text Widget");
		
		Validator.notEmptyString("URL", url);
		Validator.equalOrLargerThan0("index", index);

		//click content wrapper area to load ckeditor
		switchTextWidgetToEditMode(driver, index);

		driver.waitForElementPresent("css=" + DashBoardPageId.IMAGEICONCSS);

		driver.click("css=" + DashBoardPageId.IMAGEICONCSS);

		driver.waitForElementPresent("css=" + DashBoardPageId.IMAGEDIALOGCSS);
		driver.clear("xpath=" + DashBoardPageId.IMAGEURLINPUT);
		driver.sendKeys("xpath=" + DashBoardPageId.IMAGEURLINPUT, url);
		
		if(alternativeText != null)
		{
			driver.clear("xpath=" + DashBoardPageId.ALTERNATIVEINPUT);
			driver.waitForElementVisible("xpath=" + DashBoardPageId.ALTERNATIVEINPUT);
			driver.sendKeys("xpath=" + DashBoardPageId.ALTERNATIVEINPUT, alternativeText);
		}

		driver.click("css=" + DashBoardPageId.OKBTNCSS);

		driver.getLogger().info("add link in text widget completed");
	}
	
	@Override
	public void addLinkInTextWidget(WebDriver driver, int index, String url, String option)
	{
		driver.getLogger().info("add link in Text Widget");
		
		switchTextWidgetToEditMode(driver, index);
		
		driver.waitForElementPresent("css=" + DashBoardPageId.LINKICONCSS);
		
	 	driver.click("css=" + DashBoardPageId.LINKICONCSS);
	 	
	 	driver.waitForElementPresent("css=" + DashBoardPageId.LINKDIALOGCSS);
	 	
	 	driver.clear(DashBoardPageId.URLINPUT);
	 	driver.sendKeys(DashBoardPageId.URLINPUT, url);
	 	
	 	driver.click(DashBoardPageId.PROTOCOLOPTION);
	 	
	 	driver.click(DashBoardPageId.PROTOCOLOPTION + "//select");
	 	
	 	switch(option)
	 	{
	 		case DashBoardPageId.PROTOCOLOPTION_HTTP:
	 			driver.getLogger().info("Click http protocol");	 
	 			driver.click("css=" + DashBoardPageId.HTTPCSS);	    
	 			break;
	 		case DashBoardPageId.PROTOCOLOPTION_HTTPS:
	 			driver.getLogger().info("Click https protocol");	 			
	 			driver.click("css=" + DashBoardPageId.HTTPSCSS);
	 			break;	
	 		case DashBoardPageId.PROTOCOLOPTION_FTP:
	 			driver.getLogger().info("Click ftp protocol");		
	 			driver.click("css=" + DashBoardPageId.FTPCSS);
	 			break;
	 		case DashBoardPageId.PROTOCOLOPTION_NEWS:
	 			driver.getLogger().info("Click news protocol");
	 			driver.click("css=" + DashBoardPageId.NEWSCSS);
	 			break;
	 		case DashBoardPageId.PROTOCOLOPTION_OTHER:
	 			driver.getLogger().info("Click other protocol");
	 			driver.click("xpath=" + DashBoardPageId.OTHERXPATH);
	 			break;
	 		default:
	 				break;
	 	}
	 	driver.click("css=" + DashBoardPageId.OKBTNCSS);	
		
		driver.getLogger().info("add link in text widget completed");
	}
	
	@Override
	public void switchTextWidgetToEditMode(WebDriver driver, int index) {
		driver.getLogger().info("start to switch text widget to edit mode");
		//click "Edit" option in text widget title to load ckeditor
		clickTextWidgetConfigureButton(driver, index);

		//click edit button in widget config menu
		driver.waitForElementPresent(DashBoardPageId_1200.BUILDERTILEEDITLOCATOR);
		driver.click(DashBoardPageId_1200.BUILDERTILEEDITLOCATOR);
		driver.getLogger().info("switch to text widget edit mode complete");
	}
	
	protected void clickTextWidgetConfigureButton(WebDriver driver, int index)
	{
		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.waitForElementPresent("xpath=" + DashBoardPageId.TEXTWIDGETXPATH);
		driver.moveToElement("xpath=(" + DashBoardPageId.TEXTWIDGETXPATH + ")[" + index + "]");
		
		driver.click("xpath=(" + DashBoardPageId_190.BUILDERTILECONFIGLOCATOR + ")[" + index + "]");		
	}
	
	protected void getTextWidgetTitleElement(WebDriver driver, int index)
	{
		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.waitForElementPresent("xpath=" + DashBoardPageId.TEXTWIDGETXPATH);
		driver.moveToElement("xpath=(" + DashBoardPageId.TEXTWIDGETXPATH + "[" + index + "]");
		
		driver.click("xpath=(" + DashBoardPageId_190.BUILDERTILECONFIGLOCATOR + ")[" + index + "]");
	}
}
