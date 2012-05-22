/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.io.File;
import java.io.FilenameFilter;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Lukasz
 */
public class FilenameExtensionOnly extends FileFilter implements FilenameFilter {

    String ext;

    public FilenameExtensionOnly(String ext) {
        this.ext = "." + ext;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(ext);
    }

    @Override
    public boolean accept(File pathname) {
         return pathname.isFile()?pathname.getName().endsWith(ext):true;
    }

    @Override
    public String getDescription() {
        return "Files: "+ext;
    }
}
