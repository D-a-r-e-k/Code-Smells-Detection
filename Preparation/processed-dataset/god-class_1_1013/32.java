/**
 * Constructs a function that returns the number rounded to the given precision; <tt>Math.rint(a/precision)*precision</tt>.
 * Examples:
 * <pre>
 * precision = 0.01 rounds 0.012 --> 0.01, 0.018 --> 0.02
 * precision = 10   rounds 123   --> 120 , 127   --> 130
 * </pre>
 */
public static DoubleFunction round(final double precision) {
    return new DoubleFunction() {

        public final double apply(double a) {
            return Math.rint(a / precision) * precision;
        }
    };
}
