package com.kiwatch.openfire;

import static com.kiwatch.openfire.Event.CONNECTION;
import static com.kiwatch.openfire.Event.DISCONNECTION;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.concurrent.Future;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;

import org.bouncycastle.util.encoders.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestClient {

    private static final Logger Log = LoggerFactory.getLogger(RestClient.class);
    private final WebTarget target;
    private final String basicAuthString;

    public RestClient(final String url, final String user, final String password) {
        this.target = ClientBuilder.newClient().target(url);
        Base64.Encoder encoder = Base64.getEncoder();
        this.basicAuthString = user == null ? null : "Basic " + new String(encoder.encode((user + ":" + password).getBytes()));
    }

    public void sendConnection(final String user, final ZonedDateTime date) {
        Log.debug("sendConnection user '{}', date '{}'", user, date);
        sendRequest(user, date, CONNECTION);
    }

    public void sendDisconnection(final String user, final ZonedDateTime date) {
        Log.debug("sendDisconnection user '{}', date '{}'", user, date);
        sendRequest(user, date, DISCONNECTION);
    }

    private void sendRequest(final String user, final ZonedDateTime date, final Event event) {
        Object[] parameters = { user, date, event };
        Log.debug("sendRequest user '{}', date '{}', event '{}'", parameters);
        SessionEventDto sessionEventDto = new SessionEventDto(user, date, event);
        Invocation.Builder request = target.request();
        if (basicAuthString != null) {
            request = request.header(HttpHeaders.AUTHORIZATION, basicAuthString);
        }
        Future<Response> responseFuture = request
            .async()
            .post(Entity.json(sessionEventDto));
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
