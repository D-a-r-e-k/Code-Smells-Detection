/******************************************************************************/
/**
 * Costfunction. A constant penalty value is added for every two edges that 
 * cross.
 * @param lambda Normalizing factor. Increasing lambda means attributing more
 * importance to the elimination of edge crossings, and results in pictures
 * with fewer crossings on average. However, this may be at the expense of other
 * aesthetics.
 * @return costs of this criterion.
 */
private double getEdgeCrossing(double f, double lambda) {
    int n = 0;
    // counts edgecrossings around vertex[i] 
    Line2D.Double[] lineList = getEdgeLines(edgeList);
    for (int i = 0; i < lineList.length; i++) for (int j = i; j < lineList.length; j++) if (j != i)
        if (lineList[i].intersectsLine(lineList[j])) {
            if (((lineList[i].getP1().getX() != lineList[j].getP1().getX()) && (lineList[i].getP1().getY() != lineList[j].getP1().getY())) && ((lineList[i].getP1().getX() != lineList[j].getP2().getX()) && (lineList[i].getP1().getY() != lineList[j].getP2().getY())) && ((lineList[i].getP2().getX() != lineList[j].getP1().getX()) && (lineList[i].getP2().getY() != lineList[j].getP1().getY())) && ((lineList[i].getP2().getX() != lineList[j].getP2().getX()) && (lineList[i].getP2().getY() != lineList[j].getP2().getY()))) {
                n++;
            }
        }
    //        System.out.println("EdgeCrossings : "+n); 
    return lambda * f * n;
}
