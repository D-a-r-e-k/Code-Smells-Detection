public void fileReadError(File file) {
    JOptionPane.showMessageDialog(this, LANGUAGE.getString("MainFrame.CouldNotReadFile") + " " + file.getPath(), LANGUAGE.getString("MainFrame.FileError"), JOptionPane.ERROR_MESSAGE);
}
