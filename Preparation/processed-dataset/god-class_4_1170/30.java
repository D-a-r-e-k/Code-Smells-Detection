/** Add all of the components for the JUnit panel of the preferences window. */
private void _setupJUnitPanel(ConfigPanel panel) {
    final BooleanOptionComponent junitLocEnabled = new BooleanOptionComponent(OptionConstants.JUNIT_LOCATION_ENABLED, "Use external JUnit", this, "<html>If this is enabled, DrJava will use the JUnit configured<br>" + "below under 'JUnit/ConcJUnit Location'. If it is disabled,<br>" + "DrJava will use the JUnit that is built-in.</html>", false).setEntireColumn(true);
    addOptionComponent(panel, junitLocEnabled);
    final FileOptionComponent junitLoc = new FileOptionComponent(OptionConstants.JUNIT_LOCATION, "JUnit/ConcJUnit Location", this, "<html>Optional location of the JUnit or ConcJUnit jar file.<br>" + "(Changes will not be applied until the Interactions Pane<br>" + "is reset.)</html>", new FileSelectorComponent(this, _jarChooser, 30, 10f) {

        public void setFileField(File file) {
            if (edu.rice.cs.drjava.model.junit.ConcJUnitUtils.isValidJUnitFile(file) || edu.rice.cs.drjava.model.junit.ConcJUnitUtils.isValidConcJUnitFile(file)) {
                super.setFileField(file);
            } else if (file.exists()) {
                // invalid JUnit/ConcJUnit file, but exists 
                new edu.rice.cs.drjava.ui.DrJavaScrollableDialog(_parent, "Invalid JUnit/ConcJUnit File", "Stack trace:", edu.rice.cs.util.StringOps.getStackTrace(), 600, 400, false).show();
                JOptionPane.showMessageDialog(_parent, "The file '" + file.getName() + "'\nis not a valid JUnit/ConcJUnit file.", "Invalid JUnit/ConcJUnit File", JOptionPane.ERROR_MESSAGE);
                resetFileField();
            }
        }

        public boolean validateTextField() {
            String newValue = _fileField.getText().trim();
            File newFile = FileOps.NULL_FILE;
            if (!newValue.equals(""))
                newFile = new File(newValue);
            if (newFile != FileOps.NULL_FILE && !newFile.exists()) {
                JOptionPane.showMessageDialog(_parent, "The file '" + newFile.getName() + "'\nis invalid because it does not exist.", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
                if (_file != null && !_file.exists())
                    _file = FileOps.NULL_FILE;
                resetFileField();
                // revert if not valid 
                return false;
            } else {
                if (edu.rice.cs.drjava.model.junit.ConcJUnitUtils.isValidJUnitFile(newFile) || edu.rice.cs.drjava.model.junit.ConcJUnitUtils.isValidConcJUnitFile(newFile) || FileOps.NULL_FILE.equals(newFile)) {
                    setFileField(newFile);
                    return true;
                } else {
                    new edu.rice.cs.drjava.ui.DrJavaScrollableDialog(_parent, "Invalid JUnit/ConcJUnit File", "newFile is NULL_FILE? " + (FileOps.NULL_FILE.equals(newFile)), edu.rice.cs.util.StringOps.getStackTrace(), 600, 400, false).show();
                    JOptionPane.showMessageDialog(_parent, "The file '" + newFile.getName() + "'\nis not a valid JUnit/ConcJUnit file.", "Invalid JUnit/ConcJUnit File", JOptionPane.ERROR_MESSAGE);
                    resetFileField();
                    // revert if not valid 
                    return false;
                }
            }
        }
    });
    junitLoc.setFileFilter(ClassPathFilter.ONLY);
    addOptionComponent(panel, junitLoc);
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    final ForcedChoiceOptionComponent concJUnitChecksEnabledComponent = new ForcedChoiceOptionComponent(OptionConstants.CONCJUNIT_CHECKS_ENABLED, "Enabled ConcJUnit Checks", this, "<html>The concurrent unit testing checks that should be performed.<br>" + "'none' uses plain JUnit. ConcJUnit can also detect failures in<br>" + "all threads ('all-threads'), detect threads that did not end in<br>" + "time ('all-threads, join'), and threads that ended in time only<br>" + "because they were lucky ('all-threads, nojoin, lucky).<br>" + "The last setting requires a 'ConcJUnit Runtime Location' to be set.</html>");
    addOptionComponent(panel, concJUnitChecksEnabledComponent);
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    final FileOptionComponent rtConcJUnitLoc = new FileOptionComponent(OptionConstants.RT_CONCJUNIT_LOCATION, "ConcJUnit Runtime Location", this, "<html>Optional location of the Java Runtime Library processed<br>" + "to generate &quot;lucky&quot; warnings. If left blank, &quot;lucky&quot; warnings<br>" + "will not be generated. This setting is deactivated if the path to<br>" + "ConcJUnit has not been specified above.<br>" + "(Changes will not be applied until the Interactions Pane is reset.)</html>", new FileSelectorComponent(this, _jarChooser, 30, 10f) {

        public void setFileField(File file) {
            if (edu.rice.cs.drjava.model.junit.ConcJUnitUtils.isValidRTConcJUnitFile(file)) {
                super.setFileField(file);
            } else if (file.exists()) {
                // invalid but exists 
                JOptionPane.showMessageDialog(_parent, "The file '" + file.getName() + "'\nis not a valid ConcJUnit Runtime file.", "Invalid ConcJUnit Runtime File", JOptionPane.ERROR_MESSAGE);
                resetFileField();
            }
        }

        public boolean validateTextField() {
            String newValue = _fileField.getText().trim();
            File newFile = FileOps.NULL_FILE;
            if (!newValue.equals(""))
                newFile = new File(newValue);
            if (newFile != FileOps.NULL_FILE && !newFile.exists()) {
                JOptionPane.showMessageDialog(_parent, "The file '" + newFile.getName() + "'\nis invalid because it does not exist.", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
                if (_file != null && !_file.exists())
                    _file = FileOps.NULL_FILE;
                resetFileField();
                // revert if not valid 
                return false;
            } else {
                if (edu.rice.cs.drjava.model.junit.ConcJUnitUtils.isValidRTConcJUnitFile(newFile) || FileOps.NULL_FILE.equals(newFile)) {
                    setFileField(newFile);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(_parent, "The file '" + newFile.getName() + "'\nis not a valid ConcJUnit Runtime file.", "Invalid ConcJUnit Runtime File", JOptionPane.ERROR_MESSAGE);
                    resetFileField();
                    // revert if not valid 
                    return false;
                }
            }
        }
    });
    rtConcJUnitLoc.setFileFilter(ClassPathFilter.ONLY);
    ActionListener processRTListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            File concJUnitJarFile = FileOps.getDrJavaFile();
            if (junitLocEnabled.getComponent().isSelected()) {
                concJUnitJarFile = junitLoc.getComponent().getFileFromField();
            }
            File rtFile = rtConcJUnitLoc.getComponent().getFileFromField();
            edu.rice.cs.drjava.model.junit.ConcJUnitUtils.showGenerateRTConcJUnitJarFileDialog(ConfigFrame.this, rtFile, concJUnitJarFile, new Runnable1<File>() {

                public void run(File targetFile) {
                    rtConcJUnitLoc.getComponent().setFileField(targetFile);
                }
            }, new Runnable() {

                public void run() {
                }
            });
        }
    };
    final ButtonComponent processRT = new ButtonComponent(processRTListener, "Generate ConcJUnit Runtime File", this, "<html>Generate the ConcJUnit Runtime file specified above.<br>" + "This setting is deactivated if the path to ConcJUnit has not been specified above.</html>");
    OptionComponent.ChangeListener rtConcJUnitListener = new OptionComponent.ChangeListener() {

        public Object value(Object oc) {
            File f = junitLoc.getComponent().getFileFromField();
            boolean enabled = (!junitLocEnabled.getComponent().isSelected()) || edu.rice.cs.drjava.model.junit.ConcJUnitUtils.isValidConcJUnitFile(f);
            rtConcJUnitLoc.getComponent().setEnabled(enabled);
            processRT.getComponent().setEnabled(enabled);
            concJUnitChecksEnabledComponent.getComponent().setEnabled(enabled);
            return null;
        }
    };
    OptionComponent.ChangeListener junitLocListener = new OptionComponent.ChangeListener() {

        public Object value(Object oc) {
            boolean enabled = junitLocEnabled.getComponent().isSelected();
            junitLoc.getComponent().setEnabled(enabled);
            return null;
        }
    };
    junitLocEnabled.addChangeListener(junitLocListener);
    junitLocEnabled.addChangeListener(rtConcJUnitListener);
    junitLoc.addChangeListener(rtConcJUnitListener);
    addOptionComponent(panel, rtConcJUnitLoc);
    addOptionComponent(panel, processRT);
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    final LabelComponent internalExternalStatus = new LabelComponent("<html>&nbsp;</html>", this, true);
    final LabelComponent threadsStatus = new LabelComponent("<html>&nbsp;</html>", this, true);
    final LabelComponent joinStatus = new LabelComponent("<html>&nbsp;</html>", this, true);
    final LabelComponent luckyStatus = new LabelComponent("<html>&nbsp;</html>", this, true);
    OptionComponent.ChangeListener junitStatusChangeListener = new OptionComponent.ChangeListener() {

        public Object value(Object oc) {
            File f = junitLoc.getComponent().getFileFromField();
            String[] s = new String[] { " ", " ", " ", " " };
            boolean isConcJUnit = true;
            if ((!junitLocEnabled.getComponent().isSelected()) || (f == null) || FileOps.NULL_FILE.equals(f) || !f.exists()) {
                s[0] = "DrJava uses the built-in ConcJUnit framework.";
            } else {
                String type = "ConcJUnit";
                if (!edu.rice.cs.drjava.model.junit.ConcJUnitUtils.isValidConcJUnitFile(f)) {
                    type = "JUnit";
                    isConcJUnit = false;
                }
                s[0] = "DrJava uses an external " + type + " framework.";
            }
            if (!isConcJUnit) {
                s[1] = "JUnit does not support all-thread, no-join";
                s[2] = "or lucky checks. They are all disabled.";
            } else {
                s[1] = "All-thread checks are disabled.";
                s[2] = "No-join checks are disabled.";
                s[3] = "Lucky checks are disabled.";
                if (!concJUnitChecksEnabledComponent.getCurrentComboBoxValue().equals(OptionConstants.ConcJUnitCheckChoices.NONE)) {
                    s[1] = "All-thread checks are enabled.";
                    if (concJUnitChecksEnabledComponent.getCurrentComboBoxValue().equals(OptionConstants.ConcJUnitCheckChoices.ALL) || concJUnitChecksEnabledComponent.getCurrentComboBoxValue().equals(OptionConstants.ConcJUnitCheckChoices.NO_LUCKY)) {
                        s[2] = "No-join checks are enabled.";
                        if (concJUnitChecksEnabledComponent.getCurrentComboBoxValue().equals(OptionConstants.ConcJUnitCheckChoices.ALL)) {
                            File rtf = rtConcJUnitLoc.getComponent().getFileFromField();
                            if ((rtf != null) && !FileOps.NULL_FILE.equals(rtf) && rtf.exists() && edu.rice.cs.drjava.model.junit.ConcJUnitUtils.isValidRTConcJUnitFile(rtf)) {
                                s[3] = "Lucky checks are enabled.";
                            }
                        }
                    }
                }
            }
            internalExternalStatus.getComponent().setText(s[0]);
            threadsStatus.getComponent().setText(s[1]);
            joinStatus.getComponent().setText(s[2]);
            luckyStatus.getComponent().setText(s[3]);
            return null;
        }
    };
    concJUnitChecksEnabledComponent.addChangeListener(junitStatusChangeListener);
    junitLocEnabled.addChangeListener(junitStatusChangeListener);
    junitLoc.addChangeListener(junitStatusChangeListener);
    rtConcJUnitLoc.addChangeListener(junitStatusChangeListener);
    addOptionComponent(panel, internalExternalStatus);
    addOptionComponent(panel, threadsStatus);
    addOptionComponent(panel, joinStatus);
    addOptionComponent(panel, luckyStatus);
    junitLocListener.value(null);
    rtConcJUnitListener.value(null);
    junitStatusChangeListener.value(null);
    addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
    final BooleanOptionComponent forceTestSuffix = new BooleanOptionComponent(OptionConstants.FORCE_TEST_SUFFIX, "Require test classes in projects to end in \"Test\"", this, "Whether to force test classes in projects to end in \"Test\".", false).setEntireColumn(true);
    addOptionComponent(panel, forceTestSuffix);
    panel.displayComponents();
}
