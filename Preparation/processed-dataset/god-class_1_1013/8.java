/**
 * Constructs a function that returns the constant <tt>c</tt>.
 */
public static DoubleFunction constant(final double c) {
    return new DoubleFunction() {

        public final double apply(double a) {
            return c;
        }
    };
}
