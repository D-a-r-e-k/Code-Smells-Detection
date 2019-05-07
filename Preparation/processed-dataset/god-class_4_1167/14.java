/** Abstracted out since this is called from findNext and findPrevious. */
private void _doFind() {
    if (_findField.getText().length() > 0) {
        _updateMachine();
        final String findWord = _findField.getText();
        _machine.setFindWord(findWord);
        _machine.setReplaceWord(_replaceField.getText());
        _frame.clearStatusMessage();
        // _message.setText(""); // JL 
        final boolean searchAll = _machine.getSearchAllDocuments();
        // FindResult contains the document that the result was found in, offset to the next occurrence of  
        // the string, and a flag indicating whether the end of the document was wrapped around while searching 
        // for the string. 
        _frame.hourglassOn();
        try {
            FindResult fr = _machine.findNext();
            OpenDefinitionsDocument matchDoc = fr.getDocument();
            //      OpenDefinitionsDocument matchDoc = _model.getODDForDocument(doc); 
            OpenDefinitionsDocument openDoc = _defPane.getOpenDefDocument();
            final boolean docChanged = matchDoc != openDoc;
            final int pos = fr.getFoundOffset();
            if (pos >= 0)
                _model.addToBrowserHistory();
            // pos >= 0  <=> search succeeded 
            if (searchAll) {
                // if search was global, reset the active document 
                if (docChanged)
                    _model.setActiveDocument(matchDoc);
                else
                    _model.refreshActiveDocument();
            }
            if (fr.getWrapped() && !searchAll) {
                Toolkit.getDefaultToolkit().beep();
                if (!_machine.isSearchBackwards())
                    _frame.setStatusMessage("Search wrapped to beginning.");
                else
                    _frame.setStatusMessage("Search wrapped to end.");
            }
            if (fr.getAllWrapped() && searchAll) {
                Toolkit.getDefaultToolkit().beep();
                _frame.setStatusMessage("Search wrapped around all documents.");
            }
            if (pos >= 0) {
                // found a match 
                //        Caret c = _defPane.getCaret(); 
                //        c.setDot(c.getDot()); 
                _defPane.setCaretPosition(pos);
                _caretChanged = true;
                _updateMachine();
                final Runnable command = new Runnable() {

                    public void run() {
                        _selectFoundOrReplacedItem(findWord.length());
                        _replaceAction.setEnabled(true);
                        _replaceFindNextAction.setEnabled(true);
                        _replaceFindPreviousAction.setEnabled(true);
                        _machine.setLastFindWord();
                        _model.addToBrowserHistory();
                        if (DrJava.getConfig().getSetting(OptionConstants.FIND_REPLACE_FOCUS_IN_DEFPANE).booleanValue()) {
                            // moves focus to DefinitionsPane 
                            _frame.toFront();
                            EventQueue.invokeLater(new Runnable() {

                                public void run() {
                                    if (_defPane != null) {
                                        _defPane.requestFocusInWindow();
                                    }
                                }
                            });
                        }
                    }
                };
                if (docChanged)
                    // defer executing this code until after active document switch is complete 
                    EventQueue.invokeLater(command);
                else
                    command.run();
            } else {
                Toolkit.getDefaultToolkit().beep();
                final StringBuilder statusMessage = new StringBuilder("Search text \"");
                if (findWord.length() <= 50)
                    statusMessage.append(findWord);
                else
                    statusMessage.append(findWord.substring(0, 49) + "...");
                statusMessage.append("\" not found.");
                _frame.setStatusMessage(statusMessage.toString());
            }
        } finally {
            _frame.hourglassOff();
        }
    }
    if (!DrJava.getConfig().getSetting(OptionConstants.FIND_REPLACE_FOCUS_IN_DEFPANE).booleanValue()) {
        _findField.requestFocusInWindow();
    }
}
