/** Adds a viewer preference
     * @param key a key for a viewer preference
     * @param value	a value for the viewer preference
     * @see PdfViewerPreferences#addViewerPreference
     */
public void addViewerPreference(PdfName key, PdfObject value) {
    this.viewerPreferences.addViewerPreference(key, value);
    setViewerPreferences(this.viewerPreferences);
}
