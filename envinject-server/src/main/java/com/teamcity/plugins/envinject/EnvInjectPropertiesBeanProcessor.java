package com.teamcity.plugins.envinject;

import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;

import java.util.Collection;
import java.util.Map;

public class EnvInjectPropertiesBeanProcessor implements PropertiesProcessor{

    public Collection<InvalidProperty> process(Map<String, String> map) {
        return null;
    }

}
