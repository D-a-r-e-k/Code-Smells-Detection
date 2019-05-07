void method0() { 
/**
	Little trick to allow for "aliasing", that is, renaming this class.
	Writing code like
	<p>
	<tt>Functions.chain(Functions.plus,Functions.sin,Functions.chain(Functions.square,Functions.cos));</tt>
	<p>
	is a bit awkward, to say the least.
	Using the aliasing you can instead write
	<p>
	<tt>Functions F = Functions.functions; <br>
	F.chain(F.plus,F.sin,F.chain(F.square,F.cos));</tt>
	*/
public static final Functions functions = new Functions();
/*****************************
	 * <H3>Unary functions</H3>
	 *****************************/
/**
	 * Function that returns <tt>Math.abs(a)</tt>.
	 */
public static final DoubleFunction abs = new DoubleFunction() {

    public final double apply(double a) {
        return Math.abs(a);
    }
};
/**
	 * Function that returns <tt>Math.acos(a)</tt>.
	 */
public static final DoubleFunction acos = new DoubleFunction() {

    public final double apply(double a) {
        return Math.acos(a);
    }
};
/**
	 * Function that returns <tt>com.imsl.math.Sfun.acosh(a)</tt>.
	 */
/*
	public static final DoubleFunction acosh = new DoubleFunction() {
		public final double apply(double a) { return Sfun.acosh(a); }
	};
	*/
/**
	 * Function that returns <tt>Math.asin(a)</tt>.
	 */
public static final DoubleFunction asin = new DoubleFunction() {

    public final double apply(double a) {
        return Math.asin(a);
    }
};
/**
	 * Function that returns <tt>com.imsl.math.Sfun.asinh(a)</tt>.
	 */
/*
	public static final DoubleFunction asinh = new DoubleFunction() {
		public final double apply(double a) { return Sfun.asinh(a); }
	};
	*/
/**
	 * Function that returns <tt>Math.atan(a)</tt>.
	 */
public static final DoubleFunction atan = new DoubleFunction() {

    public final double apply(double a) {
        return Math.atan(a);
    }
};
/**
	 * Function that returns <tt>com.imsl.math.Sfun.atanh(a)</tt>.
	 */
/*
	public static final DoubleFunction atanh = new DoubleFunction() {
		public final double apply(double a) { return Sfun.atanh(a); }
	};
	*/
/**
	 * Function that returns <tt>Math.ceil(a)</tt>.
	 */
public static final DoubleFunction ceil = new DoubleFunction() {

    public final double apply(double a) {
        return Math.ceil(a);
    }
};
/**
	 * Function that returns <tt>Math.cos(a)</tt>.
	 */
public static final DoubleFunction cos = new DoubleFunction() {

    public final double apply(double a) {
        return Math.cos(a);
    }
};
/**
	 * Function that returns <tt>com.imsl.math.Sfun.cosh(a)</tt>.
	 */
/*
	public static final DoubleFunction cosh = new DoubleFunction() {
		public final double apply(double a) { return Sfun.cosh(a); }
	};
	*/
/**
	 * Function that returns <tt>com.imsl.math.Sfun.cot(a)</tt>.
	 */
/*
	public static final DoubleFunction cot = new DoubleFunction() {
		public final double apply(double a) { return Sfun.cot(a); }
	};
	*/
/**
	 * Function that returns <tt>com.imsl.math.Sfun.erf(a)</tt>.
	 */
/*
	public static final DoubleFunction erf = new DoubleFunction() {
		public final double apply(double a) { return Sfun.erf(a); }
	};
	*/
/**
	 * Function that returns <tt>com.imsl.math.Sfun.erfc(a)</tt>.
	 */
/*
	public static final DoubleFunction erfc = new DoubleFunction() {
		public final double apply(double a) { return Sfun.erfc(a); }
	};
	*/
/**
	 * Function that returns <tt>Math.exp(a)</tt>.
	 */
public static final DoubleFunction exp = new DoubleFunction() {

    public final double apply(double a) {
        return Math.exp(a);
    }
};
/**
	 * Function that returns <tt>Math.floor(a)</tt>.
	 */
public static final DoubleFunction floor = new DoubleFunction() {

    public final double apply(double a) {
        return Math.floor(a);
    }
};
/**
	 * Function that returns <tt>com.imsl.math.Sfun.gamma(a)</tt>.
	 */
/*
	public static final DoubleFunction gamma = new DoubleFunction() {
		public final double apply(double a) { return Sfun.gamma(a); }
	};
	*/
/**
	 * Function that returns its argument.
	 */
public static final DoubleFunction identity = new DoubleFunction() {

    public final double apply(double a) {
        return a;
    }
};
/**
	 * Function that returns <tt>1.0 / a</tt>.
	 */
public static final DoubleFunction inv = new DoubleFunction() {

    public final double apply(double a) {
        return 1.0 / a;
    }
};
/**
	 * Function that returns <tt>Math.log(a)</tt>.
	 */
public static final DoubleFunction log = new DoubleFunction() {

    public final double apply(double a) {
        return Math.log(a);
    }
};
/**
	 * Function that returns <tt>com.imsl.math.Sfun.log10(a)</tt>.
	 */
/*
	public static final DoubleFunction log10 = new DoubleFunction() {
		public final double apply(double a) { return Sfun.log10(a); }
	};
	*/
/**
	 * Function that returns <tt>Math.log(a) / Math.log(2)</tt>.
	 */
public static final DoubleFunction log2 = new DoubleFunction() {

    // 1.0 / Math.log(2) == 1.4426950408889634  
    public final double apply(double a) {
        return Math.log(a) * 1.4426950408889634;
    }
};
/**
	 * Function that returns <tt>com.imsl.math.Sfun.logGamma(a)</tt>.
	 */
/*
	public static final DoubleFunction logGamma = new DoubleFunction() {
		public final double apply(double a) { return Sfun.logGamma(a); }
	};
	*/
/**
	 * Function that returns <tt>-a</tt>.
	 */
public static final DoubleFunction neg = new DoubleFunction() {

    public final double apply(double a) {
        return -a;
    }
};
/**
	 * Function that returns <tt>Math.rint(a)</tt>.
	 */
public static final DoubleFunction rint = new DoubleFunction() {

    public final double apply(double a) {
        return Math.rint(a);
    }
};
/**
	 * Function that returns <tt>a < 0 ? -1 : a > 0 ? 1 : 0</tt>.
	 */
public static final DoubleFunction sign = new DoubleFunction() {

    public final double apply(double a) {
        return a < 0 ? -1 : a > 0 ? 1 : 0;
    }
};
/**
	 * Function that returns <tt>Math.sin(a)</tt>.
	 */
public static final DoubleFunction sin = new DoubleFunction() {

    public final double apply(double a) {
        return Math.sin(a);
    }
};
/**
	 * Function that returns <tt>com.imsl.math.Sfun.sinh(a)</tt>.
	 */
/*
	public static final DoubleFunction sinh = new DoubleFunction() {
		public final double apply(double a) { return Sfun.sinh(a); }
	};
	*/
/**
	 * Function that returns <tt>Math.sqrt(a)</tt>.
	 */
public static final DoubleFunction sqrt = new DoubleFunction() {

    public final double apply(double a) {
        return Math.sqrt(a);
    }
};
/**
	 * Function that returns <tt>a * a</tt>.
	 */
public static final DoubleFunction square = new DoubleFunction() {

    public final double apply(double a) {
        return a * a;
    }
};
/**
	 * Function that returns <tt>Math.tan(a)</tt>.
	 */
public static final DoubleFunction tan = new DoubleFunction() {

    public final double apply(double a) {
        return Math.tan(a);
    }
};
/**
	 * Function that returns <tt>com.imsl.math.Sfun.tanh(a)</tt>.
	 */
/*
	public static final DoubleFunction tanh = new DoubleFunction() {
		public final double apply(double a) { return Sfun.tanh(a); }
	};
	*/
/**
	 * Function that returns <tt>Math.toDegrees(a)</tt>.
	 */
/*
	public static final DoubleFunction toDegrees = new DoubleFunction() {
		public final double apply(double a) { return Math.toDegrees(a); }
	};
	*/
/**
	 * Function that returns <tt>Math.toRadians(a)</tt>.
	 */
/*
	public static final DoubleFunction toRadians = new DoubleFunction() {
		public final double apply(double a) { return Math.toRadians(a); }
	};		
	*/
/*****************************
	 * <H3>Binary functions</H3>
	 *****************************/
/**
	 * Function that returns <tt>Math.atan2(a,b)</tt>.
	 */
public static final DoubleDoubleFunction atan2 = new DoubleDoubleFunction() {

    public final double apply(double a, double b) {
        return Math.atan2(a, b);
    }
};
/**
	 * Function that returns <tt>com.imsl.math.Sfun.logBeta(a,b)</tt>.
	 */
/*
	public static final DoubleDoubleFunction logBeta = new DoubleDoubleFunction() {
		public final double apply(double a, double b) { return Sfun.logBeta(a,b); }
	};
	*/
/**
	 * Function that returns <tt>a < b ? -1 : a > b ? 1 : 0</tt>.
	 */
public static final DoubleDoubleFunction compare = new DoubleDoubleFunction() {

    public final double apply(double a, double b) {
        return a < b ? -1 : a > b ? 1 : 0;
    }
};
/**
	 * Function that returns <tt>a / b</tt>.
	 */
public static final DoubleDoubleFunction div = new DoubleDoubleFunction() {

    public final double apply(double a, double b) {
        return a / b;
    }
};
/**
	 * Function that returns <tt>a == b ? 1 : 0</tt>.
	 */
public static final DoubleDoubleFunction equals = new DoubleDoubleFunction() {

    public final double apply(double a, double b) {
        return a == b ? 1 : 0;
    }
};
/**
	 * Function that returns <tt>a > b ? 1 : 0</tt>.
	 */
public static final DoubleDoubleFunction greater = new DoubleDoubleFunction() {

    public final double apply(double a, double b) {
        return a > b ? 1 : 0;
    }
};
/**
	 * Function that returns <tt>Math.IEEEremainder(a,b)</tt>.
	 */
public static final DoubleDoubleFunction IEEEremainder = new DoubleDoubleFunction() {

    public final double apply(double a, double b) {
        return Math.IEEEremainder(a, b);
    }
};
/**
	 * Function that returns <tt>a == b</tt>.
	 */
public static final DoubleDoubleProcedure isEqual = new DoubleDoubleProcedure() {

    public final boolean apply(double a, double b) {
        return a == b;
    }
};
/**
	 * Function that returns <tt>a < b</tt>.
	 */
public static final DoubleDoubleProcedure isLess = new DoubleDoubleProcedure() {

    public final boolean apply(double a, double b) {
        return a < b;
    }
};
/**
	 * Function that returns <tt>a > b</tt>.
	 */
public static final DoubleDoubleProcedure isGreater = new DoubleDoubleProcedure() {

    public final boolean apply(double a, double b) {
        return a > b;
    }
};
/**
	 * Function that returns <tt>a < b ? 1 : 0</tt>.
	 */
public static final DoubleDoubleFunction less = new DoubleDoubleFunction() {

    public final double apply(double a, double b) {
        return a < b ? 1 : 0;
    }
};
/**
	 * Function that returns <tt>Math.log(a) / Math.log(b)</tt>.
	 */
public static final DoubleDoubleFunction lg = new DoubleDoubleFunction() {

    public final double apply(double a, double b) {
        return Math.log(a) / Math.log(b);
    }
};
/**
	 * Function that returns <tt>Math.max(a,b)</tt>.
	 */
public static final DoubleDoubleFunction max = new DoubleDoubleFunction() {

    public final double apply(double a, double b) {
        return Math.max(a, b);
    }
};
/**
	 * Function that returns <tt>Math.min(a,b)</tt>.
	 */
public static final DoubleDoubleFunction min = new DoubleDoubleFunction() {

    public final double apply(double a, double b) {
        return Math.min(a, b);
    }
};
/**
	 * Function that returns <tt>a - b</tt>.
	 */
public static final DoubleDoubleFunction minus = plusMult(-1);
/*
	new DoubleDoubleFunction() {
		public final double apply(double a, double b) { return a - b; }
	};
	*/
/**
	 * Function that returns <tt>a % b</tt>.
	 */
public static final DoubleDoubleFunction mod = new DoubleDoubleFunction() {

    public final double apply(double a, double b) {
        return a % b;
    }
};
/**
	 * Function that returns <tt>a * b</tt>.
	 */
public static final DoubleDoubleFunction mult = new DoubleDoubleFunction() {

    public final double apply(double a, double b) {
        return a * b;
    }
};
/**
	 * Function that returns <tt>a + b</tt>.
	 */
public static final DoubleDoubleFunction plus = plusMult(1);
/*
	new DoubleDoubleFunction() {
		public final double apply(double a, double b) { return a + b; }
	};
	*/
/**
	 * Function that returns <tt>Math.abs(a) + Math.abs(b)</tt>.
	 */
public static final DoubleDoubleFunction plusAbs = new DoubleDoubleFunction() {

    public final double apply(double a, double b) {
        return Math.abs(a) + Math.abs(b);
    }
};
/**
	 * Function that returns <tt>Math.pow(a,b)</tt>.
	 */
public static final DoubleDoubleFunction pow = new DoubleDoubleFunction() {

    public final double apply(double a, double b) {
        return Math.pow(a, b);
    }
};
}
