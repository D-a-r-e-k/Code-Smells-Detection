/**
	 * Returns all views, including descendants that have a parent in
	 * <code>views</code>, especially the PortViews. Note: Iterative
	 * Implementation using model.getChild and getMapping on this cell mapper.
	 */
public CellView[] getAllDescendants(CellView[] views) {
    Stack stack = new Stack();
    for (int i = 0; i < views.length; i++) if (views[i] != null)
        stack.add(views[i]);
    ArrayList result = new ArrayList();
    while (!stack.isEmpty()) {
        CellView tmp = (CellView) stack.pop();
        Object[] children = tmp.getChildViews();
        for (int i = 0; i < children.length; i++) stack.add(children[i]);
        result.add(tmp);
        // Add Port Views 
        for (int i = 0; i < graphModel.getChildCount(tmp.getCell()); i++) {
            Object child = graphModel.getChild(tmp.getCell(), i);
            if (graphModel.isPort(child)) {
                CellView view = getMapping(child, false);
                if (view != null)
                    stack.add(view);
            }
        }
    }
    CellView[] ret = new CellView[result.size()];
    result.toArray(ret);
    return ret;
}
