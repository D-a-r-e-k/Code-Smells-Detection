/**
   * Check to see if the any records contain the search string.
   * Searching now supports NDC messages and date.
   */
protected boolean matches(LogRecord record, String text) {
    String message = record.getMessage();
    String NDC = record.getNDC();
    if (message == null && NDC == null || text == null) {
        return false;
    }
    if (message.toLowerCase().indexOf(text.toLowerCase()) == -1 && NDC.toLowerCase().indexOf(text.toLowerCase()) == -1) {
        return false;
    }
    return true;
}
