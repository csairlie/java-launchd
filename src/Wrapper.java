import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Wrapper {
    public enum EntryType {
        USER_AGENT,
        GLOBAL_AGENT,
        GLOBAL_DAEMON
    }
    private ArrayList<Launchd> allEntries;

    private EntryDecorator<Launchd> userAgentDecorator;
    private EntryDecorator<Launchd> globalAgentDecorator;
    private EntryDecorator<Launchd> globalDaemonDecorator;

    public Wrapper() throws IOException {
        this.userAgentDecorator = new EntryDecorator<>(initEntries(EntryType.USER_AGENT));
        this.globalAgentDecorator = new EntryDecorator<>(initEntries(EntryType.GLOBAL_AGENT));
        this.globalDaemonDecorator = new EntryDecorator<>(initEntries(EntryType.GLOBAL_DAEMON));

        allEntries = new ArrayList<>();
        allEntries.addAll(userAgentDecorator.getEntries());
        allEntries.addAll(globalAgentDecorator.getEntries());
        allEntries.addAll(globalDaemonDecorator.getEntries());
    }

    private ArrayList<Launchd> initEntries(EntryType entryType) throws IOException {
        final String path = switch (entryType) {
            case USER_AGENT -> System.getProperty("user.home") + "/Library/LaunchAgents";
            case GLOBAL_AGENT -> "/Library/LaunchAgents";
            case GLOBAL_DAEMON -> "/Library/LaunchDaemons";
        };

        Path directoryPath = Paths.get(path).normalize();
        if (!Files.exists(directoryPath) || !Files.isDirectory(directoryPath)) {
            throw new IOException("Directory does not exist: " + directoryPath);
        }

        ArrayList<Launchd> entries = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {
            for (Path file : directoryStream) {
                if(!file.getFileName().toString().startsWith(".")) {
                    Launchd newEntry = new Launchd(file.getFileName().toString(), path, file.toFile());
                    entries.add(newEntry);
                }
            }
        }
        return entries;
    }

    public ArrayList<Launchd> getAllEntries() {
        return allEntries;
    }

    /* User Agents */
    public ArrayList<Launchd> getUserAgents() {
        return userAgentDecorator.getEntries();
    }

    public void addUserAgent(Launchd entry) {
        userAgentDecorator.add(entry);
    }

    public void removeUserAgent(Launchd entry) {
        userAgentDecorator.remove(entry);
    }

    /* Global Agents */
    public ArrayList<Launchd> getGlobalAgents() {
        return globalAgentDecorator.getEntries();
    }

    public void addGlobalAgent(Launchd entry) {
        globalAgentDecorator.add(entry);
    }

    public void removeGlobalAgent(Launchd entry) {
        globalAgentDecorator.remove(entry);
    }

    /* Global Daemons */
    public ArrayList<Launchd> getGlobalDaemons() {
        return globalDaemonDecorator.getEntries();
    }

    public void addGlobalDaemon(Launchd entry) {
        globalDaemonDecorator.add(entry);
    }

    public void removeGlobalDaemon(Launchd entry) {
        globalDaemonDecorator.remove(entry);
    }
}
