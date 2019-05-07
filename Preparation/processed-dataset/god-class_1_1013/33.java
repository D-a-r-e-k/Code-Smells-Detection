/**
 * Constructs a function that returns <tt>function.apply(b,a)</tt>, i.e. applies the function with the first operand as second operand and the second operand as first operand.
 * 
 * @param function a function taking operands in the form <tt>function.apply(a,b)</tt>.
 * @return the binary function <tt>function(b,a)</tt>.
 */
public static DoubleDoubleFunction swapArgs(final DoubleDoubleFunction function) {
    return new DoubleDoubleFunction() {

        public final double apply(double a, double b) {
            return function.apply(b, a);
        }
    };
}
