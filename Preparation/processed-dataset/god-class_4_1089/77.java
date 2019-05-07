//}}}  
//{{{ recoverAutosave() method  
private boolean recoverAutosave(final View view) {
    if (!autosaveFile.canRead())
        return false;
    // this method might get called at startup  
    GUIUtilities.hideSplashScreen();
    final Object[] args = { autosaveFile.getPath() };
    int result = GUIUtilities.confirm(view, "autosave-found", args, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    if (result == JOptionPane.YES_OPTION) {
        VFSManager.getFileVFS().load(view, this, autosaveFile.getPath());
        // show this message when all I/O requests are  
        // complete  
        VFSManager.runInAWTThread(new Runnable() {

            public void run() {
                GUIUtilities.message(view, "autosave-loaded", args);
            }
        });
        return true;
    } else
        return false;
}
