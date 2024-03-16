import java.util.ArrayList;

public class EntryDecorator<T extends Launchd> {
    private ArrayList<T> entries;

    public EntryDecorator(ArrayList<T> entries) {
        this.entries = entries;
    }

    public void add(T entry) {
        assert entry != null;
        entries.add(entry);
    }

    public void remove(T entry) {
        assert entry != null;
        entries.remove(entry);
    }

    public ArrayList<T> getEntries() {
        return entries;
    }
}
