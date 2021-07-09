package com.kiwatch.openfire;

import static com.kiwatch.openfire.Event.CONNECTION;
import static com.kiwatch.openfire.Event.DISCONNECTION;

import java.util.Date;
import java.util.concurrent.Future;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestClient {

    private static final Logger Log = LoggerFactory.getLogger(RestClient.class);
    private final WebTarget target;

    public RestClient(final String url) {
        this.target = ClientBuilder.newClient().target(url);
    }

    public void sendConnection(final String user, final Date date) {
        Log.debug("sendConnection user '{}', date '{}'", user, date);
        sendRequest(user, date, CONNECTION);
    }

    public void sendDisconnection(final String user, final Date date) {
        Log.debug("sendDisconnection user '{}', date '{}'", user, date);
        sendRequest(user, date, DISCONNECTION);
    }

    private void sendRequest(final String user, final Date date, final Event event) {
        Object[] parameters = { user, date, event };
        Log.debug("sendRequest user '{}', date '{}', event '{}'", parameters);
        ConnectionDto connectionDto = new ConnectionDto(user, date, event);
        Future<Response> responseFuture = target
            .request()
            .async()
            .post(Entity.json(connectionDto));
        try {
            Response response = responseFuture.get();
            if (response.getStatus() >= HttpStatus.SC_BAD_REQUEST) {
                Log.error("got response status url='{}' status='{}'", target, response.getStatus());
            } else {
                Log.debug("got response status url='{}' status='{}'", target, response.getStatus());
            }
        } catch (Exception e) {
            Log.error("can't get response status url='{}'", target, e);
        }
    }
}
