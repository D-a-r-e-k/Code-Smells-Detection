/**
   * Execute scripts found in user home directory.
   */
public static void executeScripts(JextFrame parent) {
    String dir = SETTINGS_DIRECTORY + "scripts" + File.separator;
    String[] scripts = Utilities.getWildCardMatches(dir, "*.jext-script", false);
    if (scripts == null)
        return;
    for (int i = 0; i < scripts.length; i++) org.jext.scripting.dawn.Run.runScript(dir + scripts[i], parent, false);
    scripts = Utilities.getWildCardMatches(dir, "*.py", false);
    if (scripts == null)
        return;
    for (int i = 0; i < scripts.length; i++) org.jext.scripting.python.Run.runScript(dir + scripts[i], parent);
}
