package io.vehiclehistory.safebus.data.api;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateFormatter {
    public String formatDateFromApi(String dateFromApi) {
        DateTime dateTime = new DateTime(dateFromApi);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
        return formatter.print(dateTime);
    }
}
