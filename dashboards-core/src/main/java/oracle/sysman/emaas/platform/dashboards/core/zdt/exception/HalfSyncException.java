package oracle.sysman.emaas.platform.dashboards.core.zdt.exception;

/**
 * #1.when 'SYNC_TYPE=half' in sync table is more than 1 record, should throw this exception
 * #2. when getting lastComparisonDateForSync, if last compared date is null, throw this exception
 * Created by chehao on 9/18/2017 12:00.
 */
public class HalfSyncException extends  Exception{
    public HalfSyncException(String message)
    {
        super(message);
    }
}
