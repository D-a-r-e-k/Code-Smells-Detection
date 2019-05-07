/**
 * Constructs a function that returns <tt>a % b</tt>.
 * <tt>a</tt> is a variable, <tt>b</tt> is fixed.
 */
public static DoubleFunction mod(final double b) {
    return new DoubleFunction() {

        public final double apply(double a) {
            return a % b;
        }
    };
}
