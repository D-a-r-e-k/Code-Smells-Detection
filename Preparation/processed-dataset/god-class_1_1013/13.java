/**
 * Constructs a function that returns <tt>a > b ? 1 : 0</tt>.
 * <tt>a</tt> is a variable, <tt>b</tt> is fixed.
 */
public static DoubleFunction greater(final double b) {
    return new DoubleFunction() {

        public final double apply(double a) {
            return a > b ? 1 : 0;
        }
    };
}
