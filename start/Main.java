package start;

import graphics.Mandelbrot;
import window.Window;

/**
 *
 * @author main
 */
public class Main {

    public static void main(String args[]) {
        Window window = new Window(640, 420, "Mandelbrot");
        window.frameBasicInit();
        window.frame().setVisible(true);
        Mandelbrot mand = new Mandelbrot(window, 600, 600, -2, 1, -1.5, 1.5, 384);
        mand.draw();
    }
}
