/**
   * Does not update gui or cause any events to be fired.
   */
protected void setFontSizeSilently(int fontSize) {
    _fontSize = fontSize;
    setFontSize(_table._detailTextArea, fontSize);
    selectRow(0);
    setFontSize(_table, fontSize);
}
