//	[L7] DocListener interface 
/**
     * Sets the page number.
     *
     * @param	pageN		the new page number
     */
@Override
public void setPageCount(int pageN) {
    if (writer != null && writer.isPaused()) {
        return;
    }
    super.setPageCount(pageN);
}
