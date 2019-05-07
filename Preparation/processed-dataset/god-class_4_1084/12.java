/**
     * @see com.itextpdf.text.DocListener#setMarginMirroring(boolean)
     * @since	2.1.6
     */
@Override
public boolean setMarginMirroringTopBottom(boolean MarginMirroringTopBottom) {
    if (writer != null && writer.isPaused()) {
        return false;
    }
    return super.setMarginMirroringTopBottom(MarginMirroringTopBottom);
}
