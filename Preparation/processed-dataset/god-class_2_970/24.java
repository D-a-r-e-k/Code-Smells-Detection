/**
     * After layout, moves the graph as close to origin as possible.
     *
     * <p>After the layout has complete, this algorithm calculates the
     * minimum X and Y coordinates over all selected cells. If
     * flushToOrigin parameter is set to false, the algorithm will place
     * cells starting at coordinates corresponding to those minimum
     * values.
     *
     * <p>If set to true, the layout will place cells starting at the
     * origin, possibly shrinking the overall graph canvas size
     *
     * @param newFlushToOrigin The new FlushToOrigin value.
     */
public final void setFlushToOrigin(final boolean newFlushToOrigin) {
    this.flushToOrigin = newFlushToOrigin;
}
