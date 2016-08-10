package com.traveloka.gradle

import com.traveloka.gradle.delegator.BazelDelegator
import com.traveloka.gradle.delegator.DependencyDelegator
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by salvian on 04/08/16.
 */
public class DependencyDelegatorPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        DependencyDelegator dependencyDelegator = new BazelDelegator(project)
        dependencyDelegator.delegateDependencies()
    }
}
