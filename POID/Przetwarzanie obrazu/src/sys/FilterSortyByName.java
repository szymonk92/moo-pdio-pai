/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.util.Comparator;

/**
 *
 * @author Lukasz
 */
public class FilterSortyByName implements Comparator<IFilter>{

    @Override
    public int compare(IFilter o1, IFilter o2) {
       return o1.getName().compareToIgnoreCase(o2.getName());
    }
    
}
