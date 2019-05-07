//	[L6] DocListener interface 
/**
     * @see com.itextpdf.text.DocListener#setMarginMirroring(boolean)
     */
@Override
public boolean setMarginMirroring(boolean MarginMirroring) {
    if (writer != null && writer.isPaused()) {
        return false;
    }
    return super.setMarginMirroring(MarginMirroring);
}
