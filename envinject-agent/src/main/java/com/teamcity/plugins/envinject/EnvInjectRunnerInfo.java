package com.teamcity.plugins.envinject;

import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import org.jetbrains.annotations.NotNull;

public class EnvInjectRunnerInfo implements AgentBuildRunnerInfo {
    @NotNull
    public String getType() {
        return AppCommon.ENV_INJECT_RUNNER_ID;
    }

    public boolean canRun(@NotNull BuildAgentConfiguration buildAgentConfiguration) {
        return true;
    }
}
