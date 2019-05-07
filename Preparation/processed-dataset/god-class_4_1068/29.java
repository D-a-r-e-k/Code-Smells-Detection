private java.awt.Rectangle getBoundingRectangle(TaskActivity activity) {
    //System.err.println("[TaskRendererImpl] getBoundingRectangle():\nunit start="+myUnitStart+"\nactivity="+activity); 
    int posX = myPosX;
    int length;
    if (false == activity.getStart().equals(myUnitStart)) {
        int deltaX = 0;
        if (activity.getStart().before(myUnitStart)) {
            deltaX = (int) getNegativeOffset(activity.getStart(), myUnitStart);
        } else if (activity.getStart().after(myUnitStart)) {
            deltaX = (int) getPositiveOffset(activity.getStart(), myUnitStart);
        }
        posX += deltaX;
        length = (int) getPositiveOffset(activity.getEnd(), myUnitStart) - deltaX;
    } else {
        length = (int) (activity.getDuration().getLength(myCurrentUnit) * getChartModel().getBottomUnitWidth());
    }
    Integer nextOrdNumber = (Integer) myActivity2ordinalNumber.get(activity);
    int topy = nextOrdNumber.intValue() * getRowHeight() + 4;
    // JA Added 
    // 4 so that 
    // it draws 
    // in middle 
    // of row 
    return new java.awt.Rectangle(posX, topy, length, getRowHeight());
}
