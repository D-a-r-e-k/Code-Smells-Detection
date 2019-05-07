/**
 * Demonstrates usage of this class.
 */
public static void demo1() {
    cern.jet.math.Functions F = cern.jet.math.Functions.functions;
    double a = 0.5;
    double b = 0.2;
    double v = Math.sin(a) + Math.pow(Math.cos(b), 2);
    System.out.println(v);
    DoubleDoubleFunction f = F.chain(F.plus, F.sin, F.chain(F.square, F.cos));
    //DoubleDoubleFunction f = F.chain(plus,sin,F.chain(square,cos));  
    System.out.println(f.apply(a, b));
    DoubleDoubleFunction g = new DoubleDoubleFunction() {

        public final double apply(double x, double y) {
            return Math.sin(x) + Math.pow(Math.cos(y), 2);
        }
    };
    System.out.println(g.apply(a, b));
    DoubleFunction m = F.plus(3);
    DoubleFunction n = F.plus(4);
    System.out.println(m.apply(0));
    System.out.println(n.apply(0));
}
