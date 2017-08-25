package oracle.sysman.emaas.platform.dashboards.core.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.CommonResourceException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.UpdateSystemDashboardException;
import oracle.sysman.emaas.platform.dashboards.core.nls.DatabaseResourceBundleUtil;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.util.BigIntegerSerializer;
import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.DateUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsSubDashboard;

import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class Dashboard
{
	public static enum EnableDescriptionState
	{
		FALSE("FALSE", 0), TRUE("TRUE", 1), AUTO("AUTO", 2);

		@JsonCreator
		public static EnableDescriptionState fromName(String name)
		{
			if (name == null) {
				return null;
			}
			for (EnableDescriptionState eefs : EnableDescriptionState.values()) {
				if (eefs.getName().equalsIgnoreCase(name)) {
					return eefs;
				}
			}
			return null;
		}

		public static EnableDescriptionState fromValue(Integer value)
		{
			for (EnableDescriptionState eefs : EnableDescriptionState.values()) {
				if (eefs.getValue().equals(value)) {
					return eefs;
				}
			}
			return null;
		}

		private String name;

		@JsonIgnore
		private Integer value;

		private EnableDescriptionState(String name, Integer value)
		{
			this.name = name;
			this.value = value;
		}

		@JsonValue
		public String getName()
		{
			return name;
		}

		public Integer getValue()
		{
			return value;
		}
	}

	public static enum EnableEntityFilterState
	{
		FALSE("FALSE", 0), TRUE("TRUE", 1),GC("GC", 2);
	
		@JsonCreator
		public static EnableEntityFilterState fromName(String name)
		{
			if (name == null) {
				return null;
			}
			for (EnableEntityFilterState eefs : EnableEntityFilterState.values()) {
				if (eefs.getName().equalsIgnoreCase(name)) {
					return eefs;
				}
			}
			return null;
		}

		public static EnableEntityFilterState fromValue(Integer value)
		{
			for (EnableEntityFilterState eefs : EnableEntityFilterState.values()) {
				if (eefs.getValue().equals(value)) {
					return eefs;
				}
			}
			return null;
		}

		private String name;

		@JsonIgnore
		private Integer value;

		private EnableEntityFilterState(String name, Integer value)
		{
			this.name = name;
			this.value = value;
		}

		@JsonValue
		public String getName()
		{
			return name;
		}

		public Integer getValue()
		{
			return value;
		}
	}

	public static enum EnableTimeRangeState
	{
		FALSE("FALSE", 0), TRUE("TRUE", 1), GC("GC", 2);

		@JsonCreator
		public static EnableTimeRangeState fromName(String name)
		{
			if (name == null) {
				return null;
			}
			for (EnableTimeRangeState etrs : EnableTimeRangeState.values()) {
				if (etrs.getName().equalsIgnoreCase(name)) {
					return etrs;
				}
			}
			return null;
		}

		public static EnableTimeRangeState fromValue(Integer value)
		{
			for (EnableTimeRangeState etrs : EnableTimeRangeState.values()) {
				if (etrs.getValue().equals(value)) {
					return etrs;
				}
			}
			return null;
		}

		private String name;

		@JsonIgnore
		private Integer value;

		private EnableTimeRangeState(String name, Integer value)
		{
			this.name = name;
			this.value = value;
		}

		@JsonValue
		public String getName()
		{
			return name;
		}

		public Integer getValue()
		{
			return value;
		}
	}

	public static final String DASHBOARD_TYPE_NORMAL = "NORMAL";
	public static final Integer DASHBOARD_TYPE_CODE_NORMAL = Integer.valueOf(0);
	public static final String DASHBOARD_TYPE_SINGLEPAGE = "SINGLEPAGE";
	public static final Integer DASHBOARD_TYPE_CODE_SINGLEPAGE = Integer.valueOf(1);
	public static final String DASHBOARD_TYPE_SET = "SET";
	public static final Integer DASHBOARD_TYPE_CODE_SET = Integer.valueOf(2);

	public static final EnableTimeRangeState DASHBOARD_ENABLE_TIME_RANGE_DEFAULT = EnableTimeRangeState.TRUE;
	public static final EnableEntityFilterState DASHBOARD_ENABLE_ENTITY_FILTER_DEFAULT = EnableEntityFilterState.TRUE;
	public static final EnableDescriptionState DASHBOARD_ENABLE_DESCRIPTION_DEFAULT = EnableDescriptionState.FALSE;
	public static final boolean DASHBOARD_ENABLE_REFRESH_DEFAULT = Boolean.FALSE;

	public static final boolean DASHBOARD_DELETED_DEFAULT = Boolean.FALSE;

	/**
	 * Create a new dashboard instance from giving EmsDashboard instance
	 *
	 * @param ed
	 * @return
	 */
	public static Dashboard valueOf(EmsDashboard ed)
	{
		return Dashboard.valueOf(ed, null, true, true, true);
	}

    public static Dashboard valueOf(EmsDashboard from, Dashboard to, boolean loadSubDashboards, boolean alwaysLoadTiles,
            boolean loadTileParams)
    {
        return Dashboard.valueOf(from, to, loadSubDashboards, alwaysLoadTiles, loadTileParams, false);
    }

	/**
	 * Get a dashboard instance from EmsDashboard instance, by providing the prototype dashboard object
	 *
	 * @param from
	 *            EmsDashboard instance
	 * @param to
	 *            prototype Dashboard object, and it's values will be covered by value from EmsDashboard instance, or a new
	 *            Dashboard instance will be created if it's null
	 * @param alwaysLoadTiles
	 *            false: just load single page tiles, true: load tiles data for the dashboard without considering its types
	 * @return
	 */
	public static Dashboard valueOf(EmsDashboard from, Dashboard to, boolean loadSubDashboards, boolean alwaysLoadTiles,
			boolean loadTileParams, boolean loadScreenShot)
	{
		if (from == null) {
			return null;
		}
		if (to == null) {
			to = new Dashboard();
		}
		to.setCreationDate(from.getCreationDate());
		to.setDashboardId(from.getDashboardId());
		to.setDeleted(from.getDeleted() == null ? null : from.getDeleted().compareTo(BigInteger.ZERO) > 0);
		to.setEnableTimeRange(EnableTimeRangeState.fromValue(from.getEnableTimeRange()));
		to.setEnableEntityFilter(EnableEntityFilterState.fromValue(from.getEnableEntityFilter()));
		to.setEnableDescription(EnableDescriptionState.fromValue(from.getEnableDescription()));
		to.setEnableRefresh(DataFormatUtils.integer2Boolean(from.getEnableRefresh()));
		to.setIsSystem(DataFormatUtils.integer2Boolean(from.getIsSystem()));
		to.setShowInHome(DataFormatUtils.integer2Boolean(from.getShowInHome()));
		to.setSharePublic(DataFormatUtils.integer2Boolean(from.getSharePublic()));
		to.setLastModificationDate(from.getLastModificationDate());
		to.setLastModifiedBy(from.getLastModifiedBy());
		to.setOwner(from.getOwner());
		// by default, we'll not load screenshot for query
		if(loadScreenShot) {
		    to.setScreenShot(from.getScreenShot());
		}
		to.setType(DataFormatUtils.dashboardTypeInteger2String(from.getType()));
		to.setExtendedOptions(from.getExtendedOptions());
		to.setApplicationType(from.getApplicationType());
		
		// translate OOB data
		if(to.isSystem) {
		    to.setName(DatabaseResourceBundleUtil.getTranslatedString(DashboardApplicationType.fromValue(from.getApplicationType()), from.getName()));
		    to.setDescription(DatabaseResourceBundleUtil.getTranslatedString(DashboardApplicationType.fromValue(from.getApplicationType()), from.getDescription()));
		} else {
		    to.setName(from.getName());
		    to.setDescription(from.getDescription());
		}
		
		if (from.getType().equals(DASHBOARD_TYPE_CODE_SET)) {
			to.setEnableTimeRange(null);
			to.setIsSystem(DataFormatUtils.integer2Boolean(from.getIsSystem()));
			if (loadSubDashboards) {
				List<EmsSubDashboard> emsSubDashboards = from.getSubDashboardList();
				if (emsSubDashboards != null) {
					List<BigInteger> subDashboardIds = new ArrayList<BigInteger>();
					getSubDashboardIds(emsSubDashboards, subDashboardIds);
					Long tenantId = from.getTenantId();
					EntityManager em = null;
					try {
						DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
						List<EmsDashboard> subEmsDashboards = dsf.getEmsDashboardByIds(subDashboardIds, tenantId);
						List<Dashboard> subDashboardList = new ArrayList<Dashboard>();
						getSubDashboardsFromEmsSubDashboards(subEmsDashboards,
								subDashboardList);
						to.setSubDashboards(subDashboardList);
						em = dsf.getEntityManager();
					} finally {
						if (em != null) {
							em.close();
						}
					}
				}	
			}
		}
		else {
			to.setEnableTimeRange(EnableTimeRangeState.fromValue(from.getEnableTimeRange()));

			if (alwaysLoadTiles || Dashboard.DASHBOARD_TYPE_SINGLEPAGE.equals(to.getType())) {
				List<EmsDashboardTile> edtList = from.getDashboardTileList();
				if (edtList != null) {
					List<Tile> tileList = new ArrayList<Tile>();
					for (EmsDashboardTile edt : edtList) {
						Tile tile = Tile.valueOf(edt, loadTileParams);
						tile.setDashboard(to);
						tileList.add(tile);
					}
					to.setTileList(tileList);
				}
			}
		}

		return to;
	}

	@JsonProperty("id")
	@JsonSerialize(using = BigIntegerSerializer.class)
	private BigInteger dashboardId;

	private String name;

	@JsonProperty("createdOn")
	private Date creationDate;

	@JsonIgnore
	private Boolean deleted;

	private String description;

	private EnableDescriptionState enableDescription;

	private EnableTimeRangeState enableTimeRange;

	private EnableEntityFilterState enableEntityFilter;

	private Boolean enableRefresh;

	@JsonProperty("systemDashboard")
	private Boolean isSystem;

	private Boolean showInHome;

	private Boolean sharePublic;

	@JsonProperty("lastModifiedOn")
	private Date lastModificationDate;

	private String lastModifiedBy;

	private String owner;

	private String screenShot;

	private String screenShotHref;

	private String optionsHref;

	private String href;

	private String type;

	private DashboardApplicationType appicationType;
	
	private String extendedOptions;

	@JsonProperty("tiles")
	private List<Tile> tileList;

	@JsonProperty("subDashboards")
	private List<Dashboard> subDashboards;

	@JsonProperty("dashboardSets")
	private List<Dashboard> dashboardSets;
	
	@JsonProperty("linkedDashboardList")
	private List<String> linkedDashboardList;
	
	public List<String> getLinkedDashboardList() {
		return linkedDashboardList;
	}

	public void setLinkedDashboardList(List<String> linkedDashboardList) {
		this.linkedDashboardList = linkedDashboardList;
	}

	@JsonProperty("dupDashboardId")
	private BigInteger dupDashboardId;
	
	@JsonProperty("applicationType")
	private Integer applicationType;

	public Dashboard()
	{		
		// defaults for non-null values
		type = Dashboard.DASHBOARD_TYPE_NORMAL;
		enableTimeRange = Dashboard.DASHBOARD_ENABLE_TIME_RANGE_DEFAULT;
		enableDescription = Dashboard.DASHBOARD_ENABLE_DESCRIPTION_DEFAULT;
		enableEntityFilter = Dashboard.DASHBOARD_ENABLE_ENTITY_FILTER_DEFAULT;
		enableRefresh = Dashboard.DASHBOARD_ENABLE_REFRESH_DEFAULT;
		deleted = DASHBOARD_DELETED_DEFAULT;
		isSystem = Boolean.FALSE;
		sharePublic = Boolean.FALSE;
		showInHome=Boolean.TRUE;
	}

	public Tile addTile(Tile tile)
	{
		if (tileList == null) {
			tileList = new ArrayList<Tile>();
		}
		tileList.add(tile);
		tile.setDashboard(this);
		return tile;
	}

	public DashboardApplicationType getAppicationType()
	{
		return appicationType;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public BigInteger getDashboardId()
	{
		return dashboardId;
	}

	public boolean getDeleted()
	{
		return deleted;
	}

	public String getDescription()
	{
		return description;
	}

	public EnableDescriptionState getEnableDescription()
	{
		return enableDescription;
	}

	public EnableEntityFilterState getEnableEntityFilter()
	{
		return enableEntityFilter;
	}

	/**
	 * @return the enableRefresh
	 */
	public Boolean getEnableRefresh()
	{
		return enableRefresh;
	}
	
	public String getExtendedOptions() 
	{
		return extendedOptions;
	}

	public EnableTimeRangeState getEnableTimeRange()
	{
		return enableTimeRange;
	}

	public String getHref()
	{
		return href;
	}

	public Boolean getIsSystem()
	{
		return isSystem;
	}

	public Boolean getShowInHome()
	{
		return showInHome;
	}

	public Date getLastModificationDate()
	{
		return lastModificationDate;
	}

	public String getLastModifiedBy()
	{
		return lastModifiedBy;
	}

	public String getName()
	{
		return name;
	}

	public String getOptionsHref()
	{
		return optionsHref;
	}

	public String getOwner()
	{
		return owner;
	}
	

	/**
	 * @return the dupDashboardId
	 */
	public BigInteger getDupDashboardId()
	{
		return dupDashboardId;
	}

	/**
	 * @param dupDashboardId the dupDashboardId to set
	 */
	public void setDupDashboardId(BigInteger dupDashboardId)
	{
		this.dupDashboardId = dupDashboardId;
	}

	/**
	 * @return the applicationType
	 */
	public Integer getApplicationType()
	{
		return applicationType;
	}

	/**
	 * @param applicationType the applicationType to set
	 */
	public void setApplicationType(Integer applicationType)
	{
		this.applicationType = applicationType;
	}

	public EmsDashboard getPersistenceEntity(EmsDashboard ed) throws DashboardException
	{
		//check dashboard name
		if (name == null || "".equals(name.trim()) || name.length() > 64) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_INVALID_NAME_ERROR));
		}
		//check dashboard description
		if (description != null && description.length() > 256) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_INVALID_DESCRIPTION_ERROR));
		}
		Integer isEnableDescription = enableDescription == null ? null : enableDescription.getValue();
		Integer isEnableTimeRange = enableTimeRange == null ? null : enableTimeRange.getValue();
		Integer isEnableEntityFilter = enableEntityFilter == null ? null : enableEntityFilter.getValue();
		Integer isEnableRefresh = DataFormatUtils.boolean2Integer(enableRefresh);
		Integer isIsSystem = DataFormatUtils.boolean2Integer(isSystem);
		Integer isShare = DataFormatUtils.boolean2Integer(sharePublic);
		Integer isShowInHome=DataFormatUtils.boolean2Integer(showInHome);
		Integer dashboardType = DataFormatUtils.dashboardTypeString2Integer(type);
		Integer appType = appicationType == null ? null : appicationType.getValue();
		//TODO Integer appType = applicationType;
		String htmlEcodedName = StringEscapeUtils.escapeHtml4(name);
		String htmlEcodedDesc = description == null ? null : StringEscapeUtils.escapeHtml4(description);

		if (ed == null) {
			ed = new EmsDashboard(creationDate, dashboardId, BigInteger.ZERO, htmlEcodedDesc, isEnableTimeRange, isEnableRefresh,
					isEnableDescription, isEnableEntityFilter, isIsSystem, isShare, lastModificationDate, lastModifiedBy,
					htmlEcodedName, owner, screenShot, dashboardType, appType, isShowInHome, extendedOptions);

			if (type.equals(Dashboard.DASHBOARD_TYPE_SET)) {
				// support create subDashboards
				// support test cases in DashboardManagerTest
				                if (subDashboards != null && dashboardId!=null) {
				                    for (int index=0;index < subDashboards.size() ;index++ ) {
				                        Dashboard dbd = subDashboards.get(index);
				                        EmsSubDashboard esdbd = new EmsSubDashboard(dashboardId,dbd.getDashboardId(),index);
				                        ed.addEmsSubDashboard(esdbd);
				                    }
				                }
			}
			else {
				if (tileList != null) {
					for (Tile tile : tileList) {
						EmsDashboardTile edt = tile.getPersistenceEntity(null);
						//					edt.setPosition(i++);
						ed.addEmsDashboardTile(edt);
					}
				}
			}
		}
		else {
			ed.getScreenShot();
			ed.setDeleted(deleted ? getDashboardId() : BigInteger.ZERO);
			ed.setDescription(htmlEcodedDesc);
			ed.setEnableTimeRange(isEnableTimeRange);
			ed.setEnableDescription(isEnableDescription);
			ed.setEnableRefresh(isEnableRefresh);
			ed.setEnableEntityFilter(isEnableEntityFilter);
			if (ed.getIsSystem() != null && isIsSystem != null && !isIsSystem.equals(ed.getIsSystem())) {
				throw new UpdateSystemDashboardException();
			}
			ed.setLastModificationDate(lastModificationDate);
			ed.setLastModifiedBy(lastModifiedBy);
			ed.setName(htmlEcodedName);
			ed.setScreenShot(screenShot);
			ed.setApplicationType(appType);
			ed.setSharePublic(isShare);
			ed.setExtendedOptions(extendedOptions);
			if (ed.getType() != null && dashboardType != null && !dashboardType.equals(ed.getType())) {
				throw new CommonResourceException(
						MessageUtils.getDefaultBundleString(CommonResourceException.NOT_SUPPORT_UPDATE_TYPE_FIELD));
			}
			if (type.equals(Dashboard.DASHBOARD_TYPE_SET)) {
				updateEmsSubDashboards(subDashboards, ed);
			}
			else {
				updateEmsDashboardTiles(tileList, ed);
				removeUnsharedDashboards(ed);
			}

		}
		return ed;
	}

	@JsonIgnore
	public String getScreenShot()
	{
		return screenShot;
	}

	public String getScreenShotHref()
	{
		return screenShotHref;
	}

	/**
	 * @return the sharePublic
	 */
	public Boolean getSharePublic()
	{
		return sharePublic;
	}

	public List<Dashboard> getSubDashboards()
	{
		return subDashboards;
	}

	public List<Tile> getTileList()
	{
		return tileList;
	}

	public String getType()
	{
		return type;
	}

	public Tile removeTile(Tile tile)
	{
		getTileList().remove(tile);
		return tile;
	}

	public void setAppicationType(DashboardApplicationType appicationType)
	{
		this.appicationType = appicationType;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public void setDashboardId(BigInteger dashboardId)
	{
		this.dashboardId = dashboardId;
	}

	public void setDeleted(boolean deleted)
	{
		this.deleted = deleted;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setEnableDescription(EnableDescriptionState enableDescription)
	{
		this.enableDescription = enableDescription;
	}

	public void setEnableEntityFilter(EnableEntityFilterState enableEntityFilter)
	{
		this.enableEntityFilter = enableEntityFilter;
	}

	/**
	 * @param enableRefresh
	 *            the enableRefresh to set
	 */
	public void setEnableRefresh(Boolean enableRefresh)
	{
		this.enableRefresh = enableRefresh;
	}

	public void setEnableTimeRange(EnableTimeRangeState enableTimeRange)
	{
		this.enableTimeRange = enableTimeRange;
	}
	
	public void setExtendedOptions(String extendedOptions)
	{
		this.extendedOptions = extendedOptions;
	}

	public void setHref(String href)
	{
		this.href = href;
	}

	public void setIsSystem(Boolean isSystem)
	{
		this.isSystem = isSystem;
	}

	public void setShowInHome(Boolean showInHome)
	{
		this.showInHome = showInHome;
	}

	public void setLastModificationDate(Date lastModificationDate)
	{
		this.lastModificationDate = lastModificationDate;
	}

	public void setLastModifiedBy(String lastModifiedBy)
	{
		this.lastModifiedBy = lastModifiedBy;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setOptionsHref(String optionsHref)
	{
		this.optionsHref = optionsHref;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	@JsonProperty("screenShot")
	public void setScreenShot(String screenShot)
	{
		this.screenShot = screenShot;
	}

	public void setScreenShotHref(String screenShotHref)
	{
		this.screenShotHref = screenShotHref;
	}

	/**
	 * @param sharePublic
	 *            the sharePublic to set
	 */
	public void setSharePublic(Boolean sharePublic)
	{
		this.sharePublic = sharePublic;
	}

	public void setSubDashboards(List<Dashboard> subDashboards)
	{
		this.subDashboards = subDashboards;
	}

	public void setTileList(List<Tile> emsDashboardTileList)
	{
		tileList = emsDashboardTileList;
	}

	public void setType(String type)
	{
		this.type = type;
	}
	
	private static void getSubDashboardIds(List<EmsSubDashboard> subDashboards,
			List<BigInteger> subDashboardIds) {
		if (subDashboards != null) {
			for (EmsSubDashboard emsSubDashboard : subDashboards) {
				subDashboardIds.add(emsSubDashboard.getSubDashboardId());
			}
		}
	}

	private static void getSubDashboardsFromEmsSubDashboards(
			List<EmsDashboard> emsSubDashboards, List<Dashboard> subDashboards) {
		if (emsSubDashboards != null) {
			for (EmsDashboard emsDashboard : emsSubDashboards) {
				Dashboard dbd = new Dashboard();
				dbd.setEnableTimeRange(null);
				dbd.setEnableRefresh(null);
				dbd.setIsSystem(null);
				dbd.setType(null);
				dbd.setDashboardId(emsDashboard.getDashboardId());
				dbd.setName(emsDashboard.getName());
				dbd.setSharePublic(DataFormatUtils.integer2Boolean(emsDashboard
						.getSharePublic()));
				dbd.setOwner(emsDashboard.getOwner());
				subDashboards.add(dbd);

			}

		}

	}
	 


    public List<Dashboard> getDashboardSets() {
        return dashboardSets;
    }

    public void setDashboardSets(List<Dashboard> dashboardSets) {
        this.dashboardSets = dashboardSets;
    } 

	private void removeUnsharedDashboards(EmsDashboard ed)
	{
		if (ed.getSharePublic() == 0) {
			Long tenantId = ed.getTenantId();
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			dsf.removeUnsharedEmsSubDashboard(ed.getDashboardId(), ed.getOwner());
		}
	}

	private void updateEmsDashboardTiles(List<Tile> tiles, EmsDashboard ed) throws DashboardException
	{
		Map<Tile, EmsDashboardTile> rows = new HashMap<Tile, EmsDashboardTile>();
		// remove deleted tile row in dashboard row first
		List<EmsDashboardTile> edtList = ed.getDashboardTileList();
		if (edtList != null) {
			int edtSize = edtList.size();
			for (int i = edtSize - 1; i >= 0; i--) {
				EmsDashboardTile edt = edtList.get(i);
				boolean isDeleted = true;
				if (tiles != null) {
					for (Tile tile : tiles) {
						if (tile.getTileId() != null && tile.getTileId().compareTo(edt.getTileId()) == 0) {
							isDeleted = false;
							rows.put(tile, edt);
							break;
						}
					}
				}
				if (isDeleted) {
					ed.getDashboardTileList().remove(edt);
				}
			}
		}

		if (tiles == null) {
			return;
		}
		for (int i = 0; i < tiles.size(); i++) {
			Tile tile = tiles.get(i);
			EmsDashboardTile edt = null;
			if (!rows.containsKey(tile)) {
				//				if (tile.getTileId() != null) {
				//					throw new CommonFunctionalException(MessageUtils.getDefaultBundleString(
				//							CommonFunctionalException.DASHBOARD_TILE_INVALID_ID, String.valueOf(tile.getTileId())));
				//				}
				edt = tile.getPersistenceEntity(null);
				ed.addEmsDashboardTile(edt);
				rows.put(tile, edt);
				//				dsf.persistEntity(edt);
			}
			else {
				edt = rows.get(tile);
				tile.getPersistenceEntity(edt);
			}
			//			edt.setPosition(i);
		}
	}

	private void updateEmsSubDashboards(List<Dashboard> dashboards, EmsDashboard ed) throws DashboardException
	{
		if (dashboards == null) {
			throw new CommonSecurityException("sub dashboard is null");
		}

		Map<Dashboard, EmsSubDashboard> rows = new HashMap<Dashboard, EmsSubDashboard>();
		List<EmsSubDashboard> subDashboardList = ed.getSubDashboardList();
		DashboardServiceFacade dsf = new DashboardServiceFacade(ed.getTenantId());
		List<BigInteger> originSubList=new ArrayList<>();
		for(EmsSubDashboard dbd: subDashboardList){
			originSubList.add(dbd.getSubDashboardId());
		}
		List<BigInteger> newSubList=new ArrayList<>();
		for(Dashboard d:dashboards){
			newSubList.add(d.getDashboardId());
		}
		originSubList.removeAll(newSubList);
		//EMCPDF-2709
		if(originSubList.size()>0){
			dsf.updateSubDashboardVisibleInHome(ed,originSubList);
		}
		if (subDashboardList != null) {
			for (int i = subDashboardList.size() - 1; i >= 0; i--) {
				EmsSubDashboard emsSubDashboard = subDashboardList.get(i);

				ed.removeEmsSubDashboard(emsSubDashboard);
			}
		}

		for (int index = 0; index < dashboards.size(); index++) {
			Dashboard subDashboard = dashboards.get(index);
			BigInteger subDashboardId = subDashboard.getDashboardId();
			EmsDashboard subbed = dsf.getEmsDashboardById(subDashboardId);

			if (subbed != null) {
				// remove duplicated entity
				if (!rows.containsKey(subDashboard)) {
					EmsSubDashboard emsSubDashboard = new EmsSubDashboard(dashboardId, subDashboard.getDashboardId(), index);
					emsSubDashboard.setCreationDate(DateUtil.getGatewayTime());
					emsSubDashboard.setLastModificationDate(emsSubDashboard.getCreationDate());
					ed.addEmsSubDashboard(emsSubDashboard);
					rows.put(subDashboard, emsSubDashboard);

					// update share public property of un-oob dashboard
					if (ed.getSharePublic().equals(1) && subbed.getIsSystem().equals(0)) {
						subbed.setSharePublic(1);
						dsf.mergeEmsDashboard(subbed);
					}

				}
			}
		}
	}
}
