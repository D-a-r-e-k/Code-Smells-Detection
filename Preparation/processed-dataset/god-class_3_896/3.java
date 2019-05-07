public void fileWriteError(File file) {
    JOptionPane.showMessageDialog(this, LANGUAGE.getString("MainFrame.CouldNotWriteFile") + " " + file.getPath(), LANGUAGE.getString("MainFrame.FileError"), JOptionPane.ERROR_MESSAGE);
}
