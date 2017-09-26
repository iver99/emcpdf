package oracle.sysman.emaas.platform.dashboards.comparator.timer;

import javax.management.Notification;
import javax.management.NotificationListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.ZDTAPI;

public class DashboardComparatorHandlerNotification implements NotificationListener{

	private static String DOMAINNAME = "CloudServices";
	private static int SKIPMINS = 2;//FIXME why set to 2mins
	private final static Logger LOGGER = LogManager.getLogger(DashboardComparatorHandlerNotification.class);
	private ZDTAPI api;

	@Override
	public void handleNotification(Notification arg0, Object arg1) {
		
		LOGGER.info("******start to handle comparator*******");
		api = new ZDTAPI();
		api.compareRows(DOMAINNAME,SKIPMINS);
	    LOGGER.info("*****end to compare *********");
	}

}
