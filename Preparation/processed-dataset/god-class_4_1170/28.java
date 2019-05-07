/** Adds all of the components for the Compiler Options Panel of the preferences window
    */
private void _setupCompilerPanel(ConfigPanel panel) {
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.SHOW_UNCHECKED_WARNINGS, "Show Unchecked Warnings", this, "<html>Warn about unchecked conversions involving parameterized types.</html>", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.SHOW_DEPRECATION_WARNINGS, "Show Deprecation Warnings", this, "<html>Warn about each use or override of a deprecated method, field, or class.</html>", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.SHOW_PATH_WARNINGS, "Show Path Warnings", this, "<html>Warn about nonexistent members of the classpath and sourcepath.</html>", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.SHOW_SERIAL_WARNINGS, "Show Serial Warnings", this, "<html>Warn about missing <code>serialVersionUID</code> definitions on serializable classes.</html>", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.SHOW_FINALLY_WARNINGS, "Show Finally Warnings", this, "<html>Warn about <code>finally</code> clauses that cannot complete normally.</html>", false).setEntireColumn(true));
    addOptionComponent(panel, new BooleanOptionComponent(OptionConstants.SHOW_FALLTHROUGH_WARNINGS, "Show Fall-Through Warnings", this, "<html>Warn about <code>switch</code> block cases that fall through to the next case.</html>", false).setEntireColumn(true));
    /*
     * The drop down box containing the compiler names
     */
    final ForcedChoiceOptionComponent CPC = new ForcedChoiceOptionComponent(OptionConstants.COMPILER_PREFERENCE_CONTROL.evaluate(), "Compiler Preference", this, "Which compiler is prefered?");
    /*
     * Action listener that saves the selected compiler name into the DEFAULT_COMPILER_PREFERENCE setting
     */
    ActionListener CPCActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            if (!edu.rice.cs.drjava.DrJava.getConfig().getSetting(OptionConstants.DEFAULT_COMPILER_PREFERENCE).equals(CPC.getCurrentComboBoxValue())) {
                edu.rice.cs.drjava.DrJava.getConfig().setSetting(OptionConstants.DEFAULT_COMPILER_PREFERENCE, CPC.getCurrentComboBoxValue());
            }
        }
    };
    /*
     * insures that the change is made only when the apply or ok button is hit
     */
    _applyButton.addActionListener(CPCActionListener);
    _okButton.addActionListener(CPCActionListener);
    /*
     * adds the drop down box to the panel
     */
    addOptionComponent(panel, CPC.setEntireColumn(false));
    addOptionComponent(panel, new LabelComponent("<html><br><br>Note: Compiler warnings not shown if compiling any Java language level files.</html>", this, true));
    panel.displayComponents();
}
