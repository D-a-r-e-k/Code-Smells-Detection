/** This Method draws the graph. For the horizontal position
	 *  we are using the grid position from each graphcell.
	 *  For the vertical position we are using the level position.
	 *
	 */
protected void drawGraph(JGraph jgraph, List levels, Point min, Point spacing) {
    // paint the graph 
    Map viewMap = new Hashtable();
    for (int rowCellCount = 0; rowCellCount < levels.size(); rowCellCount++) {
        List level = (List) levels.get(rowCellCount);
        for (int colCellCount = 0; colCellCount < level.size(); colCellCount++) {
            CellWrapper wrapper = (CellWrapper) level.get(colCellCount);
            VertexView view = wrapper.vertexView;
            // remove the temp objects 
            /* While the Algorithm is running we are putting some
				 *  attributeNames to the MyGraphCells. This method
				 *  cleans this objects from the MyGraphCells.
				 *
				 */
            view.getAttributes().remove(SUGIYAMA_CELL_WRAPPER);
            view.getAttributes().remove(SUGIYAMA_VISITED);
            wrapper.vertexView = null;
            // get the bounds from the cellView 
            if (view == null)
                continue;
            Rectangle2D rect = (Rectangle2D) view.getBounds().clone();
            Rectangle bounds = new Rectangle((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
            //(Rectangle) view.getBounds().clone(); 
            // adjust 
            bounds.x = min.x + spacing.x * ((vertical) ? wrapper.getGridPosition() : rowCellCount);
            bounds.y = min.y + spacing.y * ((vertical) ? rowCellCount : wrapper.getGridPosition());
            Object cell = view.getCell();
            Map map = new Hashtable();
            GraphConstants.setBounds(map, (Rectangle2D) bounds.clone());
            viewMap.put(cell, map);
        }
    }
    jgraph.getGraphLayoutCache().edit(viewMap, null, null, null);
}
