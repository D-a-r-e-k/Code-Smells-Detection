/**
     * @param dependencyDrawData
     */
private void drawDependencies(List dependencyDrawData) {
    // if(dependencyDrawData.size() == 0) 
    // System.out.println("VIDE"); 
    GraphicPrimitiveContainer primitiveContainer = getPrimitiveContainer().getLayer(2);
    int arrowLength = 7;
    for (int i = 0; i < dependencyDrawData.size(); i++) {
        DependencyDrawData next = (DependencyDrawData) dependencyDrawData.get(i);
        if (next.myDependeeVector.reaches(next.myDependantVector.getPoint())) {
            // when dependee.end <= dependant.start && dependency.type is 
            // any 
            // or dependee.end <= dependant.end && dependency.type==FF 
            // or dependee.start >= dependant.end && dependency.type==SF 
            int ysign = signum(next.myDependantVector.getPoint().y - next.myDependeeVector.getPoint().y);
            Point first = new Point(next.myDependeeVector.getPoint().x, next.myDependeeVector.getPoint().y);
            Point second = new Point(next.myDependantVector.getPoint(-3).x, next.myDependeeVector.getPoint().y);
            Point third = new Point(next.myDependantVector.getPoint(-3).x, next.myDependantVector.getPoint().y);
            java.awt.Rectangle arrowBoundary = null;
            String style;
            if (next.myDependantVector.reaches(third)) {
                second.x += arrowLength;
                third.x += arrowLength;
                Point forth = next.myDependantVector.getPoint();
                primitiveContainer.createLine(third.x, third.y, forth.x, forth.y);
                arrowBoundary = new java.awt.Rectangle(forth.x, forth.y - 3, arrowLength, 6);
                style = "dependency.arrow.left";
            } else {
                third.y -= ysign * next.myDependantRectangle.myHeight / 2;
                arrowBoundary = new java.awt.Rectangle(third.x - 3, third.y - (ysign > 0 ? ysign * arrowLength : 0), 6, arrowLength);
                style = ysign > 0 ? "dependency.arrow.down" : "dependency.arrow.up";
            }
            primitiveContainer.createLine(first.x, first.y, second.x, second.y);
            primitiveContainer.createLine(second.x, second.y, third.x, third.y);
            if (arrowBoundary != null) {
                Rectangle arrow = getPrimitiveContainer().createRectangle(arrowBoundary.x, arrowBoundary.y, arrowBoundary.width, arrowBoundary.height);
                arrow.setStyle(style);
            }
        } else {
            Point first = next.myDependeeVector.getPoint(3);
            if (next.myDependantVector.reaches(first)) {
                Point second = new Point(first.x, next.myDependantVector.getPoint().y);
                primitiveContainer.createLine(next.myDependeeVector.getPoint().x, next.myDependeeVector.getPoint().y, first.x, first.y);
                primitiveContainer.createLine(first.x, first.y, second.x, second.y);
                primitiveContainer.createLine(second.x, second.y, next.myDependantVector.getPoint().x, next.myDependantVector.getPoint().y);
                int xsign = signum(next.myDependantVector.getPoint().x - second.x);
                java.awt.Rectangle arrowBoundary = new java.awt.Rectangle(next.myDependantVector.getPoint(7).x, next.myDependantVector.getPoint().y - 3, xsign * 7, 6);
                Rectangle arrow = primitiveContainer.createRectangle(arrowBoundary.x, arrowBoundary.y, arrowBoundary.width, arrowBoundary.height);
                arrow.setStyle(xsign < 0 ? "dependency.arrow.left" : "dependency.arrow.right");
            } else {
                Point forth = next.myDependantVector.getPoint(3);
                Point second = new Point(first.x, (first.y + forth.y) / 2);
                Point third = new Point(forth.x, (first.y + forth.y) / 2);
                primitiveContainer.createLine(next.myDependeeVector.getPoint().x, next.myDependeeVector.getPoint().y, first.x, first.y);
                primitiveContainer.createLine(first.x, first.y, second.x, second.y);
                primitiveContainer.createLine(second.x, second.y, third.x, third.y);
                primitiveContainer.createLine(third.x, third.y, forth.x, forth.y);
                primitiveContainer.createLine(forth.x, forth.y, next.myDependantVector.getPoint().x, next.myDependantVector.getPoint().y);
            }
        }
    }
}
