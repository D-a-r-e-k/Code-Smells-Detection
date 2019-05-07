public static double calcApproxThickness(double t[], float s[], int i, int j, int k) {
    double w1 = 0.125 * s[j], w2 = 1.0 - 2.0 * w1;
    return (w1 * t[i] + w2 * t[j] + w1 * t[k]);
}
