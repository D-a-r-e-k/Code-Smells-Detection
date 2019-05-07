private static void setFileNeedsSavingIndicator(boolean sterretje) {
    if (jagGenerator != null && jagGenerator.file != null) {
        String filename = jagGenerator.fileNameLabel.getText();
        if (sterretje && filename.charAt(filename.length() - 1) != '*') {
            jagGenerator.fileNameLabel.setText(filename + '*');
        } else if (!sterretje && filename.charAt(filename.length() - 1) == '*') {
            jagGenerator.fileNameLabel.setText(filename.substring(0, filename.length() - 1));
        }
    }
}
