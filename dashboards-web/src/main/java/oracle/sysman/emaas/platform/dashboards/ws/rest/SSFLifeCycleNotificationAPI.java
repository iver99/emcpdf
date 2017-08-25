package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.ssfnotification.SSFLifeCycleNotifyEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by guochen on 3/16/17.
 */
@Path("/v1/ssflifecycle.ntf")
public class SSFLifeCycleNotificationAPI extends APIBase {
	private static final Logger LOGGER = LogManager.getLogger(SSFLifeCycleNotificationAPI.class);

	// make sure this is the same with the URI for current API
	public static final String SSF_LIFECYCLE_NTF_URI = "/emcpdf/api/v1/ssflifecycle.ntf";

	/**
	 * This REST API accepcts and handles the SSF notification events. The expected input is:
	 * {
	 *		type: "UP"		// the SSF lifecycle type, value could be "UP" or "DOWN"
	 * }
	 *
	 * @param data
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response notifySsfLifecycle(JSONObject data)
	{
		try {
			infoInteractionLogAPIIncomingCall( "Saved Search Service",
					"Service call to [PUT] /v1/ssflifecycle \npayload is \"{}\"", data.toString());

			// shold be careful if changes are made on this method, as it won't go to auth listener
			SSFLifeCycleNotifyEntity ssflcne = getJsonUtil().fromJson(data.toString(), SSFLifeCycleNotifyEntity.class);
			// only care about the SSSF up lifecycle. This will clear the dashboard-api side OOB widget cache
			if (ssflcne != null && SSFLifeCycleNotifyEntity.SSFNotificationType.UP.equals(ssflcne.getType())) {
			    // TODO do something
				return Response.status(Response.Status.NO_CONTENT).build();
			}
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}
	}
}
