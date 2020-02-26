package com.thedeanda.ajaxproxy.config;

import com.thedeanda.ajaxproxy.config.model.Config;
import com.thedeanda.ajaxproxy.config.model.ConfigChangeListener;
import com.thedeanda.javajson.JsonException;
import com.thedeanda.javajson.JsonObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * config service will manage loading, saving and updates to a single config file
 */
public class ConfigFileService {
    private List<ConfigChangeListener> listeners = new ArrayList<>();
    private Config config;

    public void addConfigChangeListener(ConfigChangeListener listener) {
        listeners.add(listener);
    }

    private void configChanged() {
        listeners.stream().forEach(l -> l.configChanged(config));
    }
    
    public void loadConfigFile(File configFile) throws IOException, JsonException {
        Config co;
        try (InputStream is = new FileInputStream(configFile)) {
            JsonObject json = JsonObject.parse(is);

            ConfigLoader cl = new ConfigLoader();
            co = cl.loadConfig(json, configFile.getParentFile());
        }

        this.config = co;
        configChanged();
    }
}
