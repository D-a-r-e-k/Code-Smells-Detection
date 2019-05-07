/******************************************************************************/
/**
 * Computes an Impulse representing a Force affecting the position of the given 
 * Cell. This impulse consists of a attracting force, pulling the cell to the
 * barycenter of all cells, a pulse with user defined length and random 
 * direction, a force repulsing cells from each other, a force attracting 
 * connected cells together, and, as a additional feature, a force, repulsing 
 * the current cell from overlapping cells.
 * 
 * @param view Cell, the impulse should be computed
 * @return impulse, transformed in a Point2D.Double-Instance.  
 */
private Point2D.Double computeImpulse(CellView view) {
    Point2D.Double impulse = new Point2D.Double();
    Point2D.Double pos = getPosition(view);
    //        boolean isCellACluster = isCluster(view); 
    //the more edges a cell have, the heavier the cell is 
    double massIndex = getNodeWeight(view);
    //gets the barycenter of all cells 
    Point2D.Double barycenter = computeBarycenter(cellList);
    //attracting force from the barycenter to every cell 
    Point2D.Double gravitationForce = new Point2D.Double((barycenter.getX() - pos.getX()) * gravitation * massIndex, (barycenter.getY() - pos.getY()) * gravitation * massIndex);
    //random glitch is added to force 
    Point2D.Double randomImpulse = getRandomVector(randomImpulseRange);
    //repulsive Forces 
    //from all nodes 
    ArrayList repulsiveForce = new ArrayList();
    for (int i = 0; i < cellList.size(); i++) if (cellList.get(i) != view) {
        //all cells except the actual view 
        //                CellView uView = (CellView) cellList.get(i); 
        //                if( !isCluster(uView)){ 
        Point2D.Double uPos = getPosition(i, cellList);
        double deltaX = (pos.getX() - uPos.getX());
        double deltaY = (pos.getY() - uPos.getY());
        /*                
                double sgnX = MathExtensions.sgn(deltaX);
                double sgnY = MathExtensions.sgn(deltaY);
                
                if( isCellACluster && isCluster(uView) ){
                    deltaX -= uView.getBounds().getWidth() / 2.0;
                    deltaY -= uView.getBounds().getHeight()/ 2.0;
                }
                if( isCellACluster ){
                    deltaX -= view.getBounds().getWidth() / 2.0;
                    deltaY -= view.getBounds().getHeight()/ 2.0;
                }
                
                if( sgnX != MathExtensions.sgn(deltaX) ){
                    deltaX *= sgnX;
                }
                if( sgnY != MathExtensions.sgn(deltaY) ){
                    deltaY *= sgnY;
                }
*/
        double absDelta = MathExtensions.abs(deltaX, deltaY);
        if (absDelta > equalsNull) {
            repulsiveForce.add(new Point2D.Double(deltaX * ((prefEdgeLength * prefEdgeLength) / (absDelta * absDelta)), deltaY * ((prefEdgeLength * prefEdgeLength) / (absDelta * absDelta))));
        }
    }
    //attractive Forces: 
    //from all nodes that have an edge with view 
    ArrayList relatives = getRelativesFrom(cellList, view);
    ArrayList attractiveForce = new ArrayList(relatives.size());
    for (int i = 0; i < relatives.size(); i++) {
        //            CellView child = (CellView) relatives.get(i); 
        Point2D.Double cPos = getPosition(i, relatives);
        double deltaX = (pos.getX() - cPos.getX());
        double deltaY = (pos.getY() - cPos.getY());
        /*          
            double sgnX = MathExtensions.sgn(deltaX);
            double sgnY = MathExtensions.sgn(deltaY);
            
            if( isCellACluster && isCluster( child )){
                deltaX -= child.getBounds().getWidth() / 2.0;
                deltaY -= child.getBounds().getHeight()/ 2.0;
            }
            if( isCellACluster ){
                deltaX -= view.getBounds().getWidth() / 2.0;
                deltaY -= view.getBounds().getHeight()/ 2.0;
            }
            
            if( sgnX != MathExtensions.sgn(deltaX) )
                deltaX *= sgnX;
            if( sgnY != MathExtensions.sgn(deltaY) )
                deltaY *= sgnY;
*/
        double absDelta = MathExtensions.abs(deltaX, deltaY);
        attractiveForce.add(new Point2D.Double(deltaX * ((absDelta * absDelta) / (prefEdgeLength * prefEdgeLength * massIndex)), deltaY * ((absDelta * absDelta) / (prefEdgeLength * prefEdgeLength * massIndex))));
    }
    /* the next part is NOT part of the original algorithm */
    /* it adds a force if the actual cell overlapps another cell */
    ArrayList forcesByOverlapping = new ArrayList();
    if (avoidOverlapping) {
        Rectangle viewBounds = new Rectangle((int) pos.x, (int) pos.y, (int) view.getBounds().getWidth(), (int) view.getBounds().getHeight());
        Rectangle viewBorder = new Rectangle((int) (viewBounds.getX() - overlapDetectWidth), (int) (viewBounds.getY() - overlapDetectWidth), (int) (viewBounds.getWidth() + (2.0 * overlapDetectWidth)), (int) (viewBounds.getHeight() + (2.0 * overlapDetectWidth)));
        for (int i = 0; i < cellList.size(); i++) {
            Point2D.Double uPos = getPosition(i, cellList);
            Rectangle uBounds = new Rectangle((int) uPos.x, (int) uPos.y, (int) ((CellView) cellList.get(i)).getBounds().getWidth(), (int) ((CellView) cellList.get(i)).getBounds().getHeight());
            if (view != cellList.get(i) && viewBorder.intersects(uBounds)) {
                Dimension viewSize = viewBounds.getSize();
                Dimension uSize = uBounds.getSize();
                double minDistance = (Math.max(viewSize.getWidth(), viewSize.getHeight()) / 2.0) + (Math.max(uSize.getWidth(), uSize.getHeight()) / 2.0) + overlapPrefDistance;
                double deltaX = (pos.x - uPos.x);
                double deltaY = (pos.y - uPos.y);
                /*                  
                    if( isCellACluster ){
                        deltaX -= view.getBounds().getWidth() / 2.0;
                        deltaY -= view.getBounds().getHeight()/ 2.0;
                    }
                    if( isCluster((CellView)cellList.get(i))){
                        deltaX -= ((CellView)cellList.get(i)).getBounds().getWidth() / 2.0;
                        deltaY -= ((CellView)cellList.get(i)).getBounds().getHeight()/ 2.0;
                    }*/
                if (deltaX < equalsNull && deltaX >= 0.0) {
                    deltaX = equalsNull;
                } else if (deltaX > -equalsNull && deltaX <= 0.0) {
                    deltaX = -equalsNull;
                }
                if (deltaY < equalsNull && deltaY >= 0.0) {
                    deltaY = equalsNull;
                } else if (deltaY > -equalsNull && deltaY <= 0.0) {
                    deltaY = -equalsNull;
                }
                double absDelta = MathExtensions.abs(deltaX, deltaY);
                Point2D.Double force = new Point2D.Double(deltaX * (minDistance * minDistance) / (absDelta * absDelta), deltaY * (minDistance * minDistance) / (absDelta * absDelta));
                //                System.out.println("Overlapping Nodes: ("+pos.x+"|"+pos.y+") and ("+uPos.x+"|"+uPos.y+") -> Distance = "+absDelta+" -> Force = "+force); 
                forcesByOverlapping.add(force);
            }
        }
    }
    ArrayList additionalForce = new ArrayList();
    //adding the forces 
    impulse = add(impulse, gravitationForce);
    impulse = add(impulse, randomImpulse);
    for (int i = 0; i < repulsiveForce.size(); i++) impulse = add(impulse, (Point2D.Double) repulsiveForce.get(i));
    for (int i = 0; i < attractiveForce.size(); i++) impulse = sub(impulse, (Point2D.Double) attractiveForce.get(i));
    for (int i = 0; i < forcesByOverlapping.size(); i++) impulse = add(impulse, (Point2D.Double) forcesByOverlapping.get(i));
    for (int i = 0; i < additionalForce.size(); i++) impulse = add(impulse, (Point2D.Double) additionalForce.get(i));
    return impulse;
}
