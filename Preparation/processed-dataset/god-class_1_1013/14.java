/**
 * Constructs a function that returns <tt>Math.IEEEremainder(a,b)</tt>.
 * <tt>a</tt> is a variable, <tt>b</tt> is fixed.
 */
public static DoubleFunction IEEEremainder(final double b) {
    return new DoubleFunction() {

        public final double apply(double a) {
            return Math.IEEEremainder(a, b);
        }
    };
}
