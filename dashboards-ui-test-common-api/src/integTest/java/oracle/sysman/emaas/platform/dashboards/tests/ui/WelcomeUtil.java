package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WelcomeUtil
{
	private WelcomeUtil() {
	  }

	/**
	 * Visit Log/Analyze/Search from Data Explorers dropdown in welcome
	 * 
	 * @param driver
	 * @param selection
	 * 				log | analyze | search
	 */
	public static void dataExplorers(WebDriver driver, String selection) 
	{
		String eleXpath = null;
		driver.getLogger().info("Visiting Data Explorer-" + selection + " from Welcome Page...");

		Validator.fromValidValues("dataExplorersSelection", selection, DATA_EXPLORERS_LOG, DATA_EXPLORERS_ANALYZE,
				DATA_EXPLORERS_SEARCH);

		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId.WELCOME_DATAEXP_SELECTID);
		driver.click("id=oj-select-choice-" + DashBoardPageId.WELCOME_DATAEXP_SELECTID);
		driver.takeScreenShot();
		switch (selection) {
			case DATA_EXPLORERS_LOG:
				eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.WELCOME_DATAEXP_SELECTID,
						DashBoardPageId.WELCOME_DATAEXP_LOG);
				break;
			case DATA_EXPLORERS_ANALYZE:
				eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.WELCOME_DATAEXP_SELECTID,
						DashBoardPageId.WELCOME_DATAEXP_ANALYZE);
				break;
			case DATA_EXPLORERS_SEARCH:
				eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.WELCOME_DATAEXP_SELECTID,
						DashBoardPageId.WELCOME_DATAEXP_SEARCH);
				break;
			default:
				break;
		}
		driver.getWebDriver().findElement(By.xpath(eleXpath)).click();
		driver.takeScreenShot();
	}

	/**
	 * Get text of each service or item shown in welcome page.
	 * 
	 * @param serviceName
	 * 		APM | LA | ITA | infraMonitoring | compliance | securityAnalytics | orchestration |dashboards | dataExplorers | getStarted | videos | serviceOfferings
	 * @return
	 */
	private static String getExpectedText(String serviceName)
	{
		String expectedName = null;
		switch (serviceName) {
			case SERVICE_NAME_APM:
				expectedName = "Application Performance Monitoring";
				break;
			case SERVICE_NAME_LA:
				expectedName = "Log Analytics";
				break;
			case SERVICE_NAME_ITA:
				expectedName = "IT Analytics";
				break;
			case SERVICE_NAME_INFRA_MONITORING:
				expectedName = "Infrastructure Monitoring";
				break;
			case SERVICE_NAME_COMPLIANCE:
				expectedName = "Compliance";
				break;
			case SERVICE_NAME_SECURITY_ANALYTICS:
				expectedName = "Security Monitoring and Analytics";
				break;
                        case SERVICE_NAME_ORCHESTRATION:
				expectedName = "Orchestration";
				break;
			case SERVICE_NAME_DASHBOARDS:
				expectedName = "Dashboards";
				break;
			case SERVICE_NAME_DATA_EXPLORERS:
				expectedName = "Explorers";
				break;
			case LEARN_MORE_GET_STARTED:
				expectedName = "How to get started";
				break;
			case LEARN_MORE_VIDEOS:
				expectedName = "Videos";
				break;
			case LEARN_MORE_SERVICE_OFFERINGS:
				expectedName = "Service Offerings";
				break;
			default:
				break;
		}
		return expectedName;
	}

	/**
	 *  Get id of items in Learn More
	 * 
	 * @param itemName
	 * 		getstarted | videos | serviceOfferings
	 * @return
	 */
	private static String getLearnMoreItemId(String itemName)
	{
		String itemId = null;
		switch (itemName) {
			case LEARN_MORE_GET_STARTED:
				itemId = DashBoardPageId.WELCOME_LEARNMORE_GETSTARTED;
				break;
			case LEARN_MORE_VIDEOS:
				itemId = DashBoardPageId.WELCOME_LEARNMORE_VIDEOS;
				break;
			case LEARN_MORE_SERVICE_OFFERINGS:
				itemId = DashBoardPageId.WELCOME_LEARNMORE_SERVICEOFFERING;
				break;
			default:
				break;
		}
		return itemId;
	}

	/**
	 * Get jet's option xpath according to "select" id and "option" id
	 * 
	 * @param driver
	 * @param selectId
	 * @param optionId
	 * @return
	 * @
	 */
	private static String getOptionXpath(WebDriver driver, String selectId, String optionId) 
	{
		String optionXpath;
		WebElement li = driver.getWebDriver().findElement(By.id(optionId));
		List<WebElement> list = driver.getWebDriver().findElements(By.xpath("//select[@id='" + selectId + "']/option"));
		//get the index of option in select dropdown
		int index = list.indexOf(li);
		//get option's xpath generated by jet
		optionXpath = "//ul[@id='oj-listbox-results-" + selectId + "']/li[" + (index + 1) + "]/div";
		return optionXpath;
	}

	/**
	 * Get wrapper id of each service
	 * 
	 * @param serviceName
	 * 		APM | LA | ITA | infraMonitoring | compliance | securityAnalytics | orchestration | dashboards | dataExplorers
	 * @return
	 */
	private static String getServiceWrapperId(String serviceName)
	{
		String serviceWrapperId = null;
		switch (serviceName) {
			case SERVICE_NAME_APM:
				serviceWrapperId = DashBoardPageId.WELCOME_APMLINKCSS;
				break;
			case SERVICE_NAME_LA:
				serviceWrapperId = DashBoardPageId.WELCOME_LALINKCSS;
				break;
			case SERVICE_NAME_ITA:
				serviceWrapperId = DashBoardPageId.WELCOME_ITALINKID;
				break;
			case SERVICE_NAME_INFRA_MONITORING:
				serviceWrapperId = DashBoardPageId.WELCOME_INFRAMONITORINGID;
				break;
			case SERVICE_NAME_COMPLIANCE:
				serviceWrapperId = DashBoardPageId.WELCOME_COMPLIANCEID;
				break;
			case SERVICE_NAME_SECURITY_ANALYTICS:
				serviceWrapperId = DashBoardPageId.WELCOME_SECURITYANALYTICSID;
				break;
                        case SERVICE_NAME_ORCHESTRATION:
				serviceWrapperId = DashBoardPageId.WELCOME_ORCHESTRATIONID;
				break;
			case SERVICE_NAME_DASHBOARDS:
				serviceWrapperId = DashBoardPageId.WELCOME_DASHBOARDSLINKID;
				break;
			case SERVICE_NAME_DATA_EXPLORERS:
				serviceWrapperId = DashBoardPageId.WELCOME_DATAEXP;
				break;
			default:
				break;
		}
		return serviceWrapperId;
	}

	/**
	 * Check if specific item in Learn More is existed in welcome.
	 * 
	 * @param driver
	 * @param itemName
	 * 		getStarted | videos | serviceOfferings
	 * @return
	 */
	public static boolean isLearnMoreItemExisted(WebDriver driver, String itemName)
	{
		driver.getLogger().info("Start to check if learn more item: " + itemName + " is existed in welcome page...");

		Validator.fromValidValues("learMoreItem", itemName, LEARN_MORE_GET_STARTED, LEARN_MORE_VIDEOS,
				LEARN_MORE_SERVICE_OFFERINGS);

		boolean isExisted = false;
		String itemId = WelcomeUtil.getLearnMoreItemId(itemName);
		String nameExpected = WelcomeUtil.getExpectedText(itemName);
		driver.waitForElementPresent("id=" + itemId);
		driver.waitForText("id=" + itemId, nameExpected);
		String nameDisplayed = driver.getWebDriver().findElement(By.id(itemId)).getText();
		if (nameDisplayed.equals(nameExpected)) {
			isExisted = true;
		}
		driver.getLogger().info("Check of learn more item: " + itemName + " existence is finished! Result: " + isExisted);
		return isExisted;
	}

	/**
	 * Check if specific service is existed in welcome
	 * 
	 * @param driver
	 * @param serviceName
	 * 			APM | LA | ITA | infraMonitoring | compliance | securityAnalytics | orchestration | dashboards | dataExplorers 
	 * @return
	 * @
	 */
	public static boolean isServiceExistedInWelcome(WebDriver driver, String serviceName) 
	{
		driver.getLogger().info("Start to check if service: " + serviceName + " is existed in welcome page...");

		Validator.fromValidValues("serviceName", serviceName, SERVICE_NAME_APM, SERVICE_NAME_LA, SERVICE_NAME_ITA,
				SERVICE_NAME_INFRA_MONITORING, SERVICE_NAME_COMPLIANCE, SERVICE_NAME_SECURITY_ANALYTICS, SERVICE_NAME_ORCHESTRATION, SERVICE_NAME_DASHBOARDS, SERVICE_NAME_DATA_EXPLORERS);

		boolean isExisted = false;
		String serviceWrapperId = WelcomeUtil.getServiceWrapperId(serviceName);
		String xpath = "//*[@id='"
				+ serviceWrapperId
				+ "']/div[@class='service-box-wrapper']/div[@class='landing-home-box-content']/div[@class='landing-home-box-content-head']";

		String nameExpected = WelcomeUtil.getExpectedText(serviceName);
		driver.waitForElementPresent(xpath);
		driver.waitForText(xpath, nameExpected);
		String nameDisplayed = driver.getWebDriver().findElement(By.xpath(xpath)).getText();
		if (nameDisplayed.equals(nameExpected)) {
			isExisted = true;
		}
		driver.getLogger().info(
				"Check of service: " + serviceName + " existence in welcome page is finished! Result: " + isExisted);
		return isExisted;
	}

	/**
	 * Visit "How to get started" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void learnMoreHow(WebDriver driver) 
	{
		driver.getLogger().info("Visiting 'Learn More-How to get started' from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_LEARNMORE_GETSTARTED);
		driver.click("id=" + DashBoardPageId.WELCOME_LEARNMORE_GETSTARTED);
		driver.takeScreenShot();
	}

	/**
	 * Visit "Service Offerings" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void learnMoreServiceOffering(WebDriver driver) 
	{
		driver.getLogger().info("Visiting 'Learn More-Service Offerings' from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_LEARNMORE_SERVICEOFFERING);
		driver.click("id=" + DashBoardPageId.WELCOME_LEARNMORE_SERVICEOFFERING);
		driver.takeScreenShot();
	}

	/**
	 * Visit "Videos" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void learnMoreVideo(WebDriver driver) 
	{
		driver.getLogger().info("Visiting 'Learn More-Videos' from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_LEARNMORE_VIDEOS);
		driver.click("id=" + DashBoardPageId.WELCOME_LEARNMORE_VIDEOS);
		driver.takeScreenShot();
	}

	/**
	 * Visit "Application Performance Monitoring" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitAPM(WebDriver driver) 
	{
		driver.getLogger().info("Visit APM from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_APMLINKCSS);
		driver.click("id=" + DashBoardPageId.WELCOME_APMLINKCSS);
		driver.takeScreenShot();
	}
	
	/**
	 * Visit "Complliance Service" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitCompliance(WebDriver driver) 
	{
		driver.getLogger().info("Visit Compliance Service from Welcome page..");
		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_COMPLIANCEID);
		driver.click("id=" + DashBoardPageId.WELCOME_COMPLIANCEID);
		driver.takeScreenShot();
	}

	/**
	 * Visit "Dashboards" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitDashboards(WebDriver driver) 
	{
		driver.getLogger().info("Visit Dashboards from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_DASHBOARDSLINKID);
		driver.click("id=" + DashBoardPageId.WELCOME_DASHBOARDSLINKID);
		driver.takeScreenShot();
	}

	/**
	 * Visit "Infrustructure Monitoring" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitInfraMonitoring(WebDriver driver) 
	{
		driver.getLogger().info("Visit Infrastructure Monitoring from Welcome page..");
		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_INFRAMONITORINGID);
		driver.click("id=" + DashBoardPageId.WELCOME_INFRAMONITORINGID);
		driver.takeScreenShot();
	}	
	
	/**
	 * Visit specific item in IT Analytics in welcome
	 * 
	 * @param driver
	 * @param selection
	 * 		default | performanceAnayticsDatabase | performanceAnalyticsMiddleware | 
	 * 		resourceAnalyticsDatabase | resourceAnalyticsMiddleware | resourceAnalyticsHost | 
	 *  	dataExplorerAnalyze | dataExplorer
	 * @
	 */
	public static void visitITA(WebDriver driver, String selection) 
	{
		driver.getLogger().info("Visiting ITA-" + selection + " from Welcome Page...");

		Validator.fromValidValues("ITASelection", selection, ITA_DEFAULT, ITA_PERFORMANCE_ANALYTICS_DATABASE,
				ITA_PERFORMANCE_ANALYTICS_MIDDLEWARE, ITA_RESOURCE_ANALYTICS_DATABASE, ITA_RESOURCE_ANALYTICS_MIDDLEWARE,
				ITA_RESOURCE_ANALYTICS_HOST, ITA_DATA_EXPLORER_ANALYZE, ITA_DATA_EXPLORER);


		WaitUtil.waitForPageFullyLoaded(driver);
		
		if (ITA_DEFAULT.equals(selection)) {
			driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_ITALINKID);
			driver.click("id=" + DashBoardPageId.WELCOME_ITALINKID);
			driver.takeScreenShot();
		}
		else {
			String eleXpath = null;
			driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId.WELCOME_ITA_SELECTID);
			driver.click("id=oj-select-choice-" + DashBoardPageId.WELCOME_ITA_SELECTID);
			driver.takeScreenShot();
			switch (selection) {
				case ITA_PERFORMANCE_ANALYTICS_DATABASE:
					eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_PADATABASE);
					break;
				case ITA_PERFORMANCE_ANALYTICS_MIDDLEWARE:
					eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_PAMIDDLEWARE);
					break;
				case ITA_RESOURCE_ANALYTICS_DATABASE:
					eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_RADATABASE);
					break;
				case ITA_RESOURCE_ANALYTICS_MIDDLEWARE:
					eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_RAMIDDLEWARE);
					break;
				case ITA_RESOURCE_ANALYTICS_HOST:
					eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_RA_HOST);
					break;
				case ITA_DATA_EXPLORER_ANALYZE:
					eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_DEANALYZE);
					break;
				case ITA_DATA_EXPLORER:
					eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_DE);
					break;
				default:
					break;
			}
			driver.click(eleXpath);
			driver.takeScreenShot();
		}
	}

	/**
	 * Visit "Log Analytics" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitLA(WebDriver driver) 
	{
		driver.getLogger().info("Visiting LA from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_LALINKCSS);
		driver.click("id=" + DashBoardPageId.WELCOME_LALINKCSS);
		driver.takeScreenShot();
	}
	
	/**
	 * Visit "Security Monitoring and Analytics" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitSecurityAnalytics(WebDriver driver) 
	{
		driver.getLogger().info("Visit Security Monitoring and Analytics from Welcome page..");
		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_SECURITYANALYTICSID);
		driver.click("id=" + DashBoardPageId.WELCOME_SECURITYANALYTICSID);
		driver.takeScreenShot();
	}

        /**
	 * Visit "Orchestration" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitOrchestration(WebDriver driver) 
	{
		driver.getLogger().info("Visit Orchestration from Welcome page..");
		WaitUtil.waitForPageFullyLoaded(driver);
		
		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_ORCHESTRATIONID);
		driver.click("id=" + DashBoardPageId.WELCOME_ORCHESTRATIONID);
		driver.takeScreenShot();
	}

	public static final String DATA_EXPLORERS_LOG = "log";
	public static final String DATA_EXPLORERS_ANALYZE = "analyze";
	public static final String DATA_EXPLORERS_SEARCH = "search";

	public static final String ITA_DEFAULT = "default";
	public static final String ITA_PERFORMANCE_ANALYTICS_DATABASE = "performanceAnalyticsDatabase";
	public static final String ITA_PERFORMANCE_ANALYTICS_MIDDLEWARE = "performanceAnalyticsMiddleware";
	public static final String ITA_RESOURCE_ANALYTICS_DATABASE = "resourceAnalyticsDatabase";
	public static final String ITA_RESOURCE_ANALYTICS_MIDDLEWARE = "resourceAnalyticsMiddleware";
	public static final String ITA_RESOURCE_ANALYTICS_HOST = "resourceAnalyticsHost";
	public static final String ITA_DATA_EXPLORER_ANALYZE = "dataExplorerAnalyze";
	public static final String ITA_DATA_EXPLORER = "dataExplorer";

	public static final String LEARN_MORE_GET_STARTED = "getStarted";
	public static final String LEARN_MORE_VIDEOS = "videos";
	public static final String LEARN_MORE_SERVICE_OFFERINGS = "serviceOfferings";

	public static final String SERVICE_NAME_APM = "APM";
	public static final String SERVICE_NAME_LA = "LA";
	public static final String SERVICE_NAME_ITA = "ITA";
	public static final String SERVICE_NAME_INFRA_MONITORING = "infraMonitoring";
	public static final String SERVICE_NAME_COMPLIANCE = "compliance";
	public static final String SERVICE_NAME_SECURITY_ANALYTICS = "securityAnalytics";
	public static final String SERVICE_NAME_ORCHESTRATION = "orchestration";
	public static final String SERVICE_NAME_DASHBOARDS = "dashboards";
	public static final String SERVICE_NAME_DATA_EXPLORERS = "dataExplorers";

}
