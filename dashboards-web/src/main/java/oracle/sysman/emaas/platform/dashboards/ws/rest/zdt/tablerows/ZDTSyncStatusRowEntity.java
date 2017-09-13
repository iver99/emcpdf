package oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows;

import org.codehaus.jackson.annotate.JsonProperty;

public class ZDTSyncStatusRowEntity {
	

	@JsonProperty("lastSyncDateTime")
	private String lastSyncDateTime;
	
	@JsonProperty("syncType")
	private String syncType;
	
	@JsonProperty("nextScheduledSyncDateTime")
	private String nextScheduledSyncDateTime;
	
	@JsonProperty("divergencePercentage")
	private double divergencePercentage;

	public String getLastSyncDateTime() {
		return lastSyncDateTime;
	}

	public void setLastSyncDateTime(String lastSyncDateTime) {
		this.lastSyncDateTime = lastSyncDateTime;
	}

	public String getSyncType() {
		return syncType;
	}

	public void setSyncType(String syncType) {
		this.syncType = syncType;
	}

	public String getNextScheduledSyncDateTime() {
		return nextScheduledSyncDateTime;
	}

	public void setNextScheduledSyncDateTime(String nextScheduledSyncDateTime) {
		this.nextScheduledSyncDateTime = nextScheduledSyncDateTime;
	}

	public double getDivergencePercentage() {
		return divergencePercentage;
	}

	public void setDivergencePercentage(double divergencePercentage) {
		this.divergencePercentage = divergencePercentage;
	}

	public ZDTSyncStatusRowEntity(String lastSyncDateTime, String syncType,
			String nextScheduledSyncDateTime, double divergencePercentage) {
		super();
		this.lastSyncDateTime = lastSyncDateTime;
		this.syncType = syncType;
		this.nextScheduledSyncDateTime = nextScheduledSyncDateTime;
		this.divergencePercentage = divergencePercentage;
	}
	

}
