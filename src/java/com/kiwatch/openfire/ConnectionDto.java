package com.kiwatch.openfire;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ConnectionDto {

    private final String username;
    private final Date date;
    private final Event event;

    public ConnectionDto(final String username, final Date date, final Event event) {
        this.username = username;
        this.date = date;
        this.event = event;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date == null ? null : DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
    }

    public Event getEvent() {
        return event;
    }
}
