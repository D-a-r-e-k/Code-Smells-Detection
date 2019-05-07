/**
	 * Removes empty space between parts of the layout
	 * 
	 * @param model
	 */
// private void compactLayout(JGraphHierarchyModel model) { 
// // List of separate areas in layout 
// Set areas = new HashSet(); 
// for (int i = 0; i < model.ranks.size(); i++) { 
// JGraphHierarchyRank rank = (JGraphHierarchyRank) model.ranks 
// .get(new Integer(i)); 
// Iterator iter = rank.iterator(); 
// while (iter.hasNext()) { 
// JGraphAbstractHierarchyCell cell = (JGraphAbstractHierarchyCell) iter 
// .next(); 
// double positionX = 0; 
// if (cell.isVertex()) { 
// JGraphHierarchyNode node = (JGraphHierarchyNode) cell; 
// positionX = node.x[0] - node.width / 2; 
// Rectangle2D area = new Rectangle2D.Double(positionX-intraCellSpacing, 0, 
// node.width+(intraCellSpacing*2), 100000); 
// integrateNewArea(area, areas, cell); 
// } else if (cell.isEdge()) { 
// JGraphHierarchyEdge edge = (JGraphHierarchyEdge) cell; 
// // For parallel edges we need to seperate out the points a 
// // little 
// int numParallelEdges = edge.edges.size(); 
// int parallelEdgeBuffer = (int)parallelEdgeSpacing * numParallelEdges/2; 
// for (int j = edge.x.length - 1; j >= 0; j--) { 
// positionX = edge.x[j]; 
// if (orientation == SwingConstants.EAST 
// || orientation == SwingConstants.WEST) { 
// positionX = edge.y[j]; 
// } 
// Rectangle2D area = new Rectangle2D.Double(positionX-intraCellSpacing, 0, 
// parallelEdgeBuffer+(intraCellSpacing*2), 100000); 
// integrateNewArea(area, areas, cell); 
// } 
// } 
// } 
// } 
// // If there is more than one area need to compact sections 
// if (areas.size() > 1) { 
// Iterator iter = areas.iterator(); 
// while (iter.hasNext()) { 
// Rectangle2D area = (Rectangle2D)iter.next(); 
// } 
// } 
// } 
/**
	 * Adds a new rectangle to any intersecting rectangles stored in areas. If
	 * no intersection a new area is created from its values.
	 * 
	 * @param area
	 * @param areas
	 */
// private void integrateNewArea(Rectangle2D area, Set areas, 
// JGraphAbstractHierarchyCell cell) { 
// Iterator iter = areas.iterator(); 
// // Whether or not a cached area is found that contains area 
// boolean areaFound = false; 
// while (iter.hasNext()) { 
// AreaSpatialCache cachedArea = (AreaSpatialCache)iter.next(); 
// if (cachedArea.intersects(area)) { 
// cachedArea.setRect(cachedArea.createUnion(area)); 
// if (areaFound == false) { 
// cachedArea.cells.add(cell); 
// areaFound = true; 
// } 
// } 
// } 
// if (areaFound == false) { 
// // Create new area to hold cell's area 
// AreaSpatialCache newArea = new AreaSpatialCache(); 
// newArea.setRect(area.getX(), 0, area.getWidth(), 100000); 
// areas.add(newArea); 
// newArea.cells.add(cell); 
//			 
// } 
// // Check if any of the cached areas now overlap, if they do, merge them 
// Set removedAreas = new HashSet(); 
// while (iter.hasNext()) { 
// AreaSpatialCache cachedArea = (AreaSpatialCache)iter.next(); 
// // Skip area if already flagged for removal 
// if (!removedAreas.contains(cachedArea)) { 
// Iterator iter2 = areas.iterator(); 
// while (iter2.hasNext()) { 
// AreaSpatialCache cachedArea2 = (AreaSpatialCache) iter2.next(); 
// if (cachedArea.intersects(cachedArea2)) { 
// cachedArea.setRect(cachedArea.createUnion(cachedArea2)); 
// removedAreas.add(cachedArea2); 
// } 
// } 
// } 
// } 
// areas.removeAll(removedAreas); 
// } 
/**
	 * Sets the cell locations in the facade to those stored after this layout
	 * processing step has completed.
	 * 
	 * @param facade
	 *            the facade describing the input graph
	 * @param model
	 *            an internal model of the hierarchical layout
	 */
private void setCellLocations(JGraphFacade facade, JGraphHierarchyModel model) {
    // Stores any translation needs to separate this context properly 
    // from any last context 
    double contextTranslation = 0.0;
    // Store all the vertices in case we need to translate them 
    List vertices = new ArrayList();
    // Run through the ranks twice, once for vertices, then for edges 
    // The reason for this is if the vertices need to offset again from 
    // the last context 
    for (int cellType = 0; cellType < 2; cellType++) {
        for (int i = 0; i < model.ranks.size(); i++) {
            JGraphHierarchyRank rank = (JGraphHierarchyRank) model.ranks.get(new Integer(i));
            Iterator iter = rank.iterator();
            while (iter.hasNext()) {
                JGraphAbstractHierarchyCell cell = (JGraphAbstractHierarchyCell) iter.next();
                if (cellType == 0 && cell.isVertex()) {
                    JGraphHierarchyNode node = (JGraphHierarchyNode) cell;
                    Object realCell = node.cell;
                    vertices.add(realCell);
                    double positionX = node.x[0] - node.width / 2;
                    double positionY = node.y[0] - node.height / 2;
                    if (orientation == SwingConstants.NORTH || orientation == SwingConstants.SOUTH) {
                        facade.setLocation(realCell, positionX, positionY);
                    } else {
                        facade.setLocation(realCell, positionY, positionX);
                    }
                    // Stores the positive X limit of this graph context to 
                    // know where the next context can start 
                    limitX = Math.max(limitX, positionX + node.width);
                    // It is possible that a rank sticks out further in the 
                    // -ve x direction than the widest rank. In this case 
                    // store a shift that is required to apply to all cells 
                    // after positioning is complete. 
                    if (positionX + 1 < initialX) {
                        contextTranslation = initialX - positionX;
                    }
                } else if (cellType == 1 && cell.isEdge()) {
                    JGraphHierarchyEdge edge = (JGraphHierarchyEdge) cell;
                    // For parallel edges we need to seperate out the points 
                    // a 
                    // little 
                    Iterator parallelEdges = edge.edges.iterator();
                    double offsetX = 0.0;
                    // Only set the edge control points once 
                    if (edge.temp[0] != 101207) {
                        while (parallelEdges.hasNext()) {
                            Object realEdge = parallelEdges.next();
                            List oldPoints = facade.getPoints(realEdge);
                            List newPoints = new ArrayList((edge.x.length) + 2);
                            newPoints.add(oldPoints.get(0));
                            if (edge.isReversed()) {
                                // Reversed edges need the points inserted 
                                // in 
                                // reverse order 
                                for (int j = 0; j < edge.x.length; j++) {
                                    double positionX = edge.x[j] + offsetX + contextTranslation;
                                    if (orientation == SwingConstants.NORTH || orientation == SwingConstants.SOUTH) {
                                        newPoints.add(new Point2D.Double(positionX, edge.y[j]));
                                    } else {
                                        newPoints.add(new Point2D.Double(edge.y[j], positionX));
                                    }
                                    limitX = Math.max(limitX, positionX);
                                }
                                processReversedEdge(edge, realEdge);
                            } else {
                                for (int j = edge.x.length - 1; j >= 0; j--) {
                                    double positionX = edge.x[j] + offsetX + contextTranslation;
                                    if (orientation == SwingConstants.NORTH || orientation == SwingConstants.SOUTH) {
                                        newPoints.add(new Point2D.Double(positionX, edge.y[j]));
                                    } else {
                                        newPoints.add(new Point2D.Double(edge.y[j], positionX));
                                    }
                                    limitX = Math.max(limitX, positionX);
                                }
                            }
                            newPoints.add(oldPoints.get(oldPoints.size() - 1));
                            facade.setPoints(realEdge, newPoints);
                            facade.disableRouting(realEdge);
                            // Increase offset so next edge is drawn next to 
                            // this one 
                            if (offsetX == 0.0) {
                                offsetX = parallelEdgeSpacing;
                            } else if (offsetX > 0) {
                                offsetX = -offsetX;
                            } else {
                                offsetX = -offsetX + parallelEdgeSpacing;
                            }
                        }
                        edge.temp[0] = 101207;
                    }
                }
            }
        }
    }
    // Move the context by the amount it's overlapping the last context 
    if (contextTranslation >= 1.0) {
        if (orientation == SwingConstants.NORTH || orientation == SwingConstants.SOUTH) {
            facade.translateCells(vertices, contextTranslation, 0);
        } else if (orientation == SwingConstants.EAST || orientation == SwingConstants.WEST) {
            facade.translateCells(vertices, 0, contextTranslation);
        }
    }
    // Increase the limit of this context accordingly 
    limitX += contextTranslation;
}
