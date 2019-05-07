/**
     * Saves the old session.
     * Returns false if canceled by user.
     */
private boolean saveOldSession() {
    if (session != null && session.isModified()) {
        String title = LANGUAGE.getString("MainFrame.saveOldSessionTitle");
        String question = LANGUAGE.getString("MainFrame.saveOldSessionQuestion");
        int answer = JOptionPane.showConfirmDialog(this, question, title, JOptionPane.YES_NO_CANCEL_OPTION);
        if (answer == JOptionPane.YES_OPTION)
            saveSession();
        return answer != JOptionPane.CANCEL_OPTION;
    }
    return true;
}
