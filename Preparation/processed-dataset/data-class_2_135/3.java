/** retruns the average value for the edge crosses indicator
		 *
		 *  for the wrapped cell
		 *
		 */
double getEdgeCrossesIndicator() {
    if (additions == 0)
        return 0;
    return edgeCrossesIndicator / additions;
}
