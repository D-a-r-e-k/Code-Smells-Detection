/**
     * Set a parent region from the geographic regions now we
     * know where each region is.
     *
     * @param sr The <code>ServerRegion</code> to find a parent for.
     */
private void setGeographicRegion(ServerRegion sr) {
    if (geographicRegions == null)
        return;
    for (ServerRegion gr : geographicRegions) {
        Position cen = sr.getCenter();
        if (gr.getBounds().contains(cen.getX(), cen.getY())) {
            sr.setParent(gr);
            gr.addChild(sr);
            gr.setSize(gr.getSize() + sr.getSize());
            break;
        }
    }
}
