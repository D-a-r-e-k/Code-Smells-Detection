//  /** Shows the dialog and sets the focus appropriately. */ 
//  public void show() { 
//   //super.show(); 
//   System.err.println("*** Called show ***"); 
////   if (!isVisible()) 
//     _frame.installFindReplaceDialog(this); 
//     _updateMachine(); 
//     _findField.requestFocusInWindow(); 
//     _findField.selectAll(); 
//   } 
/** This method is used to select the item that has been inserted in a replacement.  Assumes the current offset
    * identifies the found or replaced item.  In a forward search, this offset is the RIGHT edge of the found/replaced
    * item; in a backwards search it is the LEFT edge. */
private void _selectFoundOrReplacedItem(int length) {
    int offset = _machine.getCurrentOffset();
    int from, to;
    if (_machine.isSearchBackwards()) {
        from = offset + length;
        // "to" is the offset where the caret will be positioned 
        // when searching backwards, "to" has to be the smaller offset 
        to = offset;
    } else {
        from = offset - length;
        to = offset;
    }
    _selectFoundOrReplacedItem(from, to);
}
