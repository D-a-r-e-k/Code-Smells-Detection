/**
 * Constructs a unary function from a binary function with the second operand (argument) fixed to the given constant <tt>c</tt>.
 * The first operand is variable (free).
 * 
 * @param function a binary function taking operands in the form <tt>function.apply(var,c)</tt>.
 * @return the unary function <tt>function(var,c)</tt>.
 */
public static DoubleFunction bindArg2(final DoubleDoubleFunction function, final double c) {
    return new DoubleFunction() {

        public final double apply(double var) {
            return function.apply(var, c);
        }
    };
}
