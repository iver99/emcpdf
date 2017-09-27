/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.model;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author wenjzhu
 */
public class TileTest
{

	@Test(groups = { "s1" })
	public void testTile() throws DashboardException
	{
		Tile tile = new Tile();
		Date now = new Date();
		tile.setColumn(1);
		tile.setContent("ss");
		tile.setCreationDate(now);
		Dashboard d = new Dashboard();
		tile.setDashboard(d);
		tile.setHeight(1);
		tile.setIsMaximized(true);
		tile.setLastModificationDate(now);
		tile.setLastModifiedBy("ss");
		tile.setLinkText("ss");
		tile.setLinkUrl("ss");
		tile.setOwner("ss");
		TileParam tp = new TileParam();
		tp.setName("ss");
		tp.setValue("");
		tile.setParameters(Arrays.asList(new TileParam[] { tp }));
		tile.setProviderAssetRoot("ss");
		tile.setProviderName("ss");
		tile.setProviderVersion("ss");
		tile.setRow(1);
		tile.setTileId("1");
		tile.setTitle("ss");
		tile.setType("ss");
		tile.setWidgetCreationTime("ss");
		tile.setWidgetDescription("ss");
		tile.setWidgetGroupName("ss");
		tile.setWidgetHistogram("ss");
		tile.setWidgetIcon("ss");
		tile.setWidgetName("s");
		tile.setWidgetKocName("ss");
		tile.setWidgetLinkedDashboard(BigInteger.valueOf(1L));
		tile.setWidgetOwner("ss");
		tile.setWidgetSource(1);
		tile.setWidgetSupportTimeControl(true);
		tile.setWidgetTemplate("ss");
		tile.setWidgetUniqueId("ss");
		tile.setWidgetViewmode("ss");
		tile.setWidth(2);
		tile.setFederationSupported(FederationSupportedType.FEDERATION_AND_NON_FEDERATION);
		Assert.assertNotNull(tile.getWidgetSupportTimeControl());
		Assert.assertNotNull(tile.getWidgetUniqueId());
		Assert.assertNotNull(tile.getWidgetViewmode());
		Assert.assertNotNull(tile.getLastModificationDate());
		Assert.assertNotNull(tile.getLastModifiedBy());
		Assert.assertNotNull(tile.getWidgetLinkedDashboard());
		Assert.assertNotNull(tile.getFederationSupported());
		Assert.assertNull(tile.getParameter("NonExit"));
		Assert.assertNull(tile.removeParameter("NonExit"));

		EmsDashboardTile et = new EmsDashboardTile();
		EmsDashboardTileParams edtp = new EmsDashboardTileParams();
		edtp.setParamType(0);
		edtp.setIsSystem(0);
		edtp.setParamName("ss");
		edtp.setParamValueNum(0);
		edtp.setParamValueStr("ss");
		edtp.setParamValueTimestamp(now);
		et.addEmsDashboardTileParams(edtp);
		Assert.assertNotNull(edtp.getDashboardTile());
		Assert.assertNotNull(edtp.getIsSystem());
		Assert.assertNotNull(edtp.getParamName());
		Assert.assertNotNull(edtp.getParamType());
		Assert.assertNotNull(edtp.getParamValueStr());
		Assert.assertNotNull(edtp.getParamValueTimestamp());

		tile.setType(Tile.TILE_TYPE_TEXT_WIDGET);

		EmsDashboardTile edt = tile.getPersistenceEntity(et);
		Assert.assertNotNull(edt);
		edt = tile.getPersistenceEntity(null);
		Assert.assertNotNull(edt);

		tile.setType(Tile.TILE_TYPE_DEFAULT);
		edt = tile.getPersistenceEntity(new EmsDashboardTile());
		Assert.assertNotNull(edt);
		edt = tile.getPersistenceEntity(null);
		Assert.assertNotNull(edt);
	}

}
