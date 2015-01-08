package com.github.arteam.changes.watcher.conf;

/**
 * Date: 1/8/15
 * Time: 1:56 PM
 *
 * @author Artem Prigoda
 */
public enum Target {
    LOCAL, REMOTE;

    public static Target from(String source) {
        return valueOf(source.toUpperCase());
    }
}
