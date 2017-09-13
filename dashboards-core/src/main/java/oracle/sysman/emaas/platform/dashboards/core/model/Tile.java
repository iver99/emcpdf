package oracle.sysman.emaas.platform.dashboards.core.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.CommonResourceException;
import oracle.sysman.emaas.platform.dashboards.core.nls.DatabaseResourceBundleUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.BigIntegerSerializer;
import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.DateUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;

import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class Tile
{
	public static final Integer TILE_TYPE_CODE_DEFAULT = 0;
	public static final Integer TILE_TYPE_CODE_TEXT_WIDGET = 1;
	public static final String TILE_TYPE_DEFAULT = "DEFAULT";
	public static final String TILE_TYPE_TEXT_WIDGET = "TEXT_WIDGET";

	public static final int WIDGET_SOURCE_DASHBOARD_FRAMEWORK = 0;

	public static final int WIDGET_SOURCE_INTEGRATOR = 1;
	public static final Boolean TILE_DEFAULT_IS_MAX = false;
	public static final Integer TILE_DEFAULT_ROW = 0;
	public static final Integer TILE_DEFAULT_COLUMN = 0;
	public static final Integer TILE_DEFAULT_WIDTH = 2;
	public static final Integer TILE_DEFAULT_HEIGHT = 220;

	// specific for text widget
	private static final Integer TEXT_WIDGET_WIDTH = 8;
	private static final String TEXT_WIDGET_NAME = "DF_BUILTIN_TEXT";
	private static final String TEXT_WIDGET_TITLE = "BUILT_IN_TEXT";
	private static final String TEXT_WIDGET_DESCRIPTION = TEXT_WIDGET_NAME;
	private static final String TEXT_WIDGET_OWNER = "ORACLE";
	private static final String TEXT_WIDGET_KOC_NAME = "DF_V1_WIDGET_TEXT";
	private static final String TEXT_WIDGET_VIEWMODEL = "../emcsDependencies/widgets/textwidget/js/textwidget";
	private static final String TEXT_WIDGET_TEMPLATE = "../emcsDependencies/widgets/textwidget/textwidget.html";
	public static final String TEXT_WIDGET_PARAM_NAME_CONTENT = "DF_BUILTIN_WIDGET_TEXT_CONTENT";
	private static final String DF_BUILTIN_WIDGET_LINK_TEXT = "DF_BUILTIN_WIDGET_LINK_TEXT";
	private static final String DF_BUILTIN_WIDGET_LINK_URL = "DF_BUILTIN_WIDGET_LINK_URL";

	private static final Integer TILE_TITLE_MAX_LEN = 64;
	private static final Integer TILE_PARAM_STR_VALUE_MAX_LEN = 4000;
	private static final Integer TEXT_WIDGET_MAX_CONTENT_LEN = TILE_PARAM_STR_VALUE_MAX_LEN;
	private static final Integer TEXT_WIDGET_MAX_LINK_TEXT_LEN = TILE_PARAM_STR_VALUE_MAX_LEN;
	private static final Integer TEXT_WIDGET_MAX_LINK_URL_LEN = TILE_PARAM_STR_VALUE_MAX_LEN;

	public static Tile valueOf(EmsDashboardTile edt, boolean loadTileParams)
	{
		if (edt == null) {
			return null;
		}
		Tile tile = new Tile();
		tile.setCreationDate(edt.getCreationDate());
		tile.setHeight(edt.getHeight());
		tile.setIsMaximized(DataFormatUtils.integer2Boolean(edt.getIsMaximized()));
		tile.setLastModificationDate(edt.getLastModificationDate());
		tile.setLastModifiedBy(edt.getLastModifiedBy());
		tile.setOwner(edt.getOwner());
		tile.setType(DataFormatUtils.tileTypeInteger2String(edt.getType()));
		tile.setRow(edt.getRow());
		tile.setColumn(edt.getColumn());
		tile.setProviderAssetRoot(edt.getProviderAssetRoot());
		tile.setProviderName(edt.getProviderName());
		tile.setProviderVersion(edt.getProviderVersion());
		tile.setTileId(edt.getTileId());
		tile.setWidgetCreationTime(edt.getWidgetCreationTime());
		tile.setWidgetDeleted(DataFormatUtils.integer2Boolean(edt.getWidgetDeleted()));
		tile.setWidgetDeletionDate(edt.getWidgetDeletionDate());
		tile.setWidgetGroupName(edt.getWidgetGroupName());
		tile.setWidgetHistogram(edt.getWidgetHistogram());
		tile.setWidgetIcon(edt.getWidgetIcon());
		tile.setWidgetKocName(edt.getWidgetKocName());
		tile.setWidgetOwner(edt.getWidgetOwner());
		tile.setWidgetSource(edt.getWidgetSource());
		tile.setWidgetTemplate(edt.getWidgetTemplate());
		tile.setWidgetUniqueId(edt.getWidgetUniqueId());
		tile.setWidgetViewmode(edt.getWidgetViewmode());
		tile.setWidgetSupportTimeControl(DataFormatUtils.integer2Boolean(edt.getWidgetSupportTimeControl()));
		tile.setWidgetLinkedDashboard(edt.getWidgetLinkedDashboard());
		tile.setWidth(edt.getWidth());
		
        // translate OOB data
        if(TEXT_WIDGET_OWNER.equalsIgnoreCase(tile.getWidgetOwner())) {
            tile.setTitle(DatabaseResourceBundleUtil.getTranslatedString(edt.getProviderName(), edt.getTitle()));
            tile.setWidgetName(DatabaseResourceBundleUtil.getTranslatedString(edt.getProviderName(), edt.getWidgetName()));
            tile.setWidgetDescription(DatabaseResourceBundleUtil.getTranslatedString(edt.getProviderName(), edt.getWidgetDescription()));
        } else {
            tile.setTitle(edt.getTitle());
            tile.setWidgetName(edt.getWidgetName());
            tile.setWidgetDescription(edt.getWidgetDescription());
        }
		
		if (loadTileParams) {
			List<EmsDashboardTileParams> edtpList = edt.getDashboardTileParamsList();
			if (edtpList != null) {
				List<TileParam> paras = new ArrayList<TileParam>(edtpList.size());
				for (EmsDashboardTileParams edtp : edtpList) {
					TileParam tp = TileParam.valueOf(edtp);
					tp.setTile(tile);
					if (Tile.TILE_TYPE_CODE_TEXT_WIDGET.equals(edt.getType())
							&& tp.getName().equals(Tile.TEXT_WIDGET_PARAM_NAME_CONTENT)
							&& TileParam.PARAM_TYPE_STRING.equals(tp.getType())) {
						tile.setContent(tp.getStringValue());
						continue;
					}
					if (!Tile.TILE_TYPE_CODE_TEXT_WIDGET.equals(edt.getType())
							&& tp.getName().equals(Tile.DF_BUILTIN_WIDGET_LINK_TEXT)
							&& TileParam.PARAM_TYPE_STRING.equals(tp.getType())) {
						tile.setLinkText(tp.getStringValue());
						continue;
					}
					if (!Tile.TILE_TYPE_CODE_TEXT_WIDGET.equals(edt.getType())
							&& tp.getName().equals(Tile.DF_BUILTIN_WIDGET_LINK_URL)
							&& TileParam.PARAM_TYPE_STRING.equals(tp.getType())) {
						tile.setLinkUrl(tp.getStringValue());
						continue;
					}
					paras.add(tp);
				}
				tile.setParameters(paras);
			}
		}
		return tile;
	}

	@JsonIgnore
	private Date creationDate;

	private String type;

	private Integer row;

	private Integer column;

	private Integer height;
	private Boolean isMaximized;

	@JsonIgnore
	private Date lastModificationDate;

	@JsonIgnore
	private String lastModifiedBy;

	@JsonIgnore
	private String owner;

	@JsonProperty("PROVIDER_ASSET_ROOT")
	private String providerAssetRoot;

	@JsonProperty("PROVIDER_NAME")
	private String providerName;

	//	private int position;

	@JsonProperty("PROVIDER_VERSION")
	private String providerVersion;

//	@JsonSerialize(using = BigIntegerSerializer.class)
	private String tileId;
	private String title;
	@JsonProperty("WIDGET_CREATION_TIME")
	private String widgetCreationTime;
	@JsonProperty("WIDGET_DESCRIPTION")
	private String widgetDescription;

	@JsonProperty("WIDGET_GROUP_NAME")
	private String widgetGroupName;

	@JsonProperty("WIDGET_HISTOGRAM")
	private String widgetHistogram;

	@JsonProperty("WIDGET_ICON")
	private String widgetIcon;

	@JsonProperty("WIDGET_KOC_NAME")
	private String widgetKocName;

	@JsonProperty("WIDGET_NAME")
	private String widgetName;

	@JsonProperty("WIDGET_OWNER")
	private String widgetOwner;

	@JsonProperty("WIDGET_SOURCE")
	private Integer widgetSource;

	@JsonProperty("WIDGET_TEMPLATE")
	private String widgetTemplate;

	@JsonProperty("WIDGET_UNIQUE_ID")
	private String widgetUniqueId;

	@JsonProperty("WIDGET_VIEWMODEL")
	private String widgetViewmode;

	@JsonProperty("WIDGET_SUPPORT_TIME_CONTROL")
	private Boolean widgetSupportTimeControl;

	@JsonProperty("WIDGET_LINKED_DASHBOARD")
	@JsonSerialize(using = BigIntegerSerializer.class)
	private BigInteger widgetLinkedDashboard;

	private Integer width;

	private String content;

	private String linkText;

	private String linkUrl;

	private Boolean widgetDeleted;

	private Date widgetDeletionDate;

	@JsonIgnore
	private Dashboard dashboard;
	@JsonProperty("tileParameters")
	private List<TileParam> parameters;

	public Tile()
	{
		// defaults for non-null values
		isMaximized = Boolean.FALSE;
		widgetDeleted = Boolean.FALSE;
	}

	public TileParam addParameter(TileParam tp)
	{
		if (tp == null) {
			return null;
		}
		if (parameters == null) {
			parameters = new ArrayList<TileParam>();
		}
		parameters.add(tp);
		tp.setTile(this);
		return tp;
	}

	/**
	 * @return the column
	 */
	public Integer getColumn()
	{
		return column;
	}

	/**
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public Dashboard getDashboard()
	{
		return dashboard;
	}

	public Integer getHeight()
	{
		return height;
	}

	public Boolean getIsMaximized()
	{
		return isMaximized;
	}

	public Date getLastModificationDate()
	{
		return lastModificationDate;
	}

	public String getLastModifiedBy()
	{
		return lastModifiedBy;
	}

	/**
	 * @return the linkText
	 */
	public String getLinkText()
	{
		return linkText;
	}

	/**
	 * @return the linkUrl
	 */
	public String getLinkUrl()
	{
		return linkUrl;
	}

	public String getOwner()
	{
		return owner;
	}

	public TileParam getParameter(String name)
	{
		if (name == null || "".equals(name) || parameters == null) {
			return null;
		}
		for (TileParam tp : parameters) {
			if (name.equals(tp.getName())) {
				return tp;
			}
		}
		return null;
	}

	public List<TileParam> getParameters()
	{
		return parameters;
	}

	/**
	 * Returns an instance of EmsDashboardTile for current tile object
	 *
	 * @return
	 * @throws CommonFunctionalException
	 */
	public EmsDashboardTile getPersistenceEntity(EmsDashboardTile to) throws DashboardException
	{
		if (TILE_TYPE_TEXT_WIDGET.equals(type)) {
			return getTextTilePersistenceEntity(to);
		}
		else {
			return getDefaultTilePersistenceEntity(to);
		}
	}

	public String getProviderAssetRoot()
	{
		return providerAssetRoot;
	}

	public String getProviderName()
	{
		return providerName;
	}

	public String getProviderVersion()
	{
		return providerVersion;
	}

	/**
	 * @return the row
	 */
	public Integer getRow()
	{
		return row;
	}

	public String getTileId()
	{
		return tileId;
	}

	public String getTitle()
	{
		return title;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	public String getWidgetCreationTime()
	{
		return widgetCreationTime;
	}

	public Boolean getWidgetDeleted()
	{
		return widgetDeleted;
	}

	/**
	 * @return the widgetDeletionDate
	 */
	public Date getWidgetDeletionDate()
	{
		return widgetDeletionDate;
	}

	public String getWidgetDescription()
	{
		return widgetDescription;
	}

	public String getWidgetGroupName()
	{
		return widgetGroupName;
	}

	public String getWidgetHistogram()
	{
		return widgetHistogram;
	}

	public String getWidgetIcon()
	{
		return widgetIcon;
	}

	public String getWidgetKocName()
	{
		return widgetKocName;
	}

	//    public Integer getPosition() {
	//        return position;
	//    }
	//
	//    public void setPosition(Integer position) {
	//        this.position = position;
	//    }

	public BigInteger getWidgetLinkedDashboard()
	{
		return widgetLinkedDashboard;
	}

	public String getWidgetName()
	{
		return widgetName;
	}

	public String getWidgetOwner()
	{
		return widgetOwner;
	}

	public Integer getWidgetSource()
	{
		return widgetSource;
	}

	public Boolean getWidgetSupportTimeControl()
	{
		return widgetSupportTimeControl;
	}

	public String getWidgetTemplate()
	{
		return widgetTemplate;
	}

	public String getWidgetUniqueId()
	{
		return widgetUniqueId;
	}

	public String getWidgetViewmode()
	{
		return widgetViewmode;
	}

	public Integer getWidth()
	{
		return width;
	}

	/**
	 * Removes a tile parameter specified by the name
	 *
	 * @param index
	 * @return
	 */
	public TileParam removeParameter(String name)
	{
		TileParam tp = getParameter(name);
		if (tp == null) {
			return null;
		}
		parameters.remove(tp);
		return tp;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(Integer column)
	{
		this.column = column;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public void setDashboard(Dashboard emsDashboard1)
	{
		dashboard = emsDashboard1;
	}

	public void setHeight(Integer height)
	{
		this.height = height;
	}

	public void setIsMaximized(Boolean isMaximized)
	{
		this.isMaximized = isMaximized;
	}

	public void setLastModificationDate(Date lastModificationDate)
	{
		this.lastModificationDate = lastModificationDate;
	}

	public void setLastModifiedBy(String lastModifiedBy)
	{
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * @param linkText
	 *            the linkText to set
	 */
	public void setLinkText(String linkText)
	{
		this.linkText = linkText;
	}

	/**
	 * @param linkUrl
	 *            the linkUrl to set
	 */
	public void setLinkUrl(String linkUrl)
	{
		this.linkUrl = linkUrl;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public void setParameters(List<TileParam> parameters)
	{
		this.parameters = parameters;
	}

	public void setProviderAssetRoot(String providerAssetRoot)
	{
		this.providerAssetRoot = providerAssetRoot;
	}

	public void setProviderName(String providerName)
	{
		this.providerName = providerName;
	}

	public void setProviderVersion(String providerVersion)
	{
		this.providerVersion = providerVersion;
	}

	/**
	 * @param row
	 *            the row to set
	 */
	public void setRow(Integer row)
	{
		this.row = row;
	}

	public void setTileId(String tileId)
	{
		this.tileId = tileId;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	public void setWidgetCreationTime(String widgetCreationTime)
	{
		this.widgetCreationTime = widgetCreationTime;
	}

	public void setWidgetDeleted(Boolean widgetDeleted)
	{
		this.widgetDeleted = widgetDeleted;
	}

	/**
	 * @param widgetDeletionDate
	 *            the widgetDeletionDate to set
	 */
	public void setWidgetDeletionDate(Date widgetDeletionDate)
	{
		this.widgetDeletionDate = widgetDeletionDate;
	}

	public void setWidgetDescription(String widgetDescription)
	{
		this.widgetDescription = widgetDescription;
	}

	public void setWidgetGroupName(String widgetGroupName)
	{
		this.widgetGroupName = widgetGroupName;
	}

	public void setWidgetHistogram(String widgetHistogram)
	{
		this.widgetHistogram = widgetHistogram;
	}

	public void setWidgetIcon(String widgetIcon)
	{
		this.widgetIcon = widgetIcon;
	}

	public void setWidgetKocName(String widgetKocName)
	{
		this.widgetKocName = widgetKocName;
	}

	public void setWidgetLinkedDashboard(BigInteger widgetLinkedDashboard)
	{
		this.widgetLinkedDashboard = widgetLinkedDashboard;
	}

	public void setWidgetName(String widgetName)
	{
		this.widgetName = widgetName;
	}

	public void setWidgetOwner(String widgetOwner)
	{
		this.widgetOwner = widgetOwner;
	}

	public void setWidgetSource(Integer widgetSource)
	{
		this.widgetSource = widgetSource;
	}

	public void setWidgetSupportTimeControl(Boolean widgetSupportTimeControl)
	{
		this.widgetSupportTimeControl = widgetSupportTimeControl;
	}

	public void setWidgetTemplate(String widgetTemplate)
	{
		this.widgetTemplate = widgetTemplate;
	}

	public void setWidgetUniqueId(String widgetUniqueId)
	{
		this.widgetUniqueId = widgetUniqueId;
	}

	public void setWidgetViewmode(String widgetViewmode)
	{
		this.widgetViewmode = widgetViewmode;
	}

	public void setWidth(Integer width)
	{
		this.width = width;
	}

	private EmsDashboardTile getDefaultTilePersistenceEntity(EmsDashboardTile to) throws DashboardException
	{
		Integer intIsMaximized = DataFormatUtils.boolean2Integer(isMaximized);
		Integer intWidgetSupportTimeControl = DataFormatUtils.boolean2Integer(widgetSupportTimeControl);
		Integer intWidgetDeleted = DataFormatUtils.boolean2Integer(widgetDeleted);

		if (title == null || "".equals(title)) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_TILE_TITLE_REQUIRED));
		}
		String encodedTitle = StringEscapeUtils.escapeHtml4(title);
		if (encodedTitle.length() > TILE_TITLE_MAX_LEN) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.TILE_INVALID_TITLE_EXCEED_MAX_LEN));
		}
		if (row == null) {
			row = TILE_DEFAULT_ROW;
		}
		if (column == null) {
			column = TILE_DEFAULT_COLUMN;
		}
		if (width == null) {
			width = TILE_DEFAULT_WIDTH;
		}
		if (height == null) {
			height = TILE_DEFAULT_HEIGHT;
		}
		if (isMaximized == null) {
			isMaximized = TILE_DEFAULT_IS_MAX;
		}
		if (intWidgetSupportTimeControl == null) {
			intWidgetSupportTimeControl = Integer.valueOf(1);
		}
		Integer tileType = DataFormatUtils.tileTypeString2Integer(type);
		if (to == null) { // newly created tile
			if (widgetName == null || "".equals(widgetName)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_NAME_REQUIRED));
			}
			if (widgetUniqueId == null || "".equals(widgetUniqueId)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_UNIQUE_ID_REQUIRED));
			}
			//			if (widgetIcon == null || "".equals(widgetIcon)) {
			//				throw new CommonFunctionalException(
			//						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_ICON_REQUIRED));
			//			}
			//			if (widgetHistogram == null || "".equals(widgetHistogram)) {
			//				throw new CommonFunctionalException(
			//						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_HISTOGRAM_REQUIRED));
			//			}
			if (widgetOwner == null || "".equals(widgetOwner)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_OWNER_REQUIRED));
			}
			if (widgetCreationTime == null || "".equals(widgetCreationTime)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_CREATIONTIME_REQUIRED));
			}
			if (widgetSource == null || "".equals(widgetSource)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_SOURCE_REQUIRED));
			}
			if (widgetKocName == null || "".equals(widgetKocName)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_KOC_NAME_REQUIRED));
			}
			if (widgetViewmode == null || "".equals(widgetViewmode)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_VIEW_MODEL_REQUIRED));
			}
			if (widgetTemplate == null || "".equals(widgetTemplate)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_TEMPLATE_REQUIRED));
			}
			if (providerName == null || "".equals(providerName)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.PROVIDER_NAME_REQUIRED));
			}
			if (providerVersion == null || "".equals(providerVersion)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.PROVIDER_VERSION_REQUIRED));
			}
			if (providerAssetRoot == null || "".equals(providerAssetRoot)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.PROVIDER_ASSET_ROOT_REQUIRED));
			}

			to = new EmsDashboardTile(creationDate, null, tileType, row, column, height, intIsMaximized, lastModificationDate,
					lastModifiedBy, owner, providerAssetRoot, providerName, providerVersion, tileId, encodedTitle,
					widgetCreationTime, widgetDescription, widgetGroupName, widgetHistogram, widgetIcon, widgetKocName,
					widgetName, widgetOwner, widgetSource, widgetTemplate, widgetUniqueId, widgetViewmode,
					intWidgetSupportTimeControl, width, widgetLinkedDashboard, intWidgetDeleted, widgetDeletionDate);
			if (parameters != null) {
				for (TileParam param : parameters) {
					EmsDashboardTileParams edtp = param.getPersistentEntity(to, null);
					to.addEmsDashboardTileParams(edtp);
				}
			}
		}
		else {
			to.setRow(row);
			to.setColumn(column);
			to.setHeight(getHeight());
			to.setIsMaximized(intIsMaximized);
			//    		edt.setPosition(this.position);
			to.setProviderAssetRoot(providerAssetRoot);
			to.setProviderName(providerName);
			to.setProviderVersion(providerVersion);
			to.setTitle(encodedTitle);
			if (to.getType() != null && tileType != null && !tileType.equals(to.getType())) {
				throw new CommonResourceException(
						MessageUtils.getDefaultBundleString(CommonResourceException.NOT_SUPPORT_UPDATE_TYPE_FIELD));
			}
			//			to.setWidgetCreationTime(widgetCreationTime);
			to.setWidgetDescription(widgetDescription);
			to.setWidgetDeleted(intWidgetDeleted);
			to.setWidgetGroupName(widgetGroupName);
			to.setWidgetHistogram(widgetHistogram);
			to.setWidgetIcon(widgetIcon);
			to.setWidgetKocName(widgetKocName);
			to.setWidgetName(widgetName);
			to.setWidgetOwner(widgetOwner);
			to.setWidgetSource(widgetSource);
			to.setWidgetTemplate(widgetTemplate);
			to.setWidgetUniqueId(widgetUniqueId);
			to.setWidgetViewmode(widgetViewmode);
			to.setWidgetSupportTimeControl(intWidgetSupportTimeControl);
			to.setWidgetLinkedDashboard(widgetLinkedDashboard);
			to.setWidth(width);
			updateEmsDashboardTileParams(parameters, to);
		}
		updateSpecificType(to);
		return to;
	}

	private EmsDashboardTile getTextTilePersistenceEntity(EmsDashboardTile to) throws DashboardException
	{
		if (row == null) {
			row = TILE_DEFAULT_ROW;
		}
		//column = 0;
		//width = 8;
		//height = 1;
		Integer tileType = DataFormatUtils.tileTypeString2Integer(type);
		// text tile does not support time control
		Integer supportTimeControl = 0;
		// whatever the input title is, text tile use the default title
		title = TEXT_WIDGET_TITLE;
		if (to == null) { // newly created tile
			to = new EmsDashboardTile(creationDate, null, tileType, row, column, height, 0, lastModificationDate, lastModifiedBy,
					owner, providerAssetRoot, providerName, providerVersion, tileId, title, widgetCreationTime, widgetDescription,
					widgetGroupName, widgetHistogram, widgetIcon, widgetKocName, widgetName, widgetOwner, widgetSource,
					widgetTemplate, widgetUniqueId, widgetViewmode, supportTimeControl, width, widgetLinkedDashboard,
					// text tile actually doesn't exist any more
					0, widgetDeletionDate);
			if (parameters != null) {
				for (TileParam param : parameters) {
					EmsDashboardTileParams edtp = param.getPersistentEntity(to, null);
					to.addEmsDashboardTileParams(edtp);
				}
			}
		}
		else {
			to.setRow(row);
			to.setColumn(column);
			to.setHeight(getHeight());
			to.setIsMaximized(0);
			to.setProviderAssetRoot(providerAssetRoot);
			to.setProviderName(providerName);
			to.setProviderVersion(providerVersion);
			to.setTitle(title);
			if (to.getType() != null && tileType != null && !tileType.equals(to.getType())) {
				throw new CommonResourceException(
						MessageUtils.getDefaultBundleString(CommonResourceException.NOT_SUPPORT_UPDATE_TYPE_FIELD));
			}
			//			to.setWidgetCreationTime(widgetCreationTime);
			to.setWidgetDescription(widgetDescription);
			to.setWidgetGroupName(widgetGroupName);
			to.setWidgetHistogram(widgetHistogram);
			to.setWidgetIcon(widgetIcon);
			to.setWidgetKocName(widgetKocName);
			to.setWidgetName(widgetName);
			to.setWidgetOwner(widgetOwner);
			to.setWidgetSource(widgetSource);
			to.setWidgetTemplate(widgetTemplate);
			to.setWidgetUniqueId(widgetUniqueId);
			to.setWidgetViewmode(widgetViewmode);
			to.setWidgetSupportTimeControl(supportTimeControl);
			to.setWidth(width);
			updateEmsDashboardTileParams(parameters, to);
		}
		updateSpecificType(to);
		return to;
	}

	private void updateEmsDashboardTileParams(List<TileParam> paramList, EmsDashboardTile tile) throws CommonFunctionalException
	{
		Map<TileParam, EmsDashboardTileParams> rows = new HashMap<TileParam, EmsDashboardTileParams>();
		List<EmsDashboardTileParams> edtpList = tile.getDashboardTileParamsList();
		if (edtpList != null) {
			int edtSize = edtpList.size();
			for (int i = edtSize - 1; i >= 0; i--) {
				EmsDashboardTileParams edtp = edtpList.get(i);
				boolean isDeleted = true;
				if (paramList != null) {
					for (TileParam param : paramList) {
						if (param.getName() != null && param.getName().equals(edtp.getParamName()) && param.getTile() != null
								&& param.getTile().getTileId() != null
								&& param.getTile().getTileId().equals(edtp.getDashboardTile().getTileId())) {
							isDeleted = false;
							rows.put(param, edtp);
							break;
						}
					}
				}
				if (isDeleted) {
					tile.getDashboardTileParamsList().remove(edtp);
				}
			}
		}

		if (paramList == null) {
			return;
		}
		for (TileParam tp : paramList) {
			EmsDashboardTileParams edtp = null;
			if (!rows.containsKey(tp)) {
				edtp = tp.getPersistentEntity(tile, null);
				tile.addEmsDashboardTileParams(edtp);
				rows.put(tp, edtp);
			}
			else {
				edtp = rows.get(tp);
				edtp = tp.getPersistentEntity(tile, edtp);
			}
		}
	}

	private void updateSpecificType(EmsDashboardTile to) throws CommonFunctionalException
	{
	    if(to.getCreationDate() == null) {
	        to.setCreationDate(DateUtil.getGatewayTime());
	        to.setLastModificationDate(to.getCreationDate());
	    }
		if (Tile.TILE_TYPE_TEXT_WIDGET.equals(getType())) {
			/*to.setWidgetName(Tile.TEXT_WIDGET_NAME);
			to.setWidgetDescription(Tile.TEXT_WIDGET_DESCRIPTION);
			to.setWidgetGroupName(Tile.TEXT_WIDGET_NAME);
			to.setWidgetOwner(Tile.TEXT_WIDGET_OWNER);
			to.setWidgetSource(Tile.WIDGET_SOURCE_DASHBOARD_FRAMEWORK);
			to.setWidgetKocName(Tile.TEXT_WIDGET_KOC_NAME);
			to.setWidgetViewmode(Tile.TEXT_WIDGET_VIEWMODEL);
			to.setWidgetTemplate(Tile.TEXT_WIDGET_TEMPLATE);
			to.setWidth(Tile.TEXT_WIDGET_WIDTH);
			to.setWidgetUniqueId(Tile.TEXT_WIDGET_NAME);*/
			to.setWidgetCreationTime(String.valueOf(DateUtil.getGatewayTime()));
			String encodedContent = StringEscapeUtils.escapeHtml4(getContent());
//			if (StringUtil.isEmpty(encodedContent)) {
//				throw new CommonFunctionalException(DashboardErrorConstants.DASHBOARD_TEXT_WIDGET_EMPTY_CONTENT_ERROR_CODE,
//						MessageUtils.getDefaultBundleString(CommonFunctionalException.TEXT_WIDGET_INVALID_EMPTY_CONTENT_ERROR));
//			}
			if (encodedContent != null && encodedContent.length() > TEXT_WIDGET_MAX_CONTENT_LEN) {
				throw new CommonFunctionalException(DashboardErrorConstants.DASHBOARD_TEXT_WIDGET_CONTENT_TOO_LONG_ERROR_CODE,
						MessageUtils.getDefaultBundleString(CommonFunctionalException.TEXT_WIDGET_CONTENT_TOO_LONG_ERROR));
			}
			EmsDashboardTileParams edtp = new EmsDashboardTileParams(1, Tile.TEXT_WIDGET_PARAM_NAME_CONTENT,
					TileParam.PARAM_TYPE_CODE_STRING, null, encodedContent, null, to);
			to.addEmsDashboardTileParams(edtp);
		}
		else {
			if (!StringUtil.isEmpty(getLinkText())) {
				String encodedLinkText = StringEscapeUtils.escapeHtml4(getLinkText());
				if (encodedLinkText != null && encodedLinkText.length() > TEXT_WIDGET_MAX_LINK_TEXT_LEN) {
					throw new CommonFunctionalException(
							MessageUtils.getDefaultBundleString(CommonFunctionalException.TEXT_WIDGET_INVALID_LINK_TEXT_ERROR));
				}
				EmsDashboardTileParams edtp = new EmsDashboardTileParams(0, Tile.DF_BUILTIN_WIDGET_LINK_TEXT,
						TileParam.PARAM_TYPE_CODE_STRING, null, encodedLinkText, null, to);
				to.addEmsDashboardTileParams(edtp);
			}
			if (!StringUtil.isEmpty(getLinkUrl())) {
				String encodedLinkUrl = StringEscapeUtils.escapeHtml4(getLinkUrl());
				if (encodedLinkUrl != null && encodedLinkUrl.length() > TEXT_WIDGET_MAX_LINK_URL_LEN) {
					throw new CommonFunctionalException(
							MessageUtils.getDefaultBundleString(CommonFunctionalException.TEXT_WIDGET_INVALID_LINK_URL_ERROR));
				}
				EmsDashboardTileParams edtp = new EmsDashboardTileParams(0, Tile.DF_BUILTIN_WIDGET_LINK_URL,
						TileParam.PARAM_TYPE_CODE_STRING, null, encodedLinkUrl, null, to);
				to.addEmsDashboardTileParams(edtp);
			}
		}
	}
}
