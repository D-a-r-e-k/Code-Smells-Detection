public void remove() {
    (new File(_graphFile)).delete();
    String[] params = { _graphFile };
    // i18n[graph.graphRemoved=Removed graph file "{0}"] 
    _session.showMessage(s_stringMgr.getString("graph.graphRemoved", params));
}
