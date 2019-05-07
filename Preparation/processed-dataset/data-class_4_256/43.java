/** Adds a viewer preference
     * @param key a key for a viewer preference
     * @param value the value for the viewer preference
     * @see PdfViewerPreferences#addViewerPreference
     */
@Override
public void addViewerPreference(PdfName key, PdfObject value) {
    useVp = true;
    this.viewerPreferences.addViewerPreference(key, value);
}
