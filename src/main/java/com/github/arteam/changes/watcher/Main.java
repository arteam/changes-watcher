package com.github.arteam.changes.watcher;

import com.github.arteam.changes.watcher.conf.Config;
import com.github.arteam.changes.watcher.conf.RemoteTarget;
import com.github.arteam.changes.watcher.conf.Target;
import org.dumb.yaml.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 1/2/15
 * Time: 11:00 AM
 * <p/>
 * Monitor local directory with subdirectories
 * and copy changes to a remote host by SSH
 *
 * @author Artem Prigoda
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger((Class) Main.class);

    /**
     * Path of watch keys
     * They are needed because watcher service returns only relative paths
     */
    private Map<WatchKey, Path> paths = new HashMap<>();

    public static void main(String[] args) throws Exception {
        new Main().start(args);
    }

    private void start(String[] args) throws Exception {
        String configLocation = "./config.yml";
        if (args.length == 1) {
            configLocation = args[0];
        }

        SshGateway sshGateway = null;
        RemoteTarget remote = null;

        Config config = new Yaml().parse(new File(configLocation), Config.class);
        if (config.active == Target.REMOTE) {
            sshGateway = new SshGateway();
            remote = config.remote;

            log.info("Connecting to a remote host: " + remote.host);
            sshGateway.connect(remote.host, remote.username, remote.password);
            log.info("Done!");
        }

        WatchService watchService = FileSystems.getDefault().newWatchService();
        log.info("Starting watcher...");

        String monitoredDirectory = config.monitoredDirectory;
        watchDirectory(monitoredDirectory, watchService);

        // Monitor for changes
        while (!Thread.currentThread().isInterrupted()) {
            WatchKey watchKey = watchService.take();
            log.info("Watch key: " + watchKey);
            List<WatchEvent<?>> events = watchKey.pollEvents();
            for (WatchEvent<?> event : events) {
                WatchEvent.Kind kind = event.kind();
                Path eventPath = (Path) event.context();
                // Ignore IDEA files
                if (eventPath.toString().endsWith("___")) {
                    continue;
                }
                log.info("Kind: " + kind);
                log.info("Path: " + eventPath);
                if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    // Get absolute path of changed source file
                    String sourcePath = paths.get(watchKey).resolve(eventPath).toString();

                    if (config.active == Target.REMOTE) {
                        String remotePath = sourcePath.replaceAll(config.monitoredDirectory, config.remote.remoteBaseDirectory);
                        log.info("Copy " + sourcePath);
                        try {
                            sshGateway.copyFile(sourcePath, remotePath);
                            log.info("Copied to remote path " + remotePath);
                        } catch (Exception e) {
                            log.error("Unable copy file", e);
                        }
                    } else {
                        String remotePath = sourcePath.replaceAll(config.monitoredDirectory, config.local.targetDirectory);
                        log.info("Copy " + sourcePath);
                        try {
                            Files.copy(new File(sourcePath).toPath(), new File(remotePath).toPath());
                            log.info("Copied to remote path " + remotePath);
                        } catch (Exception e) {
                            log.error("Unable copy file", e);
                        }
                    }
                }
            }
            if (!watchKey.reset()) {
                log.info("Directory is not exist");
                break;
            }
        }
        if (sshGateway != null) {
            sshGateway.disconnect();
        }
    }

    private void watchDirectory(String directory, WatchService watchService) throws IOException {
        log.info("Monitor " + directory);
        Path path = Paths.get(directory);
        WatchKey watchKey = path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
        paths.put(watchKey, path);
        File[] files = new File(directory).listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory()) continue;
                watchDirectory(file.getAbsolutePath(), watchService);
            }
        }
    }
}
