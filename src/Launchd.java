import java.io.File;

public class Launchd {
    final private String name;
    final private String path;
    final private File pList;

    public Launchd(String aName, String aPath, File aPList) {
        assert aName != null && aPath != null && aPList != null;
        name = aName;
        path = aPath;
        pList = aPList;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public File getPList() {
        return pList;
    }

}
