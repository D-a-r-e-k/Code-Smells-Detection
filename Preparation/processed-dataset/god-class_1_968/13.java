protected void leftRightNodeLayout(TreeLayoutNode node, int off_x, int off_y) {
    TreeLayoutNode child, s;
    int siblingOffest;
    node.pos.translate(off_x + node.offset.x, off_y + node.offset.y);
    child = node.child;
    //topmost child 
    if (child != null) {
        leftRightNodeLayout(child, node.pos.x, node.pos.y);
        s = child.sibling;
        siblingOffest = node.pos.y + child.offset.y;
        while (s != null) {
            leftRightNodeLayout(s, node.pos.x + child.offset.x, siblingOffest);
            siblingOffest += s.offset.y;
            s = s.sibling;
        }
    }
}
