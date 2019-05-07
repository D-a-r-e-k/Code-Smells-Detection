protected int offset(int p1, int p2, int a1, int a2, int b1, int b2) {
    int d, s, t;
    if (b1 <= p1 || p1 + a1 <= 0) {
        return 0;
    }
    t = b1 * a2 - a1 * b2;
    if (t > 0) {
        if (p1 < 0) {
            s = p1 * a2;
            d = s / a1 - p2;
        } else if (p1 > 0) {
            s = p1 * b2;
            d = s / b1 - p2;
        } else {
            d = -p2;
        }
    } else if (b1 < p1 + a1) {
        s = (b1 - p1) * a2;
        d = b2 - (p2 + s / a1);
    } else if (b1 > p1 + a1) {
        s = (a1 + p1) * b2;
        d = s / b1 - (p2 + a2);
    } else {
        d = b2 - (p2 + a2);
    }
    if (d > 0) {
        return d;
    }
    return 0;
}
