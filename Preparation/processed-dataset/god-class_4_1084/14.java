//	[L8] DocListener interface 
/**
     * Sets the page number to 0.
     */
@Override
public void resetPageCount() {
    if (writer != null && writer.isPaused()) {
        return;
    }
    super.resetPageCount();
}
