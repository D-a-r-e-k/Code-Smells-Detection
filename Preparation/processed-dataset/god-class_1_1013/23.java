/**
 * Constructs a function that returns <tt>Math.min(a,b)</tt>.
 * <tt>a</tt> is a variable, <tt>b</tt> is fixed.
 */
public static DoubleFunction min(final double b) {
    return new DoubleFunction() {

        public final double apply(double a) {
            return Math.min(a, b);
        }
    };
}
