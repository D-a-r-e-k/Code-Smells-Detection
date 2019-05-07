/**
     * Packs given plug-in fragment into single ZIP file. Resulting file may be
     * used to run plug-ins from.
     * @param fragment plug-in fragment descriptor
     * @param pathResolver path resolver instance
     * @param destFile target file
     * @throws IOException if an I/O error has occurred
     */
public static void pack(final PluginFragment fragment, final PathResolver pathResolver, final File destFile) throws IOException {
    pack(pathResolver.resolvePath(fragment, "/"), //$NON-NLS-1$  
    "JPF plug-in fragment " + fragment.getId() + " of version " + fragment.getVersion(), destFile);
}
