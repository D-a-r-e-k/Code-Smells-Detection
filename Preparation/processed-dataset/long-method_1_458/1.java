/**
    * Either graphPane or graphFileName might be null.
    */
public GraphXmlSerializer(GraphPlugin plugin, ISession session, GraphMainPanelTab graphPane, String graphFileName) {
    try {
        _plugin = plugin;
        _session = session;
        String url = _session.getAlias().getUrl();
        if (null == graphFileName) {
            _graphFile = getFileName(plugin.getPluginUserSettingsFolder().getPath(), url, graphPane.getTitle());
        } else {
            _graphFile = graphFileName;
        }
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
