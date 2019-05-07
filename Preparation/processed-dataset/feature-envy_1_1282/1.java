public void write(GraphControllerXmlBean xmlBean) {
    try {
        XMLBeanWriter bw = new XMLBeanWriter(xmlBean);
        bw.save(_graphFile);
        String[] params = { xmlBean.getTitle(), _graphFile };
        // i18n[graph.graphSaved=Graph "{0}" saved to "{1}"] 
        String msg = s_stringMgr.getString("graph.graphSaved", params);
        _session.showMessage(msg);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
