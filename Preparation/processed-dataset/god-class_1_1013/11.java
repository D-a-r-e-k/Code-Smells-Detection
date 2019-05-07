/**
 * Constructs a function that returns <tt>a / b</tt>.
 * <tt>a</tt> is a variable, <tt>b</tt> is fixed.
 */
public static DoubleFunction div(final double b) {
    return mult(1 / b);
}
