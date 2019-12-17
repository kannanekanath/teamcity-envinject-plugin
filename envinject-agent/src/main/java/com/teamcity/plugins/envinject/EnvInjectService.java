package com.teamcity.plugins.envinject;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collections;

import static java.nio.file.Files.newBufferedReader;

public class EnvInjectService extends BuildServiceAdapter {
    public static final String ENV_VAR_PREFIX = "env.";
    public static final String SYSTEM_VAR_PREFIX = "system.";

    @NotNull
    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
        PropertyPlaceholderHelper.PlaceholderResolver resolver = new TeamcityPlaceholderResolver();
        int envVarsFromFile = injectPropertiesFromFile(resolver);
        int envVarsFromContent = injectPropertiesFromContent(resolver);
        if (envVarsFromContent == 0 && envVarsFromFile == 0) {
            getLogger().error("You have used the EnvInject plugin but not provided a properties file or content");
            getLogger().error("This runner will effectively be a no-op");
        }
        return new SimpleProgramCommandLine(getRunnerContext(), "java", Collections.singletonList("-version"));
    }

    private int injectPropertiesFromContent(PropertyPlaceholderHelper.PlaceholderResolver resolver)
             throws RunBuildException {
        String propertiesFileContent = getRunnerParameters().get("propertiesFileContent");
        int propertiesCount = 0;
        if (StringUtils.hasText(propertiesFileContent)) {
            getLogger().message("Will use properties content string configured by user");
            try (BufferedReader reader = new BufferedReader(new StringReader(propertiesFileContent))) {
                propertiesCount = injectEnvironmentVars(reader, resolver);

            } catch (IOException | RuntimeException e) {
                getLogger().exception(e);
                throw new RunBuildException("Exception occured reading/parsing given properties content. Message ["
                        + e.getMessage() + "]");
            }
            getLogger().message("Plugin injected [" + propertiesCount + "] parameters from properties content");
        }
        return propertiesCount;
    }

    private int injectPropertiesFromFile(PropertyPlaceholderHelper.PlaceholderResolver resolver)
            throws RunBuildException {
        String propertiesFilePath = getRunnerParameters().get("propertiesFilePath");
        int propertiesCount = 0;
        if (StringUtils.hasText(propertiesFilePath)) {
            getLogger().message("Will use properties from file [" + propertiesFilePath + "]");
            try (BufferedReader reader = newBufferedReader(Paths.get(propertiesFilePath), StandardCharsets.UTF_8)) {
                propertiesCount = injectEnvironmentVars(reader, resolver);

            } catch (IOException | RuntimeException e) {
                throw new RunBuildException("Exception occured reading/parsing [" + propertiesFilePath
                        + "]. Message [" + e.getMessage() + "]");
            }
            getLogger().message("Plugin injected [" + propertiesCount + "] parameters from ["
                    + propertiesFilePath + "]");
        }
        return propertiesCount;
    }

    protected int injectEnvironmentVars(BufferedReader reader, PropertyPlaceholderHelper.PlaceholderResolver resolver)
            throws IOException, RunBuildException {
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("{", "}");
        String line;
        int propertiesCount = 0;
        while ((line = reader.readLine()) != null) {
            addDebugMessage("Read line [%s]", line);
            if(isIgnoredLine(line)) {
                continue;
            }
            if (!line.contains("=")) {
                throw new RunBuildException("The line [" + line + "] does contain property of the form key=value");
            }
            String[] split = line.split("=", 2);
            addDebugMessage("Will use key [%s] and value [%s]", split[0], split[1]);
            String finalValue = helper.replacePlaceholders(split[1], resolver);
            addDebugMessage("Resolved value of [%s] is [%s]", split[1], finalValue);
            if (split[0].startsWith(ENV_VAR_PREFIX)) {
                addDebugMessage("Will set environment variable");
                getAgentConfiguration().addEnvironmentVariable(split[0].replace(ENV_VAR_PREFIX, ""), finalValue);
            } else if (split[0].startsWith(SYSTEM_VAR_PREFIX)) {
                addDebugMessage("Will set system property");
                getAgentConfiguration().addSystemProperty(split[0].replace(SYSTEM_VAR_PREFIX, ""), finalValue);
            } else {
                addDebugMessage("Will set configuration property");
                getAgentConfiguration().addConfigurationParameter(split[0], finalValue);
            }
            propertiesCount++;
        }
        return propertiesCount;
    }

    private boolean isIgnoredLine(String line) {
        return !StringUtils.hasLength(line) || line.startsWith("#");
    }

    private void addDebugMessage(String message, Object... args) {
        boolean debug = Boolean.valueOf(getRunnerParameters().get("showDebugInfo"));
        if (debug) {
            getLogger().message(String.format(message, args));
        }
    }

    class TeamcityPlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {

        @Override
        public String resolvePlaceholder(String placeholderName) {
            String value;
            if(placeholderName.startsWith(ENV_VAR_PREFIX)) {
                value = EnvInjectService.this.getEnvironmentVariables().get(placeholderName.replace(ENV_VAR_PREFIX, ""));
            } else if(placeholderName.startsWith(SYSTEM_VAR_PREFIX)) {
                value = EnvInjectService.this.getSystemProperties().get(placeholderName.replace(SYSTEM_VAR_PREFIX, ""));
            } else {
                value = EnvInjectService.this.getConfigParameters().get(placeholderName);
            }
            if(value == null) {
                throw new RuntimeException("The place holder [" + placeholderName + "] cannot be resolved");
            }
            return value;
        }
    }
}
