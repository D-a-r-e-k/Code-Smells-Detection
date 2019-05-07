/**
 * Constructs a function that returns <tt>a * b</tt>.
 * <tt>a</tt> is a variable, <tt>b</tt> is fixed.
 */
public static DoubleFunction mult(final double b) {
    return new Mult(b);
}
