package com.zjiecode.gradle.plugin.rndown;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GradlePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        ConfigExtension configExtension = project.getExtensions().create("RNDownloadConfig", ConfigExtension.class);
        RNBundleDownTask rnBundleDownTask = project.getTasks().create(
                "RNBundleDownLoad", RNBundleDownTask.class, task -> task.setGroup("React Native"));
        project.afterEvaluate((p) -> {
            project.getRootDir().getPath();
            rnBundleDownTask.setConfig(configExtension);
            rnBundleDownTask.setProjectRootDir(project.getRootDir().toString());
            project.getTasks().matching(task -> task.getName().startsWith("package"))
                    .forEach(task -> task.dependsOn(rnBundleDownTask));
        });

    }

}
