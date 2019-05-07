protected PolyLine bridge(PolyLine line1, int x1, int y1, PolyLine line2, int x2, int y2) {
    int dy, dx, s;
    PolyLine r;
    dx = x2 + line2.dx - x1;
    if (line2.dx == 0) {
        dy = line2.dy;
    } else {
        s = dx * line2.dy;
        dy = s / line2.dx;
    }
    r = new PolyLine(dx, dy, line2.link);
    line1.link = new PolyLine(0, y2 + line2.dy - dy - y1, r);
    return r;
}
