/* These two routines are used by subdivideTube for calculating the thickness at vertices. */
public static double calcInterpThickness(double t[], float s[], int i, int j, int k, int m) {
    double w1, w2, w3, w4;
    w1 = -0.0625 * s[j];
    w2 = 0.5 - w1;
    w4 = -0.0625 * s[k];
    w3 = 0.5 - w4;
    return (w1 * t[i] + w2 * t[j] + w3 * t[k] + w4 * t[m]);
}
