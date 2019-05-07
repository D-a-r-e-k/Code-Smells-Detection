private void setSessionFile(File file) {
    sessionFile = file;
    setTitle(sessionFile == null ? title : title + " - " + sessionFile.getName());
}
