//  private static Container wrap(JComponent comp) { 
//    Container stretcher = Box.createHorizontalBox(); 
//    stretcher.add(comp); 
//    stretcher.add(Box.createHorizontalGlue()); 
//    return stretcher; 
//  } 
// 
//  /** Consider a parent container.  Change its layout to GridBagLayout 
//   * with 2 columns, 2 rows.  Consider them quadrants in a coordinate plain. 
//   * put the arguments in their corresponding quadrants, ignoring q3. 
//   */ 
//  private static void hookComponents(Container parent, JComponent q1, 
//                                     JComponent q2, JComponent q4) { 
//    GridBagLayout gbl = new GridBagLayout(); 
//    GridBagConstraints c = new GridBagConstraints(); 
//    parent.setLayout(gbl); 
//    c.fill = c.BOTH; 
//    addComp(parent, q2, c, gbl, 0, 0, 0f, 0f, 1, 0); 
//    addComp(parent, q1, c, gbl, 0, 1, 1f, 0f, 1, 0); 
//    addComp(parent, new JPanel(), c, gbl, 1, 0, 1f, 1f, 2, 0); 
//    addComp(parent, new JPanel(), c, gbl, 2, 0, 0f, 0f, 1, 0); 
//    addComp(parent, q4, c, gbl, 2, 1, 1f, 0f, 1, 0); 
//  } 
//  private static void addComp(Container p, JComponent child, 
//                              GridBagConstraints c, GridBagLayout gbl, 
//                              int row, int col, 
//                              float weightx, float weighty, int gridw, 
//                              int ipady) { 
//    c.gridx = col; c.gridy = row; 
//    c.weightx = weightx; c.weighty = weighty; 
//    c.gridwidth = gridw; 
//    c.ipady = ipady; 
//    gbl.setConstraints(child,c); 
//    p.add(child); 
//  } 
/** Sets appropriate variables in the FindReplaceMachine if the caret has been changed. */
private void _updateMachine() {
    if (_caretChanged) {
        OpenDefinitionsDocument doc = _model.getActiveDocument();
        _machine.setDocument(doc);
        if (_machine.getFirstDoc() == null)
            _machine.setFirstDoc(doc);
        //      _machine.setStart(_defPane.getCaretPosition()); 
        _machine.setPosition(_defPane.getCaretPosition());
        _caretChanged = false;
    }
}
