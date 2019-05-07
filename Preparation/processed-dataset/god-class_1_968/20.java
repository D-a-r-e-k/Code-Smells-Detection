protected void decorateNode(VertexView node) {
    List cl = getChildren(node);
    TreeLayoutNode parent = getTreeLayoutNode(node);
    if (cl.size() > 0) {
        //Decorate children with Moen's nodes, parent has a reference 
        //to a first child only. Each child has a reference to a parent 
        //and possible next sibling 
        for (int i = 0; i < cl.size(); i++) {
            VertexView currentChild = (VertexView) cl.get(i);
            TreeLayoutNode cln = getTreeLayoutNode(currentChild);
            if (i == 0) {
                parent.child = cln;
            } else {
                VertexView previousChild = (VertexView) cl.get(i - 1);
                TreeLayoutNode pln = getTreeLayoutNode(previousChild);
                pln.sibling = cln;
            }
            cln.parent = parent;
            decorateNode(currentChild);
        }
    }
}
