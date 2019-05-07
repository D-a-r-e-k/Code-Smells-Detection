/**
     * Returns a value as a Point2D.Double array.
     * as specified in http://www.w3.org/TR/SVGMobile12/shapes.html#PointsBNF
     */
private Point2D.Double[] toPoints(IXMLElement elem, String str) throws IOException {
    StringTokenizer tt = new StringTokenizer(str, " ,");
    Point2D.Double[] points = new Point2D.Double[tt.countTokens() / 2];
    for (int i = 0; i < points.length; i++) {
        points[i] = new Point2D.Double(toNumber(elem, tt.nextToken()), toNumber(elem, tt.nextToken()));
    }
    return points;
}
