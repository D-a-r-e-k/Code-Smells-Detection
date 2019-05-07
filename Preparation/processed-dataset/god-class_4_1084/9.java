//	[L4] DocListener interface 
/**
     * Sets the pagesize.
     *
     * @param pageSize the new pagesize
     * @return <CODE>true</CODE> if the page size was set
     */
@Override
public boolean setPageSize(Rectangle pageSize) {
    if (writer != null && writer.isPaused()) {
        return false;
    }
    nextPageSize = new Rectangle(pageSize);
    return true;
}
