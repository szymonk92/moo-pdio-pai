/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.awt.image.BufferedImage;
import sys.AbstractFilter;
import sys.IFilter;

/**
 *
 * @author pawel
 */
public class MeanFilter extends AbstractFilter {
    
    int[][] filterDefault =     {
        {1,1,1},
        {1,1,1},
        {1,1,1}
    };
    
    int[][] filterCustom ;

    @Override
    public IFilter getCopy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    void matrixFilterOp(BufferedImage image) {
        
        
    }
    
} 
