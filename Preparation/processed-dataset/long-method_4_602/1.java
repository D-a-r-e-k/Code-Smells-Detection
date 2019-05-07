/**
Nonsymmetric reduction from Hessenberg to real Schur form.
*/
private void hqr2() {
    //  This is derived from the Algol procedure hqr2,  
    //  by Martin and Wilkinson, Handbook for Auto. Comp.,  
    //  Vol.ii-Linear Algebra, and the corresponding  
    //  Fortran subroutine in EISPACK.  
    // Initialize  
    int nn = this.n;
    int n = nn - 1;
    int low = 0;
    int high = nn - 1;
    double eps = Math.pow(2.0, -52.0);
    double exshift = 0.0;
    double p = 0, q = 0, r = 0, s = 0, z = 0, t, w, x, y;
    // Store roots isolated by balanc and compute matrix norm  
    double norm = 0.0;
    for (int i = 0; i < nn; i++) {
        if (i < low | i > high) {
            d[i] = H[i][i];
            e[i] = 0.0;
        }
        for (int j = Math.max(i - 1, 0); j < nn; j++) {
            norm = norm + Math.abs(H[i][j]);
        }
    }
    // Outer loop over eigenvalue index  
    int iter = 0;
    while (n >= low) {
        // Look for single small sub-diagonal element  
        int l = n;
        while (l > low) {
            s = Math.abs(H[l - 1][l - 1]) + Math.abs(H[l][l]);
            if (s == 0.0) {
                s = norm;
            }
            if (Math.abs(H[l][l - 1]) < eps * s) {
                break;
            }
            l--;
        }
        // Check for convergence  
        // One root found  
        if (l == n) {
            H[n][n] = H[n][n] + exshift;
            d[n] = H[n][n];
            e[n] = 0.0;
            n--;
            iter = 0;
        } else if (l == n - 1) {
            w = H[n][n - 1] * H[n - 1][n];
            p = (H[n - 1][n - 1] - H[n][n]) / 2.0;
            q = p * p + w;
            z = Math.sqrt(Math.abs(q));
            H[n][n] = H[n][n] + exshift;
            H[n - 1][n - 1] = H[n - 1][n - 1] + exshift;
            x = H[n][n];
            // Real pair  
            if (q >= 0) {
                if (p >= 0) {
                    z = p + z;
                } else {
                    z = p - z;
                }
                d[n - 1] = x + z;
                d[n] = d[n - 1];
                if (z != 0.0) {
                    d[n] = x - w / z;
                }
                e[n - 1] = 0.0;
                e[n] = 0.0;
                x = H[n][n - 1];
                s = Math.abs(x) + Math.abs(z);
                p = x / s;
                q = z / s;
                r = Math.sqrt(p * p + q * q);
                p = p / r;
                q = q / r;
                // Row modification  
                for (int j = n - 1; j < nn; j++) {
                    z = H[n - 1][j];
                    H[n - 1][j] = q * z + p * H[n][j];
                    H[n][j] = q * H[n][j] - p * z;
                }
                // Column modification  
                for (int i = 0; i <= n; i++) {
                    z = H[i][n - 1];
                    H[i][n - 1] = q * z + p * H[i][n];
                    H[i][n] = q * H[i][n] - p * z;
                }
                // Accumulate transformations  
                for (int i = low; i <= high; i++) {
                    z = V[i][n - 1];
                    V[i][n - 1] = q * z + p * V[i][n];
                    V[i][n] = q * V[i][n] - p * z;
                }
            } else {
                d[n - 1] = x + p;
                d[n] = x + p;
                e[n - 1] = z;
                e[n] = -z;
            }
            n = n - 2;
            iter = 0;
        } else {
            // Form shift  
            x = H[n][n];
            y = 0.0;
            w = 0.0;
            if (l < n) {
                y = H[n - 1][n - 1];
                w = H[n][n - 1] * H[n - 1][n];
            }
            // Wilkinson's original ad hoc shift  
            if (iter == 10) {
                exshift += x;
                for (int i = low; i <= n; i++) {
                    H[i][i] -= x;
                }
                s = Math.abs(H[n][n - 1]) + Math.abs(H[n - 1][n - 2]);
                x = y = 0.75 * s;
                w = -0.4375 * s * s;
            }
            // MATLAB's new ad hoc shift  
            if (iter == 30) {
                s = (y - x) / 2.0;
                s = s * s + w;
                if (s > 0) {
                    s = Math.sqrt(s);
                    if (y < x) {
                        s = -s;
                    }
                    s = x - w / ((y - x) / 2.0 + s);
                    for (int i = low; i <= n; i++) {
                        H[i][i] -= s;
                    }
                    exshift += s;
                    x = y = w = 0.964;
                }
            }
            iter = iter + 1;
            // (Could check iteration count here.)  
            // Look for two consecutive small sub-diagonal elements  
            int m = n - 2;
            while (m >= l) {
                z = H[m][m];
                r = x - z;
                s = y - z;
                p = (r * s - w) / H[m + 1][m] + H[m][m + 1];
                q = H[m + 1][m + 1] - z - r - s;
                r = H[m + 2][m + 1];
                s = Math.abs(p) + Math.abs(q) + Math.abs(r);
                p = p / s;
                q = q / s;
                r = r / s;
                if (m == l) {
                    break;
                }
                if (Math.abs(H[m][m - 1]) * (Math.abs(q) + Math.abs(r)) < eps * (Math.abs(p) * (Math.abs(H[m - 1][m - 1]) + Math.abs(z) + Math.abs(H[m + 1][m + 1])))) {
                    break;
                }
                m--;
            }
            for (int i = m + 2; i <= n; i++) {
                H[i][i - 2] = 0.0;
                if (i > m + 2) {
                    H[i][i - 3] = 0.0;
                }
            }
            // Double QR step involving rows l:n and columns m:n  
            for (int k = m; k <= n - 1; k++) {
                boolean notlast = (k != n - 1);
                if (k != m) {
                    p = H[k][k - 1];
                    q = H[k + 1][k - 1];
                    r = (notlast ? H[k + 2][k - 1] : 0.0);
                    x = Math.abs(p) + Math.abs(q) + Math.abs(r);
                    if (x != 0.0) {
                        p = p / x;
                        q = q / x;
                        r = r / x;
                    }
                }
                if (x == 0.0) {
                    break;
                }
                s = Math.sqrt(p * p + q * q + r * r);
                if (p < 0) {
                    s = -s;
                }
                if (s != 0) {
                    if (k != m) {
                        H[k][k - 1] = -s * x;
                    } else if (l != m) {
                        H[k][k - 1] = -H[k][k - 1];
                    }
                    p = p + s;
                    x = p / s;
                    y = q / s;
                    z = r / s;
                    q = q / p;
                    r = r / p;
                    // Row modification  
                    for (int j = k; j < nn; j++) {
                        p = H[k][j] + q * H[k + 1][j];
                        if (notlast) {
                            p = p + r * H[k + 2][j];
                            H[k + 2][j] = H[k + 2][j] - p * z;
                        }
                        H[k][j] = H[k][j] - p * x;
                        H[k + 1][j] = H[k + 1][j] - p * y;
                    }
                    // Column modification  
                    for (int i = 0; i <= Math.min(n, k + 3); i++) {
                        p = x * H[i][k] + y * H[i][k + 1];
                        if (notlast) {
                            p = p + z * H[i][k + 2];
                            H[i][k + 2] = H[i][k + 2] - p * r;
                        }
                        H[i][k] = H[i][k] - p;
                        H[i][k + 1] = H[i][k + 1] - p * q;
                    }
                    // Accumulate transformations  
                    for (int i = low; i <= high; i++) {
                        p = x * V[i][k] + y * V[i][k + 1];
                        if (notlast) {
                            p = p + z * V[i][k + 2];
                            V[i][k + 2] = V[i][k + 2] - p * r;
                        }
                        V[i][k] = V[i][k] - p;
                        V[i][k + 1] = V[i][k + 1] - p * q;
                    }
                }
            }
        }
    }
    // while (n >= low)  
    // Backsubstitute to find vectors of upper triangular form  
    if (norm == 0.0) {
        return;
    }
    for (n = nn - 1; n >= 0; n--) {
        p = d[n];
        q = e[n];
        // Real vector  
        if (q == 0) {
            int l = n;
            H[n][n] = 1.0;
            for (int i = n - 1; i >= 0; i--) {
                w = H[i][i] - p;
                r = 0.0;
                for (int j = l; j <= n; j++) {
                    r = r + H[i][j] * H[j][n];
                }
                if (e[i] < 0.0) {
                    z = w;
                    s = r;
                } else {
                    l = i;
                    if (e[i] == 0.0) {
                        if (w != 0.0) {
                            H[i][n] = -r / w;
                        } else {
                            H[i][n] = -r / (eps * norm);
                        }
                    } else {
                        x = H[i][i + 1];
                        y = H[i + 1][i];
                        q = (d[i] - p) * (d[i] - p) + e[i] * e[i];
                        t = (x * s - z * r) / q;
                        H[i][n] = t;
                        if (Math.abs(x) > Math.abs(z)) {
                            H[i + 1][n] = (-r - w * t) / x;
                        } else {
                            H[i + 1][n] = (-s - y * t) / z;
                        }
                    }
                    // Overflow control  
                    t = Math.abs(H[i][n]);
                    if ((eps * t) * t > 1) {
                        for (int j = i; j <= n; j++) {
                            H[j][n] = H[j][n] / t;
                        }
                    }
                }
            }
        } else if (q < 0) {
            int l = n - 1;
            // Last vector component imaginary so matrix is triangular  
            if (Math.abs(H[n][n - 1]) > Math.abs(H[n - 1][n])) {
                H[n - 1][n - 1] = q / H[n][n - 1];
                H[n - 1][n] = -(H[n][n] - p) / H[n][n - 1];
            } else {
                cdiv(0.0, -H[n - 1][n], H[n - 1][n - 1] - p, q);
                H[n - 1][n - 1] = cdivr;
                H[n - 1][n] = cdivi;
            }
            H[n][n - 1] = 0.0;
            H[n][n] = 1.0;
            for (int i = n - 2; i >= 0; i--) {
                double ra, sa, vr, vi;
                ra = 0.0;
                sa = 0.0;
                for (int j = l; j <= n; j++) {
                    ra = ra + H[i][j] * H[j][n - 1];
                    sa = sa + H[i][j] * H[j][n];
                }
                w = H[i][i] - p;
                if (e[i] < 0.0) {
                    z = w;
                    r = ra;
                    s = sa;
                } else {
                    l = i;
                    if (e[i] == 0) {
                        cdiv(-ra, -sa, w, q);
                        H[i][n - 1] = cdivr;
                        H[i][n] = cdivi;
                    } else {
                        // Solve complex equations  
                        x = H[i][i + 1];
                        y = H[i + 1][i];
                        vr = (d[i] - p) * (d[i] - p) + e[i] * e[i] - q * q;
                        vi = (d[i] - p) * 2.0 * q;
                        if (vr == 0.0 & vi == 0.0) {
                            vr = eps * norm * (Math.abs(w) + Math.abs(q) + Math.abs(x) + Math.abs(y) + Math.abs(z));
                        }
                        cdiv(x * r - z * ra + q * sa, x * s - z * sa - q * ra, vr, vi);
                        H[i][n - 1] = cdivr;
                        H[i][n] = cdivi;
                        if (Math.abs(x) > (Math.abs(z) + Math.abs(q))) {
                            H[i + 1][n - 1] = (-ra - w * H[i][n - 1] + q * H[i][n]) / x;
                            H[i + 1][n] = (-sa - w * H[i][n] - q * H[i][n - 1]) / x;
                        } else {
                            cdiv(-r - y * H[i][n - 1], -s - y * H[i][n], z, q);
                            H[i + 1][n - 1] = cdivr;
                            H[i + 1][n] = cdivi;
                        }
                    }
                    // Overflow control  
                    t = Math.max(Math.abs(H[i][n - 1]), Math.abs(H[i][n]));
                    if ((eps * t) * t > 1) {
                        for (int j = i; j <= n; j++) {
                            H[j][n - 1] = H[j][n - 1] / t;
                            H[j][n] = H[j][n] / t;
                        }
                    }
                }
            }
        }
    }
    // Vectors of isolated roots  
    for (int i = 0; i < nn; i++) {
        if (i < low | i > high) {
            for (int j = i; j < nn; j++) {
                V[i][j] = H[i][j];
            }
        }
    }
    // Back transformation to get eigenvectors of original matrix  
    for (int j = nn - 1; j >= low; j--) {
        for (int i = low; i <= high; i++) {
            z = 0.0;
            for (int k = low; k <= Math.min(j, high); k++) {
                z = z + V[i][k] * H[k][j];
            }
            V[i][j] = z;
        }
    }
}
