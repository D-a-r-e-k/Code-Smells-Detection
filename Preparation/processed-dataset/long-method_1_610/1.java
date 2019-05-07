/**
     * Sets whether or not this panel is enabled.
     */
public void setEnabled(boolean newValue) {
    for (int i = 0; i < searchTerms.size(); i++) {
        SearchEntryPair currentPair = (SearchEntryPair) searchTerms.elementAt(i);
        currentPair.form.setEnabled(newValue);
        currentPair.connector.getCombo().setEnabled(newValue);
    }
    buttonOne.setEnabled(newValue);
    buttonTwo.setEnabled(newValue);
}
