/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

/**
 *
 * @author Lukasz
 */
public class ImageType {
    String name;
    String description;
    int value;

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public ImageType(String name, String description, int value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }
    
}
