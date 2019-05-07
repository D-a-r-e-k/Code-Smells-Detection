/**
     * Packs all plug-ins from given registry as one archive file.
     * @param registry plug-ins registry
     * @param pathResolver path resolver (only local file URLs are supported)
     * @param destFile target archive file (will be overridden if any exists)
     * @return set of UID's of all packed plug-ins
     * @throws IOException if an I/O error has occurred
     */
public static Set<String> pack(final PluginRegistry registry, final PathResolver pathResolver, final File destFile) throws IOException {
    return pack(registry, pathResolver, destFile, new Filter() {

        public boolean accept(final String id, final Version version, final boolean isFragment) {
            return true;
        }
    });
}
