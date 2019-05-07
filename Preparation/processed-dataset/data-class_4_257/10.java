/**
     * Sets the margins.
     *
     * @param	marginLeft		the margin on the left
     * @param	marginRight		the margin on the right
     * @param	marginTop		the margin on the top
     * @param	marginBottom	the margin on the bottom
     * @return	a <CODE>boolean</CODE>
     */
@Override
public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
    if (writer != null && writer.isPaused()) {
        return false;
    }
    nextMarginLeft = marginLeft;
    nextMarginRight = marginRight;
    nextMarginTop = marginTop;
    nextMarginBottom = marginBottom;
    return true;
}
