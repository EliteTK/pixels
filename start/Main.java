package start;

import graphics.mandelbrot.Mandelbrot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import window.Window;

/**
 *
 * @author main
 */
public class Main implements KeyListener {

    static int maxIter = 50, sampleRate = 1;
    static double posx = 0.0, posy = 0.0, pixelStep = 0.007;

    static boolean event = true;
    static Mandelbrot mand;

    static final int width = 640, height = 480;

    public static void main(String args[]) {
        Window window = new Window(width, height, "Mandelbrot");
        window.frameBasicInit();
        window.frame().setVisible(true);

        window.addKeyListener(new Main());

        mand = new Mandelbrot(window, new ColourFunc(), width, height, posx, posy, pixelStep, maxIter, sampleRate, false);

        while (true) {
            mand.draw();
            if (event) {
                mand.updateViewport(posx, posy, pixelStep, maxIter, sampleRate, false);
                event = false;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'd':
                posx += pixelStep * 10 * sampleRate;
                event = true;
                break;
            case 'w':
                posy += pixelStep * 10 * sampleRate;
                event = true;
                break;
            case 'a':
                posx -= pixelStep * 10 * sampleRate;
                event = true;
                break;
            case 's':
                posy -= pixelStep * 10 * sampleRate;
                event = true;
                break;
            case '+':
                pixelStep /= 1.1;
                event = true;
                break;
            case '-':
                pixelStep *= 1.1;
                event = true;
                break;
            case ',':
                if (maxIter >= 10) {
                    maxIter -= 10;
                    event = true;
                }
                break;
            case '.':
                maxIter += 10;
                event = true;
                break;
            case 'r':
                mand.getDrawingPlane().redraw();
                break;
            case 'R':
                maxIter = 50;
                pixelStep = 0.007;
                posx = 0;
                posy = 0;
                event = true;
                break;
            case '[':
                if (sampleRate > 1) {
                    sampleRate -= 1;
                    event = true;
                }
                break;
            case ']':
                sampleRate += 1;
                event = true;
                break;
        }
    }
}
