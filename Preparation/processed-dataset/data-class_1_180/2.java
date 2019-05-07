/**
 * Constructs a unary function from a binary function with the first operand (argument) fixed to the given constant <tt>c</tt>.
 * The second operand is variable (free).
 * 
 * @param function a binary function taking operands in the form <tt>function.apply(c,var)</tt>.
 * @return the unary function <tt>function(c,var)</tt>.
 */
public static DoubleFunction bindArg1(final DoubleDoubleFunction function, final double c) {
    return new DoubleFunction() {

        public final double apply(double var) {
            return function.apply(c, var);
        }
    };
}
