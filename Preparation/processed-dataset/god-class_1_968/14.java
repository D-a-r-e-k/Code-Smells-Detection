protected void upDownNodeLayout(TreeLayoutNode node, int off_x, int off_y) {
    TreeLayoutNode child, s;
    int siblingOffset;
    node.pos.translate(off_x + (-1 * node.offset.y), off_y + node.offset.x);
    child = node.child;
    //rightmost child 
    if (child != null) {
        upDownNodeLayout(child, node.pos.x, node.pos.y);
        s = child.sibling;
        siblingOffset = node.pos.x - child.offset.y;
        while (s != null) {
            upDownNodeLayout(s, siblingOffset, node.pos.y + child.offset.x);
            siblingOffset -= s.offset.y;
            s = s.sibling;
        }
    }
}
