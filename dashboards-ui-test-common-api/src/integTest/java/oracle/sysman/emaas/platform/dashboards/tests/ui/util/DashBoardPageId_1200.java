package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

public class DashBoardPageId_1200
{
	//dashboard builder tile add link locator
	public static final String BUILDERTILEEDITLOCATOR = "//ul[not(contains(@style,'display:none'))]/li[@data-option='edit']/a";
	public static final String BUILDERRIGHTPANELEDITCONTENTAREACSSLOCATOR = ".dbd-right-panel-editcontent-title";
	public static final String BUILDERRIGHTPANELEDITCONTENTSEARCHBOXCSSLOCATOR = "input#dashboard-content-search-input";
	public static final String BUILDERRIGHTPANELEDITCONTENTREMOVELINKCSSLOCATOR = "button.clear-content-search-input-btn[title^='Remove Link']";
	public static final String BUILDERRIGHTPANELEDITCONTENTSEARCHBTNCSSLOCATOR = "button[id^='dashboard-content-search-btn_']";
	public static final String BUILDERRIGHTPANELEDITCONTENTSEARCHGETCSSLOCATOR = ".search-content-dropdown-list-ul li span";
	public static final String BUILDERRIGHTPANELEDITCONTENTADDBTNCSSLOCATOR = "button.oj-button-primary[id^='dashboard-content-add-btn_']";
	public static final String BUILDERLINKEDTILETITLELOCATOR_1 = "h2>a[data-tile-title='%s']";
	public static final String BUILDERLINKEDTILETITLELOCATOR = "//h2//a[contains(@data-tile-title, '%s')]";

	private DashBoardPageId_1200()
	{
	}
}