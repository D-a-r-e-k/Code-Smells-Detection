void method0() { 
static final long serialVersionUID = 1020;
/** Row and column dimension (square matrix).
	@serial matrix dimension.
	*/
private int n;
/** Symmetry flag.
	@serial internal symmetry flag.
	*/
private boolean issymmetric;
/** Arrays for internal storage of eigenvalues.
	@serial internal storage of eigenvalues.
	*/
private double[] d, e;
/** Array for internal storage of eigenvectors.
	@serial internal storage of eigenvectors.
	*/
private double[][] V;
/** Array for internal storage of nonsymmetric Hessenberg form.
	@serial internal storage of nonsymmetric Hessenberg form.
	*/
private double[][] H;
/** Working storage for nonsymmetric algorithm.
	@serial working storage for nonsymmetric algorithm.
	*/
private double[] ort;
// Complex scalar division.  
private transient double cdivr, cdivi;
}
