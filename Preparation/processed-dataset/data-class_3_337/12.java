/** Inserts a string of text into the document.  This is not where we do custom processing of the insert; that is
    * done in {@link #insertUpdate}.  If _removeTabs is set to true, remove all tabs from str. It is a current invariant
    * of the tabification functionality that the document contains no tabs, but we want to allow the user to override 
    * this functionality.
    */
public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
    if (_tabsRemoved)
        str = _removeTabs(str);
    _setModifiedSinceSave();
    super.insertString(offset, str, a);
}
