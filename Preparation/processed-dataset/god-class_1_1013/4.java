/**
 * Constructs the function <tt>f( g(a), h(b) )</tt>.
 * 
 * @param f a binary function.
 * @param g a unary function.
 * @param h a unary function.
 * @return the binary function <tt>f( g(a), h(b) )</tt>.
 */
public static DoubleDoubleFunction chain(final DoubleDoubleFunction f, final DoubleFunction g, final DoubleFunction h) {
    return new DoubleDoubleFunction() {

        public final double apply(double a, double b) {
            return f.apply(g.apply(a), h.apply(b));
        }
    };
}
