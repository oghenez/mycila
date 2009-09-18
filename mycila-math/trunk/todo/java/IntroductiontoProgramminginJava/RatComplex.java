/*************************************************************************
 *  Compilation:  javac RatComplex.java
 *  Execution:    java RatComplex
 *
 *  A complex number with arbitrary precision rational components.
 *
 *************************************************************************/

public class RatComplex {
    private final BigRational re;
    private final BigRational im;


    // create a new object with the given real and imaginary parts
    public RatComplex(BigRational real, BigRational imag) {
        re = real;
        im = imag;
    }


    // return a string representation of the invoking Complex object
    public String toString() {
        if (im.equals(BigRational.ZERO)) return re + "";
        if (re.equals(BigRational.ZERO)) return im + "i";
        if (im.isNegative()) return re + " - " + (im.negate()) + "i";
        return re + " + " + im + "i";
    }

    // return a new Complex object whose value is (this + b)
    public RatComplex plus(RatComplex b) {
        RatComplex a = this;
        BigRational real = a.re.plus(b.re);
        BigRational imag = a.im.plus(b.im);
        return new RatComplex(real, imag);
    }

    // return a new Complex object whose value is (this - b)
    public RatComplex minus(RatComplex b) {
        RatComplex a = this;
        BigRational real = a.re.minus(b.re);
        BigRational imag = a.im.minus(b.im);
        return new RatComplex(real, imag);
    }

    // return a new Complex object whose value is (this * b)
    public RatComplex times(RatComplex b) {
        RatComplex a = this;
        BigRational real = a.re.times(b.re).minus(a.im.times(b.im));
        BigRational imag = a.re.times(b.im).plus(a.im.times(b.re));
        return new RatComplex(real, imag);
    }

    // square of 2 norm
    public BigRational normSquared() {
        return re.times(re).plus(im.times(im));
    }


  public static void main(String[] args) {
        BigRational a1 = new BigRational(5, 1);
        BigRational a2 = new BigRational(6, 1);
        BigRational b1 = new BigRational(-1, 3);
        BigRational b2 = new BigRational(2, 7);
        RatComplex a = new RatComplex(a1, a2);
        RatComplex b = new RatComplex(b1, b2);

        System.out.println("a            = " + a);
        System.out.println("b            = " + b);
        System.out.println("b + a        = " + b.plus(a));
        System.out.println("a - b        = " + a.minus(b));
        System.out.println("a * b        = " + a.times(b));
        System.out.println("b * a        = " + b.times(a));
    }


}
