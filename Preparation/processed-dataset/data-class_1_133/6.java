protected void layout(TreeLayoutNode t) {
    TreeLayoutNode c;
    if (t == null) {
        return;
    }
    c = t.child;
    while (c != null) {
        layout(c);
        c = c.sibling;
    }
    if (t.child != null) {
        attachParent(t, join(t));
    } else {
        layoutLeaf(t);
    }
}
