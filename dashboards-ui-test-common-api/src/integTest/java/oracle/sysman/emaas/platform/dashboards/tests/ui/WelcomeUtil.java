package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class WelcomeUtil
{
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
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.dataExplorers(driver, selection);
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
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		return wu.isLearnMoreItemExisted(driver, itemName);
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
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		return wu.isServiceExistedInWelcome(driver, serviceName);
	}

	/**
	 * Visit "How to get started" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void learnMoreHow(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.learnMoreHow(driver);
	}

	/**
	 * Visit "Service Offerings" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void learnMoreServiceOffering(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.learnMoreServiceOffering(driver);
	}

	/**
	 * Visit "Videos" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void learnMoreVideo(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.learnMoreVideo(driver);
	}

	/**
	 * Visit "Application Performance Monitoring" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitAPM(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitAPM(driver);
	}
	
	/**
	 * Visit "Complliance Service" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitCompliance(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitCompliance(driver);
	}

	/**
	 * Visit "Dashboards" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitDashboards(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitDashboards(driver);
	}

	/**
	 * Visit "Infrustructure Monitoring" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitInfraMonitoring(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitInfraMonitoring(driver);
	}	
	
	/**
	 * Visit specific item in IT Analytics in welcome
	 * 
	 * @param driver
	 * @param selection
	 * 		default | performanceAnalyticsDatabase | performanceAnalyticsApplicationServer | 		
	 * 		resourceAnalyticsDatabase | resourceAnalyticsMiddleware | resourceAnalyticsHost | 
	 *  	applicationPerformanceAnalytic | availabilityAnalytics | dataExplorer | exadataAnalytics
	 * @
	 */
	public static void visitITA(WebDriver driver, String selection) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitITA(driver,selection);
	}

	/**
	 * Visit "Log Analytics" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitLA(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitLA(driver);
	}
	
	/**
	 * Visit "Security Monitoring and Analytics" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitSecurity(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitSecurity(driver);
	}

        /**
	 * Visit "Orchestration" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public static void visitOrchestration(WebDriver driver) 
	{
		IWelcomeUtil wu = new UtilLoader<IWelcomeUtil>().loadUtil(driver, IWelcomeUtil.class);
		wu.visitOrchestration(driver);
	}

}
