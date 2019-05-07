/**
 * Constructs a function that returns <tt>Math.max(a,b)</tt>.
 * <tt>a</tt> is a variable, <tt>b</tt> is fixed.
 */
public static DoubleFunction max(final double b) {
    return new DoubleFunction() {

        public final double apply(double a) {
            return Math.max(a, b);
        }
    };
}
