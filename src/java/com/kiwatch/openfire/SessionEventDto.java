package com.kiwatch.openfire;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class SessionEventDto {

    private final String username;
    private final ZonedDateTime date;
    private final Event event;

    public SessionEventDto(final String username, final ZonedDateTime date, final Event event) {
        this.username = username;
        this.date = date;
        this.event = event;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date == null ? null : DateTimeFormatter.ISO_DATE_TIME.format(date);
    }

    public Event getEvent() {
        return event;
    }
}
