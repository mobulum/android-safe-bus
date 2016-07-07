package io.vehiclehistory.safebus.config;

public class ApplicationConfiguration {
    public static final String TRUSTSTORE_NAME = "vehiclehistory.io.truststore.bks";
    public static final String HOSTNAME = "vehicle-history.io";
    public static final String HOST = "https://" + HOSTNAME;
    public static final Integer RETRY_COUNT = 2;
    public static final Integer READ_TIMEOUT_MS = 10000;
    public static final Integer CONNECTION_TIMEOUT_MS = 5000;
    public static final String JSON = "json";
    public static final String API_VERSION_1 = "application/vnd.vehicle-history.v1+" + JSON;
}
