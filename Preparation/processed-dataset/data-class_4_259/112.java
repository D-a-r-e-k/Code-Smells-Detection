/** Sets the viewer preferences as the sum of several constants.
     * @param preferences the viewer preferences
     * @see PdfViewerPreferences#setViewerPreferences
     */
public void setViewerPreferences(int preferences) {
    this.viewerPreferences.setViewerPreferences(preferences);
    setViewerPreferences(this.viewerPreferences);
}
