package com.kiwatch.openfire;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.event.SessionEventDispatcher;
import org.jivesoftware.util.JiveGlobals;

import java.io.File;

public class SessionInterceptorCallbackPlugin implements Plugin {

    private static final String PROPERTY_URL = "plugin.session_interceptor.url";
    private static final String PROPERTY_BASIC_AUTH_USER = "plugin.session_interceptor.basic_auth_user";
    private static final String PROPERTY_BASIC_AUTH_PASSWORD = "plugin.session_interceptor.basic_auth_password";
    private SessionInterceptor sessionInterceptor;

    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        String url = JiveGlobals.getProperty(PROPERTY_URL, null);
        String user = JiveGlobals.getProperty(PROPERTY_BASIC_AUTH_USER, null);
        String password = JiveGlobals.getProperty(PROPERTY_BASIC_AUTH_PASSWORD, null);
        if (url != null) {
            sessionInterceptor = new SessionInterceptor(url, user, password);
            SessionEventDispatcher.addListener(sessionInterceptor);
        }
    }

    public void destroyPlugin() {
        if (sessionInterceptor != null) {
            SessionEventDispatcher.removeListener(sessionInterceptor);
            sessionInterceptor = null;
        }
    }
}
