/**
 * Constructs the function <tt>g( h(a,b) )</tt>.
 * 
 * @param g a unary function.
 * @param h a binary function.
 * @return the unary function <tt>g( h(a,b) )</tt>.
 */
public static DoubleDoubleFunction chain(final DoubleFunction g, final DoubleDoubleFunction h) {
    return new DoubleDoubleFunction() {

        public final double apply(double a, double b) {
            return g.apply(h.apply(a, b));
        }
    };
}
