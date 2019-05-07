/**
 * Constructs a function that returns <tt>(from<=a && a<=to) ? 1 : 0</tt>.
 * <tt>a</tt> is a variable, <tt>from</tt> and <tt>to</tt> are fixed.
 */
public static DoubleFunction between(final double from, final double to) {
    return new DoubleFunction() {

        public final double apply(double a) {
            return (from <= a && a <= to) ? 1 : 0;
        }
    };
}
