package src.vision.util;

import java.text.DecimalFormat;
import java.util.Arrays;
import org.jscience.mathematics.function.Polynomial;
import org.jscience.mathematics.function.Term;
import org.jscience.mathematics.function.Variable;
import org.jscience.mathematics.number.Complex;

/**
 * Test roots obtained by Durand-Kerner-Weierstrass method.
 * Uses <a href="http://jscience.org/">jscience-4.3</a>
 * 
 * @author John B. Matthews; distrbution per LGPL.
 */
public class ComplexRoots {

    // const
    private static final double epsilon = ComplexPolynomial.getEpsilon();
    private static final DecimalFormat form =
        new DecimalFormat(" 0.00000000000000E0;-0.00000000000000E0");
    
    // args
    
    
    // CONSTRUCTOR METHOD
    public ComplexRoots() {
        // null
    }
    
    

	/*
    public static void main(String[] args) {
        // different real roots (x - 1)(x - 2) = x^2  - 3x + 2
        showResult(1.0, -3.0, 2.0);

        // identical real roots (x - 1)(x - 1) = x^2  - 2x + 1
        showResult(1.0, -2.0, 1.0);
        
        // complex conjugate roots (x + 2i)(x - 2i) = x^2 + 4
        showResult(1.0, 0.0, 4.0);
        
        // more complex roots (2x + i)(2x - i) = 4x^2 + 1
        showResult(4.0, 0.0, 1.0);

        // three real roots x(x - 1)(x - 2) = x^3  - 3x^2 + 2x
        showResult(1.0, -3.0, 2.0, 0);

        // (x + 2)(x - 2)(x + 3)(x - 3) = x^4 + -13x^2 + 36
        showResult(1.0, 0.0, -13.0, 0.0, 36.0);

        // x(x + 2)(x - 2)(x + 3)(x - 3) = x^5 - 13x^3 + 36x
        showResult(1.0, 0.0, -13.0, 0.0, 36.0, 0.0);

        // all-one polynomials
        for (int i = 2; i < 16; i++) { // orders 2..15
            double[] a = new double[i + 1];
            Arrays.fill(a, 1.0);
            showResult(a);
        }
        double[] a = new double[38]; // order 37
        Arrays.fill(a, 1.0);
        showResult(a);

        // (1 + 3i)x^2 + (2 + 2i)x + (3 + i)
        showResult(
            Complex.valueOf(1, 3),
            Complex.valueOf(2, 2),
            Complex.valueOf(3, 1));

        // (1 + i)x^3 + (2 + i)x^2 + (3 + i)x + (4 + i)
        showResult(
            Complex.valueOf(1, 1),
            Complex.valueOf(2, 1),
            Complex.valueOf(3, 1),
            Complex.valueOf(4, 1));

        // (1 + i)x^3 + (2 + 2i)x^2 + (3 + 3i)x + (4 + 4i)
        showResult(
            Complex.valueOf(1, 1),
            Complex.valueOf(2, 2),
            Complex.valueOf(3, 3),
            Complex.valueOf(4, 4));
    }
	*/
    
    // Return roots of polynomial with complex coefficients
    public static double[] solveRoots(double... a) {
        Complex[] ca = ComplexPolynomial.complexArray(a);
        Polynomial<Complex> px = ComplexPolynomial.create(ca);
        System.out.println("Poly: " + px);
        Complex[] r = ComplexPolynomial.roots(ca);
        validate(ca, r);
        
        // RETURN ROOTS IN DOUBLE FORM
        
    }

    // Find roots of polynomial with complex coefficients
    public static void showResult(Complex... a) {
        Polynomial<Complex> px = ComplexPolynomial.create(a);
        System.out.println("Poly: " + px);
        Complex[] r = ComplexPolynomial.roots(a);
        validate(a, r);
    }

    // Find roots of polynomial with real coefficients
    public static void showResult(double... a) {
        Complex[] ca = ComplexPolynomial.complexArray(a);
        Polynomial<Complex> px = ComplexPolynomial.create(ca);
        System.out.println("Poly: " + px);
        Complex[] r = ComplexPolynomial.roots(ca);
        validate(ca, r);
    }

    // Compare f(r) to zero; print roots; find largest error
    private static void validate(Complex[] ca, Complex... r) {
        double max = 0.0;
        Arrays.sort(r);
        int ix = 0;
        while (ix < r.length) {
            Complex error = ComplexPolynomial.
                eval(ca, r[ix]).minus(Complex.ZERO);
            max = Math.max(max, error.magnitude());
            double re = r[ix].getReal();
            double im = r[ix].getImaginary();
            if (Math.abs(re) < epsilon) re = 0;
            if (Math.abs(im) < epsilon) im = 0;
            if (im == 0) {
                System.out.println("Real: " + form.format(re));
            } else {
                if (ix + 1 < r.length && conjugate(r[ix], r[ix + 1])) {
                    System.out.println("Comp: " + form.format(re)
                        + " +-" + form.format(Math.abs(im)) + "i");
                    ix++;
                } else {
                    System.out.println("Comp: " + form.format(re)
                        + (im < 0 ? " -" : " +")
                        + form.format(Math.abs(im)) + "i");
                }
            }
            ix++;
        }
        System.out.println("Error: "
            + (max < epsilon ? "< " + epsilon : form.format(max))
            + "\n");
    }

    // Return true if a and b are conjugates
    private static boolean conjugate(Complex a, Complex b) {
        double dr = a.getReal() - b.getReal();
        double di = Math.abs(a.getImaginary()) - Math.abs(b.getImaginary());
        return Math.abs(dr) < epsilon && Math.abs(di) < epsilon;
    }
}

/**
 * An implementation of the Durand-Kerner-Weierstrass method to
 * determine the roots of a complex univariate polynomial, as described
 * <a href="http://en.wikipedia.org/wiki/Durand-Kerner_method">here</a>.
 *
 * @author John B. Matthews; distribution per LGPL.
 */
final class ComplexPolynomial {

    private static final int MAX_COUNT = 999;
    private static final boolean DEBUG = false;
    private static double epsilon = 1E-15;

    private ComplexPolynomial() {}

    /**
     * Create a complex polynomial from a number of Complex values.
     * The array should have the highest order coefficient first.
     *
     * @param  a a variable number of Complex coefficients.
     * @return a Polynomial having those Complex coefficients.
     */
    public static Polynomial<Complex> create(Complex... a) {
        Variable<Complex> x = new Variable.Local<Complex>("x");
        Polynomial<Complex> px = Polynomial.valueOf(Complex.ZERO, x);
        for (int i = 0, e = a.length - 1; i < a.length; i++, e--) {
            px = px.plus(Polynomial.valueOf(a[i], Term.valueOf(x, e)));
        }
        return px;
    }

    /**
     * Create a complex array from an array of double.
     * The array should have the highest order coefficient first.
     *
     * @param  a a variable number of double coefficients.
     * @return an array of the corresponding Complex coefficients.
     */
    public static Complex[] complexArray(double... a) {
        Complex[] ca = new Complex[a.length];
        for (int i = 0; i < a.length; i++) {
            ca[i] = Complex.valueOf(a[i], 0);
        }
        return ca;
    }

    /**
     * This implementation uses the Durand-Kerner-Weierstrass method
     * to find the roots of a polynomial with complex coefficients.
     * The method requires a monic polynomial; some error may occur
     * when dividing by the leading coefficient.
     * The array should have the highest order coefficient first.
     *
     * @param  ca a variable number of Complex polynomial coefficients.
     * @return an array of the Complex roots of the polynomial.
     */
    public static Complex[] roots(Complex[] ca) {
        Complex[] a0 = new Complex[ca.length - 1];
        Complex[] a1 = new Complex[ca.length - 1];

        // Divide by leading coefficient if not monic
        Complex leading = ca[0];
        if (!ca[0].equals(Complex.ONE)) {
            for (int i = 0; i < ca.length; i++) {
                ca[i] = ca[i].divide(leading);
            }
        }

        // Initialize a0
        Complex result = Complex.valueOf(0.4, 0.9);
        a0[0] = Complex.ONE;
        for (int i = 1; i < a0.length; i++) {
            a0[i] = a0[i - 1].times(result);
        }

        // Iterate
        int count = 0;
        while (true) {
            for (int i = 0; i < a0.length; i++) {
               result = Complex.ONE;
               for (int j = 0; j < a0.length; j++) {
                  if (i != j) {
                     result = a0[i].minus(a0[j]).times(result);
                  }
               }
               a1[i] = a0[i].minus(ComplexPolynomial.
                   eval(ca, a0[i]).divide(result));
            }
            count++;
            if (count > MAX_COUNT || done(a0, a1)) break;
            System.arraycopy(a1, 0, a0, 0, a1.length); // a0 := a1
        }
        if (DEBUG) {
            System.out.println("Iterations: " + count);
            for (Complex c : a0) System.out.println(c);
        }
        return a1;
    }

    // Determine if the components of two arrays are unchanging
    private static boolean done(Complex[] a, Complex[] b) {
        boolean unchanged = true;
        Complex delta;
        for (int i = 0; i < a.length; i++) {
            delta = a[i].minus(b[i]);
            unchanged &= Math.abs(delta.getReal()) < epsilon
                 && Math.abs(delta.getImaginary()) < epsilon;
        }
        return unchanged;
    }

    /**
     * Evaluate the polynomial at x using
     * <a href="http://en.wikipedia.org/wiki/Horner_scheme">
     * Horner's scheme</a>.
     * The array should have the highest order coefficient first.
     *
     * @param  ca an array of Complex polynomial coefficients.
     * @param  x the function argument.
     * @return the Complex value of the function at x.
     */
    public static Complex eval(Complex[] ca, Complex x) {
        Complex result = ca[0];
        for (int i = 1; i < ca.length; i++) {
           result = result.times(x).plus(ca[i]);
        }
        return result;
    }

    /** Return the nominal tolerance value. */
    public static double getEpsilon() { return epsilon; }

    /** Set the nominal tolerance value. */
    public static void setEpsilon(double e) { epsilon = e; }
}
