package oracle.sysman.emaas.platform.dashboards.comparator.webutils.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chehao on 8/28/2017 10:37.
 */
public class TimeUtil {

    public static Date getCurrentUTCTime()
    {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        long localNow = System.currentTimeMillis();
        long offset = cal.getTimeZone().getOffset(localNow);
        Date utcDate = new Date(localNow - offset);

        return utcDate;
    }
    public static String getTimeString(Date date)
    {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * currentTime - skipMins = compare time stamp
     * @param skipMinutes
     * @return
     */
    public static String getMaxTimeStampStr(int skipMinutes) {
        Date currentUtcDate = TimeUtil.getCurrentUTCTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentUtcDate);
        cal.add(Calendar.MINUTE, (skipMinutes * (-1)));
        Date maxTimeStamp = cal.getTime();
        String maxTimeStampStr = TimeUtil.getTimeString(maxTimeStamp);
        return maxTimeStampStr;
    }
}
