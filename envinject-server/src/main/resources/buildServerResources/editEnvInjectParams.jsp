<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props"%>
<jsp:useBean id="bean" class="com.teamcity.plugins.envinject.EnvInjectPropertiesBean"/>
<b>Use this plugin to set configuration/environment/system parameters for this build
from properties file or text content</b>
<br/>
<ul>
    <li>You can choose to load properties from a file using the "Properties file path" argument</li>
    <li>You can also load properties by directly specifying key=value pairs in the "Properties file content" argument</li>
    <li>If your property starts with env: it will be set as an environment variable</li>
    <li>If your property starts with system: it will be set as a system property</li>
    <li>otherwise it will be set as a configuration property</li>
    <li>If same property is present in both the file and the inline content then the inline will win</li>
    <li>You can also use teamcity predefined properties ex: <pre>env.foo={system.build.number}bar</pre>
    will set environment variable called foo to be "3bar" assuming the build number was "3".
    </li>
</ul>
<tr>
    <th>Properties file path</th>
    <td>
        <props:textProperty name="propertiesFilePath"/>
    </td>
    <td>
        This should be a valid path readable by the agent.
    </td>
</tr>
<tr>
    <th>Properties inline content</th>
    <td>
        <props:multilineProperty cols="40" rows="6" name="propertiesFileContent"
                linkTitle="Enter environment properties content"></props:multilineProperty>
    </td>
    <td>
        You can type any number of key=value pairs in this area
    </td>
</tr>
<tr>
    <th>Show debug information?</th>
    <td>
        <props:checkboxProperty name="showDebugInfo"/>
    </td>
    <td>
        Choosing this will print detailed information (useful for debugging when plugin does not produce
        expected results)
    </td>
</tr>