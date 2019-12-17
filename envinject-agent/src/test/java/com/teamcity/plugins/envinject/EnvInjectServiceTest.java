package com.teamcity.plugins.envinject;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnvInjectServiceTest {

    private Map<String, String> systemProperties, runnerParameters, configParameters, envVariables;

    private EnvInjectService service;

    @Mock
    private BuildRunnerContext mockRunnerContext;

    @Mock
    private AgentRunningBuild mockAgentRunningBuild;

    @Mock
    private BuildAgentConfiguration mockBuildAgentConfiguration;

    @Mock
    private BuildProgressLogger mockBuildProgressLogger;

    @Mock
    private BuildParametersMap buildParametersMap;

    private String sampleContent = "foo={system.build.number}bar\n" +
            "system.bug=xyz\n" +
            "\n" +
            "#This is a comment\n" +
            "env.abc=123";

    @Before
    public void setUp() throws Exception {
        this.systemProperties = new HashMap<>();
        systemProperties.put("build.number", "3");
        this.runnerParameters = new HashMap<>();
        this.configParameters = new HashMap<>();
        this.envVariables = new HashMap<>();
        service = new EnvInjectService();
    }

    @Test
    public void testInjectFromContent() throws RunBuildException {
        service.initialize(mockAgentRunningBuild, mockRunnerContext);
        runnerParameters.put("propertiesFileContent", sampleContent);
        runnerParameters.put("showDebugInfo", "true");
        //Most methods are final and protected so we need to setup a hierarchy of mocks
        when(mockRunnerContext.getRunnerParameters()).thenReturn(this.runnerParameters);
        when(mockRunnerContext.getConfigParameters()).thenReturn(this.configParameters);
        when(mockRunnerContext.getBuildParameters()).thenReturn(this.buildParametersMap);
        when(mockRunnerContext.getWorkingDirectory()).thenReturn(new File("java"));
        when(buildParametersMap.getSystemProperties()).thenReturn(this.systemProperties);
        when(buildParametersMap.getEnvironmentVariables()).thenReturn(this.envVariables);
        when(mockAgentRunningBuild.getAgentConfiguration()).thenReturn(this.mockBuildAgentConfiguration);
        when(mockAgentRunningBuild.getBuildLogger()).thenReturn(this.mockBuildProgressLogger);
        service.makeProgramCommandLine();

        verify(mockBuildAgentConfiguration).addConfigurationParameter("foo", "3bar");
        verify(mockBuildAgentConfiguration).addEnvironmentVariable("abc", "123");
        verify(mockBuildAgentConfiguration).addSystemProperty("bug", "xyz");
    }

    @Test
    public void testWithEmptyLinesAndComments() {

    }
}
