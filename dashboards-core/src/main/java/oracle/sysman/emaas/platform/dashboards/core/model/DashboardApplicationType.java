/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author guobaochen
 */
public enum DashboardApplicationType
{
	/**
	 * There is no draft version for the book
	 */
	APM(1, false),
	/**
	 * The draft version is under edit
	 */
	ITAnalytics(2, false),
	/**
	 * The draft version is submitted for release
	 */
	LogAnalytics(3, false),
	// Infrastructure Monitoring service
	Monitoring(4, false),
	// Security Analytics service
	SecurityAnalytics(5, false),
	// Orchestration
	Orchestration(6, false),
	// Compliance
	Compliance(7, false),
	// UDE
	UDE(8, false),
	// EVENT
	EVT(9, false),

	// following are bundle services, will be transferred to individual services
	OMCSE(100, true),
	OMCEE(101, true),
	OMCLOG(102, true),
	SECSE(103, true),
	SECSMA(104, true),
	OMC(105, true), // OMC is the combination of OMCSE, OMCEE, OMCLOG
	OSMACC(106, true); // OSMACC is the combination of SECSE and SECSMA

	public static final String APM_STRING = "APM";
	public static final String ITA_SRING = "ITAnalytics";
	public static final String LA_STRING = "LogAnalytics";
	public static final String MONITORING_STRING = "Monitoring";
	public static final String SECURITY_ANALYTICS_STRING = "SecurityAnalytics";
	public static final String ORCHESTRATION_STRING = "Orchestration";
	public static final String COMPLIANCE_STRING = "Compliance";
	public static final String UDE_STRING = "UDE";
	public static final String EVENT_STRING = "Alerts";
	public static final String OMCSE_STRING = "OMCSE";
	public static final String OMCEE_STRING = "OMCEE";
	public static final String OMCLOG_STRING = "OMCLOG";
	public static final String SECSE_STRING = "SECSE";
	public static final String SECSMA_STRING = "SECSMA";
	public static final String OMC_STRING = "OMC";
	public static final String OSMACC_STRING = "OSMACC";

	private static final Logger LOGGER = LogManager.getLogger(DashboardApplicationType.class);
//	public static final List<DashboardApplicationType> allBasicService = Arrays.asList(APM, ITAnalytics, LogAnalytics, Monitoring, SecurityAnalytics, Orchestration, Compliance);

	@JsonCreator
	public static DashboardApplicationType fromJsonValue(String value)
	{
		if (APM_STRING.equals(value)) {
			return APM;
		}
		if (ITA_SRING.equals(value)) {
			return ITAnalytics;
		}
		if (LA_STRING.equals(value)) {
			return LogAnalytics;
		}
		if (MONITORING_STRING.equals(value)) {
			return Monitoring;
		}
		if (SECURITY_ANALYTICS_STRING.equals(value)) {
			return SecurityAnalytics;
		}
		if (ORCHESTRATION_STRING.equals(value)) {
			return Orchestration;
		}
        if (COMPLIANCE_STRING.equals(value)) {
            return Compliance;
        }
        if (UDE_STRING.equals(value)) {
            return UDE;
        }
        if (EVENT_STRING.equals(value)) {
            return EVT;
        }
		if (OMC_STRING.equals(value)) {
			return OMC;
		}
		if (OSMACC_STRING.equals(value)) {
			return OSMACC;
		}
		if (OMCSE_STRING.equals(value)) {
			return OMCSE;
		}
		if (OMCEE_STRING.equals(value)) {
			return OMCEE;
		}
		if (OMCLOG_STRING.equals(value)) {
			return OMCLOG;
		}
		if (SECSE_STRING.equals(value)) {
			return SECSE;
		}
		if (SECSMA_STRING.equals(value)) {
			return SECSMA;
		}
		throw new IllegalArgumentException("Invalid DashboardApplicationType string value: " + value);
	}

	public static DashboardApplicationType fromValue(int value)
	{
		for (DashboardApplicationType bis : DashboardApplicationType.values()) {
			if (value == bis.getValue()) {
				return bis;
			}
		}
		throw new IllegalArgumentException("Invalid DashboardApplicationType type value: " + value);
	}

	private final int value;
	private final boolean isBundleService;

	private DashboardApplicationType(int value, boolean isBundleService)
	{
		this.value = value;
		this.isBundleService = isBundleService;
	}

	@JsonValue
	public String getJsonValue()
	{
		if (value == APM.value) {
			return APM_STRING;
		}
		if (value == ITAnalytics.value) {
			return ITA_SRING;
		}
		if (value == LogAnalytics.value) {
			return LA_STRING;
		}
		if (value == Monitoring.value) {
			return MONITORING_STRING;
		}
		if (value == SecurityAnalytics.value) {
			return SECURITY_ANALYTICS_STRING;
		}
		if (value == Orchestration.value) {
			return ORCHESTRATION_STRING;
		}
        if (value == Compliance.value) {
        	return COMPLIANCE_STRING;
        }
        if (value == UDE.value) {
            return UDE_STRING;
        }
        if (value == EVT.value) {
            return EVENT_STRING;
        }
		if (value == OMC.value) {
			return OMC_STRING;
		}
		if (value == OSMACC.value) {
			return OSMACC_STRING;
		}
		if (value == OMCSE.value) {
			return OMCSE_STRING;
		}
		if (value == OMCEE.value) {
			return OMCEE_STRING;
		}
		if (value == OMCLOG.value) {
			return OMCLOG_STRING;
		}
		if (value == SECSE.value) {
			return SECSE_STRING;
		}
		if (value == SECSMA.value) {
			return SECSMA_STRING;
		}
		throw new IllegalArgumentException("Invalid DashboardApplicationType type value: " + value);
	}

	public int getValue()
	{
		return value;
	}

	public static List<DashboardApplicationType> getBasicServiceList(List<DashboardApplicationType> services) {
		if (services == null) {
			return null;
		}
		Set<DashboardApplicationType> basicServicesSet = new HashSet<DashboardApplicationType>();
		for (DashboardApplicationType service: services) {
			if (!service.isBundleService) {
				basicServicesSet.add(service);
			}
		}
		LOGGER.info("Getting basic services: {}. Origianl service type list is {}", basicServicesSet, services);
		return new ArrayList<DashboardApplicationType>(basicServicesSet);
	}
}
