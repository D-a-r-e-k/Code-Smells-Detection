protected int join(TreeLayoutNode t) {
    TreeLayoutNode c;
    int d, h, sum;
    c = t.child;
    t.contour = c.contour;
    sum = h = c.height + 2 * c.border;
    c = c.sibling;
    while (c != null) {
        d = merge(t.contour, c.contour);
        c.offset.y = d + h;
        c.offset.x = 0;
        h = c.height + 2 * c.border;
        sum += d + h;
        c = c.sibling;
    }
    return sum;
}
