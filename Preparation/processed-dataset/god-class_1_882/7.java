public void rename(String newName) {
    try {
        String url = _session.getAlias().getUrl();
        String newGraphFile = getFileName(_plugin.getPluginUserSettingsFolder().getPath(), url, newName);
        (new File(_graphFile)).renameTo(new File(newGraphFile));
        String[] params = { _graphFile, newGraphFile };
        // i18n[graph.graphRenamed=Renamed "{0}" to "{1}"] 
        _session.showMessage(s_stringMgr.getString("graph.graphRenamed", params));
        _graphFile = newGraphFile;
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
