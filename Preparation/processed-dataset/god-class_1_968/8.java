protected void layoutLeaf(TreeLayoutNode t) {
    t.contour.upper_tail = new PolyLine(t.width + 2 * t.border, 0, null);
    t.contour.upper_head = t.contour.upper_tail;
    t.contour.lower_tail = new PolyLine(0, -t.height - 2 * t.border, null);
    t.contour.lower_head = new PolyLine(t.width + 2 * t.border, 0, t.contour.lower_tail);
}
