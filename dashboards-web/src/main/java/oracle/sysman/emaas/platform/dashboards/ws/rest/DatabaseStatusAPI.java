package oracle.sysman.emaas.platform.dashboards.ws.rest;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DatabaseDependencyUnavailableException;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.emcpdf.util.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/dbstatus")
public class DatabaseStatusAPI extends APIBase{
    private static Logger _logger = LogManager.getLogger(DatabaseStatusAPI.class);
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDBstatus(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
                                @HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer)
    {
        infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/omcstatus");

        try {
            if (!DependencyStatus.getInstance().isDatabaseUp())  {
                _logger.error("Error to call [GET] /v1/dbstatus: database is down");
                throw new DatabaseDependencyUnavailableException();
            }
        }
        catch (DatabaseDependencyUnavailableException e) {
            _logger.error(e);
            ErrorEntity ee = new ErrorEntity(e);
            return Response.status(ee.getStatusCode()).entity(JsonUtil.buildNormalMapper().toJson(ee)).build();
        }

        return Response.status(Response.Status.OK).entity("{\"dbstatus\" : \"UP\"}").build();

    }
}
