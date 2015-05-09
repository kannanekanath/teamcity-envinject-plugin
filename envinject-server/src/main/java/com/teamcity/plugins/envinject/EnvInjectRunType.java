package com.teamcity.plugins.envinject;

import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EnvInjectRunType extends RunType {

    private final PluginDescriptor descriptor;

    public EnvInjectRunType(@NotNull final RunTypeRegistry registry,
                            @NotNull final PluginDescriptor descriptor) {
        this.descriptor = descriptor;
        registry.registerRunType(this);
    }

    @NotNull
    @Override
    public String getType() {
        return AppCommon.ENV_INJECT_RUNNER_ID;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Inject environment variables";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Plugin to inject environment/system/config variables from a common properties file";
    }

    @Nullable
    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return new EnvInjectPropertiesBeanProcessor();
    }

    @Nullable
    @Override
    public String getEditRunnerParamsJspFilePath() {
        return this.descriptor.getPluginResourcesPath("editEnvInjectParams.jsp");
    }

    @Nullable
    @Override
    public String getViewRunnerParamsJspFilePath() {
        return this.descriptor.getPluginResourcesPath("viewEnvInjectParams.jsp");
    }

    @Nullable
    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        return new HashMap<String, String>();
    }
}
