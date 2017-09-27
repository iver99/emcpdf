package oracle.sysman.emaas.platform.dashboards.core.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Created by guochen on 9/20/17.
 */
public enum FederationSupportedType {
	/**
	 * Supports non-federation only
	 */
	NON_FEDERATION_ONLY(0),
	/**
	 * Supports both federation and non federation
	 */
	FEDERATION_AND_NON_FEDERATION(1),
	/**
	 * Supports federation only
	 */
	FEDERATION_ONLY(2);

	public static final String NON_FEDERATION_ONLY_STRING = "NON_FEDERATION_ONLY";
	public static final String FEDERATION_AND_NON_FEDERATION_SRING = "FEDERATION_AND_NON_FEDERATION";
	public static final String FEDERATION_ONLY_STRING = "FEDERATION_ONLY";

	@JsonCreator
	public static FederationSupportedType fromJsonValue(String value)
	{
		if (NON_FEDERATION_ONLY_STRING.equals(value)) {
			return NON_FEDERATION_ONLY;
		}
		if (FEDERATION_AND_NON_FEDERATION_SRING.equals(value)) {
			return FEDERATION_AND_NON_FEDERATION;
		}
		if (FEDERATION_ONLY_STRING.equals(value)) {
			return FEDERATION_ONLY;
		}
		throw new IllegalArgumentException("Invalid FederationSupportedType string value: " + value);
	}

	public static FederationSupportedType fromValue(int value)
	{
		for (FederationSupportedType bis : FederationSupportedType.values()) {
			if (value == bis.getValue()) {
				return bis;
			}
		}
		throw new IllegalArgumentException("Invalid FederationSupportedType type value: " + value);
	}

	private final int value;

	private FederationSupportedType(int value)
	{
		this.value = value;
	}

	@JsonValue
	public String getJsonValue()
	{
		if (value == NON_FEDERATION_ONLY.value) {
			return NON_FEDERATION_ONLY_STRING;
		}
		if (value == FEDERATION_AND_NON_FEDERATION.value) {
			return FEDERATION_AND_NON_FEDERATION_SRING;
		}
		if (value == FEDERATION_ONLY.value) {
			return FEDERATION_ONLY_STRING;
		}
		throw new IllegalArgumentException("Invalid FederationSupportedType type value: " + value);
	}

	public int getValue()
	{
		return value;
	}
}
