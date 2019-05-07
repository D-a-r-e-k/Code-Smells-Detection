/** Subdivide the curve which defines this tube to the specified tolerance. */
public Tube subdivideTube(double tol) {
    if (vertex.length < 3)
        return this;
    if (smoothingMethod == INTERPOLATING)
        return subdivideTubeInterp(tol);
    if (smoothingMethod == APPROXIMATING)
        return subdivideTubeApprox(tol);
    return this;
}
