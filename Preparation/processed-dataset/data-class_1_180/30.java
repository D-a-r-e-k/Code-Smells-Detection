/**
 * Constructs a function that returns <tt>Math.pow(a,b)</tt>.
 * <tt>a</tt> is a variable, <tt>b</tt> is fixed.
 */
public static DoubleFunction pow(final double b) {
    return new DoubleFunction() {

        public final double apply(double a) {
            return Math.pow(a, b);
        }
    };
}
