/******************************************************************************/
private void showCellList(ArrayList list, Color color) {
    Map viewMap = new Hashtable();
    for (int i = 0; i < list.size(); i++) {
        CellView view = (CellView) list.get(i);
        Point2D.Double pos = getPosition(i, list);
        Rectangle2D r = view.getBounds();
        r.setFrame(pos.getX() - r.getWidth() / 2.0, pos.getY() - r.getHeight() / 2.0, r.getWidth(), r.getHeight());
        Object cell = view.getCell();
        Map attributes = new Hashtable();
        GraphConstants.setBackground(attributes, color);
        GraphConstants.setBounds(attributes, r);
        viewMap.put(cell, attributes);
    }
    jgraph.getGraphLayoutCache().edit(viewMap, null, null, null);
}
