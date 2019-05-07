/** Performs "find all" with the specified options. */
public void findAll(String searchStr, final boolean searchAll, final boolean searchSelectionOnly, final boolean matchCase, final boolean wholeWord, final boolean noComments, final boolean noTestCases, final OpenDefinitionsDocument startDoc, final RegionManager<MovingDocumentRegion> rm, final MovingDocumentRegion region, final FindResultsPanel panel) {
    _machine.setSearchBackwards(false);
    int searchLen = searchStr.length();
    if (searchLen == 0)
        return;
    _frame.updateStatusField("Finding All");
    OpenDefinitionsDocument oldDoc = _machine.getDocument();
    OpenDefinitionsDocument oldFirstDoc = _machine.getFirstDoc();
    String oldFindWord = _machine.getFindWord();
    boolean oldSearchAll = _machine.getSearchAllDocuments();
    boolean oldSearchSelectionOnly = _machine.getSearchSelectionOnly();
    boolean oldMatchCase = _machine.getMatchCase();
    boolean oldWholeWord = _machine.getMatchWholeWord();
    boolean oldNoComments = _machine.getIgnoreCommentsAndStrings();
    boolean oldNoTestCases = _machine.getIgnoreTestCases();
    int oldPosition = _machine.getCurrentOffset();
    //    _updateMachine(); 
    _machine.setDocument(startDoc);
    if (_machine.getFirstDoc() == null)
        _machine.setFirstDoc(startDoc);
    _machine.setSearchAllDocuments(searchAll);
    _machine.setSearchSelectionOnly(searchSelectionOnly);
    _machine.setMatchCase(matchCase);
    if (wholeWord) {
        _machine.setMatchWholeWord();
    } else {
        _machine.setFindAnyOccurrence();
    }
    _machine.setIgnoreCommentsAndStrings(noComments);
    _machine.setPosition(startDoc.getCurrentLocation());
    _machine.setIgnoreTestCases(noTestCases);
    _machine.setFindWord(searchStr);
    String replaceStr = _replaceField.getText();
    _machine.setReplaceWord(replaceStr);
    _frame.clearStatusMessage();
    final List<FindResult> results = new ArrayList<FindResult>();
    _frame.hourglassOn();
    try {
        /* Accumulate all occurrences of searchStr in results. */
        final int count = _machine.processAll(new Runnable1<FindResult>() {

            public void run(FindResult fr) {
                results.add(fr);
            }
        }, region);
        _machine.setDocument(oldDoc);
        _machine.setFirstDoc(oldFirstDoc);
        _machine.setFindWord(oldFindWord);
        _machine.setSearchAllDocuments(oldSearchAll);
        _machine.setSearchSelectionOnly(oldSearchSelectionOnly);
        _machine.setMatchCase(oldMatchCase);
        if (oldWholeWord) {
            _machine.setMatchWholeWord();
        } else {
            _machine.setFindAnyOccurrence();
        }
        _machine.setIgnoreCommentsAndStrings(oldNoComments);
        _machine.setIgnoreTestCases(oldNoTestCases);
        _machine.setPosition(oldPosition);
        for (FindResult fr : results) {
            final OpenDefinitionsDocument doc = fr.getDocument();
            if (_model.getActiveDocument() != doc)
                _model.setActiveDocument(doc);
            else
                _model.refreshActiveDocument();
            int end = fr.getFoundOffset();
            int start = end - searchLen;
            int lineStart = doc._getLineStartPos(start);
            int lineEnd = doc._getLineEndPos(end);
            rm.addRegion(new MovingDocumentRegion(doc, start, end, lineStart, lineEnd));
        }
        //      EventQueue.invokeLater(new Runnable() { 
        //        public void run() { 
        if (count > 0)
            _frame.showFindResultsPanel(panel);
        else {
            Toolkit.getDefaultToolkit().beep();
            panel.freeResources();
        }
        _frame.setStatusMessage("Found " + count + " occurrence" + ((count == 1) ? "" : "s") + ".");
        //        } 
        //      }); 
        if (searchSelectionOnly) {
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    if (_defPane != null) {
                        _defPane.requestFocusInWindow();
                        _defPane.setSelectionStart(region.getStartOffset());
                        _defPane.setSelectionEnd(region.getEndOffset());
                    }
                }
            });
        }
    } finally {
        _frame.hourglassOff();
        // extracted from run() above because findAll occasionally left active document in inconsistent state  
        _model.setActiveDocument(startDoc);
    }
}
