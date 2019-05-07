/**
     * Causes JAG to die.
     *
     * @param error if not <code>null</code>, forces an error dialogue before death.
     */
public static void kickTheBucket(String error) {
    if (error == null) {
        // Store GUI-settings in user-preferences  
        saveGuiSettings();
        Settings.write();
        // Make sure the current database-settings are stored as well  
        ConfigManager.getInstance().save();
        //todo: prompt to save application file!  
        //todo: Make sure prompt allows you to check "don't show this again"  
        System.exit(0);
    } else {
        //something went horribly wrong..  
        JOptionPane.showMessageDialog(jagGenerator, error, "JAG - Fatal error!", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
