package oracle.sysman.emaas.platform.dashboards.ws.rest;

import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DatabaseDependencyUnavailableException;
import oracle.sysman.emaas.platform.dashboards.targetmodel.services.DashboardStatus;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.DashboardDependencyStatus;
import oracle.sysman.emaas.platform.emcpdf.util.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/dfstatus")
public class DashboardStatusAPI extends APIBase {
    private static Logger _logger = LogManager.getLogger(DashboardStatusAPI.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDBstatus(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
                                @HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer) {
        infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/dfstatus");
        DashboardDependencyStatus dashboardDependencyStatus = new DashboardDependencyStatus();

        if (!DependencyStatus.getInstance().isDatabaseUp()) {
            _logger.error("Error to call [GET] /v1/dfstatus: database is down");
            dashboardDependencyStatus.setDb_status(DashboardDependencyStatus.DOWN_STATUS);
        }

        if (!DependencyStatus.getInstance().isEntityNamingUp()) {
            _logger.error("Error to call [GET] /v1/dfstatus: entity naming is down");
            dashboardDependencyStatus.setEntity_naming_status(DashboardDependencyStatus.DOWN_STATUS);
        }

        return Response.status(Response.Status.OK).entity(JsonUtil.buildNormalMapper().toJson(dashboardDependencyStatus)).build();

    }
}
