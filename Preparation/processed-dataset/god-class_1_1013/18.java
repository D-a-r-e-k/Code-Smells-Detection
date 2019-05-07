/**
 * Constructs a function that returns <tt>a < b</tt>.
 * <tt>a</tt> is a variable, <tt>b</tt> is fixed.
 */
public static DoubleProcedure isLess(final double b) {
    return new DoubleProcedure() {

        public final boolean apply(double a) {
            return a < b;
        }
    };
}
