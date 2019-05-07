private long getNegativeOffset(Date offsetDate, Date anchorDate) {
    if (getChartModel().getTimeUnitStack().getDefaultTimeUnit().equals(myCurrentUnit)) {
        TaskLength length = getChartModel().getTaskManager().createLength(myCurrentUnit, offsetDate, anchorDate);
        return -length.getLength() * getChartModel().getBottomUnitWidth();
    }
    int length = 0;
    while (true) {
        ChartModelBase.Offset[] offsets = getChartModel().calculateOffsets(myCurrentTimeFrame, myCurrentUnit, offsetDate, getChartModel().getTimeUnitStack().getDefaultTimeUnit(), getChartModel().getBottomUnitWidth());
        assert offsets.length > 0;
        Date lastOffsetEnd = offsets[offsets.length - 1].getOffsetEnd();
        if (lastOffsetEnd.before(anchorDate)) {
            offsetDate = lastOffsetEnd;
            length += offsets[offsets.length - 1].getOffsetPixels();
            continue;
        }
        for (int i = offsets.length - 1; i >= 0; i--) {
            Offset offset = offsets[i];
            if (offset.getOffsetEnd().after(anchorDate)) {
                continue;
            }
            length += offset.getOffsetPixels();
            break;
        }
        break;
    }
    return -length;
}
