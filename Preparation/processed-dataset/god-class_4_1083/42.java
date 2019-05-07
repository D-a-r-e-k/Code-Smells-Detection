/**
     * Sets the viewer preferences.
     * @param preferences the viewer preferences
     * @see PdfWriter#setViewerPreferences(int)
     */
@Override
public void setViewerPreferences(int preferences) {
    useVp = true;
    this.viewerPreferences.setViewerPreferences(preferences);
}
