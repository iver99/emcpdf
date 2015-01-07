package oracle.sysman.emaas.platform.dashboards.core.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;

public class TileParam
{
	public static final int PARAM_TYPE_STRING = 1;
	public static final int PARAM_TYPE_NUMBER = 2;
	public static final int PARAM_TYPE_TIMESTAMP = 3;

	public static TileParam valueOf(EmsDashboardTileParams edtp)
	{
		if (edtp == null) {
			return null;
		}
		TileParam tp = new TileParam();
		tp.setIsSystem(DataFormatUtils.integer2Boolean(edtp.getIsSystem()));
		tp.setName(edtp.getParamName());
		tp.setParamType(edtp.getParamType());
		tp.setIntegerValue(edtp.getParamValueNum());
		tp.setStringValue(edtp.getParamValueStr());
		tp.setParamValueTimestamp(edtp.getParamValueTimestamp());
		return tp;
	}

	private Boolean isSystem;
	private String name;
	private Integer type;
	private String strValue;
	private Date dateValue;
	private Tile tile;

	private BigDecimal numValue;

	public Integer getIntegerValue()
	{
		return DataFormatUtils.bigDecimal2Integer(numValue);
	}

	public Boolean getIsSystem()
	{
		return isSystem;
	}

	public Long getLongValue()
	{
		return DataFormatUtils.bigDecimal2Long(numValue);
	}

	public String getName()
	{
		return name;
	}

	public BigDecimal getNumberValue()
	{
		return numValue;
	}

	public Integer getParamType()
	{
		return type;
	}

	public Date getParamValueTimestamp()
	{
		return dateValue;
	}

	public EmsDashboardTileParams getPersistentEntity(EmsDashboardTileParams edtp)
	{
		Integer intIsSystem = DataFormatUtils.boolean2Integer(getIsSystem());
		Integer intValue = DataFormatUtils.bigDecimal2Integer(numValue);
		Timestamp tsValue = DataFormatUtils.date2Timestamp(getParamValueTimestamp());

		if (edtp == null) {
			edtp = new EmsDashboardTileParams(intIsSystem, name, type, intValue, strValue, tsValue, null);
		}
		return edtp;
	}

	public String getStringValue()
	{
		return strValue;
	}

	public Tile getTile()
	{
		return tile;
	}

	public void setIntegerValue(Integer value)
	{
		setNumberValue(DataFormatUtils.integer2BigDecimal(value));
	}

	public void setIsSystem(Boolean isSystem)
	{
		this.isSystem = isSystem;
	}

	public void setLongValue(Long value)
	{
		setNumberValue(DataFormatUtils.long2BigDecimal(value));
	}

	public void setName(String paramName)
	{
		name = paramName;
	}

	public void setNumberValue(BigDecimal paramValueNum)
	{
		if (type == null) {
			type = PARAM_TYPE_NUMBER;
		}
		numValue = paramValueNum;
	}

	public void setParamType(Integer type)
	{
		this.type = type;
	}

	public void setParamValueTimestamp(Date paramValueTimestamp)
	{
		if (type == null) {
			type = PARAM_TYPE_TIMESTAMP;
		}
		dateValue = paramValueTimestamp;
	}

	public void setStringValue(String paramValueStr)
	{
		if (type == null) {
			type = PARAM_TYPE_STRING;
		}
		strValue = paramValueStr;
	}

	public void setTile(Tile tile)
	{
		this.tile = tile;
	}
}
