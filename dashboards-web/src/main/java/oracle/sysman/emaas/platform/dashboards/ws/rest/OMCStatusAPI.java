/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;

import oracle.sysman.emaas.platform.emcpdf.util.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author aduan
 */
@Path("/v1/omcstatus")
public class OMCStatusAPI extends APIBase
{
	private static Logger _logger = LogManager.getLogger(OMCStatusAPI.class);

	@POST
	//	@Produces(MediaType.APPLICATION_JSON)
	public Response getOmcStatus(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/omcstatus");

		try {
			initializeUserContext(tenantIdParam, userTenant);
			Response resp = Response.status(Status.OK).entity(null).build();
			return resp;
		}
		catch (DashboardException e) {
			_logger.error(e.getLocalizedMessage(), e);
			ErrorEntity ee = new ErrorEntity(e);
			return Response.status(ee.getStatusCode()).entity(JsonUtil.buildNormalMapper().toJson(ee)).build();
		}
	}
}
