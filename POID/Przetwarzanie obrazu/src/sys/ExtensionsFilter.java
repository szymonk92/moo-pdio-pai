package sys;

import java.io.File;
import java.io.FilenameFilter;

public class ExtensionsFilter implements FilenameFilter {

    String extension;

    public ExtensionsFilter(String roz) {
        this.extension = "." + roz;
    }

    @Override
    public boolean accept(File plik, String nazwa) {
        return nazwa.endsWith(this.extension);
    }
};
