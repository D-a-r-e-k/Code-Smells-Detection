/**
 * Constructs a function that returns <tt>a - b</tt>.
 * <tt>a</tt> is a variable, <tt>b</tt> is fixed.
 */
public static DoubleFunction minus(final double b) {
    return plus(-b);
}
