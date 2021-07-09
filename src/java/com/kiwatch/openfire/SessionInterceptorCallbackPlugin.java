package com.kiwatch.openfire;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.event.SessionEventDispatcher;
import org.jivesoftware.util.JiveGlobals;

import java.io.File;

public class SessionInterceptorCallbackPlugin implements Plugin {

    private static final String PROPERTY_URL = "plugin.session_interceptor.url";
    private SessionInterceptor sessionInterceptor;

    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        String url = JiveGlobals.getProperty(PROPERTY_URL, null);
        if (url != null) {
            sessionInterceptor = new SessionInterceptor(url);
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
