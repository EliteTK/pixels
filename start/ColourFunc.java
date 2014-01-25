/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package start;

import graphics.mandelbrot.MandelbrotColourtableFunction;
import java.awt.Color;
import java.util.Random;

/**
 *
 * @author main
 */
public class ColourFunc implements MandelbrotColourtableFunction {

    Random rand = new Random();

    @Override
    public int getColour(int iter, int maxIter) {
        float temp = (float) iter / (float) maxIter;
        float rainbowLoop = ((float) iter % (float)100) / 99;
        return Color.HSBtoRGB(rainbowLoop, (float) 1.0, (float) 1.0);
    }
}
