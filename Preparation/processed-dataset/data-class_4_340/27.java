/** Adds all of the components for the file types panel of the preferences window. */
private void _setupFileTypesPanel(ConfigPanel panel) {
    if (PlatformFactory.ONLY.canRegisterFileExtensions()) {
        addOptionComponent(panel, new LabelComponent("<html>Assign DrJava project files and DrJava extensions<br>" + "(with the extensions .drjava and .djapp) to DrJava.<br>" + "When double-clicking on a .drjava file, DrJava will open it.</html>", this, true));
        panel.addComponent(new ButtonComponent(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (PlatformFactory.ONLY.registerDrJavaFileExtensions()) {
                    JOptionPane.showMessageDialog(ConfigFrame.this, "Successfully set .drjava and .djapp file associations.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(ConfigFrame.this, "Could not set .drjava and .djapp file associations.", "File Types Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }, "Associate .drjava and .djapp Files with DrJava", this, "This associates .drjava and .djapp files with DrJava."));
        addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
        panel.addComponent(new ButtonComponent(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (PlatformFactory.ONLY.unregisterDrJavaFileExtensions()) {
                    JOptionPane.showMessageDialog(ConfigFrame.this, "Successfully removed .drjava and .djapp file associations.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(ConfigFrame.this, "Could not remove .drjava and .djapp file associations.", "File Types Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }, "Remove .drjava and .djapp File Associations", this, "This removes the association of .drjava and .djapp files with DrJava."));
        addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
        addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
        addOptionComponent(panel, new LabelComponent("<html>Assign Java source files with the<br>" + "extension .java to DrJava. When double-clicking<br>" + "on a .java file, DrJava will open it.</html>", this, true));
        panel.addComponent(new ButtonComponent(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (PlatformFactory.ONLY.registerJavaFileExtension()) {
                    JOptionPane.showMessageDialog(ConfigFrame.this, "Successfully set .java file association.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(ConfigFrame.this, "Could not set .java file association.", "File Types Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }, "Associate .java Files with DrJava", this, "This associates .java source files with DrJava."));
        addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
        panel.addComponent(new ButtonComponent(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (PlatformFactory.ONLY.unregisterJavaFileExtension()) {
                    JOptionPane.showMessageDialog(ConfigFrame.this, "Successfully removed .java file association.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(ConfigFrame.this, "Could not remove .java file association.", "File Types Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }, "Remove .java File Association", this, "This removes the association of .java project files with DrJava."));
        addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
        addOptionComponent(panel, new LabelComponent("<html>&nbsp;</html>", this, true));
        addOptionComponent(panel, new ForcedChoiceOptionComponent(OptionConstants.FILE_EXT_REGISTRATION, "<html>Automatically assign .java, .drjava and .djapp Files to DrJava</html>", this, "<html>Assign files with the extensions .java, .drjava and .djapp to DrJava.<br>" + "When double-clicking those files, they will be opened in DrJava.<br><br>" + "Selecting 'always' will re-establish this association every time DrJava<br>" + "started, without asking. Selecting 'ask me' will ask the user at start up<br>" + "if the association has been changed. Selecting 'never' will not assign<br>" + ".java, .drjava and .djapp files to DrJava."));
    } else {
        addOptionComponent(panel, new LabelComponent("<html><br><br>" + (PlatformFactory.ONLY.isMacPlatform() ? "File associations are managed automatically by Mac OS." : (PlatformFactory.ONLY.isWindowsPlatform() ? "To set file associations, please use the .exe file version of DrJava.<br>" + "Configuring file associations is not supported for the .jar file version." : "Managing file associations is not supported yet on this operating system.")) + "</html>", this, true));
    }
    panel.displayComponents();
}
