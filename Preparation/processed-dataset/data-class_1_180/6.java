/**
 * Constructs the function <tt>g( h(a) )</tt>.
 * 
 * @param g a unary function.
 * @param h a unary function.
 * @return the unary function <tt>g( h(a) )</tt>.
 */
public static DoubleFunction chain(final DoubleFunction g, final DoubleFunction h) {
    return new DoubleFunction() {

        public final double apply(double a) {
            return g.apply(h.apply(a));
        }
    };
}
