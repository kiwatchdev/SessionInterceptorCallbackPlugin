package com.kiwatch.openfire;

import java.time.ZonedDateTime;
import java.util.function.BiConsumer;

import org.jivesoftware.openfire.event.SessionEventListener;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionInterceptor implements SessionEventListener {

    private static final Logger Log = LoggerFactory.getLogger(SessionInterceptor.class);

    private final UserManager userManager;
    private final RestClient restClient;

    public SessionInterceptor(final String url, final String user, final String password) {
        Log.debug("Instantiate {}", SessionInterceptor.class.getSimpleName());
        this.userManager = UserManager.getInstance();
        this.restClient = new RestClient(url, user, password);
    }

    @Override public void sessionCreated(final Session session) {
        Log.debug("Manage sessionCreated event, session '{}'", session.getStreamID().getID());
        manageSessionEvent(session, restClient::sendConnection);
    }

    @Override public void sessionDestroyed(final Session session) {
        Log.debug("Manage sessionDestroyed event, session '{}'", session.getStreamID().getID());
        manageSessionEvent(session, restClient::sendDisconnection);
    }

    private void manageSessionEvent(final Session session, final BiConsumer<String, ZonedDateTime> sendFunction) {
        String node = session.getAddress().getNode();
        Log.debug("manageSessionEvent id '{}', node '{}'", session.getStreamID().getID(), node);
        try {
            User userTo = userManager.getUser(node);
            Log.debug("manageSessionEvent id '{}', userTo '{}'", session.getStreamID().getID(), userTo.getUsername());
            ZonedDateTime creationDate = ZonedDateTime.now();
            sendFunction.accept(userTo.getUsername(), creationDate);
        } catch (UserNotFoundException e) {
            Log.info("can't find user with name: '{}', id '{}'", node, session.getStreamID().getID());
        }
    }

    @Override public void anonymousSessionCreated(final Session session) {
        Log.debug("anonymousSessionCreated id '{}'", session.getStreamID().getID());
    }

    @Override public void anonymousSessionDestroyed(final Session session) {
        Log.debug("anonymousSessionDestroyed id '{}'", session.getStreamID().getID());
    }

    @Override public void resourceBound(final Session session) {
        Log.debug("resourceBound id '{}'", session.getStreamID().getID());
    }
}
