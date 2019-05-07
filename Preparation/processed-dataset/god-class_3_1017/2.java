@SuppressWarnings("unchecked")
protected void setScrollBarValues(Rectangle rectangle, Point2D h0, Point2D h1, Point2D v0, Point2D v1) {
    boolean containsH0 = rectangle.contains(h0);
    boolean containsH1 = rectangle.contains(h1);
    boolean containsV0 = rectangle.contains(v0);
    boolean containsV1 = rectangle.contains(v1);
    // horizontal scrollbar: 
    Intersector intersector = new Intersector(rectangle, new Line2D.Double(h0, h1));
    int min = 0;
    int ext;
    int val = 0;
    int max;
    Set points = intersector.getPoints();
    Point2D first = null;
    Point2D second = null;
    Point2D[] pointArray = (Point2D[]) points.toArray(new Point2D[points.size()]);
    if (pointArray.length > 1) {
        first = pointArray[0];
        second = pointArray[1];
    } else if (pointArray.length > 0) {
        first = second = pointArray[0];
    }
    if (first != null && second != null) {
        // correct direction of intersect points 
        if ((h0.getX() - h1.getX()) * (first.getX() - second.getX()) < 0) {
            // swap them 
            Point2D temp = first;
            first = second;
            second = temp;
        }
        if (containsH0 && containsH1) {
            max = (int) first.distance(second);
            val = (int) first.distance(h0);
            ext = (int) h0.distance(h1);
        } else if (containsH0) {
            max = (int) first.distance(second);
            val = (int) first.distance(h0);
            ext = (int) h0.distance(second);
        } else if (containsH1) {
            max = (int) first.distance(second);
            val = 0;
            ext = (int) first.distance(h1);
        } else {
            max = ext = rectangle.width;
            val = min;
        }
        horizontalScrollBar.setValues(val, ext + 1, min, max);
    }
    // vertical scroll bar 
    min = val = 0;
    intersector.intersectLine(new Line2D.Double(v0, v1));
    points = intersector.getPoints();
    pointArray = (Point2D[]) points.toArray(new Point2D[points.size()]);
    if (pointArray.length > 1) {
        first = pointArray[0];
        second = pointArray[1];
    } else if (pointArray.length > 0) {
        first = second = pointArray[0];
    }
    if (first != null && second != null) {
        // arrange for direction 
        if ((v0.getY() - v1.getY()) * (first.getY() - second.getY()) < 0) {
            // swap them 
            Point2D temp = first;
            first = second;
            second = temp;
        }
        if (containsV0 && containsV1) {
            max = (int) first.distance(second);
            val = (int) first.distance(v0);
            ext = (int) v0.distance(v1);
        } else if (containsV0) {
            max = (int) first.distance(second);
            val = (int) first.distance(v0);
            ext = (int) v0.distance(second);
        } else if (containsV1) {
            max = (int) first.distance(second);
            val = 0;
            ext = (int) first.distance(v1);
        } else {
            max = ext = rectangle.height;
            val = min;
        }
        verticalScrollBar.setValues(val, ext + 1, min, max);
    }
}
