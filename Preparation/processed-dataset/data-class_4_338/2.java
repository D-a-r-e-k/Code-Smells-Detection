/** Focuses the find/replace dialog in the window, placing the focus on the _findField, and selecting all the text.*/
public boolean requestFocusInWindow() {
    super.requestFocusInWindow();
    _findField.selectAll();
    return _findField.requestFocusInWindow();
}
