/** calculates the minimum for the paint area.
	 *
	 */
protected Point findMinimumAndSpacing(CellView[] graphCellViews, Point spacing) {
    try {
        // variables 
        /* represents the minimum x value for the paint area
			 */
        int min_x = 1000000;
        /* represents the minimum y value for the paint area
			 */
        int min_y = 1000000;
        // find the maximum & minimum coordinates 
        for (int i = 0; i < graphCellViews.length; i++) {
            // the cellView and their bounds 
            CellView cellView = graphCellViews[i];
            if (cellView == null)
                continue;
            Rectangle2D rect = cellView.getBounds();
            Rectangle cellViewBounds = new Rectangle((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
            // checking min area 
            try {
                if (cellViewBounds.x < min_x)
                    min_x = cellViewBounds.x;
                if (cellViewBounds.y < min_y)
                    min_y = cellViewBounds.y;
            } catch (Exception e) {
                System.err.println("---------> ERROR in calculateValues.");
                e.printStackTrace();
            }
        }
        // if the cell sice is bigger than the userspacing 
        // dublicate the spacingfactor 
        return new Point(min_x, min_y);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
