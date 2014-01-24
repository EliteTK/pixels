package complex;

/**
 *
 * @author main
 */
public class Complex {

    double real;
    double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public static Complex add(Complex c1, Complex c2) {
        return new Complex(c1.real + c2.real, c1.imaginary + c2.imaginary);
    }

    public static Complex subtract(Complex c1, Complex c2) {
        return new Complex(c1.real - c2.real, c1.imaginary = c2.imaginary);
    }

    public static Complex multiply(Complex c1, Complex c2) {
        double real;
        double imaginary;

        real = c1.real * c2.real - c1.imaginary * c2.imaginary;
        imaginary = c1.real * c2.imaginary + c1.imaginary * c2.real;

        return new Complex(real, imaginary);
    }
}
