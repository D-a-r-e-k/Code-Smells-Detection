private long getPositiveOffset(Date offsetDate, Date anchorDate) {
    if (getChartModel().getTimeUnitStack().getDefaultTimeUnit().equals(myCurrentUnit)) {
        TaskLength length = getChartModel().getTaskManager().createLength(myCurrentUnit, anchorDate, offsetDate);
        return length.getLength() * getChartModel().getBottomUnitWidth();
    }
    int length = 0;
    while (true) {
        ChartModelBase.Offset[] offsets = getChartModel().calculateOffsets(myCurrentTimeFrame, myCurrentUnit, anchorDate, getChartModel().getTimeUnitStack().getDefaultTimeUnit(), getChartModel().getBottomUnitWidth());
        if (offsets.length == 0) {
            throw new IllegalStateException("Failed to calculate offsets for anchorDate=" + anchorDate);
        }
        Date lastOffsetEnd = offsets[offsets.length - 1].getOffsetEnd();
        //System.err.println("[TaskRendererImpl] getPositiveOffset(): |offsets|="+offsets.length+" last offset end="+lastOffsetEnd+" last offset pixels="+offsets[offsets.length-1].getOffsetPixels()); 
        if (lastOffsetEnd.before(offsetDate)) {
            anchorDate = lastOffsetEnd;
            length += offsets[offsets.length - 1].getOffsetPixels();
            continue;
        }
        //int firstOffsetPixels = offsets[0].getOffsetPixels(); 
        for (int i = 0; i < offsets.length; i++) {
            Offset offset = offsets[i];
            if (false == offset.getOffsetEnd().before(offsetDate)) {
                length += (offset.getOffsetPixels());
                break;
            }
        }
        break;
    }
    return length;
}
