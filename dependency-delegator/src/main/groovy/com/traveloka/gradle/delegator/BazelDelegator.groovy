package com.traveloka.gradle.delegator

import com.traveloka.gradle.converter.BazelConverter
import com.traveloka.gradle.data.bazel.BazelBuildFile
import com.traveloka.gradle.data.bazel.BazelWorkspaceFile
import org.gradle.api.Project

/**
 * Created by salvian on 04/08/16.
 */
class BazelDelegator implements DependencyDelegator {
    Project project

    BazelDelegator(Project __project) {
        project = __project
    }

    @Override
    void delegateDependencies() {
        BazelWorkspaceFile bazelWorkspaceFile = new BazelWorkspaceFile(project.rootDir.toString())
        BazelBuildFile bazelBuildFile = new BazelBuildFile(project.projectDir.toString())
        new BazelConverter(project, bazelWorkspaceFile.mavenArtifacts, bazelBuildFile.javaLibraries)
    }

}
