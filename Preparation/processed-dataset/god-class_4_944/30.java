private File selectJagOutDirectory(String startDir) {
    File directory = null;
    int fileChooserStatus;
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select an ouput directory for the generated application..");
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setCurrentDirectory(new File(startDir));
    fileChooserStatus = fileChooser.showOpenDialog(this);
    if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
        directory = fileChooser.getSelectedFile();
        String projectName = root.app.nameText.getText();
        directory = new File(directory.getAbsoluteFile(), projectName);
    }
    return directory;
}
