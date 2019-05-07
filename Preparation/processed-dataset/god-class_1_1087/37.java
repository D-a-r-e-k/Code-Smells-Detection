//}}}  
//{{{ loadColors() method  
private static void loadColors() {
    synchronized (lock) {
        colors = new ArrayList<ColorEntry>();
        if (!jEdit.getBooleanProperty("vfs.browser.colorize"))
            return;
        String glob;
        int i = 0;
        while ((glob = jEdit.getProperty("vfs.browser.colors." + i + ".glob")) != null) {
            try {
                colors.add(new ColorEntry(Pattern.compile(StandardUtilities.globToRE(glob)), jEdit.getColorProperty("vfs.browser.colors." + i + ".color", Color.black)));
            } catch (PatternSyntaxException e) {
                Log.log(Log.ERROR, VFS.class, "Invalid regular expression: " + glob);
                Log.log(Log.ERROR, VFS.class, e);
            }
            i++;
        }
    }
}
