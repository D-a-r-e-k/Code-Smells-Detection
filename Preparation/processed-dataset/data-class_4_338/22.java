//  /** Calls _selectFoundItem(from, to) with reasonable defaults. */ 
//  private void _selectFoundItem() { 
//    int position = _machine.getCurrentOffset(); 
//    int to, from; 
//    to = from = position; 
//    if (! _machine.getSearchBackwards()) from = position - _machine.getFindWord().length(); 
//    else to = position + _machine.getFindWord().length(); 
//    _selectFoundItem(from, to); 
//  } 
/** Will select the identified text (from, to).  Note that positions are technically between characters, so there
    * is no distinction between open and closed intervals.  Originally highlighted the text, but we ran into problems
    * with the document remove method changing the view to where the cursor was located, resulting in 
    * replace constantly jumping from the replaced text back to the cursor.  There was a 
    * removePreviousHighlight method which was removed since selections are removed automatically upon
    * a caret change.
    */
private void _selectFoundOrReplacedItem(int from, int to) {
    _defPane.centerViewOnOffset(from);
    _defPane.select(from, to);
    // Found this little statement that will show the selected text in _defPane without giving _defPane  
    // focus, allowing the user to hit enter repeatedly and change the document while finding next. 
    EventQueue.invokeLater(new Runnable() {

        public void run() {
            _defPane.getCaret().setSelectionVisible(true);
        }
    });
}
