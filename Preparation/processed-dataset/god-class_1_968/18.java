protected void layoutTrees(Collection roots) {
    for (Iterator iterator = roots.iterator(); iterator.hasNext(); ) {
        VertexView view = (VertexView) iterator.next();
        TreeLayoutNode root = getTreeLayoutNode(view);
        // kick off Moen's algorithm 
        layout(root);
        Rectangle2D rect = view.getBounds();
        Point rootPos = new Point((int) rect.getX(), (int) rect.getY());
        //view.getBounds().getLocation(); 
        switch(orientation) {
            case LEFT_TO_RIGHT:
                leftRightNodeLayout(root, rootPos.x, rootPos.y);
                break;
            case UP_TO_DOWN:
                upDownNodeLayout(root, rootPos.x, rootPos.y);
                break;
            default:
                leftRightNodeLayout(root, rootPos.x, rootPos.y);
        }
    }
}
