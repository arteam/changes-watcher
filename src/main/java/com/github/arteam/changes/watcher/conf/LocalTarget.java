package com.github.arteam.changes.watcher.conf;

import org.dumb.yaml.annotation.Name;

/**
 * Date: 1/4/15
 * Time: 1:49 PM
 *
 * @author Artem Prigoda
 */
public class LocalTarget {

    public final String targetDirectory;

    public LocalTarget(@Name("target_directory") String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }
}
