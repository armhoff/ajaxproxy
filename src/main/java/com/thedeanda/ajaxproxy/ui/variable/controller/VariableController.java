package com.thedeanda.ajaxproxy.ui.variable.controller;

import com.thedeanda.ajaxproxy.config.ConfigFileService;
import com.thedeanda.ajaxproxy.config.model.Variable;
import com.thedeanda.ajaxproxy.ui.SettingsChangedListener;
import com.thedeanda.ajaxproxy.ui.variable.model.VariableModel;
import com.thedeanda.javajson.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class VariableController {
    private final ConfigFileService configFileService;
    private Collection<SettingsChangedListener> listeners = new HashSet<>();
    private Collection<VariableChangeListener> variableChangeListeners = new HashSet<>();
    private Map<String, String> datanset = new TreeMap<>();

    private VariableModel variableModel;

    public VariableController(ConfigFileService configFileService) {
        this.configFileService = configFileService;
        this.variableModel = VariableModel.builder().build();

        configFileService.addConfigChangeListener(c -> {
            setConfig(c.getVariables());
        });
    }

    private void fireSettingsChanged() {
        listeners.forEach(l -> l.settingsChanged());
    }

    //temporary, use custom listener instead maybe
    public void addListener(final SettingsChangedListener listener) {
        this.listeners.add(listener);
    }

    public void setVariableModel(VariableModel variableModel) {
        this.variableModel = variableModel;
        //fire events
        fireSettingsChanged();
    }

    public void clear() {
        this.variableModel = VariableModel.builder().build();
        fireSettingsChanged();
    }

    private void setConfig(List<Variable> variables) {
        clear();
        if (variables == null || variables.isEmpty()) return;

        for (Variable var : variables) {
            set(var.getKey(), var.getValue());
        }
    }

    public JsonObject getConfig() {
        JsonObject ret = new JsonObject();

        variableModel.getVariables().forEach(var -> {
            ret.put(var.getKey(), var.getValue());
        });
        return ret;
    }

    public int getSize() {
        return variableModel==null ? 0 : variableModel.getVariables().size();
    }

    public Optional<Variable> get(int index) {
        Variable var = null;
        if (index >= 0 && index < variableModel.getVariables().size())
            var = variableModel.getVariables().get(index);

        return Optional.ofNullable(var);
    }

    public int getKeyIndex(String key) {
        int i = 0;
        for (Variable v : variableModel.getVariables()) {
            if (StringUtils.equals(key, v.getKey())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public void remove(String key) {
        variableModel.getVariables().removeIf(var -> StringUtils.equals(key, var.getKey()));
        fireSettingsChanged();
    }

    public void set(String key, String value) {
        List<Variable> vars = variableModel.getVariables();
        vars.removeIf(var -> StringUtils.equals(key, var.getKey()));
        vars.add(Variable.builder()
                .key(key)
                .value(value)
                .build());
        Collections.sort(vars);
        fireSettingsChanged();
    }
}
