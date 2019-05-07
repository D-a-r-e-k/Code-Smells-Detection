void method0() { 
/* Other bracketing options:
   *   solid circle u25CF or u26AB (fails)
   *   half circles u25D6 (fails), u25D7
   *   diamond u2666
   *   block arrows  u25BA, u25C4
   *   big block arrows u25B6, u25C0
   *   enclosing wedges u25E4, u25E5
   *   small solid square u25FE
   *   fisheye u25C9
   */
public static final char LEFT = '◾';
public static final char RIGHT = '◾';
private JButton _findNextButton;
private JButton _findPreviousButton;
private JButton _findAllButton;
private JButton _replaceButton;
private JButton _replaceFindNextButton;
private JButton _replaceFindPreviousButton;
private JButton _replaceAllButton;
private JTextPane _findField;
private JTextPane _replaceField;
private JLabel _findLabelBot;
// Dynamically updated 
private JCheckBox _ignoreCommentsAndStrings;
private JCheckBox _matchCase;
private JCheckBox _searchAllDocuments;
private JCheckBox _matchWholeWord;
private JCheckBox _ignoreTestCases;
private JCheckBox _searchSelectionOnly;
/* MainFrame _frame is inherited from TabbedPanel */
private FindReplaceMachine _machine;
private SingleDisplayModel _model;
private DefinitionsPane _defPane = null;
private boolean _caretChanged;
private boolean _isFindReplaceActive = false;
/** Listens for changes to the cursor position in order to reset the start position */
private CaretListener _caretListener = new CaretListener() {

    public void caretUpdate(CaretEvent e) {
        assert EventQueue.isDispatchThread();
        //      Utilities.invokeLater(new Runnable() { 
        //        public void run() { 
        _replaceAction.setEnabled(false);
        _replaceFindNextAction.setEnabled(false);
        _replaceFindPreviousAction.setEnabled(false);
        _machine.positionChanged();
        _caretChanged = true;
    }
};
/** The action performed when searching forwards */
Action _findNextAction = new AbstractAction("Find Next") {

    public void actionPerformed(ActionEvent e) {
        findNext();
    }
};
Action _findPreviousAction = new AbstractAction("Find Previous") {

    public void actionPerformed(ActionEvent e) {
        findPrevious();
    }
};
private Action _findAllAction = new AbstractAction("Find All") {

    public void actionPerformed(final ActionEvent e) {
        _isFindReplaceActive = true;
        _findAll();
        _isFindReplaceActive = false;
    }
};
private Action _doFindAction = new AbstractAction("Do Find") {

    public void actionPerformed(ActionEvent e) {
        _doFind();
    }
};
Action _replaceAction = new AbstractAction("Replace") {

    public void actionPerformed(ActionEvent e) {
        _replace();
    }
};
Action _replaceFindNextAction = new AbstractAction("Replace/Find Next") {

    public void actionPerformed(ActionEvent e) {
        _replaceFindNext();
    }
};
Action _replaceFindPreviousAction = new AbstractAction("Replace/Find Previous") {

    public void actionPerformed(ActionEvent e) {
        _replaceFindPrevious();
    }

    ;
};
/** Replaces all occurences of the findfield text with that of the replacefield text both before and after the cursor
    * without prompting for wrapping around the end of the document.
    */
private Action _replaceAllAction = new AbstractAction("Replace All") {

    public void actionPerformed(ActionEvent e) {
        _replaceAll();
    }
};
// Inserts '\n' into a text field.  (The default binding for "enter" is to insert 
// the system-specific newline string (I think), which causes trouble when finding 
// in files with different newline strings.) 
// TODO: Standardize on \n in a post-processing step, rather than mucking around 
// in the workings of a text editor field.  (Notice, for example, that this 
// doesn't correctly handle an 'enter' pressed while some text is selected.) 
Action _standardNewlineAction = new TextAction("Newline Action") {

    public void actionPerformed(ActionEvent e) {
        JTextComponent c = getTextComponent(e);
        String text = c.getText();
        int caretPos = c.getCaretPosition();
        String textBeforeCaret = text.substring(0, caretPos);
        String textAfterCaret = text.substring(caretPos);
        c.setText(textBeforeCaret.concat("\n").concat(textAfterCaret));
        c.setCaretPosition(caretPos + 1);
    }
};
/** Default cut action. */
Action cutAction = new DefaultEditorKit.CutAction() {

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JTextComponent) {
            JTextComponent tc = (JTextComponent) e.getSource();
            if (tc.getSelectedText() != null) {
                super.actionPerformed(e);
                String s = edu.rice.cs.util.swing.Utilities.getClipboardSelection(FindReplacePanel.this);
                if (s != null && s.length() != 0) {
                    ClipboardHistoryModel.singleton().put(s);
                }
            }
        }
    }
};
/** Default copy action. */
Action copyAction = new DefaultEditorKit.CopyAction() {

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JTextComponent) {
            JTextComponent tc = (JTextComponent) e.getSource();
            if (tc.getSelectedText() != null) {
                super.actionPerformed(e);
                String s = edu.rice.cs.util.swing.Utilities.getClipboardSelection(FindReplacePanel.this);
                if (s != null && s.length() != 0) {
                    ClipboardHistoryModel.singleton().put(s);
                }
            }
        }
    }
};
}
