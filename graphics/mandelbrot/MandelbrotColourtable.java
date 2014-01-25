/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package graphics.mandelbrot;

/**
 *
 * @author main
 */
public final class MandelbrotColourtable {
        private final int colourTable[];
        private final int maxIter, maxIterColour;
    public MandelbrotColourtable (MandelbrotColourtableFunction mcf, int maxIter, int maxIterColour) {
        this.colourTable = new int[maxIter];
        this.maxIter = maxIter;
        this.maxIterColour = maxIterColour;
        
        this.recalculate(mcf);
    }
    
    /**
     * Recalculate the colour lookup table.
     * @param mcf
     */
    public void recalculate(MandelbrotColourtableFunction mcf) {
        for (int i = 0; i < this.maxIter; i++) {
            this.colourTable[i] = mcf.getColour(i, maxIter);
        }
    }
    
    /**
     * Lookup colour for iteration.
     * @param i
     * @return colour
     */
    public int getColour(int i) {
        if (i < this.maxIter) {
            return this.colourTable[i-1];
        } else {
            return this.maxIterColour;
        }
    }
}
