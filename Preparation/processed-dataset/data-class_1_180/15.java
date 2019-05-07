/**
 * Constructs a function that returns <tt>from<=a && a<=to</tt>.
 * <tt>a</tt> is a variable, <tt>from</tt> and <tt>to</tt> are fixed.
 */
public static DoubleProcedure isBetween(final double from, final double to) {
    return new DoubleProcedure() {

        public final boolean apply(double a) {
            return from <= a && a <= to;
        }
    };
}
