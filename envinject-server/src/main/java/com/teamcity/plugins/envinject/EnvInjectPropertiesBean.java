package com.teamcity.plugins.envinject;

public class EnvInjectPropertiesBean {

    private String propertiesFilePath;
    private String propertiesFileContent;
    private boolean showDebugInfo;
    public String getPropertiesFilePath() {
        return propertiesFilePath;
    }

    public void setPropertiesFilePath(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }

    public String getPropertiesFileContent() {
        return propertiesFileContent;
    }

    public void setPropertiesFileContent(String propertiesFileContent) {
        this.propertiesFileContent = propertiesFileContent;
    }

    public boolean isShowDebugInfo() {
        return showDebugInfo;
    }

    public void setShowDebugInfo(boolean showDebugInfo) {
        this.showDebugInfo = showDebugInfo;
    }

    @Override
    public String toString() {
        return "EnvInjectPropertiesBean{" +
                "propertiesFilePath='" + propertiesFilePath + '\'' +
                ", propertiesFileContent='" + propertiesFileContent + '\'' +
                ", showDebugInfo=" + showDebugInfo +
                '}';
    }
}
