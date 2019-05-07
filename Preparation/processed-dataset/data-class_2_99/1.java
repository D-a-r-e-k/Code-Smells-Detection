//$NON-NLS-1$  
/**
     * Packs given plug-in into single ZIP file. Resulting file may be used to
     * run plug-ins from.
     * @param descr plug-in descriptor
     * @param pathResolver path resolver instance
     * @param destFile target file
     * @throws IOException if an I/O error has occurred
     */
public static void pack(final PluginDescriptor descr, final PathResolver pathResolver, final File destFile) throws IOException {
    pack(pathResolver.resolvePath(descr, "/"), //$NON-NLS-1$  
    "JPF plug-in " + descr.getId() + " of version " + descr.getVersion(), destFile);
}
