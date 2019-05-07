public static GraphXmlSerializer[] getGraphXmSerializers(GraphPlugin plugin, ISession session) {
    try {
        File settingsPath = plugin.getPluginUserSettingsFolder();
        final String urlPrefix = javaNormalize(session.getAlias().getUrl()) + ".";
        File[] graphXmlFiles = settingsPath.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (name.startsWith(urlPrefix)) {
                    return true;
                }
                return false;
            }
        });
        GraphXmlSerializer[] ret = new GraphXmlSerializer[graphXmlFiles.length];
        for (int i = 0; i < graphXmlFiles.length; i++) {
            ret[i] = new GraphXmlSerializer(plugin, session, null, graphXmlFiles[i].getPath());
        }
        return ret;
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
