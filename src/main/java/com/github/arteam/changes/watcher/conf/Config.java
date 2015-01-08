package com.github.arteam.changes.watcher.conf;

import org.dumb.yaml.annotation.EnumConverter;
import org.dumb.yaml.annotation.Names;

/**
 * Date: 1/2/15
 * Time: 11:55 AM
 *
 * @author Artem Prigoda
 */
public class Config {

    public final String monitoredDirectory;
    public final Target active;
    public final LocalTarget local;
    public final RemoteTarget remote;

    @Names({"monitored_directory", "active", "local", "remote"})
    public Config(String monitoredDirectory, @EnumConverter("from") Target active, LocalTarget local, RemoteTarget remote) {
        this.monitoredDirectory = monitoredDirectory;
        this.active = active;
        this.local = local;
        this.remote = remote;
    }
}
