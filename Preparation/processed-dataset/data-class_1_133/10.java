protected int merge(Polygon c1, Polygon c2) {
    int x, y, total, d;
    PolyLine lower, upper, b;
    x = y = total = 0;
    upper = c1.lower_head;
    lower = c2.upper_head;
    while (lower != null && upper != null) {
        d = offset(x, y, lower.dx, lower.dy, upper.dx, upper.dy);
        y += d;
        total += d;
        if (x + lower.dx <= upper.dx) {
            y += lower.dy;
            x += lower.dx;
            lower = lower.link;
        } else {
            y -= upper.dy;
            x -= upper.dx;
            upper = upper.link;
        }
    }
    if (lower != null) {
        b = bridge(c1.upper_tail, 0, 0, lower, x, y);
        c1.upper_tail = (b.link != null) ? c2.upper_tail : b;
        c1.lower_tail = c2.lower_tail;
    } else {
        b = bridge(c2.lower_tail, x, y, upper, 0, 0);
        if (b.link == null) {
            c1.lower_tail = b;
        }
    }
    c1.lower_head = c2.lower_head;
    return total;
}
