/**
 * Constructs a function that returns <tt>a + b*constant</tt>.
 * <tt>a</tt> and <tt>b</tt> are variables, <tt>constant</tt> is fixed.
 */
public static DoubleDoubleFunction plusMult(double constant) {
    return new PlusMult(constant);
}
