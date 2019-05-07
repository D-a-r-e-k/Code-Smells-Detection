/**
		 * @see java.lang.Comparable#compareTo(Object)
		 */
public int compareTo(Object compare) {
    if (((CellWrapper) compare).getEdgeCrossesIndicator() == this.getEdgeCrossesIndicator())
        return 0;
    double compareValue = (((CellWrapper) compare).getEdgeCrossesIndicator() - this.getEdgeCrossesIndicator());
    return (int) (compareValue * 1000);
}
