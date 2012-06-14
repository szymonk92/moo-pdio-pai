/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.io.File;

/**
 *
 * @author Lukasz
 */
public class ClassFile {
    public String className;
    public File file;

    public ClassFile(String className, File file) {
        this.className = className;
        this.file = file;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    
}
