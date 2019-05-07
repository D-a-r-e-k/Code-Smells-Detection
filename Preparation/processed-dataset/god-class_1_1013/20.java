/**
 * Constructs a function that returns <tt><tt>Math.log(a) / Math.log(b)</tt></tt>.
 * <tt>a</tt> is a variable, <tt>b</tt> is fixed.
 */
public static DoubleFunction lg(final double b) {
    return new DoubleFunction() {

        private final double logInv = 1 / Math.log(b);

        // cached for speed  
        public final double apply(double a) {
            return Math.log(a) * logInv;
        }
    };
}
