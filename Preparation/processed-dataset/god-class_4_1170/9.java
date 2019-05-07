/** Write the configured option values to disk. */
public boolean saveSettings() throws IOException {
    boolean successful = apply();
    if (successful) {
        try {
            DrJava.getConfig().saveConfiguration();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, "Could not save changes to your \".drjava\" file in your home directory. \n\n" + ioe, "Could Not Save Changes", JOptionPane.ERROR_MESSAGE);
            //return false; 
            throw ioe;
        }
    }
    return successful;
}
