protected void buildLayoutHelperTree(Collection roots) {
    for (Iterator iterator = roots.iterator(); iterator.hasNext(); ) {
        VertexView vv = (VertexView) iterator.next();
        decorateNode(vv);
    }
}
