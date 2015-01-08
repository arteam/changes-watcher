package com.github.arteam.changes.watcher.conf;

import org.dumb.yaml.annotation.Names;

/**
 * Date: 1/4/15
 * Time: 1:49 PM
 *
 * @author Artem Prigoda
 */
public class RemoteTarget {

    public final String host;
    public final String username;
    public final String password;
    public final String remoteBaseDirectory;

    @Names({"host", "username", "password", "remote_directory"})
    public RemoteTarget(String host, String username, String password, String remoteBaseDirectory) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.remoteBaseDirectory = remoteBaseDirectory;
    }
}
