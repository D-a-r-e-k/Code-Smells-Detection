public boolean save() {
    //the object graph is incomplete at this stage, so finalise it now:  
    updateLocalSideRelations();
    updateForeignSideRelations();
    if (!addRelatedEntitiesToSessionBeans()) {
        return false;
    }
    try {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element skelet = doc.createElement("skelet");
        doc.appendChild(skelet);
        root.getXML(skelet);
        String XMLDoc = outXML(doc);
        String fileName = file.getName();
        if (fileName != null) {
            if (fileName.indexOf(".xml") == -1) {
                file = new File(file.getAbsolutePath() + ".xml");
            }
        }
        FileWriter fw = new FileWriter(file);
        fw.write(XMLDoc);
        fw.close();
        fileNameLabel.setText("Application file: " + file.getName());
        fileNameLabel.setToolTipText(file.getAbsolutePath());
    } catch (ParserConfigurationException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    setFileNeedsSavingIndicator(false);
    return true;
}
