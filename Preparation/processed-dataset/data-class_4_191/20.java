/**
   * Loads the column names and sizes.
   */
protected FetchProfile createColumnInformation() {
    String tableType;
    if (isSentFolder())
        tableType = "SentFolderTable";
    else if (isOutboxFolder())
        tableType = "SentFolderTable";
    else
        tableType = "FolderTable";
    FetchProfile fp = new FetchProfile();
    fp.add(FetchProfile.Item.FLAGS);
    if (columnValues == null) {
        List<String> colIds = Pooka.getResources().getPropertyAsList(tableType, "");
        Vector colvals = new Vector();
        Vector<String> colnames = new Vector<String>();
        Vector<String> colsizes = new Vector<String>();
        for (String tmp : colIds) {
            String type = Pooka.getProperty(tableType + "." + tmp + ".type", "");
            if (type.equalsIgnoreCase("Multi")) {
                SearchTermIconManager stm = new SearchTermIconManager(tableType + "." + tmp);
                colvals.addElement(stm);
                Vector toFetch = Pooka.getResources().getPropertyAsVector(tableType + "." + tmp + ".profileItems", "");
                if (toFetch != null) {
                    for (int z = 0; z < toFetch.size(); z++) {
                        String profileDef = (String) toFetch.elementAt(z);
                        if (profileDef.equalsIgnoreCase("Flags")) {
                            folderLog(Level.FINE, "adding FLAGS to FetchProfile.");
                            fp.add(FetchProfile.Item.FLAGS);
                        } else if (profileDef.equalsIgnoreCase("Envelope")) {
                            folderLog(Level.FINE, "adding ENVELOPE to FetchProfile.");
                            fp.add(FetchProfile.Item.ENVELOPE);
                        } else if (profileDef.equalsIgnoreCase("Content_Info")) {
                            folderLog(Level.FINE, "adding CONTENT_INFO to FetchProfile.");
                            fp.add(FetchProfile.Item.CONTENT_INFO);
                        } else {
                            folderLog(Level.FINE, "adding " + profileDef + " to FetchProfile.");
                            fp.add(profileDef);
                        }
                    }
                }
            } else if (type.equalsIgnoreCase("RowCounter")) {
                colvals.addElement(RowCounter.getInstance());
            } else {
                String value = Pooka.getProperty(tableType + "." + tmp + ".value", tmp);
                colvals.addElement(value);
                String fpValue = Pooka.getProperty(tableType + "." + tmp + ".profileItems", value);
                fp.add(fpValue);
            }
            colnames.addElement(Pooka.getProperty(tableType + "." + tmp + ".label", tmp));
            String value = Pooka.getProperty(getFolderProperty() + ".columnsize." + tmp + ".value", Pooka.getProperty(tableType + "." + tmp + ".value", tmp));
            colsizes.addElement(Pooka.getProperty(getFolderProperty() + ".columnsize." + tmp + ".value", Pooka.getProperty(tableType + "." + tmp + ".size", tmp)));
        }
        setColumnNames(colnames);
        setColumnValues(colvals);
        setColumnSizes(colsizes);
        setColumnIds(colIds);
    }
    // if we've already loaded the filters, then add those in, too. 
    if (filterHeaders != null) {
        for (int i = 0; i < filterHeaders.size(); i++) {
            fp.add((String) filterHeaders.get(i));
        }
    }
    if (getLogger().isLoggable(Level.FINE)) {
        folderLog(Level.FINE, "created fetch profile.");
        String[] headers = fp.getHeaderNames();
        if (headers != null) {
            for (int i = 0; i < headers.length; i++) {
                folderLog(Level.FINE, "headers[" + i + "]=" + headers[i]);
            }
        }
        folderLog(Level.FINE, "headers done.");
    }
    return fp;
}
