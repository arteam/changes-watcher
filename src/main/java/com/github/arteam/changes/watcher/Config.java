package com.github.arteam.changes.watcher;

import org.dumb.yaml.annotation.Names;

/**
 * Date: 1/2/15
 * Time: 11:55 AM
 *
 * @author Artem Prigoda
 */
public class Config {

    public final String host;
    public final String username;
    public final String password;
    public final String remoteBaseDirectory;
    public final String monitoredDirectory;

    @Names({"host", "username", "password", "remote_directory", "monitored_directory"})
    public Config(String host, String username, String password, String remoteBaseDirectory, String monitoredDirectory) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.remoteBaseDirectory = remoteBaseDirectory;
        this.monitoredDirectory = monitoredDirectory;
    }
}
