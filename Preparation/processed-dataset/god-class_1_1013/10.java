/**
 * Benchmarks and demonstrates usage of trivial and complex functions.
 */
public static void demo2(int size) {
    cern.jet.math.Functions F = cern.jet.math.Functions.functions;
    System.out.println("\n\n");
    double a = 0.0;
    double b = 0.0;
    double v = Math.abs(Math.sin(a) + Math.pow(Math.cos(b), 2));
    //double v = Math.sin(a) + Math.pow(Math.cos(b),2);  
    //double v = a + b;  
    System.out.println(v);
    //DoubleDoubleFunction f = F.chain(F.plus,F.identity,F.identity);  
    DoubleDoubleFunction f = F.chain(F.abs, F.chain(F.plus, F.sin, F.chain(F.square, F.cos)));
    //DoubleDoubleFunction f = F.chain(F.plus,F.sin,F.chain(F.square,F.cos));  
    //DoubleDoubleFunction f = F.plus;  
    System.out.println(f.apply(a, b));
    DoubleDoubleFunction g = new DoubleDoubleFunction() {

        public final double apply(double x, double y) {
            return Math.abs(Math.sin(x) + Math.pow(Math.cos(y), 2));
        }
    };
    System.out.println(g.apply(a, b));
    // emptyLoop  
    cern.colt.Timer emptyLoop = new cern.colt.Timer().start();
    a = 0;
    b = 0;
    double sum = 0;
    for (int i = size; --i >= 0; ) {
        sum += a;
        a++;
        b++;
    }
    emptyLoop.stop().display();
    System.out.println("empty sum=" + sum);
    cern.colt.Timer timer = new cern.colt.Timer().start();
    a = 0;
    b = 0;
    sum = 0;
    for (int i = size; --i >= 0; ) {
        sum += Math.abs(Math.sin(a) + Math.pow(Math.cos(b), 2));
        //sum += a + b;  
        a++;
        b++;
    }
    timer.stop().display();
    System.out.println("evals / sec = " + size / timer.minus(emptyLoop).seconds());
    System.out.println("sum=" + sum);
    timer.reset().start();
    a = 0;
    b = 0;
    sum = 0;
    for (int i = size; --i >= 0; ) {
        sum += f.apply(a, b);
        a++;
        b++;
    }
    timer.stop().display();
    System.out.println("evals / sec = " + size / timer.minus(emptyLoop).seconds());
    System.out.println("sum=" + sum);
    timer.reset().start();
    a = 0;
    b = 0;
    sum = 0;
    for (int i = size; --i >= 0; ) {
        sum += g.apply(a, b);
        a++;
        b++;
    }
    timer.stop().display();
    System.out.println("evals / sec = " + size / timer.minus(emptyLoop).seconds());
    System.out.println("sum=" + sum);
}
