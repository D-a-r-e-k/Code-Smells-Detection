// OTHER METHODS //////////////////////////////////////////////// 
/** 
     * Add the visit count from another segment. 
     *
     * @param seg Reference to the other segment.
     */
protected Segment add(final Segment seg) {
    visits += seg.getVisits();
    return this;
}
