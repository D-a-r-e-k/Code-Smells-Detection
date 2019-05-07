/** Performs "find all" command. */
private void _findAll() {
    // The following two line was moved to _findAll(...) so it is executed by FindResultsPanel._findAgain 
    //    _machine.setSearchBackwards(false); 
    _findLabelBot.setText("Next");
    String searchStr = _findField.getText();
    String title = searchStr;
    OpenDefinitionsDocument startDoc = _defPane.getOpenDefDocument();
    boolean searchAll = _machine.getSearchAllDocuments();
    boolean searchSelectionOnly = _machine.getSearchSelectionOnly();
    //    StringBuilder tabLabel = new StringBuilder("Find: "); 
    //    if (title.length() <= 10) tabLabel.append(title); 
    //    else tabLabel.append(title.substring(0,10)).append("..."); 
    String tabLabel = (title.length() <= 20) ? title : title.substring(0, 20);
    RegionManager<MovingDocumentRegion> rm = _model.createFindResultsManager();
    MovingDocumentRegion region = new MovingDocumentRegion(startDoc, _defPane.getSelectionStart(), _defPane.getSelectionEnd(), startDoc._getLineStartPos(_defPane.getSelectionStart()), startDoc._getLineEndPos(_defPane.getSelectionEnd()));
    final FindResultsPanel panel = _frame.createFindResultsPanel(rm, region, tabLabel, searchStr, searchAll, searchSelectionOnly, _machine.getMatchCase(), _machine.getMatchWholeWord(), _machine.getIgnoreCommentsAndStrings(), _ignoreTestCases.isSelected(), new WeakReference<OpenDefinitionsDocument>(startDoc), this);
    findAll(searchStr, searchAll, searchSelectionOnly, _machine.getMatchCase(), _machine.getMatchWholeWord(), _machine.getIgnoreCommentsAndStrings(), _ignoreTestCases.isSelected(), startDoc, rm, region, panel);
    //    _model.refreshActiveDocument();  // Rationale: a giant findAll left the definitions pane is a strange state 
    panel.requestFocusInWindow();
    EventQueue.invokeLater(new Runnable() {

        public void run() {
            panel._regTree.scrollRowToVisible(0);
        }
    });
}
