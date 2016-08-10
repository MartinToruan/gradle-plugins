package com.traveloka.gradle.converter

import com.traveloka.gradle.data.bazel.BazelJavaLibrary
import org.gradle.api.Project

/**
 * Created by salvian on 04/08/16.
 */
public class BazelConverter implements DependencyConverter {
    Project project
    Map<String, String> mavenArtifactsMapping

    public BazelConverter(Project __project, Map<String, String> __mavenArtifactsMapping, List<BazelJavaLibrary> BazelJavaLibraries) {
        project = __project
        mavenArtifactsMapping = __mavenArtifactsMapping
        for (BazelJavaLibrary bazelJavaLibrary : BazelJavaLibraries) {
            if (bazelJavaLibrary.name == "jar") {
                convertCompileDeps(bazelJavaLibrary.deps)
                convertRuntimeDeps(bazelJavaLibrary.runtimeDeps)
            } else if (bazelJavaLibrary.name == "testJar") {\
                convertTestCompileDeps(bazelJavaLibrary.deps)
                convertTestRuntimeDeps(bazelJavaLibrary.runtimeDeps)
            }
        }
    }

    @Override
    def convertCompileDeps(List<String> deps) {
        convertDeps("compile", deps)
    }

    @Override
    def convertRuntimeDeps(List<String> runtimeDeps) {
        convertDeps("runtime", runtimeDeps)
    }

    @Override
    def convertTestCompileDeps(List<String> testCompileDeps) {
        convertDeps("testCompile", testCompileDeps)
    }

    @Override
    def convertTestRuntimeDeps(List<String> testRuntimeDeps) {
        convertDeps("testRuntime", testRuntimeDeps)
    }

    def convertDeps(String target, List<String> depList) {
        for (String dependency : depList) {
            if (!dependency.startsWith(':')) {
                def isProject = false
                if (dependency.startsWith('@')) {
                    dependency = dependency.substring(dependency.indexOf('@') + 1, dependency.indexOf("//jar"));
                } else {
                    isProject = true
                    dependency = dependency.substring(2, dependency.length() - 4);
                    String[] dependenProject = dependency.split('/');
                    int count = dependenProject.length;
                    String tempDeps = "";
                    for (int i = 0; i < count; i++) {
                        tempDeps += ':';
                        tempDeps += dependenProject[i];
                    }
                    project.dependencies {
                        delegate."$target"(project.project("$tempDeps"))
                    }

                }
                if (!isProject) {
                    def artifact = mavenArtifactsMapping.get(dependency)
                    project.dependencies {
                        delegate."$target"("$artifact")
                    }
                }
            }
        }
    }
}
