/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.ws.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.tenant.TenantIdProcessor;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.TenantManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DatabaseDependencyUnavailableException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.NewOobExporter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;

/**
 * @author reliang
 *
 */

@Path("/v1/tool")
public class InternalToolAPI extends APIBase {
    private static final Logger LOGGER = LogManager.getLogger(InternalToolAPI.class);
    
    @GET
    @Path("dashboard/{name}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response queryDashboardByName(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
            @HeaderParam(value = "X-REMOTE-USER") String userTenant, @PathParam("name") String dashboardName){
        infoInteractionLogAPIIncomingCall(tenantIdParam, null, "Service call to [GET] /v1/tool/dashboard/{}", dashboardName);
        DashboardManager dm = DashboardManager.getInstance();
        try {
            if (!DependencyStatus.getInstance().isDatabaseUp())  {
                LOGGER.error("Error to call [GET] /v1/tool/dashboard/{}: database is down", dashboardName);
                throw new DatabaseDependencyUnavailableException();
            }
            Long tenantId = getTenantId(tenantIdParam);
            initializeUserContext(tenantIdParam, userTenant);
            Dashboard dbd = dm.getDashboardByName(dashboardName, tenantId);
            String dbJson = NewOobExporter.exportDashboard(dbd);
            return Response.ok(dbJson).build();
        }
        catch (DashboardException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return buildErrorResponse(new ErrorEntity(e));
        }
        catch (BasicServiceMalfunctionException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return buildErrorResponse(new ErrorEntity(e));
        }
        catch (JSONException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return buildErrorResponse(new ErrorEntity(e));
        }
        finally {
            clearUserContext();
        }
    }
    
    @DELETE
    @Path("offboard/{tenantName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTenant(@PathParam("tenantName") String tenantName){
        infoInteractionLogAPIIncomingCall(tenantName, null, "Service call to [DELETE] /v1/tool/offboard/{}", tenantName);
        int statusCode = 200;
        StringBuilder message = new StringBuilder();
        if (tenantName == null || tenantName.isEmpty()) {
            message.append("{\"errorMsg\":\"BAD REQUEST. PLEASE PROVIDE THE TENANT NAME.\"}");
            return Response.status(400).entity(message.toString()).build();
        }
        
        try {
            if (!DependencyStatus.getInstance().isDatabaseUp())  {
                LOGGER.error("Error to call [DELETE] /v1/tool/offboard/{}: database is down", tenantName);
                throw new DatabaseDependencyUnavailableException();
            }
            
            Long internalTenantId = TenantIdProcessor.getInternalTenantIdFromOpcTenantId(tenantName);
            LOGGER.info("Get internal tenant id {} for opc tenant id {}", internalTenantId, tenantName);
            if(internalTenantId == null) {
                throw new BasicServiceMalfunctionException("Tenant id does not exist.", "Dashboard");
            }
            
            TenantManager tenantManager  = TenantManager.getInstance();
            tenantManager.cleanTenant(internalTenantId);
            message.append(tenantName).append(" has been deleted!");
        } catch (BasicServiceMalfunctionException basicEx) {
            statusCode = 404;
            message.append("{\"errorMsg\":\"Tenant Id [").append(tenantName).append("] does not exist.\"}");
            LOGGER.error("Tenant Id {} does not exist: {}", tenantName, basicEx.getMessage());
        } catch (DashboardException e) {
            statusCode = 500;
            message.append("{\"errorMsg\":\"Fall into error while deleting tenant [").append(tenantName).append("] because: ")
                    .append(e.getMessage().toUpperCase()).append("\"}");
            LOGGER.error("Fall into error while deleting tenant [{}] because: {}", tenantName, e.getMessage());
        } catch(Exception ex) {
            statusCode = 500;
            message.append("{\"errorMsg\":\"Fall into error while deleting tenant [").append(tenantName).append("] because: ")
            .append(ex.getMessage().toUpperCase()).append("\"}");
            LOGGER.error("Fall into error while deleting tenant [{}] because: {}", tenantName, ex.getMessage());
        }
        return Response.status(statusCode).entity(message.toString()).build();
    }
}
