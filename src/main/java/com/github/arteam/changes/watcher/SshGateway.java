package com.github.arteam.changes.watcher;

import net.schmizz.sshj.SSHClient;

import java.io.IOException;

/**
 * Date: 1/2/15
 * Time: 11:22 AM
 *
 * @author Artem Prigoda
 */
public class SshGateway {

    private SSHClient ssh;

    public SshGateway() {
        ssh = new SSHClient();
        try {
            ssh.loadKnownHosts();
        } catch (IOException e) {
            throw new RuntimeException("Unable load known hosts", e);
        }
    }

    public void connect(String host, String username, String password) {
        try {
            ssh.connect(host);
            ssh.authPassword(username, password);
        } catch (IOException e) {
            throw new RuntimeException("Unable connect to a remote host", e);
        }
    }

    public void copyFile(String path, String remotePath) {
        try {
            ssh.newSCPFileTransfer().upload(path, remotePath);
        } catch (IOException e) {
            throw new RuntimeException("Unable copy file to a remote server", e);
        }
    }

    public void disconnect() {
        try {
            ssh.disconnect();
        } catch (IOException e) {
            throw new RuntimeException("Unable disconnect", e);
        }
    }
}
