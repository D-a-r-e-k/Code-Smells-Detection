private boolean dontOverwrite(File file) {
    if (file.exists()) {
        int answer = JOptionPane.showConfirmDialog(this, LANGUAGE.getString("MainFrame.OverwriteExistingFile") + " " + file.getPath() + "?", LANGUAGE.getString("MainFrame.FileExists"), JOptionPane.YES_NO_OPTION);
        return answer != JOptionPane.YES_OPTION;
    } else
        return false;
}
