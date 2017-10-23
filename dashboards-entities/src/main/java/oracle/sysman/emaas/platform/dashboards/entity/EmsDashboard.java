package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@NamedQueries({
        @NamedQuery(name = "EmsDashboard.findById", query = "select o from EmsDashboard o where o.dashboardId = :id"),
        @NamedQuery(name = "EmsDashboard.queryBySubDashboardID", query = "select a from EmsDashboard a ,EmsSubDashboard b "
                + "where a.dashboardId = b.dashboardSetId and b.subDashboardId = :p"),
        @NamedQuery(name = "EmsDashboard.findByAppType", query = "select o.dashboardId from EmsDashboard o where o.deleted = 0 and o.isSystem = 1 and o.applicationType = :appType"),
        @NamedQuery(name = "EmsDashboard.deleteByDashboardIds", query = "delete from EmsDashboard o where o.deleted = 0 and o.dashboardId in :ids"),
        @NamedQuery(name = "EmsDashboard.findByName", query = "select d from EmsDashboard d where d.name = :name and (d.owner = :owner or d.isSystem = 1) and d.deleted = 0")
})
@Table(name = "EMS_DASHBOARD")
@IdClass(EmsDashboardPK.class)
//@SequenceGenerator(name = "EmsDashboard_Id_Seq_Gen", sequenceName = "EMS_DASHBOARD_SEQ", allocationSize = 1)
//@Multitenant(MultitenantType.SINGLE_TABLE)
//@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", length = 32, primaryKey = true)
public class EmsDashboard extends EmBaseEntity implements Serializable
{
	private static final long serialVersionUID = 1219062974568988740L;

	@Id
	@Column(name = "DASHBOARD_ID", nullable = false)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EmsDashboard_Id_Seq_Gen")
	private BigInteger dashboardId;

	@Column(name = "DELETED")
	private BigInteger deleted;
	@Column(name = "DESCRIPTION", length = 1280)
	private String description;
	@Column(name = "ENABLE_TIME_RANGE", nullable = false)
	private Integer enableTimeRange;
	@Column(name = "ENABLE_REFRESH", nullable = false)
	private Integer enableRefresh;
	@Column(name = "ENABLE_DESCRIPTION", nullable = false)
	private Integer enableDescription;
	@Column(name = "ENABLE_ENTITY_FILTER", nullable = false)
	private Integer enableEntityFilter;
	@Column(name = "IS_SYSTEM", nullable = false)
	private Integer isSystem;
	@Column(name = "SHOW_INHOME", nullable = false)
	private Integer showInHome;
	@Column(name = "SHARE_PUBLIC", nullable = false)
	private Integer sharePublic;
	@Column(name = "APPLICATION_TYPE")
	private Integer applicationType;
	@Column(name = "EXTENDED_OPTIONS", length = 128)
	private String extendedOptions;
	@Column(name = "LAST_MODIFIED_BY", length = 128)
	private String lastModifiedBy;
	@Column(nullable = false, length = 320)
	private String name;
	@Column(nullable = false, length = 128)
	private String owner;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "SCREEN_SHOT", columnDefinition = "CLOB NULL")
	private String screenShot;
	@Column(nullable = false)
	private Integer type;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dashboard", orphanRemoval = true)
	@OrderBy("row, column")
	private List<EmsDashboardTile> dashboardTileList;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dashboardSet", orphanRemoval = true)
	@OrderBy("position ASC")
	private List<EmsSubDashboard> subDashboardList;
	
	@Override @Id
    public Long getTenantId() {
        return super.getTenantId();
    }

	public EmsDashboard()
	{
	}

	public EmsDashboard(Date creationDate, BigInteger dashboardId, BigInteger deleted, String description, Integer enableTimeRange,
			Integer enableRefresh, Integer enableDescription, Integer enableEntityFilter, Integer isSystem,
			Integer sharePublic, Date lastModificationDate, String lastModifiedBy, String name, String owner, String screenShot,
			Integer type, Integer applicationType,Integer showInHome,String extendedOptions)
	{
		setCreationDate(creationDate);
		setLastModificationDate(lastModificationDate);
		this.dashboardId = dashboardId;
		this.deleted = deleted;
		this.description = description;
		this.enableTimeRange = enableTimeRange;
		this.enableRefresh = enableRefresh;
		this.enableDescription = enableDescription;
		this.enableEntityFilter = enableEntityFilter;
		this.isSystem = isSystem;
		this.sharePublic = sharePublic;
		this.lastModifiedBy = lastModifiedBy;
		this.name = name;
		this.owner = owner;
		this.screenShot = screenShot;
		this.type = type;
		this.applicationType = applicationType;
		this.showInHome=showInHome;
		this.extendedOptions = extendedOptions;
	}

	public EmsDashboardTile addEmsDashboardTile(EmsDashboardTile emsDashboardTile)
	{
		if (dashboardTileList == null) {
			dashboardTileList = new ArrayList<EmsDashboardTile>();
		}
		dashboardTileList.add(emsDashboardTile);
		emsDashboardTile.setDashboard(this);
		return emsDashboardTile;
	}

	public EmsSubDashboard addEmsSubDashboard(EmsSubDashboard emsSubDashboard)
	{
		if (subDashboardList == null) {
			subDashboardList = new ArrayList<>();
		}
		subDashboardList.add(emsSubDashboard);
		emsSubDashboard.setDashboardSet(this);
		return emsSubDashboard;
	}

	public Integer getApplicationType()
	{
		return applicationType;
	}

	public BigInteger getDashboardId()
	{
		return dashboardId;
	}

	public List<EmsDashboardTile> getDashboardTileList()
	{
		return dashboardTileList;
	}

	public BigInteger getDeleted()
	{
		return deleted;
	}

	public String getDescription()
	{
		return description;
	}

	public Integer getEnableDescription()
	{
		return enableDescription;
	}

	public Integer getEnableEntityFilter()
	{
		return enableEntityFilter;
	}

	/**
	 * @return the enableRefresh
	 */
	public Integer getEnableRefresh()
	{
		return enableRefresh;
	}

	public Integer getEnableTimeRange()
	{
		return enableTimeRange;
	}

	public String getExtendedOptions()
	{
		return extendedOptions;
	}

	public Integer getIsSystem()
	{
		return isSystem;
	}

	public Integer getShowInHome()
	{
		return showInHome;
	}

	public String getLastModifiedBy()
	{
		return lastModifiedBy;
	}

	public String getName()
	{
		return name;
	}

	public String getOwner()
	{
		return owner;
	}

	public String getScreenShot()
	{
		return screenShot;
	}

	/**
	 * @return the sharePublic
	 */
	public Integer getSharePublic()
	{
		return sharePublic;
	}

	public List<EmsSubDashboard> getSubDashboardList()
	{
		return subDashboardList;
	}

	public Integer getType()
	{
		return type;
	}

	public EmsDashboardTile removeEmsDashboardTile(EmsDashboardTile emsDashboardTile)
	{
		getDashboardTileList().remove(emsDashboardTile);
		emsDashboardTile.setDashboard(null);
		return emsDashboardTile;
	}

	public EmsSubDashboard removeEmsSubDashboard(EmsSubDashboard emsSubDashboard)
	{
		subDashboardList.remove(emsSubDashboard);
		emsSubDashboard.setDashboardSet(null);
		return emsSubDashboard;
	}

	public void setApplicationType(Integer applicationType)
	{
		this.applicationType = applicationType;
	}

	/**
	 * @param dashboardId
	 *            the dashboardId to set
	 */
	public void setDashboardId(BigInteger dashboardId)
	{
		this.dashboardId = dashboardId;
	}

	public void setDashboardTileList(List<EmsDashboardTile> emsDashboardTileList)
	{
		dashboardTileList = emsDashboardTileList;
	}

	public void setDeleted(BigInteger deleted)
	{
		this.deleted = deleted;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setEnableDescription(Integer enableDescription)
	{
		this.enableDescription = enableDescription;
	}

	public void setEnableEntityFilter(Integer enableEntityFilter)
	{
		this.enableEntityFilter = enableEntityFilter;
	}

	/**
	 * @param enableRefresh
	 *            the enableRefresh to set
	 */
	public void setEnableRefresh(Integer enableRefresh)
	{
		this.enableRefresh = enableRefresh;
	}

	public void setEnableTimeRange(Integer enableTimeRange)
	{
		this.enableTimeRange = enableTimeRange;
	}

	public void setExtendedOptions(String extendedOptions)
	{
		this.extendedOptions = extendedOptions;
	}

	public void setIsSystem(Integer isSystem)
	{
		this.isSystem = isSystem;
	}

	public void setShowInHome(Integer showInHome)
	{
		this.showInHome = showInHome;
	}

	public void setLastModifiedBy(String lastModifiedBy)
	{
		this.lastModifiedBy = lastModifiedBy;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public void setScreenShot(String screenShot)
	{
		this.screenShot = screenShot;
	}

	/**
	 * @param sharePublic
	 *            the sharePublic to set
	 */
	public void setSharePublic(Integer sharePublic)
	{
		this.sharePublic = sharePublic;
	}

	public void setSubDashboardList(List<EmsSubDashboard> subDashboardList)
	{
		this.subDashboardList = subDashboardList;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}

}
