package com.teamcity.plugins.envinject;

import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import org.jetbrains.annotations.NotNull;

public class EnvInjectRunnerFactory implements CommandLineBuildServiceFactory {

    @NotNull
    public CommandLineBuildService createService() {
        return new EnvInjectService();
    }
    private static AgentBuildRunnerInfo RUNNER_INFO = new EnvInjectRunnerInfo();

    @NotNull
    public AgentBuildRunnerInfo getBuildRunnerInfo() {
        return RUNNER_INFO;
    }
}
