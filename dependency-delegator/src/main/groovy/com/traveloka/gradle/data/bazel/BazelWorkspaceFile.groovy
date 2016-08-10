package com.traveloka.gradle.data.bazel
/**
 * Created by salvian on 04/08/16.
 */
//TODO use real parser
public class BazelWorkspaceFile {
    Map<String, String> mavenArtifacts = new HashMap<>();

    public BazelWorkspaceFile(String path) {
        String name = null, artifact = null
        new File(path + "/WORKSPACE").eachLine {
            String line = "$it".trim()
            if (name == null && line.startsWith("name =") && !line.contains("default")) {
                name = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""))
            }
            if (name != null && line.startsWith("artifact =")) {
                artifact = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""))
                mavenArtifacts.put(name, artifact);
                name = null
            }
        }
    }
}
