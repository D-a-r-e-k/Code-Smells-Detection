protected void attachParent(TreeLayoutNode t, int h) {
    final int x;
    int y1;
    final int y2;
    x = t.border + childParentDistance;
    y2 = (h - t.height) / 2 - t.border;
    y1 = y2 + t.height + 2 * t.border - h;
    t.child.offset.x = x + t.width;
    t.child.offset.y = y1;
    t.contour.upper_head = new PolyLine(t.width, 0, new PolyLine(x, y1, t.contour.upper_head));
    t.contour.lower_head = new PolyLine(t.width, 0, new PolyLine(x, y2, t.contour.lower_head));
}
